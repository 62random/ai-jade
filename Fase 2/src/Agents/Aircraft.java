package Agents;

import Graphics.Configs;

import java.util.ArrayList;

public class Aircraft extends Fighter {  
    
    protected void setup() {
		super.setup();

		this.setSpeed(Configs.AIRCRAFT_SPEED);
		this.setWaterCapacity(Configs.AIRCRAFT_WATER);
		this.setFuelCapacity(Configs.AIRCRAFT_FUEL);
		this.setCurrentFuel(this.getFuelCapacity());
		this.setCurrentWater(this.getWaterCapacity());

	}
}
