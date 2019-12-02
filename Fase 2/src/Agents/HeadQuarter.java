package Agents;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import World.*;
import jade.tools.sniffer.Message;

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
							map.setnBurningCells(map.getnBurningCells());
							Fire fire = new Fire(((FireStarter) contentObject).getPos(),((FireStarter) contentObject).getIntensity());
							map.addFire(fire);
							map.changeCellStatus(((FireStarter) contentObject).getPos(),true);
							System.out.println("Cell on position " + ((FireStarter) contentObject).getPos() + " is burning!");
							addBehaviour(new HandlerCheckCombatentes(map.getnBurningCells()));
						}
						break;
					case(ACLMessage.CONFIRM):
						if(contentObject instanceof Fighter) {
							FighterInfo fInfo = map.getFighters().get(((Fighter) contentObject).getName());
							if (fInfo != null && !((Fighter) contentObject).isAvailable()) {
								fInfo.setPos(((Fighter) contentObject).getPos());
								fInfo.setAvailable(((Fighter) contentObject).isAvailable());
								map.changeFighterData(fInfo.getAID(), fInfo);
								System.out.println("Agent " + fInfo.getAID() + " moved to " + fInfo.getPos());
							}
						}
						break;
					case(ACLMessage.PROPOSE):
						if(contentObject instanceof Fighter) {
							FighterInfo fInfo = map.getFighters().get(((Fighter) contentObject).getName());
							if (fInfo != null) {
								fInfo.setAvailable(((Fighter) contentObject).isAvailable());
								map.changeFighterData(fInfo.getAID(), fInfo);
								for (Fire f : map.getFires().values()) {
									if (f.getPos().equals(fInfo.getPos())) {
										map.extinguishFire(f);
									}
								}
								System.out.println("Fire on position " + fInfo.getPos() + " was extinguished");
							}
						}
						break;
				} 
			}catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private class HandlerCheckCombatentes extends OneShotBehaviour{

		private int fireID;
		
		public HandlerCheckCombatentes(int fireID){
			this.fireID = fireID;
		}

		public void action() {
			Fire targetFire = map.getFires().get(fireID);
			Map<String,FighterInfo> fighters = map.getFighters();
			Map<String,Double> closestFighters = map.calculateClosestFighters(targetFire.getPos());
			String chosenFighter = null;

			while(chosenFighter == null) {
				for (String fighter : closestFighters.keySet()) {
					if(fighters.get(fighter).isAvailable()) {
						chosenFighter = fighter;
						break;
					}
				}
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
						if (provider.getName().equals(chosenFighter)) break;
					}

					System.out.println("Requesting help from fighter: " + provider.getName());
					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
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

}
