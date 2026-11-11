package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
/**@author Angel Santiago Bedoya Carreño
 *  */
public class LoadFile extends JPanel implements ILoad {
    private JTable table;
    private JScrollPane scrollPane;

    public LoadFile() {
        setLayout(new BorderLayout());
        add(new JLabel("Cargar"), BorderLayout.NORTH);
    }

    public void addTable(Object[][] dates, String[] titles) {
        clean();
        table = new JTable(dates, titles);
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void addImage(Image image) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        int panelW = getWidth() - 20;
        int panelH = getHeight() - 40;
        if (panelW > 0 && panelH > 0) {
            double scale = Math.min((double) panelW / width, (double) panelH / height);
            width = (int) (width * scale);
            height = (int) (height * scale);
        }
        clean();
        JLabel imageLabel = new JLabel(new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_REPLICATE)));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scrollPane = new JScrollPane(imageLabel);
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void addWeather(WeatherPanel weatherPanel) {
        clean();
        scrollPane = new JScrollPane(weatherPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    @Override
    public void clean() {
        removeAll();
        add(new JLabel("Cargar"), BorderLayout.NORTH);
        revalidate();
        repaint();
    }

    @Override
    public void load() {
    }

    @Override
    public void save() {
    }
}