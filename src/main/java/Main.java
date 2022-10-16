import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.Boolean.parseBoolean;

/**
 * Решение задачи №1 по теме "Одномерные массивы".
 * Решение задачи №1 по теме "Исключения".
 * Решение задачи №2 по теме "Работа с файлами. Потоки ввода-вывода. Сериализация".
 * Решение задачи №1 по теме "Работа с форматами СSV,JSON,XML.
 */

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        String[] products = {"Хлеб", "Молоко", "Яблоки"};
        int[] prices = {35, 87, 109};

        Scanner scanner = new Scanner(System.in);
        ClientLog clientLog = new ClientLog();
        Basket basket = new Basket(products, prices);

        File xmlFile = new File("xml_dir/shop.xml");
        File csvFile = new File("log.csv");
        File jsonFile = new File("basket.json");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();

        boolean loadIsEnabled = false;
        boolean saveIsEnabled = false;
        boolean logIsEnabled = false;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node tempNode = nodeList.item(i);
            if ((tempNode.getNodeType() == Node.ELEMENT_NODE) && (tempNode.getNodeType() != Node.TEXT_NODE)) {
                String nameParentNode = tempNode.getNodeName();
                NodeList nodeList1 = tempNode.getChildNodes();
                for (int j = 0; j < nodeList1.getLength(); j++) {
                    Node twoNode = nodeList1.item(j);
                    if ((twoNode.getNodeType() == Node.ELEMENT_NODE) && (twoNode.getNodeType() != Node.TEXT_NODE)) {
                        String nameChildNode = twoNode.getNodeName();
                        String text = twoNode.getTextContent();
                        if (nameParentNode.equals("load") && nameChildNode.equals("enabled")) {
                            loadIsEnabled = parseBoolean(text);
                        } else if (nameParentNode.equals("save") && nameChildNode.equals("enabled")) {
                            saveIsEnabled = parseBoolean(text);
                        } else if (nameParentNode.equals("log") && nameChildNode.equals("enabled")) {
                            logIsEnabled = parseBoolean(text);
                        }
                    }
                }
            }
        }

        if (loadIsEnabled && jsonFile.exists()) {
            basket = Basket.loadFromJsonFile(jsonFile);
        }
        basket.printListAllProductsForBuy();

        while (true) {
            System.out.println("Выберите товар и количество или введите \"end\" ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("end")) {
                break;
            }
            String[] parts = input.split(" ");
            if (parts.length != 2) {
                continue;
            }

            int productNumber;
            try {
                productNumber = Integer.parseInt(parts[0]) - 1; // выбор продукта
            } catch (NumberFormatException e) {
                System.out.println("Вы ввели текст заместо числа. Попробуйте снова!");
                continue;
            }
            if (productNumber >= 3 || productNumber < 0) {
                System.out.println("Вы ввели некорректное число продукта. Попробуйте снова!");
                continue;
            }

            int productCount;
            try {
                productCount = Integer.parseInt(parts[1]); // выбор количества продуктов
            } catch (NumberFormatException e) {
                System.out.println("Вы ввели текст заместо числа. Попробуйте снова!");
                continue;
            }
            if (productCount > 50 || productCount <= 0) {
                System.out.println("Вы ввели некорректное кол-во продукта. Попробуйте снова!");
                continue;
            }
            basket.addToCart(productNumber, productCount);
            clientLog.log(productNumber + 1, productCount);
        }
        if (logIsEnabled) {
            clientLog.exportAsCSV(csvFile);
        }
        if (saveIsEnabled) {
            basket.saveJson(jsonFile);
        }
        basket.printCart();
    }
}


