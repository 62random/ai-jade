package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.io.Serializable;

import World.Position;
import World.*;

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
    	this.pos.setX(this.pos.getY() - 1);
    }
    
    public void moveDown() {
    	this.pos.setX(this.pos.getY() + 1);
    }
    
    public void consumeFuel() {
    	this.currentFuel--;
    }
    
    
    protected void setup() {
		super.setup();

		this.available = true;
		addBehaviour(new NotifyOfExistence());
		
		WorldMap map = (WorldMap) getArguments()[0];
		
		pos = new Position(map.getDimension()/2, map.getDimension()/2);
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

private class MoveAndNotify extends OneShotBehaviour{
	
	private Position destination;
	
	public MoveAndNotify(Position p) {
		super();
		this.destination = p;
	}
	
	public void action(){	
		
		if (destination.equals(((Fighter) myAgent).getPos())) {
			block();
		}
		else {
			Fighter me = ((Fighter) myAgent);
			Position p = me.getPos();
			
			//substituir mais tarde o que está aqui no meio por comportamento inteligente
			if(p.getX() > destination.getX() ) {
				me.moveRight();
			}
			else if(p.getX() < destination.getX()) {
				me.moveLeft();
			}
			else if(p.getY() > destination.getY()) {
				me.moveDown();
			}
			else {
				me.moveUp();
			}
			//substituir mais tarde o que está aqui no meio por comportamento inteligente
			
			me.consumeFuel();
			
			//TODO se passou num incêndio apaga-o
		
		}
		
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("HeadQuarter");
		dfd.addServices(sd);
		
		DFAgentDescription[] results;
		
		try{
			results = DFService.search(this.myAgent, dfd);
			DFAgentDescription result = results[0];
							
			ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
			
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
}
