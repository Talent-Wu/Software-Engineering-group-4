package controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Wallet;
import utils.AIHelper;
import utils.DataUtil;

import javax.swing.*;
import java.util.List;

public class AIController {

    @FXML
    public TextArea userInput;
    public VBox chatContainer;

    @FXML
    public void initialize() {

    }

    public void onSubmit(MouseEvent mouseEvent) {
        List<Wallet> list = DataUtil.readIncome();
        double total = 0.0;
        for (Wallet wallet : list) {
            total += wallet.getAmount();
        }
        String prompt = "我目前余额还有"+total+", 您作为一个财务专家，告诉我是否应该做下面这件事，并给出简短的原因或解释，英语回答（不要markdown格式）。《"+userInput.getText()+"》";
        chatContainer.getChildren().add(createRightChatBubble(userInput.getText()));
        userInput.clear();

        String result = AIHelper.chat(prompt);
        chatContainer.getChildren().add(createLeftChatBubble(result));
    }

    private HBox createLeftChatBubble(String message) {
        HBox hbox = new HBox();
        hbox.setPrefWidth(497.0);
        hbox.getStyleClass().add("left");

        Label label = new Label(message);
        label.setMaxWidth(415.0);
        label.setWrapText(true);

        hbox.getChildren().add(label);

        return hbox;
    }

    private HBox createRightChatBubble(String message) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPrefWidth(497.0);
        hbox.setLayoutX(10.0);
        hbox.setLayoutY(10.0);
        hbox.getStyleClass().add("right");

        Label label = new Label(message);
        label.setMaxWidth(415.0);
        label.setWrapText(true);

        hbox.getChildren().add(label);

        return hbox;
    }
}
