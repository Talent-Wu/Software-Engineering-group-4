package controller;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import utils.AIHelper;
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
    public TextArea aiInput;

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

    @FXML
    public void aiAnalysis(MouseEvent mouseEvent) {
        String result = AIHelper.parseContent(aiInput.getText());
        System.out.println(result);
        if("NO".equalsIgnoreCase(result)) {
            Dialog.alert("Please provide detailed input");
            return;
        }

        String[] split = result.split("#");
        if (split[0].equalsIgnoreCase("income")){
            incomeRadioButton.setSelected(true);
        }else{
            expenditureRadioButton.setSelected(true);
        }

        switch (split[2].toLowerCase()){
            case "pet":
                petRadioButton.setSelected(true);
                break;
            case "phone":
                phoneRadioButton.setSelected(true);
                break;
            case "monetary":
                monetaryRadioButton.setSelected(true);
                break;
            case "snacks":
                snacksRadioButton.setSelected(true);
                break;
            case "tobacco":
                tobaccoRadioButton.setSelected(true);
                break;
            case "daily":
                dailyRadioButton.setSelected(true);
                break;
            case "cosmetics":
                cosmeticsRadioButton.setSelected(true);
                break;
            case "horticulture":
                horticultureRadioButton.setSelected(true);
                break;
            case "meal":
                mealRadioButton.setSelected(true);
                break;
        }

        amount.setText(split[1]);
        company.setText(split[3]);

    }
}
