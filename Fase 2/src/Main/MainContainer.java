package Main;

import World.WorldMap;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class MainContainer {
	
	Runtime rt;
	ContainerController container;

	public ContainerController initContainerInPlatform(String host, String port, String containerName) {
		// Get the JADE runtime interface (singleton)
		this.rt = Runtime.instance();

		// Create a Profile, where the launch arguments are stored
		Profile profile = new ProfileImpl();
		profile.setParameter(Profile.CONTAINER_NAME, containerName);
		profile.setParameter(Profile.MAIN_HOST, host);
		profile.setParameter(Profile.MAIN_PORT, port);
		// create a non-main agent container
		ContainerController container = rt.createAgentContainer(profile);
		return container;
	}
	
	public void initMainContainerInPlatform(String host, String port, String containerName) {

		// Get the JADE runtime interface (singleton)
		this.rt = Runtime.instance();

		// Create a Profile, where the launch arguments are stored
		Profile prof = new ProfileImpl();
		prof.setParameter(Profile.CONTAINER_NAME, containerName);
		prof.setParameter(Profile.MAIN_HOST, host);
		prof.setParameter(Profile.MAIN_PORT, port);
		prof.setParameter(Profile.MAIN, "true");
		prof.setParameter(Profile.GUI, "true");

		// create a main agent container
		this.container = rt.createMainContainer(prof);
		rt.setCloseVM(true);

	}
	
	
	public void startAgentInPlatform(String name, String classpath) {
		try {
			AgentController ac = container.createNewAgent(name, classpath, new Object[0]);
			ac.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MainContainer a 	= new MainContainer();
		WorldMap map 		= new WorldMap(200);
		Object[] objs 		= new Object[1];
		objs[0] = map;
		
		a.initMainContainerInPlatform("localhost", "9888", "MainContainer");		
		ContainerController c = a.initContainerInPlatform("localhost", "9888", "World Container");
		
		try {
			AgentController ag = c.createNewAgent("HeadQuarter", "Agents.HeadQuarter", objs);// arguments
			ag.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(int i = 0; i < 5; i++) {
			try {
				AgentController ag = c.createNewAgent("Truck_" + i, "Agents.Truck", objs);// arguments
				ag.start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
		
		for(int i = 0; i < 10; i++) {
			try {
				AgentController ag = c.createNewAgent("Drone_" + i, "Agents.Drone", objs);// arguments
				ag.start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
		
		for(int i = 0; i < 2; i++) {
			try {
				AgentController ag = c.createNewAgent("Aircraft_" + i, "Agents.Aircraft", objs);// arguments
				ag.start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		int numFire = 0;
		while(numFire < 1000) {
			AgentController ag;
			try {
				ag = c.createNewAgent("FireStarter_"+numFire++, "Agents.FireStarter", objs);
				ag.start();
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// arguments
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

	}

	
}
