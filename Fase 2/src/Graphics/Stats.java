package Graphics;

import javax.swing.*;
import javax.swing.border.Border;
import org.jfree.chart.*;

import Statistics.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;

public class Stats extends JPanel {
    Statistics.Stats stats;

    public Stats(Statistics.Stats stats){
        this.stats = stats;
        Border blackline = BorderFactory.createTitledBorder("Statistics");
        setBorder(blackline);
        JLabel cena = new JLabel("Estatísticas");
        add(cena);

        CategoryDataset dataset = createDataset();
        JFreeChart chart = ChartFactory.createBarChart("Fires Extinguished per Agent", "Agent", "Fires Cos pigs", dataset, PlotOrientation.VERTICAL,false, false, false);
        Color backiii = UIManager.getColor ( "Panel.background" );
        chart.setBackgroundPaint(backiii);


        XYSeries series = new XYSeries("keyyyy");
        for (int i = 0; i < 100; i++)
            series.add(stats.getAvgTimeBurning(), i);
        XYSeriesCollection dataset2 = new XYSeriesCollection(series);
        JFreeChart chart2 = ChartFactory.createXYLineChart(null, null, null, dataset2, PlotOrientation.HORIZONTAL, true, true, true);
        ChartPanel chartpanel2 = new ChartPanel(chart2);


        ChartPanel chartpanel = new ChartPanel(chart);
        chartpanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        Color backg = new Color(193, 215, 215);
        chart.getPlot().setBackgroundPaint(backg);

        CategoryPlot categoryPlot = chart.getCategoryPlot();
        BarRenderer br = (BarRenderer) categoryPlot.getRenderer();
        br.setMaximumBarWidth(.15);
        Color color = new Color(102, 255, 255);
        br.setSeriesPaint(0, color);

        setLayout(new BorderLayout());
        add(chartpanel2, BorderLayout.CENTER);
        add(chartpanel, BorderLayout.CENTER);
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(10, "Fires Cos pigs", "Truck");
        dataset.addValue(20, "Fires Cos pigs", "Aircraft");
        dataset.addValue(30, "Fires Cos pigs", "Drone");

        return dataset;
    }


}
