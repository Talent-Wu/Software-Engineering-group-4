package controller;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import utils.DataUtil;
import utils.Dialog;

public class InputFormController {

    public RadioButton phoneRadioButton;
    public RadioButton monetaryRadioButton;
    public RadioButton snacksRadioButton;
    public RadioButton tobaccoRadioButton;
    public RadioButton dailyRadioButton;
    public RadioButton cosmeticsRadioButton;
    public RadioButton horticultureRadioButton;
    public RadioButton mealRadioButton;
    public RadioButton incomeRadioButton;
    public RadioButton expenditureRadioButton;
    public RadioButton petRadioButton;
    public TextField company;
    public TextField amount;

    private ToggleGroup radioGroup1 = new ToggleGroup();
    private ToggleGroup radioGroup2 = new ToggleGroup();

    public void initialize() {

        incomeRadioButton.setToggleGroup(radioGroup1);
        expenditureRadioButton.setToggleGroup(radioGroup1);

        petRadioButton.setToggleGroup(radioGroup2);
        phoneRadioButton.setToggleGroup(radioGroup2);
        monetaryRadioButton.setToggleGroup(radioGroup2);
        snacksRadioButton.setToggleGroup(radioGroup2);
        tobaccoRadioButton.setToggleGroup(radioGroup2);
        dailyRadioButton.setToggleGroup(radioGroup2);
        cosmeticsRadioButton.setToggleGroup(radioGroup2);
        horticultureRadioButton.setToggleGroup(radioGroup2);
        mealRadioButton.setToggleGroup(radioGroup2);

    }

    @FXML
    public void submit(MouseEvent mouseEvent) {
        RadioButton typeRadio = (RadioButton) radioGroup1.getSelectedToggle();
        RadioButton categoryRadio = (RadioButton) radioGroup2.getSelectedToggle();

        if (typeRadio == null || categoryRadio == null || company.getText().isEmpty() || amount.getText().isEmpty()) {
            Dialog.alert("Please fill in all fields");
            return;
        }

        String type = typeRadio.getId().replaceAll("RadioButton", "");
        String category = categoryRadio.getId().replaceAll("RadioButton", "");
        DataUtil.saveWallet(type, category, amount.getText(), company.getText());
        Dialog.alert("Submit successfully");
        amount.clear();
        company.clear();
        typeRadio.setSelected(false);
        categoryRadio.setSelected(false);
    }
}
