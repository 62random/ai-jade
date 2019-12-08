package Graphics;

import World.WorldMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Map extends JPanel {

    WorldMap world;

    private BufferedImage grass_image, water_image, fire_image, truck_image, aircraft_image, drone_image, fuel_image, headquarter_image;

    public Map(WorldMap world) {
        try {
            grass_image = ImageIO.read(new File(Configs.IMG_GRASS));
            water_image = ImageIO.read(new File(Configs.IMG_WATER));
            fire_image = ImageIO.read(new File(Configs.IMG_FIRE));
            truck_image = ImageIO.read(new File(Configs.IMG_TRUCK));
            aircraft_image = ImageIO.read(new File(Configs.IMG_AIRCRAFT));
            drone_image = ImageIO.read(new File(Configs.IMG_DRONE));
            fuel_image = ImageIO.read(new File(Configs.IMG_FUEL));
            headquarter_image = ImageIO.read(new File(Configs.IMG_HEADQUARTER));
            this.world = world;
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace().toString());
        }
        setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i = 0; i < Configs.MAP_SIZE ; i++)
            for (int j = 0; j < Configs.MAP_SIZE; j++)
                drawCell(g, i, j);

    }

    private void drawCell(Graphics g, int i, int j){

        int x = Configs.CELL_DIMENSION*i, y = Configs.CELL_DIMENSION*j;

        g.drawImage(grass_image, x, y, this);
        if(i == Configs.MAP_SIZE/2 && j == Configs.MAP_SIZE/2) {
            g.drawImage(headquarter_image, x, y, this);
        }
        else {
            ArrayList<Integer> list = world.cellPropertiesOn(i, j);
            if (list.contains(Configs.CELL_FUEL)) {
                g.drawImage(fuel_image, x, y, this);
            }
            if (list.contains(Configs.CELL_WATER)) {
                g.drawImage(water_image, x, y, this);
            }
            if (list.contains(Configs.CELL_FIRE)) {
                g.drawImage(fire_image, x, y, this);
            }

            list = world.agentsOn(i, j);

            if (list.contains(Configs.AG_TRUCK)) {
                g.drawImage(truck_image, x, y, this);
            }
            if (list.contains(Configs.AG_AIRCRAFT)) {
                g.drawImage(aircraft_image, x, y, this);
            }
            if (list.contains(Configs.AG_DRONE)) {
                g.drawImage(drone_image, x, y, this);
            }
        }


    }
}
