package Main;

import Graphics.Configs;
import Graphics.Window;
import World.Position;
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
	Window window;
	Object[] objs;
	int numFire = 0;
	ContainerController cc;

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
		WorldMap map 		= new WorldMap(Configs.MAP_SIZE, a);
		a.objs 				= new Object[1];
		a.objs[0] 			= map;

		
		a.initMainContainerInPlatform("localhost", "9888", "MainContainer");		
		a.cc = a.initContainerInPlatform("localhost", "9888", "World Container");

		//Criar HeadQuarter e Analyst
		try {
			AgentController ag = a.cc.createNewAgent("HeadQuarter", "Agents.HeadQuarter", a.objs);// arguments
			ag.start();
			ag = a.cc.createNewAgent("Analyst", "Agents.Analyst", a.objs);// arguments
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
		
		for(int i = 0; i < Configs.NUM_TRUCKS; i++) {
			try {
				AgentController ag = a.cc.createNewAgent("Truck_" + i, "Agents.Truck", a.objs);// arguments
				ag.start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
		
		for(int i = 0; i < Configs.NUM_DRONES; i++) {
			try {
				AgentController ag = a.cc.createNewAgent("Drone_" + i, "Agents.Drone", a.objs);// arguments
				ag.start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
		
		for(int i = 0; i < Configs.NUM_AIRCRAFTS; i++) {
			try {
				AgentController ag = a.cc.createNewAgent("Aircraft_" + i, "Agents.Aircraft", a.objs);// arguments
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

		a.window = new Window(map);
        Thread t = new Thread(a.window);
        t.start();



		while(a.numFire < 50000) {
			AgentController ag;
			try {
				ag = a.cc.createNewAgent("FireStarter_"+ a.numFire++, "Agents.FireStarter", a.objs);
				ag.start();
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// arguments

			try {
				Thread.sleep(Configs.TICK_DURATION*5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			map.propagateFire();
		}
		

	}


    public void newFire(Position pos) {
		AgentController ag;
		Object[] objs2 = new Object[2];
		objs2[0] = objs[0];

		objs2[1] = pos;
		try {
			ag = cc.createNewAgent("FireStarter_"+ numFire++, "Agents.FireStarter", objs2);
			ag.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
    }
}
