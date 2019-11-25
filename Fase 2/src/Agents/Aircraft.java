package Agents;

import java.util.ArrayList;

public class Aircraft extends Fighter {  
    
    protected void setup() {
		super.setup();

		this.setSpeed(2);
		this.setWaterCapacity(15);
		this.setFuelCapacity(20);
		this.setCurrentFuel(this.getFuelCapacity());
		this.setCurrentWater(this.getWaterCapacity());

	}
}
