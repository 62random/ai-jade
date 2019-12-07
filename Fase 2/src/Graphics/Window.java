package Graphics;

import World.WorldMap;

import javax.swing.*;

public class Window extends JFrame implements Runnable{

    WorldMap world;
    Map     map;

    public Window(WorldMap world) {

        this.world = world;
        setTitle("AI 2019");
        setSize(1000, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        map = new Map(this.world);

        add(map);

        setVisible(true);

    }

    @Override
    public void run() {
        while (true){
            this.repaint();
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
