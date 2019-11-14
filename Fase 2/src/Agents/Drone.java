package Agents;

public class Drone extends Combatente {

    private static int velocidade = 10;
    private static int capAgua = 2;
    private static int capCombustivel = 5;

    public static int getVelocidade() {
        return velocidade;
    }

    public static void setVelocidade(int velocidade) {
        Drone.velocidade = velocidade;
    }

    public static int getCapAgua() {
        return capAgua;
    }

    public static void setCapAgua(int capAgua) {
        Drone.capAgua = capAgua;
    }

    public static int getCapCombustivel() {
        return capCombustivel;
    }

    public static void setCapCombustivel(int capCombustivel) {
        Drone.capCombustivel = capCombustivel;
    }
}
