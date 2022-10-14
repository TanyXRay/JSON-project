
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для сохранения всех операций, которые ввёл пользователь.
 */
public class ClientLog {

    List<String[]> list = new ArrayList<>();
    String[] attributes = "productNum,amount".split(",");

    /**
     * Функция добавления номера товара и его кол-ва в список
     *
     * @param productNum
     * @param amount
     */
    public void log(int productNum, int amount) {
        list.add(new String[]{String.valueOf(productNum), String.valueOf(amount)});
    }

    /**
     * функция для сохранения всего журнала действия в файл в формате csv
     *
     * @param csvFile
     */
    public void exportAsCSV(File csvFile) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
            writer.writeNext(attributes);
            writer.writeAll(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
