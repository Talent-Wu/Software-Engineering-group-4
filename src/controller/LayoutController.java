package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import utils.StageContainer;

import java.io.IOException;

public class LayoutController {

    public Button homeBtn;
    @FXML
    private BorderPane borderPane;

    @FXML
    public void initialize() {
        loadFXML("home");
    }

    public void switchHome(MouseEvent mouseEvent) {
        loadFXML("home");
    }

    public void switchIncome(MouseEvent mouseEvent) {
        loadFXML("income");
    }

    public void switchExpense(MouseEvent mouseEvent) {
        loadFXML("expense");
    }

    public void switchFuture(MouseEvent mouseEvent) {
        loadFXML("future");
    }

    public void switchWallet(MouseEvent mouseEvent) {
        loadFXML("wallet");
    }

    public void switchProfile(MouseEvent mouseEvent) {
        loadFXML("profile");
    }

    /**
     * A method to load FXML file and set center of home's borderPane
     */
    void loadFXML(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlName + ".fxml"));
            Node subPage = loader.load();

            if (fxmlName.equals("home")) {
                HomeController controller = loader.getController();
                controller.setLayoutController(this);
            } else if (fxmlName.equals("profile")) {
                ProfileController profileController = loader.getController();
                StageContainer.setController("profile", profileController);
            }

            borderPane.setCenter(subPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}