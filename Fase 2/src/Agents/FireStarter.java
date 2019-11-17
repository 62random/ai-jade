package Agents;

import jade.core.Agent;
import javafx.util.Pair;

public class Incendiario extends Agent {

    private static Pair<Integer, Integer> pair;
    private static int intensidade;

    public static Pair<Integer, Integer> getPair() {
        return pair;
    }

    public static void setPair(Pair<Integer, Integer> pair) {
        Incendiario.pair = pair;
    }

    public static int getIntensidade() {
        return intensidade;
    }

    public static void setIntensidade(int intensidade) {
        Incendiario.intensidade = intensidade;
    }
}
