package Graphics;

import World.WorldMap;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame implements Runnable{

    private WorldMap world;
    private Map     map;
    private Stats stats;
    private JSplitPane sp;

    public Window(WorldMap world, Statistics.Stats s) {

        this.world = world;
        setTitle("AI 2019");
        setSize(Configs.WINDOW_WIDTH, Configs.WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        map = new Map(this.world);
        stats = new Stats(s);

        sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, map, stats);
        sp.setDividerLocation(Configs.WINDOW_HEIGHT - 30);
        getContentPane().add(sp);
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
