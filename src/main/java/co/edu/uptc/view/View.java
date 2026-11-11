package co.edu.uptc.view;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Map;
/**@author Angel Santiago Bedoya Carreño
 *  */
public interface View {
    public void addActionListener(ActionListener al);

    public void addWindowListener(WindowListener wl);

    public File chooseFile();

    public void showFile(String content);

    public void editFile(String content);

    public void loadTable(String content);

    public String getEditedText();

    public void showMessage(String message);

    public void clean();

    public String createNameFile();

    public int selectTypeFile();

    public boolean askDelete(String name);

    public File chooseImage();

    public void showImage(Image image);

    public File chooseFileToDelete();

    public File chooseProperties();

    public void showPropertiesTable(Object[][] data);

    public boolean askXmlSerialization();

    public File chooseJson();

    public void showJsonTable(Object[][] data);

    public void addCities(String[] cities);

    public void showWeather(Map<String, String> jsonData, Map<String, String> xmlData, Image icon);
}
