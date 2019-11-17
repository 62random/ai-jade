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

public class Quartel extends Agent {
	
	protected void setup() {
		super.setup();
		addBehaviour(new ReceberInfoIncendio());
	}
    /*	
	private class ReceberInfoIncendio extends CyclicBehaviour {
		
		public void action(){
			ACLMessage msg= receive();
			
			if (msg==null) {block(); return;}
			
			try{
				Object contentObject = msg.getContentObject();
				switch(msg.getPerformative()) {
					case(ACLMessage.INFORM):
						addBehaviour(new HandlerCheckCombatentes(myAgent));
					case(ACLMessage.CONFIRM):
						addBehaviour(new HandlerEscolheCombatente(myAgent,msg));
					case(ACLMessage.ACCEPT_PROPOSAL):
						addBehaviour(new HandlerEnviaCombatente(myAgent,msg));
				}
			} catch (UnreadableException e) {
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
			try{
			
			}
		}
	}
	*/
}
