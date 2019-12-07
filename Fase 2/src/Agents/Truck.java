package Agents;

import Graphics.Configs;

public class Truck extends Fighter {

    protected void setup() {
		super.setup();

		this.setSpeed(Configs.TRUCK_SPEED);
		this.setWaterCapacity(Configs.TRUCK_WATER);
		this.setFuelCapacity(Configs.TRUCK_FUEL);
		this.setCurrentFuel(this.getFuelCapacity());
		this.setCurrentWater(this.getWaterCapacity());

	}
}
