package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataUtil {

    public static void saveWallet(String type, String category, String amount, String company) {
        try (FileWriter writer = new FileWriter("data/wallet.csv", true)) {
            if(type.equals("income")) {
                writer.write(amount + "," + category + "," + company + "\n");
            } else{
                writer.write("-" + amount + "," + category + "," + company + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveData(String filename, String data) {
        try (FileWriter writer = new FileWriter("data/" + filename)) {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readData(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/" +filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines.get(0);
    }
}
