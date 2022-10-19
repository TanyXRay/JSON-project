import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import utils.exceptions.SettingsFileReadException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public final class SettingsFile {

    private static final String SETTINGS_FILE = "shop.xml";

    private boolean loadEnabled;
    private String loadFileName;
    private String loadFormat;

    private boolean saveEnabled;
    private String saveFileName;
    private String saveFormat;

    private boolean logEnabled;
    private String logFileName;

    public SettingsFile() {
        Document document = loadFile();
        parseSettings(document);
    }

    private Document loadFile() {
        try {
            File file = new File(SETTINGS_FILE);
            if (!file.exists()) {
                throw new RuntimeException("File not found!");
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(file);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new SettingsFileReadException(e);
        }
    }

    private void parseSettings(Document doc) {
        Node config = doc.getDocumentElement();

        Node load = getNodeElementByName(config, "load");
        loadEnabled = Boolean.parseBoolean(getNodeElementByName(load, "enabled").getTextContent());
        loadFileName = getNodeElementByName(load, "fileName").getTextContent();
        loadFormat = getNodeElementByName(load, "format").getTextContent();

        Node save = getNodeElementByName(config, "save");
        saveEnabled = Boolean.parseBoolean(getNodeElementByName(save, "enabled").getTextContent());
        saveFileName = getNodeElementByName(save, "fileName").getTextContent();
        saveFormat = getNodeElementByName(save, "format").getTextContent();

        Node log = getNodeElementByName(config, "log");
        logEnabled = Boolean.parseBoolean(getNodeElementByName(log, "enabled").getTextContent());
        logFileName = getNodeElementByName(log, "fileName").getTextContent();
    }

    private Node getNodeElementByName(Node searchNode, String key) {
        NodeList childNodes = searchNode.getChildNodes();
        for (int i = 0; i <= childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE && item.getNodeName().equals(key)) {
                return item;
            }
        }
        throw new RuntimeException("Node not found by name - " + key);
    }

    public boolean isLoadEnabled() {
        return loadEnabled;
    }

    public String getLoadFileName() {
        return loadFileName;
    }

    public String getLoadFormat() {
        return loadFormat;
    }

    public boolean isSaveEnabled() {
        return saveEnabled;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public String getSaveFormat() {
        return saveFormat;
    }

    public boolean isLogEnabled() {
        return logEnabled;
    }

    public String getLogFileName() {
        return logFileName;
    }

    @Override
    public String toString() {
        return "SettingsFile{" +
               "loadEnabled=" + loadEnabled +
               ", loadFileName='" + loadFileName + '\'' +
               ", loadFormat='" + loadFormat + '\'' +
               ", saveEnabled=" + saveEnabled +
               ", saveFileName='" + saveFileName + '\'' +
               ", saveFormat='" + saveFormat + '\'' +
               ", logEnabled=" + logEnabled +
               ", logFileName='" + logFileName + '\'' +
               '}';
    }
}