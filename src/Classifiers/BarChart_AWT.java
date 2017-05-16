package Classifiers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author Saurav
 */
public class BarChart_AWT extends ApplicationFrame {

    public BarChart_AWT(String applicationTitle, String chartTitle) {
        super(applicationTitle);
        JFreeChart barChart = ChartFactory.createBarChart(
                chartTitle,
                "Category",
                "Percentage",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
        setContentPane(chartPanel);
    }

    private CategoryDataset createDataset() {
        Scanner scanner = null;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            scanner = new Scanner(new File("FeatureExtraction\\Accuracy.csv"));
            String[] algo = scanner.nextLine().split(",");
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] array = line.split(",");
                dataset.addValue(Double.parseDouble(array[1]), algo[1], array[0]);
                dataset.addValue(Double.parseDouble(array[2]), algo[2], array[0]);
                dataset.addValue(Double.parseDouble(array[3]), algo[3], array[0]);

            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return dataset;
    }

}
