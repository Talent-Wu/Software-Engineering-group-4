package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataUtil {

    private static final String CSV_FILE_PATH = "@/data/wallet_data.csv"; // 指定文件路径为项目根目录下的 data 文件夹
    private static final String CSV_HEADER = "Type,Category,Amount,Company,Date\n";

    public static void saveWallet(String type, String category, String amount, String company) {
        // Get current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);

        // Format the data to be written to CSV
        String data = String.format("%s,%s,%s,%s,%s\n", type, category, amount, company, timestamp);

        // Ensure the 'data' directory exists
        File dataDir = new File("./data");
        if (!dataDir.exists()) {
            dataDir.mkdirs(); // Create the directory if it does not exist
        }

        // Write data to CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH, true))) {
            // Check if file is empty and write header if necessary
            if (new File(CSV_FILE_PATH).length() == 0) {
                writer.write(CSV_HEADER);
            }
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}