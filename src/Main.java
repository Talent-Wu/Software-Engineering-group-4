import javafx.application.Application;
import javafx.stage.Stage;
import utils.StageContainer;

/**
 * Application launcher class
 */
public class Main extends Application {

    @Override
    public void start(Stage loginStage) {
        StageContainer.switchStage("login");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
