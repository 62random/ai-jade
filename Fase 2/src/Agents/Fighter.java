package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

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
		addBehaviour(new NotifyOfExistence());
		
		WorldMap map = (WorldMap) getArguments()[0];
		
		pos = new Position(map.getDimension()/2, map.getDimension()/2);

		addBehaviour(new MoveToFire());

    }

    
private class NotifyOfExistence extends OneShotBehaviour{
    	
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
    	}
    }

private class MoveAndNotify extends OneShotBehaviour {
	
	private Position destination;
	private Boolean available;
	
	public MoveAndNotify(Position p,boolean availability) {
		super();
		this.destination = p;
		this.available = availability;
	}

	@Override
	public void action() {
		Integer performative = 0;

		if (destination.equals(((Fighter) myAgent).getPos())) {
			performative = ACLMessage.CONFIRM;
			block();
		}
		else {
			Fighter me = ((Fighter) myAgent);
			me.setAvailable(available);
			Position p = me.getPos();
			performative = ACLMessage.ACCEPT_PROPOSAL;

			//substituir mais tarde o que está aqui no meio por comportamento inteligente
			if(p.getX() < destination.getX() ) {
				me.moveRight();
			}
			else if(p.getX() > destination.getX()) {
				me.moveLeft();
			}
			else if(p.getY() < destination.getY()) {
				me.moveDown();
			}
			else {
				me.moveUp();
			}
			//substituir mais tarde o que está aqui no meio por comportamento inteligente

			me.consumeFuel();

			//TODO se passou num incêndio apaga-o

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			me.addBehaviour(new MoveAndNotify(destination,available));

		}

		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("HeadQuarter");
		dfd.addServices(sd);

		DFAgentDescription[] results;

		try{
			results = DFService.search(this.myAgent, dfd);
			DFAgentDescription result = results[0];

			ACLMessage msg = new ACLMessage(performative);

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
                            Position destination = (((Fire) contentObject).getPos());
                            addBehaviour(new MoveAndNotify(destination,false));
                        }
                }
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }
}
}
