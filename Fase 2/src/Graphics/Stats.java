package Graphics;

import javax.swing.*;
import javax.swing.border.Border;

import Statistics.*;

import java.awt.*;

public class Stats extends JPanel {
    Statistics.Stats stats;

    public Stats(Statistics.Stats stats){
        this.stats = stats;
        Border blackline = BorderFactory.createTitledBorder("Statistics");
        setBorder(blackline);
        JLabel cena = new JLabel("Estatísticas");
        add(cena);
    }

}
