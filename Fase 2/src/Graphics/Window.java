package Graphics;

import World.WorldMap;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame implements Runnable{

    private WorldMap world;
    private Map     map;
    private Stats stats;

    public Window(WorldMap world, Statistics.Stats s) {

        this.world = world;
        setTitle("AI 2019");
        setSize(1000, 625);
        Dimension dim = new Dimension(500, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        map = new Map(this.world);
        stats = new Stats(s);

        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, map, stats);



        sp.setDividerLocation(605);
        getContentPane().add(sp);
        sp.setOneTouchExpandable(true);
        setVisible(true);

    }

    public Window() {

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
