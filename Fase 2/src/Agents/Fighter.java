package Agents;

import Graphics.Configs;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import World.Position;
import World.*;
import jade.lang.acl.UnreadableException;

public class Fighter extends Agent {

    private Position 	pos;
    private Boolean 	available;
    private int 		speed;
    private int 		waterCapacity;
    private int 		fuelCapacity;
    private int			currentFuel;
    private int			currentWater;
    private transient WorldMap	map;


    public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
    	this.available = available;
    }
    
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
    	this.speed = speed;
    }

    public int getWaterCapacity() {
        return waterCapacity;
    }

    public void setWaterCapacity(int waterCapacity) {
        this.waterCapacity = waterCapacity;
    }

    public int getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(int fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }
    

    public int getCurrentFuel() {
		return currentFuel;
	}

	public void setCurrentFuel(int currentFuel) {
		this.currentFuel = currentFuel;
	}

	public int getCurrentWater() {
		return currentWater;
	}

	public void setCurrentWater(int currentWater) {
		this.currentWater = currentWater;
	}

	public void moveUpRight() {
    	this.pos.setX(this.pos.getX() + 1);
    	this.pos.setY(this.pos.getY() - 1);
	}

	public void moveUpLeft() {
    	this.pos.setX(this.pos.getX() - 1);
		this.pos.setY(this.pos.getY() - 1);
	}

	public void moveDownLeft() {
		this.pos.setX(this.pos.getX() - 1);
		this.pos.setY(this.pos.getY() + 1);
	}

	public void moveDownRight(){
		this.pos.setX(this.pos.getX() + 1);
		this.pos.setY(this.pos.getY() + 1);
	}

	public void moveRight() {
    	this.pos.setX(this.pos.getX() + 1);
    }
    
    public void moveLeft() {
    	this.pos.setX(this.pos.getX() - 1);
    }
    
    public void moveUp() {
    	this.pos.setY(this.pos.getY() - 1);
    }
    
    public void moveDown() {
    	this.pos.setY(this.pos.getY() + 1);
    }
    
    public void consumeFuel() {
    	this.currentFuel--;
    }

    public void consumeWater() {this.currentWater--; }

    public boolean checkResources() { return true; }


    protected void setup() {
		super.setup();

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getLocalName());
		sd.setType("Fighter");
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		this.available = true;
		addBehaviour(new NotifyOfState());
		
		map = (WorldMap) getArguments()[0];
		
		pos = new Position(map.getDimension()/2, map.getDimension()/2);

		this.addBehaviour(new MoveToFire());
    }

	private class NotifyOfRefill extends OneShotBehaviour{

    	private String message;

    	public NotifyOfRefill(int resource, int quantity){
    		this.message = resource + " " + quantity;
		}

		public void action(){
    		message += " " + this.myAgent.getName();

			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("HeadQuarter");
			dfd.addServices(sd);

			DFAgentDescription[] results;

			try{
				results = DFService.search(this.myAgent, dfd);
				DFAgentDescription result = results[0];

				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

				AID quartel = result.getName();
				msg.addReceiver(quartel);
				msg.setContent(message);
				send(msg);

			} catch (FIPAException e) {
				e.printStackTrace();
			}
			block();
    	}
	}

	private class NotifyOfState extends OneShotBehaviour{

		public void action(){
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("HeadQuarter");
			dfd.addServices(sd);

			DFAgentDescription[] results;

			try{
				results = DFService.search(this.myAgent, dfd);
				DFAgentDescription result = results[0];

				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

				AID quartel = result.getName();
				msg.addReceiver(quartel);

				try{
					msg.setContentObject(this.myAgent);
				} catch (IOException e) {
					e.printStackTrace();
				}
				send(msg);

			} catch (FIPAException e) {
				e.printStackTrace();
			}
			block();
		}

    }

	private class MoveAndNotify extends OneShotBehaviour {
	
	private Position destination;
	private Position checkpoint;
	

	public MoveAndNotify(Fighter f, Position destination, Position checkpoint) {
		this.myAgent = f;
		this.destination = destination;
		if(checkpoint == null)
			this.checkpoint = map.getCheckpoint(new FighterInfo((Fighter) myAgent), destination);
		else
			this.checkpoint = checkpoint;
	}

	public void action() {
		Fighter me = ((Fighter) myAgent);
		Position p = me.getPos();
		Cell c = map.getMap().get(p);

		if(c.isBurning() && me.getCurrentWater() > 0) {
			me.consumeWater();
			Fire fire = map.getFires().values().stream().filter(f -> f.getPos().equals(p)).collect(Collectors.toList()).get(0);

			if(myAgent instanceof Drone)
				fire.setExtinguisher(Configs.AG_DRONE);
			if(myAgent instanceof Aircraft)
				fire.setExtinguisher(Configs.AG_AIRCRAFT);
			if(myAgent instanceof Truck)
				fire.setExtinguisher(Configs.AG_TRUCK);

			myAgent.addBehaviour(new NotifyFireExtinction(fire));
		}

		if (c.isWater() && me.getCurrentWater() < me.getWaterCapacity()){
			int difference = getWaterCapacity() - getCurrentWater();
			me.setCurrentWater(getWaterCapacity());
			addBehaviour(new NotifyOfRefill(Configs.CELL_WATER, difference));
		}

		if (c.isFuel() && me.getCurrentFuel() < me.getFuelCapacity()){
			int difference = getFuelCapacity() - getCurrentFuel();
			me.setCurrentFuel(getFuelCapacity());
			addBehaviour(new NotifyOfRefill(Configs.CELL_FUEL, difference));
		}

		//substituir mais tarde o que está aqui no meio por comportamento inteligente
		if(destination.equals(me.getPos())) {
			if (me.getCurrentWater() > 0){
				ArrayList<Position> poss; FighterInfo fInfo = new FighterInfo(me);
				if(p.getAdjacentDown() != null && map.getMap().get(p.getAdjacentDown()) != null && map.getMap().get(p.getAdjacentDown()).isBurning()) {
					poss = new ArrayList<>(); poss.add(p.getAdjacentDown());
					if(map.inRange(fInfo, poss)) {
						checkpoint = destination = p.getAdjacentDown();
					}
				} else if(p.getAdjacentLeft() != null && map.getMap().get(p.getAdjacentLeft()) != null && map.getMap().get(p.getAdjacentLeft()).isBurning()) {
					poss = new ArrayList<>(); poss.add(p.getAdjacentLeft());
					if(map.inRange(fInfo, poss))
						checkpoint = destination = p.getAdjacentLeft();
				}else if(p.getAdjacentUp() != null && map.getMap().get(p.getAdjacentUp()) != null && map.getMap().get(p.getAdjacentUp()).isBurning()) {
					poss = new ArrayList<>(); poss.add(p.getAdjacentUp());
					if(map.inRange(fInfo, poss))
						checkpoint = destination = p.getAdjacentUp();
				} else if(p.getAdjacentRight() != null && map.getMap().get(p.getAdjacentRight()) != null && map.getMap().get(p.getAdjacentRight()).isBurning()) {
					poss = new ArrayList<>();
					poss.add(p.getAdjacentRight());
					if (map.inRange(fInfo, poss))
						checkpoint = destination = p.getAdjacentRight();
				}else if (me.getCurrentFuel() < me.getFuelCapacity()){
					checkpoint = destination = map.getNearestFuel(new FighterInfo(me));
				} else {
					me.setAvailable(true);
					this.myAgent.addBehaviour(new NotifyOfState());
					block();
					return;
				}
			}
		}

		if(checkpoint.equals(me.getPos())){
			checkpoint = map.getCheckpoint(new FighterInfo(me), destination);
		}

		me.consumeFuel();
		try {
			if (me.getCurrentFuel() > 0) {
				if (p.getX() < checkpoint.getX()) {
					me.moveRight();
				} else if (p.getX() > checkpoint.getX()) {
					me.moveLeft();
				} else if (p.getY() < checkpoint.getY()) {
					me.moveDown();
				} else if (p.getY() > checkpoint.getY()) {
					me.moveUp();
				}
			} else {
				block();
				return;
			}
		} catch (NullPointerException e){
			e.printStackTrace();
		}


		this.myAgent.addBehaviour(new NotifyOfState());

		try {
			Thread.sleep(Configs.TICK_DURATION/me.getSpeed());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		me.addBehaviour(new MoveAndNotify((Fighter) this.myAgent, destination, checkpoint));
		block();
		return;
	}
}

	private class MoveToFire extends CyclicBehaviour{


        public void action(){

            ACLMessage msg = receive();

            if (msg==null) {block(); return;}

            try{
                Object contentObject = msg.getContentObject();
                switch (msg.getPerformative()) {
                    case(ACLMessage.PROPOSE):
                        if(contentObject instanceof Fire){
                        	Fire f = (Fire) contentObject;
                            Position destination = f.getPos();
							if(!((Fighter) this.myAgent).isAvailable())
								return;
                            ((Fighter) this.myAgent).setAvailable(false);

							DFAgentDescription dfd = new DFAgentDescription();
							ServiceDescription sd = new ServiceDescription();
							sd.setType("HeadQuarter");
							dfd.addServices(sd);

							DFAgentDescription[] results;

							try{
								results = DFService.search(myAgent, dfd);
								DFAgentDescription result = results[0];

								ACLMessage resp = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);

								AID quartel = result.getName();
								resp.addReceiver(quartel);


								try {
									resp.setContentObject(destination);
								} catch (IOException e) {
									e.printStackTrace();
									((Fighter) this.myAgent).setAvailable(false);
								}

								send(resp);

							} catch (FIPAException e) {
								e.printStackTrace();
								((Fighter) this.myAgent).setAvailable(true);
							}

                            myAgent.addBehaviour(new MoveAndNotify((Fighter) this.myAgent, destination, null));
                        }
                }
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }
}

	private class NotifyFireExtinction extends OneShotBehaviour {

    	private Fire fire;
    	public  NotifyFireExtinction(Fire f) {
    		this.fire = f;
		}

		@Override
		public void action() {
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("HeadQuarter");
			dfd.addServices(sd);

			DFAgentDescription[] results;

			try{
				results = DFService.search(this.myAgent, dfd);
				DFAgentDescription result = results[0];
				Integer performative = ACLMessage.INFORM;
				ACLMessage msg = new ACLMessage(performative);

				AID quartel = result.getName();
				msg.addReceiver(quartel);

				try{
					msg.setContentObject(this.fire);
				} catch (IOException e) {
					e.printStackTrace();
				}
				send(msg);

			} catch (FIPAException e) {
				e.printStackTrace();
			}
		}
	}
}
