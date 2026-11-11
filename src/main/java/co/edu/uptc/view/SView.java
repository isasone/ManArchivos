package co.edu.uptc.view;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
/**@author Angel Santiago Bedoya Carreño
 *  */
public class SView implements View {
    private MyJFrame myJFrame;
    private JFileChooser fileChooser;
    private JFileChooser imageChooser;
    private JFileChooser propertiesChooser;
    private JFileChooser jsonChooser;

    public SView() {
        myJFrame = new MyJFrame();
        fileChooser = new JFileChooser(System.getProperty("user.home"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos (*.txt, *.csv, *.properties, *.bin)", "txt", "csv", "properties", "bin"));
        imageChooser = new JFileChooser(System.getProperty("user.home"));
        imageChooser.setFileFilter(new FileNameExtensionFilter("Imágenes (*.png, *.jpg, *.bmp, *.gif)", "png", "jpg", "jpeg", "bmp", "gif"));
        propertiesChooser = new JFileChooser(System.getProperty("user.dir"));
        propertiesChooser.setFileFilter(new FileNameExtensionFilter("Archivos properties (*.properties)", "properties"));
        jsonChooser = new JFileChooser(System.getProperty("user.dir"));
        jsonChooser.setFileFilter(new FileNameExtensionFilter("Archivos JSON (*.json)", "json"));
        myJFrame.setVisible(true);
    }

    @Override
    public void addActionListener(ActionListener al) {
        myJFrame.addListener(al);
    }

    @Override
    public File chooseFile() {
        int option = fileChooser.showOpenDialog(myJFrame);
        if (option == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    @Override
    public void showFile(String content) {
        myJFrame.getPanelSee().setText(content);
    }

    @Override
    public void editFile(String content) {
        myJFrame.getPanelEdit().setText(content);
    }

    @Override
    public void loadTable(String content) {
        String[] lines = content.split("\n");
        int columns = 1;
        for (String line : lines) {
            columns = Math.max(columns, line.split("[,;]").length);
        }
        String[] titles = new String[columns];
        for (int i = 0; i < columns; i++) {
            titles[i] = "Columna " + (i + 1);
        }
        Object[][] data = new Object[lines.length][columns];
        for (int i = 0; i < lines.length; i++) {
            String[] parts = lines[i].split("[,;]");
            for (int j = 0; j < parts.length; j++) {
                data[i][j] = parts[j].trim();
            }
        }
        myJFrame.getLoadItem().addTable(data, titles);
    }

    @Override
    public String getEditedText() {
        return myJFrame.getPanelEdit().getText();
    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(myJFrame, message);
    }

    @Override
    public void clean() {
        myJFrame.getPanelSee().setText("");
        myJFrame.getPanelEdit().setText("");
        myJFrame.getLoadItem().clean();
    }

    @Override
    public String createNameFile() {
        String name = JOptionPane.showInputDialog("Ingrese el nombre del archivo a crear:");
        return name;
    }

    @Override
    public int selectTypeFile() {
        String option = JOptionPane.showInputDialog("Ingrese el tipo de archivo que quiere crear: \n 1. .txt \n 2. .bin");
        try {
            return Integer.parseInt(option.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public boolean askDelete(String name) {
        int option = JOptionPane.showConfirmDialog(myJFrame,
                "¿Seguro que quieres borrar \"" + name + "\"?\nEsta acción no se puede deshacer.",
                "Borrar archivo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return option == JOptionPane.YES_OPTION;
    }

    @Override
    public File chooseImage() {
        int option = imageChooser.showOpenDialog(myJFrame);
        if (option == JFileChooser.APPROVE_OPTION) {
            return imageChooser.getSelectedFile();
        }
        return null;
    }

    @Override
    public void showImage(Image image) {
        myJFrame.getLoadItem().addImage(image);
    }

    @Override
    public File chooseFileToDelete() {
        JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
        chooser.setDialogTitle("Elige el archivo a borrar");
        int option = chooser.showOpenDialog(myJFrame);
        if (option == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    @Override
    public File chooseProperties() {
        int option = propertiesChooser.showOpenDialog(myJFrame);
        if (option == JFileChooser.APPROVE_OPTION) {
            return propertiesChooser.getSelectedFile();
        }
        return null;
    }

    @Override
    public void showPropertiesTable(Object[][] data) {
        String[] titles = { "Llave", "Valor" };
        myJFrame.getLoadItem().addTable(data, titles);
    }

    @Override
    public void addWindowListener(WindowListener wl) {
        myJFrame.addWindowListener(wl);
    }

    @Override
    public boolean askXmlSerialization() {
        String[] options = { "Serialización normal (.ser)", "XML (.xml)" };
        int option = JOptionPane.showOptionDialog(myJFrame,
                "Seleccione como guardar el historial",
                "Tipo de serialización", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        return option == 1;
    }

    @Override
    public File chooseJson() {
        int option = jsonChooser.showOpenDialog(myJFrame);
        if (option == JFileChooser.APPROVE_OPTION) {
            return jsonChooser.getSelectedFile();
        }
        return null;
    }

    @Override
    public void showJsonTable(Object[][] data) {
        String[] titles = { "Archivo", "Creación", "Guardado", "Edición" };
        myJFrame.getLoadItem().addTable(data, titles);
    }

    @Override
    public void addCities(String[] cities) {
        myJFrame.addCities(cities);
    }

    @Override
    public void showWeather(Map<String, String> jsonData, Map<String, String> xmlData, Image icon) {
        myJFrame.getLoadItem().addWeather(new WeatherPanel(jsonData, xmlData, icon));
    }
}
