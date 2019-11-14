package Agents;

import jade.core.Agent;
import javafx.util.Pair;

public class Combatente extends Agent {

    private static int velocidade;
    private static int capAgua;
    private static int capCombustivel;
    private static Pair<Integer, Integer> pair;
    private static Boolean disponibilidade;

    public static int getVelocidade() {
        return velocidade;
    }

    public static void setVelocidade(int velocidade) {
        Combatente.velocidade = velocidade;
    }

    public static int getCapAgua() {
        return capAgua;
    }

    public static void setCapAgua(int capAgua) {
        Combatente.capAgua = capAgua;
    }

    public static int getCapCombustivel() {
        return capCombustivel;
    }

    public static void setCapCombustivel(int capCombustivel) {
        Combatente.capCombustivel = capCombustivel;
    }

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
