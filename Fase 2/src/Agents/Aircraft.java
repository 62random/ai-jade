package Agents;

import java.util.ArrayList;

public class Aircraft extends Fighter {  
    
    protected void setup() {
		super.setup();

		this.setSpeed(2);
		this.setWaterCapacity(15);
		this.setFuelCapacity(20);
		
		//exemplo da ficha da loja
		//addBehaviour(new Comprarproduto()); // fazer a compra de um produto
	}
}
