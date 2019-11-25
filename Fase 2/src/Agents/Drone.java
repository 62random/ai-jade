package Agents;

public class Drone extends Fighter {

    protected void setup() {
		super.setup();

		this.setSpeed(10);
		this.setWaterCapacity(2);
		this.setFuelCapacity(5);
		this.setCurrentFuel(this.getFuelCapacity());
		this.setCurrentWater(this.getWaterCapacity());

	}
}
