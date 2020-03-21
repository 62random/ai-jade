package Agents;

import Graphics.Configs;

public class Drone extends Fighter {

    protected void setup() {
		super.setup();

		this.setSpeed(Configs.DRONE_SPEED);
		this.setWaterCapacity(Configs.DRONE_WATER);
		this.setFuelCapacity(Configs.DRONE_FUEL);
		this.setCurrentFuel(this.getFuelCapacity());
		this.setCurrentWater(this.getWaterCapacity());

	}
}
