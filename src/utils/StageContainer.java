package utils;

import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * Store all the stages.
 */
public class StageContainer {
    private static final String DB_PATH = "";

    /**
     * Store all stage
     */
    private static Map<String, Stage> stages = new HashMap<>();

    /**
     * Close stage by a name
     *
     * @param stageName
     */
    public static void closeStage(String stageName) {
        stages.get(stageName).close();
    }

    /**
     * Cache stage
     *
     * @param stageName
     * @param stage
     */
    public static void addStage(String stageName, Stage stage) {
        stages.put(stageName, stage);
    }

    /**
     * Save data to disk
     */
    public static void saveData() {
        try {
            System.out.println("start to save data to disk...");
            FileOutputStream fileOut = new FileOutputStream(DB_PATH);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
            objOut.writeObject(null);
            objOut.close();
            System.out.println("save successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load data from disk
     */
    public static void loadData() {
        try {
            FileInputStream fileIn = new FileInputStream(DB_PATH);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
            String s = (String) objIn.readObject();
            objIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
