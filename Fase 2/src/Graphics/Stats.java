package Graphics;

import javax.swing.*;
import javax.swing.border.Border;

import Statistics.*;

import java.awt.*;

public class Stats extends JPanel {
    Statistics.Stats stats;

    public Stats(){
        Border blackline = BorderFactory.createTitledBorder("Statistics");
        setBorder(blackline);
        JTextArea cena = new JTextArea();
        cena.setText("yay");
        add(cena);
    }

}
