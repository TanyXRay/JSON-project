import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Класс, описывающий покупательскую корзину.
 */

public class Basket implements Serializable {

    private String[] productsNames;
    private int[] productsCount;
    private int[] prices;

    public Basket(String[] productsNames, int[] prices) {
        this.productsNames = productsNames;
        this.prices = prices;
        this.productsCount = new int[productsNames.length];
    }

    public Basket() {
    }

    public String[] getProductsNames() {
        return productsNames;
    }

    public void setProductsNames(String[] productsNames) {
        this.productsNames = productsNames;
    }

    public int[] getProductsCount() {
        return productsCount;
    }

    public int[] getPrices() {
        return prices;
    }

    public void setPrices(int[] prices) {
        this.prices = prices;
    }

    public void setProductsCount(int[] productsCount) {
        this.productsCount = productsCount;
    }

    /**
     * Выводит на экран весь ассортимент продуктов для покупки.
     */
    protected void printListAllProductsForBuy() {
        System.out.println("Список возможных товаров для покупки: ");
        for (int i = 0; i < productsNames.length; i++) {
            System.out.println((i + 1) + ". " + productsNames[i] + " " + prices[i] + " руб/шт.");
        }
    }

    /**
     * Добавляет кол-во продуктов по номеру.
     *
     * @param productNum номер продукта из списка.
     * @param amount     кол-во продукта из списка.
     */
    protected void addToCart(int productNum, int amount) {
        productsCount[productNum] += amount;
    }

    /**
     * Выводит на экран покупательскую корзину.
     */
    protected void printCart() {
        System.out.println("Ваша корзина:");
        int sum = 0;
        for (int i = 0; i < productsCount.length; i++) {
            int allCountProduct = productsCount[i];
            int priceSumByProduct = prices[i] * allCountProduct;
            if (allCountProduct > 0) {
                System.out.println(
                        productsNames[i] + " " + allCountProduct + " шт. в сумме " + priceSumByProduct
                        + " руб.");
                sum += priceSumByProduct;
            }
        }
        System.out.println("Итого: " + sum + " руб.");
    }

    /**
     * Сохраняет покупательскую корзину в текстовый файл.
     *
     * @param textFile текстовый файл.
     */
    public void saveTxt(File textFile) {
        try (PrintWriter writer = new PrintWriter(textFile)) {
            for (int pos = 0; pos < productsNames.length; pos++) {
                writer.println(productsNames[pos] + " " + prices[pos] + " " + productsCount[pos]);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error! File not found!");
        }
    }

    /**
     * Восстанавливает объект корзины из текстового файла, в который ранее она была сохранена.
     *
     * @param textFile текстовый файл.
     * @return покупательская корзина.
     */
    public static Basket loadFromTxtFile(File textFile) throws IOException {
        Path path = textFile.toPath();
        List<String> basketList = Files.readAllLines(path);

        String[] productsNames = new String[basketList.size()];
        int[] prices = new int[basketList.size()];
        int[] productsCount = new int[basketList.size()];

        for (int pos = 0; pos <= basketList.size() - 1; pos++) {
            String[] data = basketList.get(pos).split(" ");
            productsNames[pos] = data[0];
            prices[pos] = Integer.parseInt(data[1]);
            productsCount[pos] = Integer.parseInt(data[2]);
        }

        Basket basket = new Basket(productsNames, prices);
        basket.setProductsCount(productsCount);
        System.out.print("Корзина восстановлена!:" + "\n");
        basket.printCart();
        return basket;
    }

    /**
     * Сохраняет покупательскую корзину в файл в бинарном формате.
     *
     * @param file файл в бинарном формате.
     */
    public void saveBin(File file) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            objectOutputStream.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Восстанавливает объект корзины из бинарного файла, в который ранее она была сохранена.
     *
     * @param binFile файл в бинарном формате.
     */
    public static Basket loadFromBinFile(File binFile) throws IOException, ClassNotFoundException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(binFile))) {
            Basket basket = (Basket) objectInputStream.readObject();
            System.out.print("Корзина восстановлена!:" + "\n");
            basket.printCart();
            return basket;
        }
    }

    /**
     * Сохраняет объект корзины в формат JSON.
     *
     * @param jsonFile файл формата JSON.
     */
    public void saveJson(File jsonFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(jsonFile, this);
        } catch (IOException e) {
            throw new RuntimeException("File not found in there!!!");
        }
    }

    /**
     * Восстанавливает объект корзины из JSON файла, в который ранее она была сохранена.
     *
     * @param jsonFile файл формата JSON.
     * @throws IOException
     */
    public static Basket loadFromJsonFile(File jsonFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Basket basket = objectMapper.readValue(jsonFile, Basket.class);
        System.out.print("Корзина восстановлена!:" + "\n");
        basket.printCart();
        return basket;
    }

    @Override
    public String toString() {
        return "Basket{" +
               "productsNames=" + Arrays.toString(productsNames) +
               ", productsCount=" + Arrays.toString(productsCount) +
               ", prices=" + Arrays.toString(prices) +
               '}';
    }
}
