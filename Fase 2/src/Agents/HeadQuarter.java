package Agents;

import Graphics.Configs;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import World.*;
import jade.tools.sniffer.Message;

public class HeadQuarter extends Agent {
	
	private WorldMap map;
	private Position pos;
	private ArrayDeque<Fire> queue;
	
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
		queue = new ArrayDeque<Fire>();

		addBehaviour(new CheckFireQueue(this));
	}
    
	private class ReceiveInfo extends CyclicBehaviour {

		public void action(){
				
			ACLMessage msg = receive();
			
			if (msg==null) {block(); return;}

				Object contentObject;
				try {
					contentObject = msg.getContentObject();
				} catch (UnreadableException e){
					contentObject = msg.getContent();
				}
				switch(msg.getPerformative()) {
					case(ACLMessage.INFORM):
						if (contentObject instanceof  Fire){
							Fire fire = (Fire) contentObject;
							map.extinguishFire(fire);
							addBehaviour(new SendFireStats(fire));
							System.out.println("Fire on position " + fire.getPos() + " was extinguished");
						}
						else if (contentObject instanceof  String) {
							String str = (String) contentObject;
							try{
								DFAgentDescription dfd = new DFAgentDescription();
								ServiceDescription sd = new ServiceDescription();
								sd.setType("Analyst");
								dfd.addServices(sd);

								DFAgentDescription[] results = DFService.search(this.myAgent, dfd);

								if (results.length > 0) {
									ACLMessage forward = new ACLMessage(ACLMessage.INFORM);
									forward.addReceiver(results[0].getName());

									try{
										forward.setContentObject(str);
									} catch (IOException e) {
										e.printStackTrace();
									}
									send(forward);
								}
							} catch (FIPAException e) {
								e.printStackTrace();
							}

						}
						else if(contentObject instanceof Fighter) {
							FighterInfo fInfo = new FighterInfo(((Fighter) contentObject));
							map.addFighter(fInfo);
							if (fInfo.isAvailable()) {
								System.out.println("Agent " + ((Fighter) contentObject).getName() + " available at " + fInfo.getPos());
							} else {
								System.out.println("Agent " + ((Fighter) contentObject).getName() + " moved to " + fInfo.getPos());
							}
						}
						if(contentObject instanceof FireStarter) {
							Fire fire = new Fire(((FireStarter) contentObject).getPos(),((FireStarter) contentObject).getIntensity());
							queue.add(fire);
							map.addFire(fire);
							System.out.println("Cell on position " + ((FireStarter) contentObject).getPos() + " is burning!");
						}
						break;
					case(ACLMessage.ACCEPT_PROPOSAL):
						if(contentObject instanceof Fire) {
							FighterInfo fInfo = map.getFighters().get(msg.getSender());
							if (fInfo != null) {
								fInfo.setAvailable(false);
								map.changeFighterData(fInfo.getAID(), fInfo);
								queue.remove(contentObject);
							}
						}
						break;
				}
		}
	}

	private class HandlerCheckCombatentes extends OneShotBehaviour{

		private Fire targetFire;
		
		public HandlerCheckCombatentes(Fire f){
			this.targetFire = f;
		}

		public void action() {
			Map<String,FighterInfo> fighters = map.getFighters();
			Map<String,Double> closestFighters = map.calculateClosestFighters(targetFire.getPos());
			String chosenFighter = null;


			for (String fighter : closestFighters.keySet()) {
				if(fighters.get(fighter).isAvailable() && map.inRange(fighters.get(fighter), targetFire.getPos())) {
					chosenFighter = fighter;
					break;
				}
			}
			if(chosenFighter == null) {
				queue.add(targetFire);
				block();
				return;
			}




			try{
				DFAgentDescription dfd = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("Fighter");
				dfd.addServices(sd);

				DFAgentDescription[] results = DFService.search(this.myAgent, dfd);
				AID provider = new AID();

				if (results.length > 0) {
					for (DFAgentDescription dfd1 : results) {
						provider = dfd1.getName();
						if (provider.getName().equals(chosenFighter))
							break;
					}

					System.out.println("Requesting help from fighter: " + provider.getName());
					ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
					msg.addReceiver(provider);

					try{
						msg.setContentObject(targetFire);
					} catch (IOException e) {
						e.printStackTrace();
					}
					send(msg);

				}
				else {
					System.out.println("Fighter " + chosenFighter + " not found!");
				}
			} catch (FIPAException e) {
				e.printStackTrace();
			}
		}
		
	}

	private class SendFireStats extends OneShotBehaviour{

		private Fire fire;

		public SendFireStats(Fire fire){
			this.fire = fire;
		}

		@Override
		public void action() {
				DFAgentDescription dfd = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("Analyst");
				dfd.addServices(sd);

				DFAgentDescription[] results;

				try{
					results = DFService.search(this.myAgent, dfd);
					DFAgentDescription result = results[0];


					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

					AID quartel = result.getName();
					msg.addReceiver(quartel);

					try{
						msg.setContentObject(fire);
					} catch (IOException e) {
						e.printStackTrace();
					}
					send(msg);

				} catch (FIPAException e) {
					e.printStackTrace();
				}
			}
	}


	private class CheckFireQueue extends TickerBehaviour {

		public CheckFireQueue(Agent a) {
			super(a, Configs.TICK_DURATION/5);
		}

		@Override
		protected void onTick() {
			if (queue.size() > 0){
				this.myAgent.addBehaviour(new HandlerCheckCombatentes(queue.pop()));
			}
		}
	}
}
