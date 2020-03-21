package Graphics;

import javax.swing.*;
import javax.swing.border.Border;


import java.awt.*;

public class Stats extends JPanel {
    private Statistics.Stats stats;
    private JLabel firesLitLabel, firesLitValue;
    private JLabel firesExtinguishedLabel, firesExtinguishedValue;
    private JLabel firesExtinguishedDronesLabel, firesExtinguishedDronesValue;
    private JLabel firesExtinguishedAircraftsLabel, firesExtinguishedAircraftsValue;
    private JLabel firesExtinguishedTrucksLabel, firesExtinguishedTrucksValue;
    private JLabel avgTimeLabel, avgTimeValue;
    private JLabel waterRefillsLabel, waterRefillsValue;
    private JLabel fuelRefillsLabel, fuelRefillsValue;



    public Statistics.Stats getStats() {
        return stats;
    }

    public Stats(Statistics.Stats stats){
        this.stats = stats;


        Border blackline = BorderFactory.createTitledBorder("Statistics");
        setBorder(blackline);

        setLayout(new GridLayout(0, 1));

        JPanel up = new JPanel(new GridLayout(0, 2));
        JPanel down = new JPanel(new GridLayout(0, 2));
        up.setBorder(BorderFactory.createTitledBorder("Fires"));
        down.setBorder(BorderFactory.createTitledBorder("Efficiency"));
        add(up);
        add(down);

        firesLitLabel = new JLabel("Fires Lit: "); up.add(firesLitLabel);
        firesLitValue = new JLabel("0"); up.add(firesLitValue);

        firesExtinguishedLabel = new JLabel("Fires Extinguished: "); up.add(firesExtinguishedLabel);
        firesExtinguishedValue = new JLabel("0 (0%)"); up.add(firesExtinguishedValue);

        firesExtinguishedAircraftsLabel = new JLabel("Fires Extinguished by Aircrafts: "); up.add(firesExtinguishedAircraftsLabel);
        firesExtinguishedAircraftsValue = new JLabel("0"); up.add(firesExtinguishedAircraftsValue);

        firesExtinguishedDronesLabel = new JLabel("Fires Extinguished by Drones: "); up.add(firesExtinguishedDronesLabel);
        firesExtinguishedDronesValue = new JLabel("0"); up.add(firesExtinguishedDronesValue);

        firesExtinguishedTrucksLabel = new JLabel("Fires Extinguished by Trucks: "); up.add(firesExtinguishedTrucksLabel);
        firesExtinguishedTrucksValue = new JLabel("0"); up.add(firesExtinguishedTrucksValue);

        avgTimeLabel = new JLabel("Average Time to extinct fires: "); down.add(avgTimeLabel);
        avgTimeValue = new JLabel("0"); down.add(avgTimeValue);

        fuelRefillsLabel = new JLabel("Fuel refilled volume: "); down.add(fuelRefillsLabel);
        fuelRefillsValue = new JLabel("0"); down.add(fuelRefillsValue);

        waterRefillsLabel = new JLabel("Water refilled volume: "); down.add(waterRefillsLabel);
        waterRefillsValue = new JLabel("0"); down.add(waterRefillsValue);


        this.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        firesLitValue.setText("" + stats.getFiresLit());
        firesExtinguishedValue.setText("" + stats.getFiresExtinguished() + " (" +  100*stats.getFiresExtinguished()/(float) stats.getFiresLit() +"%)");
        firesExtinguishedAircraftsValue.setText("" + 100*stats.getFiresExtinguishedByAircrafts()/(float) stats.getFiresExtinguished() + "%");
        firesExtinguishedDronesValue.setText("" + 100*stats.getFiresExtinguishedByDrones()/(float) stats.getFiresExtinguished() + "%");
        firesExtinguishedTrucksValue.setText("" + 100*stats.getFiresExtinguishedByTrucks()/(float) stats.getFiresExtinguished() + "%");

        avgTimeValue.setText("" + stats.getAvgTimeBurning() + "ms");
        waterRefillsValue.setText(""+ stats.getWaterRefills() + " units");
        fuelRefillsValue.setText(""+ stats.getFuelRefills() + " units");

    }

}
