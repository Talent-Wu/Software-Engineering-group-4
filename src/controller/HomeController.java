package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class HomeController {
    @FXML
    public StackPane calendarPane;

    private DesktopController homeController;

    @FXML
    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/calendar.fxml"));
            GridPane calendarRoot = loader.load();
            calendarPane.getChildren().add(calendarRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleCsvImport(MouseEvent mouseEvent) {
        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) mouseEvent.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Csv File");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            openFile(file);
        }
    }

    private void openFile(File file) {
        EventQueue.invokeLater(() -> {

        });
    }

    public void handleImageClick(MouseEvent mouseEvent) {
    }

    public void switchWritingPane(MouseEvent mouseEvent) {
        homeController.loadFXML("inputForm");
    }


    public void setHomeController(DesktopController homeController) {
        this.homeController = homeController;
    }


}
