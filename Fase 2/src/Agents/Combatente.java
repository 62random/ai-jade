package Agents;

import jade.core.Agent;
import javafx.util.Pair;

public class Combatente extends Agent {

    private static Pair<Integer, Integer> pair;
    private static Boolean disponibilidade;

    public static Pair<Integer, Integer> getPair() {
        return pair;
    }

    public static void setPair(Pair<Integer, Integer> pair) {
        Combatente.pair = pair;
    }

    public static Boolean getDisponibilidade() {
        return disponibilidade;
    }

    public static void setDisponibilidade(Boolean disponibilidade) {
        Combatente.disponibilidade = disponibilidade;
    }
}
