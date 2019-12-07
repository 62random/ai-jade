package Agents;

import Graphics.Configs;
import Statistics.Stats;
import World.FighterInfo;
import World.Fire;
import World.Position;
import World.WorldMap;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class Analyst extends Agent {
    private Stats stats;


    protected void setup() {
        super.setup();

        this.stats = (Stats) getArguments()[0];

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("Analyst");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new ReceiveStats());
    }

    private class ReceiveStats extends CyclicBehaviour {

        public void action(){

            ACLMessage msg = receive();

            if (msg==null) {block(); return;}

            try{
                Object contentObject = msg.getContentObject();
                switch(msg.getPerformative()) {
                    case(ACLMessage.INFORM):
                        if(contentObject instanceof Fire) {
                            Fire f = (Fire) contentObject;
                            if(!f.isActive()){
                                stats.extinguishedFire(f);
                            }
                        }
                        else if (contentObject instanceof  String){
                            System.out.println((String) contentObject);
                            int resource = Integer.parseInt(((String) contentObject).split(" ")[0]);
                            int quantity = Integer.parseInt(((String) contentObject).split(" ")[1]);
                            String agent = (((String) contentObject).split(" "))[2];

                            switch (resource){
                                case Configs.CELL_WATER:
                                    stats.incrementWaterRefills(quantity);
                                    System.out.println("Agent " + agent + " refilled water!");
                                    break;
                                case Configs.CELL_FUEL:
                                    stats.incrementFuelRefills(quantity);
                                    System.out.println("Agent " + agent + " refilled fuel!");
                                    break;
                            }
                        }
                        break;
                }
            }catch (UnreadableException e) {
                e.printStackTrace();
            }
        }
    }
}
