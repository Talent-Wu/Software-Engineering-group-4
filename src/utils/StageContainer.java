package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

/**
 * Store all the stages.
 */
public class StageContainer {
    private static final String DB_PATH = "";
    private static final Map<String, String> fxml2Title = new HashMap<>();

    static {
        fxml2Title.put("login", "Login");
        fxml2Title.put("signup", "Sign Up");
        fxml2Title.put("home", "Home");
        fxml2Title.put("writing", "Writing");
    }
    /**
     * Store all stage
     */
    private static Stack<Stage> stack = new Stack<>();

    private static String getTitle(String fxml){
        String title = fxml2Title.get(fxml);
        if(title == null) {
            System.out.println("no title for " + fxml);
            return "Untitled";
        } else {
            return title;
        }
    }

    /**
     * Cache stage
     */
    public static void switchStage(String fxml) {
        switchStage(getTitle(fxml), fxml, true);
    }
    public static void switchStage(String title, String fxml, boolean removeParent) {
        try {
            Parent panel = FXMLLoader.load(StageContainer.class.getResource("/fxml/" + fxml + ".fxml"));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(panel));
            stage.show();

            if (removeParent && !stack.isEmpty()) {
                Stage parent = stack.pop();
                parent.close();
            }
            stack.push(stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
