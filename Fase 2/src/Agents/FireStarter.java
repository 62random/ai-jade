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
import javafx.util.Pair;

public class Incendiario extends Agent {

    private static Pair<Integer, Integer> posicao;
    private static int intensidade;

    protected void setup() {
		super.setup();
		addBehaviour(new InformaIncendio());
	}
    
    public static Pair<Integer, Integer> getPosicao() {
        return posicao;
    }

    public static void setPosicao(Pair<Integer, Integer> posicao) {
        Incendiario.posicao = posicao;
    }

    public static int getIntensidade() {
        return intensidade;
    }

    public static void setIntensidade(int intensidade) {
        Incendiario.intensidade = intensidade;
    }
    
    // Classe que permite informar incêndios ao Quartel
    private class InformaIncendio extends OneShotBehaviour{
    	
    	public void action(){
    		DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("Quartel");
			dfd.addServices(sd);
			
			DFAgentDescription[] results;
			
			try{
				results = DFService.search(this.myAgent, dfd);
				DFAgentDescription result = results[0];
				
				Incendiario inc = new Incendiario();
				inc.setPosicao(posicao);
				inc.setIntensidade(intensidade);
				
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
    	}
    }
    
}
