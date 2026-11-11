package co.edu.uptc.presenter;

import co.edu.uptc.model.History;
import co.edu.uptc.model.IOManager;
import co.edu.uptc.view.SView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
/**@author Angel Santiago Bedoya Carreño
 *  */
public class Presenter implements ActionListener, WindowListener {
    private SView view;
    private IOManager ioManager;
    private History history;
    private boolean xmlMode;

    public Presenter() {
        view = new SView();
        ioManager = new IOManager();
        ioManager.loadWeatherProperties();
        view.addCities(ioManager.getCities());
        history = new History();
        xmlMode = view.askXmlSerialization();
        loadHistory();
        saveHistory();
        view.addActionListener(this);
        view.addWindowListener(this);
    }

    private String readChosenFile() {
        File file = view.chooseFile();
        if (file == null) {
            return null;
        }
        ioManager.setCurrentFile(file);
        ioManager.setEditingImage(false);
        if (isBinary(file)) {
            return readBinary(file);
        }
        String name = file.getName();
        String path = file.getParent() + File.separator;
        return ioManager.ReadFile(name, path);
    }

    private boolean isBinary(File file) {
        return file.getName().endsWith(".bin");
    }

    private String readBinary(File file) {
        try {
            byte[] bytes = ioManager.readBinaryFile(file.getAbsolutePath());
            return ioManager.binaryToText(bytes);
        } catch (IOException e) {
            view.showMessage("El archivo no tiene el formato esperado (dato por línea con su tipo).");
            return null;
        }
    }

    private void loadHistory() {
        if (xmlMode) {
            history = ioManager.deserializeXml();
        } else {
            history = ioManager.deserialize();
        }
    }

    private void saveHistory() {
        if (xmlMode) {
            ioManager.serializeXml(history);
        } else {
            ioManager.serialize(history);
        }
        ioManager.createJSon(history);
    }

    private void registerCreated(File file) {
        history.registredCreated(file.getAbsolutePath());
        saveHistory();
    }

    private void registerEdited(File file) {
        history.registerEdit(file.getAbsolutePath());
        saveHistory();
    }

    private void registerSaved(File file) {
        history.registerSave(file.getAbsolutePath());
        saveHistory();
    }

    public void loadFile(ActionEvent ae) {
        String content;
        switch (ae.getActionCommand()) {
            case "Open":
                content = readChosenFile();
                if (content != null)
                    view.showFile(content);
                break;
            case "Edit":
                content = readChosenFile();
                if (content != null) {
                    File currentFile = ioManager.getCurrentFile();
                    view.editFile(content);
                    if (isBinary(currentFile)) {
                        view.showFile(ioManager.readBits(currentFile));
                    }
                    registerEdited(currentFile);
                }
                break;
            case "Load":
                content = readChosenFile();
                if (content != null) {
                    File currentFile = ioManager.getCurrentFile();
                    if (currentFile.getName().endsWith(".properties")) {
                        view.showPropertiesTable(ioManager.readProperties(currentFile));
                    } else {
                        view.loadTable(content);
                    }
                }
                break;
            case "LoadImage":
                loadImage();
                break;
            case "LoadProperties":
                loadProperties();
                break;
            case "OpenJson":
                openJson();
                break;
            default:
                if (ae.getActionCommand().startsWith("Weather")) {
                    consultWeather(Integer.parseInt(ae.getActionCommand().substring(7)));
                }
                break;
            case "Save":
                save();
                break;
            case "Create":
                fileType();
                break;
            case "Delete":
                deleteFile();
                break;
        }
    }

    public void save() {
        File currentFile = ioManager.getCurrentFile();
        boolean editingImage = ioManager.isEditingImage();
        if (editingImage) {
            saveImage();
        } else if (currentFile == null) {
            view.showMessage("Primero abre o edita un archivo.");
        } else if (isBinary(currentFile)) {
            saveBinary();
        } else {
            ioManager.writeFile(currentFile.getName(),
                    currentFile.getParent() + File.separator,
                    view.getEditedText());
            view.showFile(view.getEditedText());
            if (currentFile.getName().endsWith(".properties")) {
                view.showPropertiesTable(ioManager.readProperties(currentFile));
            }
            registerSaved(currentFile);
            view.showMessage("Archivo guardado: " + currentFile.getName());
        }
    }

    public void loadImage() {
        int maxPixels = 600 * 600;
        File file = view.chooseImage();
        if (file == null) {
            return;
        }
        BufferedImage image = ioManager.readImage(file);
        if (image == null) {
            view.showMessage("No se pudo leer la imagen.");
            return;
        }
        if (image.getWidth() * image.getHeight() > maxPixels) {
            view.showMessage("La imagen es muy grande (" + image.getWidth() + "x" + image.getHeight()
                    + "). Usa una de máximo 600x600 pixeles.");
            return;
        }
        ioManager.setCurrentImage(file);
        ioManager.setEditingImage(true);
        view.showImage(image);
        view.showFile(ioManager.readBits(file));
        view.editFile(ioManager.imageToMatrix(image));
        registerEdited(file);
    }

    public void saveImage() {
        File currentImage = ioManager.getCurrentImage();
        try {
            BufferedImage image = ioManager.matrixToImage(view.getEditedText());
            String name = currentImage.getName();
            int dot = name.lastIndexOf('.');
            if (dot > 0) {
                name = name.substring(0, dot);
            }
            if (!name.endsWith("_editada")) {
                name = name + "_editada";
            }
            new File("resources/images/").mkdirs();
            File output = new File("resources/images/", name + ".png");
            if (ioManager.saveImage(image, output)) {
                ioManager.setCurrentImage(output);
                view.showImage(image);
                view.showFile(ioManager.readBits(output));
                registerSaved(output);
                view.showMessage("Imagen guardada: " + output.getName());
            } else {
                view.showMessage("No se pudo guardar la imagen.");
            }
        } catch (IllegalArgumentException e) {
            view.showMessage(e.getMessage());
        }
    }

    public void loadProperties() {
        File file = view.chooseProperties();
        if (file == null) {
            return;
        }
        ioManager.setCurrentFile(file);
        ioManager.setEditingImage(false);
        String content = ioManager.ReadFile(file.getName(), file.getParent() + File.separator);
        view.showFile(content);
        view.editFile(content);
        view.showPropertiesTable(ioManager.readProperties(file));
        registerEdited(file);
    }

    public void openJson() {
        File file = view.chooseJson();
        if (file == null) {
            return;
        }
        String content = ioManager.readJson(file);
        if (content == null) {
            view.showMessage("El archivo no es un JSON válido.");
            return;
        }
        ioManager.setCurrentFile(file);
        ioManager.setEditingImage(false);
        view.showFile(content);
        view.editFile(content);
        view.showJsonTable(ioManager.readJsonHistory(file));
    }

    public void consultWeather(int city) {
        String[] cities = ioManager.getCities();
        String lat = ioManager.getLatitude(city);
        String lon = ioManager.getLongitude(city);
        String urlJson = ioManager.buildWeatherUrl(lat, lon, "json");
        String urlXml = ioManager.buildWeatherUrl(lat, lon, "xml");
        try {
            String json = ioManager.requestWeather(lat, lon, "json");
            String xml = ioManager.requestWeather(lat, lon, "xml");
            File jsonFile = ioManager.saveWeather(json, cities[city], "json");
            File xmlFile = ioManager.saveWeather(xml, cities[city], "xml");
            view.showFile("URL JSON:\n" + urlJson + "\n\n" + ioManager.prettyJson(json));
            view.editFile("URL XML:\n" + urlXml + "\n\n" + ioManager.prettyXml(xml));
            Map<String, String> jsonData = ioManager.readWeatherJson(json);
            Map<String, String> xmlData = ioManager.readWeatherXml(xml);
            view.showWeather(jsonData, xmlData, ioManager.downloadIcon(jsonData.get("icono")));
            registerCreated(jsonFile.getAbsoluteFile());
            registerCreated(xmlFile.getAbsoluteFile());
            view.showMessage("Clima de " + cities[city] + " guardado en:\n"
                    + jsonFile.getPath() + "\n" + xmlFile.getPath());
        } catch (IOException e) {
            view.showMessage("No se pudo consultar el clima:\n" + e.getMessage());
        } catch (RuntimeException e) {
            view.showMessage("La respuesta no tiene el formato esperado:\n" + e.getMessage());
        }
    }

    public void fileType() {
        switch (view.selectTypeFile()) {
            case 1:
                createFile();
                break;
            case 2:
                createBinFile();
                break;
        }
    }

    public void createFile() {
        String name = view.createNameFile();
        if (name == null || name.trim().isEmpty()) {
            view.showMessage("El nombre esta vacio");
        } else {
            name = name.trim();
            boolean create = ioManager.createFileTxt(name);
            if (create) {
                registerCreated(new File("resources/txt/" + name + ".txt").getAbsoluteFile());
                view.showMessage("Archivo creado exitosamente");
            } else {
                view.showMessage("El Archivo no fue creado");
            }
        }
    }

    public void createBinFile() {
        String name = view.createNameFile();
        if (name == null || name.trim().isEmpty()) {
            view.showMessage("El nombre esta vacio");
            return;
        }
        name = name.trim();
        if (ioManager.writeBin("resources/bin/", name, view.getEditedText())) {
            File currentFile = new File("resources/bin/" + name + ".bin").getAbsoluteFile();
            ioManager.setCurrentFile(currentFile);
            ioManager.setEditingImage(false);
            view.showFile(ioManager.readBits(currentFile));
            registerCreated(currentFile);
            view.showMessage("Archivo binario creado: " + currentFile.getName());
        } else {
            view.showMessage("El Archivo no fue creado");
        }
    }

    public void saveBinary() {
        File currentFile = ioManager.getCurrentFile();
        boolean saved = ioManager.writeBin(currentFile.getParent() + File.separator,
                currentFile.getName(), view.getEditedText());
        if (saved) {
            view.showFile(ioManager.readBits(currentFile));
            registerSaved(currentFile);
            view.showMessage("Archivo binario guardado: " + currentFile.getName());
        } else {
            view.showMessage("No se pudo guardar el archivo binario.");
        }
    }

    public void deleteFile() {
        File file = view.chooseFileToDelete();
        if (file == null) {
            return;
        }
        if (!view.askDelete(file.getName())) {
            return;
        }
        if (ioManager.deleteFile(file)) {
            File currentFile = ioManager.getCurrentFile();
            File currentImage = ioManager.getCurrentImage();
            if (file.equals(currentFile) || file.equals(currentImage)) {
                ioManager.setCurrentFile(null);
                ioManager.setCurrentImage(null);
                ioManager.setEditingImage(false);
                view.clean();
            }
            view.showMessage("Archivo borrado: " + file.getName());
        } else {
            view.showMessage("No se pudo borrar el archivo. Revisa que no esté abierto en otro programa.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        loadFile(e);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        
    }

    @Override
    public void windowClosing(WindowEvent e) {
        saveHistory();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}