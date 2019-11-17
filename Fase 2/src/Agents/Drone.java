package Agents;

public class Drone extends Fighter {

    protected void setup() {
		super.setup();

		this.setSpeed(10);
		this.setWaterCapacity(2);
		this.setFuelCapacity(5);
		
		//exemplo da ficha da loja
		//addBehaviour(new Comprarproduto()); // fazer a compra de um produto
	}
}
