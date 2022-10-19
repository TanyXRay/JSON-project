import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Решение задачи №1 по теме "Одномерные массивы".
 * Решение задачи №1 по теме "Исключения".
 * Решение задачи №2 по теме "Работа с файлами. Потоки ввода-вывода. Сериализация".
 * Решение задачи №1, №2 по теме "Работа с форматами СSV,JSON,XML.
 */

public class Main {

    public static void main(String[] args) throws IOException {
        String[] products = {"Хлеб", "Молоко", "Яблоки"};
        int[] prices = {35, 87, 109};

        SettingsFile settingsFile = new SettingsFile();
        Scanner scanner = new Scanner(System.in);
        ClientLog clientLog = new ClientLog();
        Basket basket = new Basket(products, prices);

        File csvFile = new File("log.csv");
        File jsonFile = new File("basket.json");

        if (settingsFile.isLoadEnabled() && jsonFile.exists()) {
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
        if (settingsFile.isLogEnabled()) {
            clientLog.exportAsCSV(csvFile);
        }

        if (settingsFile.isSaveEnabled()) {
            basket.saveJson(jsonFile);
        }
        basket.printCart();
    }
}


