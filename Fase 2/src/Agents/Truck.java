package Agents;

public class Truck extends Fighter {

    protected void setup() {
		super.setup();

		this.setSpeed(5);
		this.setWaterCapacity(10);
		this.setFuelCapacity(15);
		
		//exemplo da ficha da loja
		//addBehaviour(new Comprarproduto()); // fazer a compra de um produto
	}
}
