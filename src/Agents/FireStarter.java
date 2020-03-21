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
import java.util.Random;

import World.Position;
import World.WorldMap;;

public class FireStarter extends Agent {

    private Position 	pos;
    private int 		intensity;

    public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public int getIntensity() {
		return intensity;
	}

	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}

	protected void setup() {
		super.setup();
		addBehaviour(new FireNotification());
		
		Random r = new Random();
		
		WorldMap map = (WorldMap) getArguments()[0];

		if(getArguments().length == 2)
			pos = (Position) getArguments()[1];
		else
			pos = new Position(r.nextInt(map.getDimension()), r.nextInt(map.getDimension()));

		intensity = r.nextInt(6);
	}
    
    // Classe que permite informar inc�ndios ao Quartel
    private class FireNotification extends OneShotBehaviour{
    	
    	public void action(){
    		DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("HeadQuarter");
			dfd.addServices(sd);
			
			DFAgentDescription[] results;
			
			try{
				results = DFService.search(this.myAgent, dfd);
				DFAgentDescription result = results[0];
				
				FireStarter inc = new FireStarter();
				inc.setPos(pos);
				inc.setIntensity(intensity);
				
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				
				AID quartel = result.getName();
				msg.addReceiver(quartel);
				
				try{
					msg.setContentObject(inc);
				} catch (IOException e) {
					e.printStackTrace();
				}
				send(msg);
				
				} catch (FIPAException e) {
					e.printStackTrace();
				}

			myAgent.doDelete();
    	}
    }
    
}
