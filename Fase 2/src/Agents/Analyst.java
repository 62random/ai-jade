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

        stats = new Stats();

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
                            int resource = Integer.parseInt(((String) contentObject).split(" ")[0]);
                            int quantity = Integer.parseInt(((String) contentObject).split(" ")[1]);

                            switch (resource){
                                case Configs.CELL_WATER:
                                    stats.incrementWaterRefills(quantity);
                                    break;
                                case Configs.CELL_FUEL:
                                    stats.incrementFuelRefills(quantity);
                                    break;
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
}
