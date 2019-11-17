package Agents;

public class Aeronave extends Combatente {

    private static int velocidade = 2;
    private static int capAgua = 15;
    private static int capCombustivel = 20;

    public static int getVelocidade() {
        return velocidade;
    }

    public static void setVelocidade(int velocidade) {
        Aeronave.velocidade = velocidade;
    }

    public static int getCapAgua() {
        return capAgua;
    }

    public static void setCapAgua(int capAgua) {
        Aeronave.capAgua = capAgua;
    }

    public static int getCapCombustivel() {
        return capCombustivel;
    }

    public static void setCapCombustivel(int capCombustivel) {
        Aeronave.capCombustivel = capCombustivel;
    }
}
