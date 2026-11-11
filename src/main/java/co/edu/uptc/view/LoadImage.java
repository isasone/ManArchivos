package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
/**@author Angel Santiago Bedoya Carreño
 *  */
public class LoadImage extends JPanel implements ILoad {
    private JLabel imageLabel;
    private JScrollPane scrollPane;

    public LoadImage() {
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scrollPane = new JScrollPane(imageLabel);
        setLayout(new BorderLayout());
        add(new JLabel("Imagen"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addImages(Image image) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        int panelW = scrollPane.getWidth() - 20;
        int panelH = scrollPane.getHeight() - 20;
        if (panelW > 0 && panelH > 0) {
            double scale = Math.min((double) panelW / width, (double) panelH / height);
            width = (int) (width * scale);
            height = (int) (height * scale);
        }
        imageLabel.setIcon(new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_REPLICATE)));
        revalidate();
        repaint();
    }

    @Override
    public void clean() {
        imageLabel.setIcon(null);
    }

    @Override
    public void save() {
    }

    @Override
    public void load() {
    }
}