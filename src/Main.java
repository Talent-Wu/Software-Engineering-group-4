import utils.StageContainer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application launcher class
 */
public class Main extends Application {

    @Override
    public void start (Stage loginStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("./fxml/login.fxml"));
        loginStage.setTitle("Login");
        loginStage.setScene(new Scene(root));
        loginStage.show();

        StageContainer.addStage("loginStage", loginStage);
    }

    public static void main (String[] args) {
        launch(args);
    }
}
