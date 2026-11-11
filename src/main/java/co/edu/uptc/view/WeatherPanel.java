package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
/**@author Angel Santiago Bedoya Carreño
 *  */
public class WeatherPanel extends JPanel {
    private Map<String, String> jsonData;
    private Map<String, String> xmlData;
    private Image icon;
    private JLabel cityLabel;
    private JLabel tempLabel;
    private JLabel descriptionLabel;
    private JLabel sourceLabel;
    private JPanel dataPanel;
    private Color background = new Color(225, 240, 255);
    private Color textColor = new Color(20, 50, 100);

    public WeatherPanel(Map<String, String> jsonData, Map<String, String> xmlData, Image icon) {
        this.jsonData = jsonData;
        this.xmlData = xmlData;
        this.icon = icon;
        setLayout(new BorderLayout(10, 10));
        setBackground(background);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        build();
        showData(jsonData, "JSON");
    }

    private void build() {
        cityLabel = new JLabel();
        cityLabel.setFont(new Font("Arial", Font.BOLD, 22));
        cityLabel.setForeground(textColor);
        add(cityLabel, BorderLayout.NORTH);

        JPanel left = new JPanel(new GridLayout(3, 1));
        left.setBackground(background);
        tempLabel = new JLabel();
        tempLabel.setFont(new Font("Arial", Font.BOLD, 40));
        tempLabel.setForeground(textColor);
        if (icon != null) {
            tempLabel.setIcon(new ImageIcon(icon.getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        }
        descriptionLabel = new JLabel();
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionLabel.setForeground(textColor);
        sourceLabel = new JLabel();
        sourceLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        sourceLabel.setForeground(Color.GRAY);
        left.add(tempLabel);
        left.add(descriptionLabel);
        left.add(sourceLabel);
        add(left, BorderLayout.WEST);

        dataPanel = new JPanel(new GridLayout(4, 6, 10, 5));
        dataPanel.setBackground(Color.WHITE);
        dataPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(170, 200, 230)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        add(dataPanel, BorderLayout.CENTER);

        JButton jsonButton = new JButton("Ver datos del JSON");
        JButton xmlButton = new JButton("Ver datos del XML");
        jsonButton.addActionListener(e -> showData(jsonData, "JSON"));
        xmlButton.addActionListener(e -> showData(xmlData, "XML"));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setBackground(background);
        buttons.add(jsonButton);
        buttons.add(xmlButton);
        add(buttons, BorderLayout.SOUTH);
    }

    private void showData(Map<String, String> data, String source) {
        cityLabel.setText(data.get("ciudad") + ", " + data.get("pais"));
        tempLabel.setText(data.get("temp") + " °C");
        descriptionLabel.setText(data.get("descripcion"));
        sourceLabel.setText("Información leída del " + source);

        dataPanel.removeAll();
        addData("Sensación:", data.get("sensacion") + " °C");
        addData("Mínima:", data.get("min") + " °C");
        addData("Máxima:", data.get("max") + " °C");
        addData("Humedad:", data.get("humedad") + " %");
        addData("Presión:", data.get("presion") + " hPa");
        addData("Nubosidad:", data.get("nubes") + " %");
        addData("Viento:", data.get("viento") + " m/s");
        addData("Dirección:", data.get("direccion") + "°");
        addData("Visibilidad:", data.get("visibilidad") + " m");
        addData("Amanecer:", data.get("amanecer"));
        addData("Atardecer:", data.get("atardecer"));
        addData("Coordenadas:", data.get("lat") + ", " + data.get("lon"));
        dataPanel.revalidate();
        dataPanel.repaint();
    }

    private void addData(String name, String value) {
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        nameLabel.setForeground(textColor);
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        dataPanel.add(nameLabel);
        dataPanel.add(valueLabel);
    }
}
