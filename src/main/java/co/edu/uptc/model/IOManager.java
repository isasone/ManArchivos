package co.edu.uptc.model;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.StringWriter;
import java.util.HashMap;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import java.util.List;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
/**@author Angel Santiago Bedoya Carreño
 *  */
public class IOManager {

    private File currentFile;
    private File currentImage;
    private boolean editingImage;
    private Properties weatherProperties;


    public void writeFile(String name, String path, String content) {
        FileWriter archivo = null;
        PrintWriter pw = null;
        try {
            archivo = new FileWriter(path + name);
            pw = new PrintWriter(archivo);
            pw.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pw != null)
                    pw.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public String ReadFile(String name, String path) {
        String content = "";
        FileReader file = null;
        BufferedReader br = null;
        try {
            file = new FileReader(path + name);
            br = new BufferedReader(file);
            String line;
            while ((line = br.readLine()) != null) {
                content += line + "\n";
            }
        } catch (IOException e) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return content;
    }

    public boolean createFileTxt(String name) {
        File newFile = new File("resources/txt/" + name + ".txt");
        newFile.getParentFile().mkdirs();
        try {
            return newFile.createNewFile();
        } catch (Exception e) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, "Error al cargar el archivo", e);
            return false;
        }
    }

    public boolean deleteFile(File file) {
        if (!file.exists()) {
            return false;
        }
        return file.delete();
    }

    public BufferedImage readImage(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public String imageToMatrix(BufferedImage image) {
        StringBuilder matrix = new StringBuilder();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                matrix.append(String.format("%06X", rgb & 0xFFFFFF)).append(" ");
            }
            matrix.append("\n");
        }
        return matrix.toString();
    }

    public BufferedImage matrixToImage(String text) {
        String[] rows = text.trim().split("\n");
        int height = rows.length;
        int width = rows[0].trim().split("\\s+").length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            String[] pixels = rows[y].trim().split("\\s+");
            if (pixels.length != width) {
                throw new IllegalArgumentException("La fila " + (y + 1) + " tiene " + pixels.length
                        + " pixeles y debería tener " + width);
            }
            for (int x = 0; x < width; x++) {
                try {
                    int rgb = Integer.parseInt(pixels[x], 16);
                    image.setRGB(x, y, rgb);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Color inválido \"" + pixels[x] + "\" en fila "
                            + (y + 1) + ", columna " + (x + 1));
                }
            }
        }
        return image;
    }

    public boolean saveImage(BufferedImage image, File file) {
        try {
            return ImageIO.write(image, "png", file);
        } catch (IOException e) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    public String readBits(File file) {
        StringBuilder bits = new StringBuilder();
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            int b;
            int count = 0;
            while ((b = bis.read()) != -1) {
                String binary = Integer.toBinaryString(b);
                while (binary.length() < 8) {
                    binary = "0" + binary;
                }
                bits.append(binary).append(" ");
                count++;
                if (count % 8 == 0) {
                    bits.append("\n");
                }
            }
        } catch (IOException e) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (bis != null)
                    bis.close();
            } catch (IOException e) {
                Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return bits.toString();
    }

    public void createPropiertiesFile(String path, String name) {
        File file = new File(path + name);
        try {
            OutputStream outputStream = new FileOutputStream(file);
            Properties properties = new Properties();
            properties.setProperty("Ubicacion", "resources/");
            properties.setProperty("name", "Archivos");
            properties.store(outputStream, "Properties");
            outputStream.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Object[][] readProperties(File file) {
        Properties properties = new Properties();
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            properties.load(reader);
        } catch (IOException e) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        TreeSet<String> keys = new TreeSet<>(properties.stringPropertyNames());
        Object[][] data = new Object[keys.size()][2];
        int row = 0;
        for (String key : keys) {
            data[row][0] = key;
            data[row][1] = properties.getProperty(key);
            row++;
        }
        return data;
    }

    public boolean writeBin(String path, String name, String text) {
        String nameFile = path + name;
        if (!nameFile.endsWith(".bin")) {
            nameFile = nameFile + ".bin";
        }
        File binFile = new File(nameFile);
        if (binFile.getParentFile() != null) {
            binFile.getParentFile().mkdirs();
        }
        DataOutputStream exit = null;
        String[] data = text.trim().split("\n");
        if (text.trim().isEmpty()) {
            data = new String[0];
        }
        try {
            FileOutputStream fos = new FileOutputStream(nameFile);
            exit = new DataOutputStream(fos);
            exit.writeInt(data.length);
            for (int i = 0; i < data.length; i++) {
                String line = data[i].trim();
                if (line.matches("-?\\d+")) {
                    int number = Integer.parseInt(line);
                    exit.writeChar('I');
                    exit.writeInt(number);
                } else if (line.matches("-?\\d+\\.\\d+")) {
                    double doubleNumber = Double.parseDouble(line);
                    exit.writeChar('D');
                    exit.writeDouble(doubleNumber);
                } else {
                    exit.writeChar('S');
                    exit.writeUTF(line);
                }
            }
            return true;
        } catch (IOException e) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            try {
                if (exit != null)
                    exit.close();
            } catch (IOException e) {
                Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    public byte[] readBinaryFile(String fileName) throws IOException {
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        try (FileInputStream fileInput = new FileInputStream(fileName);
            BufferedInputStream buffer = new BufferedInputStream(fileInput)) {
            byte[] array = new byte[1024];
            int bytesRead;
            while ((bytesRead = buffer.read(array)) != -1) {
                content.write(array, 0, bytesRead);
            }
        }
        return content.toByteArray();
    }

    public String binaryToText(byte[] bytes) throws IOException {
        if (bytes.length == 0) {
            return "";
        }
        StringBuilder text = new StringBuilder();
        DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes));
        int count = input.readInt();
        for (int i = 0; i < count; i++) {
            char mark = input.readChar();
            switch (mark) {
                case 'I':
                    text.append(input.readInt());
                    break;
                case 'D':
                    text.append(input.readDouble());
                    break;
                case 'S':
                    text.append(input.readUTF());
                    break;
                default:
                    throw new IOException("Marca desconocida: " + mark);
            }
            text.append("\n");
        }
        input.close();
        return text.toString();
    }

    public void serialize(History history) {
        String path = "resources/history/";
        String name = "historial.ser";
        File file = new File(path + name);
        file.getParentFile().mkdirs();
        ObjectOutputStream objectOutputStream = null;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fos);
            objectOutputStream.writeObject(history);
        } catch (IOException e) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (objectOutputStream != null)
                    objectOutputStream.close();
            } catch (IOException e) {
                Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
public History deserialize() {
    String path = "resources/history/";
    String name = "historial.ser";
    File archivo = new File(path + name);
    History history = new History();
    ObjectInputStream objFile = null;
    if (!archivo.exists()) {
        return history;
    }
    try {
        FileInputStream inFile = new FileInputStream(archivo);
        objFile = new ObjectInputStream(inFile);
        if (objFile != null) {
            try {
                history = (History) objFile.readObject();
            } catch (EOFException eof) {
            }
        }
    } catch (FileNotFoundException ex) {
        Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, "Archivo no encontrado", ex);
    } catch (IOException ex) {
        Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, "Error de entrada salida", ex);
    } catch (ClassNotFoundException ex) {
        Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, "Clase no encontrada", ex);
    } finally {
        try {
            if (objFile != null)
                objFile.close();
        } catch (IOException ex) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    return history;
}
    public void serializeXml(History history) {
        String path = "resources/history/";
        String name = "historial.xml";
        File file = new File(path + name);
        file.getParentFile().mkdirs();
        Element root = new Element("historial");
        for (Map.Entry<String, FileManager> entry : history.getFileMap().entrySet()) {
            FileManager fileManager = entry.getValue();
            Element fileElement = new Element("archivo");
            fileElement.setAttribute("nombre", fileManager.getName());
            fileElement.addContent(dateElement("creacion", fileManager.getCreation()));
            fileElement.addContent(dateElement("guardado", fileManager.getSave()));
            fileElement.addContent(dateElement("edicion", fileManager.getEdit()));
            root.addContent(fileElement);
        }
        Document document = new Document(root);
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            outputter.output(document, fos);
        } catch (IOException e) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Element dateElement(String name, LocalDateTime date) {
        Element element = new Element(name);
        if (date != null) {
            element.setText(date.toString());
        }
        return element;
    }

    public History deserializeXml() {
        String path = "resources/history/";
        String name = "historial.xml";
        File file = new File(path + name);
        History history = new History();
        if (!file.exists()) {
            return history;
        }
        try {
            Document document = new SAXBuilder().build(file);
            for (Element fileElement : document.getRootElement().getChildren("archivo")) {
                FileManager fileManager = new FileManager(fileElement.getAttributeValue("nombre"));
                fileManager.setCreation(readDate(fileElement, "creacion"));
                fileManager.setSave(readDate(fileElement, "guardado"));
                fileManager.setEdit(readDate(fileElement, "edicion"));
                history.getFileMap().put(fileManager.getName(), fileManager);
            }
        } catch (JDOMException ex) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, "XML mal formado", ex);
        } catch (IOException ex) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, "Error de entrada salida", ex);
        }
        return history;
    }

    private LocalDateTime readDate(Element parent, String name) {
        String text = parent.getChildTextTrim(name);
        if (text == null || text.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(text);
    }

    public String readFileXml(String name) throws IOException, JDOMException {
    File inputFile = new File("resources/xml/" + name);
    SAXBuilder saxBuilder = new SAXBuilder();
    Document document = saxBuilder.build(inputFile);
    Element rootElement = document.getRootElement();

    StringBuilder content = new StringBuilder();
    content.append("Raíz: ").append(rootElement.getName()).append("\n");

    List<Element> children = rootElement.getChildren();
    for (Element child : children) {
        content.append("\n<").append(child.getName()).append(">");
        for (Attribute attribute : child.getAttributes()) {
            content.append(" ").append(attribute.getName()).append("=").append(attribute.getValue());
        }
        content.append("\n");
        for (Element field : child.getChildren()) {
            content.append("   ").append(field.getName()).append(": ").append(field.getTextTrim()).append("\n");
        }
    }
    return content.toString();
}
    public void createJSon(History history){
        String name="historial.json";
        String path="resources/json/";


        JsonObjectBuilder jsonObjectBuilder= Json.createObjectBuilder();
        jsonObjectBuilder.add("Fecha",LocalDateTime.now().toString());
        JsonObjectBuilder file= Json.createObjectBuilder();
        for(FileManager fm:history.getFileMap().values()){
            file.add(fm.getName(), Json.createObjectBuilder()
                    .add("creacion", textDate(fm.getCreation()))
                    .add("guardado", textDate(fm.getSave()))
                    .add("edicion", textDate(fm.getEdit())));
        }
        jsonObjectBuilder.add("Archivos", file);

        JsonObject objetoGuardar = jsonObjectBuilder.build();
        new File(path).mkdirs();
        try (OutputStream os = new FileOutputStream(path + name);
             JsonWriter jsonWriter = Json.createWriter(os)) {
            jsonWriter.writeObject(objetoGuardar);
        } catch (IOException e) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private String textDate(LocalDateTime date) {
        if (date == null) {
            return "sin registro";
        }
        return date.toString();
    }

    public String readJson(File file) {
        JsonObject jsonObject = readJsonObject(file);
        if (jsonObject == null) {
            return null;
        }
        Map<String, Object> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(config);
        StringWriter text = new StringWriter();
        try (JsonWriter jsonWriter = writerFactory.createWriter(text)) {
            jsonWriter.writeObject(jsonObject);
        }
        return text.toString().trim();
    }

    public Object[][] readJsonHistory(File file) {
        JsonObject jsonObject = readJsonObject(file);
        if (jsonObject == null || !jsonObject.containsKey("Archivos")
                || jsonObject.get("Archivos").getValueType() != JsonValue.ValueType.OBJECT) {
            return new Object[0][0];
        }
        JsonObject archivos = jsonObject.getJsonObject("Archivos");
        Object[][] data = new Object[archivos.size()][4];
        int row = 0;
        for (String fileName : archivos.keySet()) {
            data[row][0] = fileName;
            if (archivos.get(fileName) instanceof JsonObject) {
                JsonObject dates = archivos.getJsonObject(fileName);
                data[row][1] = dates.getString("creacion", "");
                data[row][2] = dates.getString("guardado", "");
                data[row][3] = dates.getString("edicion", "");
            }
            row++;
        }
        return data;
    }

    private JsonObject readJsonObject(File file) {
        try (InputStream is = new FileInputStream(file);
             JsonReader jsonReader = Json.createReader(is)) {
            return jsonReader.readObject();
        } catch (IOException | JsonException | ClassCastException e) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, "JSON no válido", e);
            return null;
        }
    }

    public void loadWeatherProperties() {
        weatherProperties = new Properties();
        String path = "resources/properties/";
        String name = "ciudades.properties";
        try (FileReader reader = new FileReader(path + name)) {
            weatherProperties.load(reader);
        } catch (IOException e) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, "No se encontró " + name, e);
        }
    }

    public String[] getCities() {
        int totalCities = 5;
        String[] cities = new String[totalCities];
        for (int i = 0; i < totalCities; i++) {
            cities[i] = weatherProperties.getProperty("ciudad" + (i + 1) + ".nombre");
        }
        return cities;
    }

    public String getLatitude(int city) {
        return weatherProperties.getProperty("ciudad" + (city + 1) + ".lat");
    }

    public String getLongitude(int city) {
        return weatherProperties.getProperty("ciudad" + (city + 1) + ".lon");
    }

    public String buildWeatherUrl(String lat, String lon, String mode) {
        String key = weatherProperties.getProperty("appid");
        String weatherUrl = "https://api.openweathermap.org/data/2.5/weather";
        return weatherUrl + "?lat=" + lat + "&lon=" + lon + "&appid=" + key
                + "&mode=" + mode + "&units=metric&lang=es";
    }

    public String requestWeather(String lat, String lon, String mode) throws IOException {
        URL url = new URL(buildWeatherUrl(lat, lon, mode));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            throw new IOException("La llave no es válida o todavía no está activada (error 401).\n"
                    + "Las llaves nuevas pueden tardar hasta 2 horas en funcionar.");
        } else {
            throw new IOException("El servicio respondió con el código " + responseCode);
        }
    }

    public File saveWeather(String content, String city, String mode) {
        String path = "resources/weather/";
        new File(path).mkdirs();
        File file = new File(path + "clima_" + city + "." + mode);
        try (PrintWriter pw = new PrintWriter(file, StandardCharsets.UTF_8)) {
            pw.print(content);
        } catch (IOException e) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return file;
    }

    public String prettyJson(String json) {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            Map<String, Object> config = new HashMap<>();
            config.put(JsonGenerator.PRETTY_PRINTING, true);
            StringWriter text = new StringWriter();
            try (JsonWriter writer = Json.createWriterFactory(config).createWriter(text)) {
                writer.writeObject(reader.readObject());
            }
            return text.toString().trim();
        } catch (JsonException e) {
            return json;
        }
    }

    public String prettyXml(String xml) {
        try {
            Document document = new SAXBuilder().build(new StringReader(xml));
            return new XMLOutputter(Format.getPrettyFormat()).outputString(document);
        } catch (JDOMException | IOException e) {
            return xml;
        }
    }

    public Map<String, String> readWeatherJson(String json) {
        Map<String, String> data = new HashMap<>();
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            JsonObject root = reader.readObject();
            JsonObject coord = root.getJsonObject("coord");
            JsonObject main = root.getJsonObject("main");
            JsonObject wind = root.getJsonObject("wind");
            JsonObject sys = root.getJsonObject("sys");
            JsonObject weather = root.getJsonArray("weather").getJsonObject(0);
            int timezone = root.getInt("timezone", 0);
            data.put("ciudad", root.getString("name", ""));
            data.put("pais", sys.getString("country", ""));
            data.put("lat", jsonValue(coord, "lat"));
            data.put("lon", jsonValue(coord, "lon"));
            data.put("descripcion", weather.getString("description", ""));
            data.put("icono", weather.getString("icon", ""));
            data.put("temp", jsonValue(main, "temp"));
            data.put("sensacion", jsonValue(main, "feels_like"));
            data.put("min", jsonValue(main, "temp_min"));
            data.put("max", jsonValue(main, "temp_max"));
            data.put("humedad", jsonValue(main, "humidity"));
            data.put("presion", jsonValue(main, "pressure"));
            data.put("viento", jsonValue(wind, "speed"));
            data.put("direccion", jsonValue(wind, "deg"));
            data.put("nubes", jsonValue(root.getJsonObject("clouds"), "all"));
            data.put("visibilidad", jsonValue(root, "visibility"));
            data.put("amanecer", unixToHour(sys.getJsonNumber("sunrise"), timezone));
            data.put("atardecer", unixToHour(sys.getJsonNumber("sunset"), timezone));
        }
        return data;
    }

    private String jsonValue(JsonObject object, String key) {
        if (object == null || !object.containsKey(key) || object.isNull(key)) {
            return "-";
        }
        return object.get(key).toString().replace("\"", "");
    }

    private String unixToHour(JsonNumber seconds, int timezone) {
        if (seconds == null) {
            return "-";
        }
        return Instant.ofEpochSecond(seconds.longValue())
                .atOffset(ZoneOffset.ofTotalSeconds(timezone))
                .format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    public Map<String, String> readWeatherXml(String xml) throws IOException {
        Map<String, String> data = new HashMap<>();
        try {
            Document document = new SAXBuilder().build(new StringReader(xml));
            Element root = document.getRootElement();
            Element city = root.getChild("city");
            Element coord = city.getChild("coord");
            Element sun = city.getChild("sun");
            Element temperature = root.getChild("temperature");
            Element wind = root.getChild("wind");
            Element weather = root.getChild("weather");
            int timezone = Integer.parseInt(city.getChildTextTrim("timezone"));
            data.put("ciudad", city.getAttributeValue("name"));
            data.put("pais", city.getChildTextTrim("country"));
            data.put("lat", coord.getAttributeValue("lat"));
            data.put("lon", coord.getAttributeValue("lon"));
            data.put("descripcion", weather.getAttributeValue("value"));
            data.put("icono", weather.getAttributeValue("icon"));
            data.put("temp", temperature.getAttributeValue("value"));
            data.put("sensacion", xmlValue(root.getChild("feels_like")));
            data.put("min", temperature.getAttributeValue("min"));
            data.put("max", temperature.getAttributeValue("max"));
            data.put("humedad", xmlValue(root.getChild("humidity")));
            data.put("presion", xmlValue(root.getChild("pressure")));
            data.put("viento", xmlValue(wind.getChild("speed")));
            data.put("direccion", xmlValue(wind.getChild("direction")));
            data.put("nubes", xmlValue(root.getChild("clouds")));
            data.put("visibilidad", xmlValue(root.getChild("visibility")));
            data.put("amanecer", utcToHour(sun.getAttributeValue("rise"), timezone));
            data.put("atardecer", utcToHour(sun.getAttributeValue("set"), timezone));
        } catch (JDOMException e) {
            throw new IOException("La respuesta no es un XML válido", e);
        }
        return data;
    }

    private String xmlValue(Element element) {
        if (element == null || element.getAttributeValue("value") == null) {
            return "-";
        }
        return element.getAttributeValue("value");
    }

    private String utcToHour(String date, int timezone) {
        if (date == null) {
            return "-";
        }
        return LocalDateTime.parse(date).atOffset(ZoneOffset.UTC)
                .withOffsetSameInstant(ZoneOffset.ofTotalSeconds(timezone))
                .format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    public BufferedImage downloadIcon(String code) {
        try {
            URL url = new URL("https://openweathermap.org/img/wn/" + code + "@2x.png");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            BufferedImage icon = ImageIO.read(con.getInputStream());
            con.disconnect();
            return icon;
        } catch (IOException e) {
            return null;
        }
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

    public File getCurrentImage() {
        return currentImage;
    }

    public void setCurrentImage(File currentImage) {
        this.currentImage = currentImage;
    }

    public boolean isEditingImage() {
        return editingImage;
    }

    public void setEditingImage(boolean editingImage) {
        this.editingImage = editingImage;
    }
}