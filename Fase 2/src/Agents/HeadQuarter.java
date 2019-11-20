package Agents;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.io.IOException;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import World.*;

public class HeadQuarter extends Agent {
	
	private WorldMap map;
	private Position pos;
	
	protected void setup() {
		super.setup();
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getLocalName());
		sd.setType("HeadQuarter");
		dfd.addServices(sd);
		
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		
		addBehaviour(new ReceiveInfo());
		
		map = (WorldMap) getArguments()[0];
		
		pos = new Position(map.getDimension()/2, map.getDimension()/2);
	}
    
	private class ReceiveInfo extends CyclicBehaviour {

		public void action(){
				
			ACLMessage msg = receive();
			
			if (msg==null) {block(); return;}
			
			try{
				Object contentObject = msg.getContentObject();
				switch(msg.getPerformative()) {
					case(ACLMessage.INFORM):
						if(contentObject instanceof Fighter) {
							FighterInfo fInfo = new FighterInfo(((Fighter) contentObject).getName(),((Fighter) contentObject).getPos(),((Fighter) contentObject).isAvailable());
							map.addFighter(fInfo);
							System.out.println("Added agent " + ((Fighter) contentObject).getName());
						}
						if(contentObject instanceof FireStarter) {
							map.changeCellStatus(((FireStarter) contentObject).getPos(),true);
							System.out.println("Cell on position " + ((FireStarter) contentObject).getPos() + " is burning!");
							addBehaviour(new HandlerCheckCombatentes(myAgent));
						}
					case(ACLMessage.CONFIRM):
						if(contentObject instanceof Fighter) {
							
						}
					/*case(ACLMessage.CONFIRM):
							addBehaviour(new HandlerEscolheCombatente(myAgent,msg));
					case(ACLMessage.ACCEPT_PROPOSAL):
							addBehaviour(new HandlerEnviaCombatente(myAgent,msg)); */
				} 
			}catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private class HandlerCheckCombatentes extends OneShotBehaviour{
		
		
		public HandlerCheckCombatentes(Agent a){
			super(a);
		}

		public void action() {
	
		}
		
	}

}
