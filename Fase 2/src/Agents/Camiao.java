package Agents;

public class Camiao extends Combatente {

    private static int velocidade = 5;
    private static int capAgua = 10;
    private static int capCombustivel = 15;

    public static int getVelocidade() {
        return velocidade;
    }

    public static void setVelocidade(int velocidade) {
        Camiao.velocidade = velocidade;
    }

    public static int getCapAgua() {
        return capAgua;
    }

    public static void setCapAgua(int capAgua) {
        Camiao.capAgua = capAgua;
    }

    public static int getCapCombustivel() {
        return capCombustivel;
    }

    public static void setCapCombustivel(int capCombustivel) {
        Camiao.capCombustivel = capCombustivel;
    }
}
