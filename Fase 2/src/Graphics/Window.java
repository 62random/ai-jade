package Graphics;

import World.WorldMap;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame implements Runnable{

    WorldMap world;
    Map     map;

    public Window(WorldMap world) {

        this.world = world;
        setTitle("AI 2019");
        setSize(1000, 625);
        Dimension dim = new Dimension(500, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        Map map = new Map(world);
        Stats stats = new Stats();
        map = new Map(this.world);

        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, map, stats);

        sp.setDividerLocation(605);
        getContentPane().add(sp);
        sp.setOneTouchExpandable(true);
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
