package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Wallet;
import model.Wish;
import utils.DataUtil;
import utils.Dialog;
import utils.WishCalculater;

import java.util.ArrayList;
import java.util.List;

public class FutureController {

    @FXML
    public ProgressBar progressBar;
    @FXML
    public TableView table;
    @FXML
    public TableColumn nameColumn;
    @FXML
    public TableColumn amountColumn;
    @FXML
    public TableColumn completeColumn;
    @FXML
    public Label goalLabel;
    public Label completeGoalPercent;
    public TextField wishName;
    public TextField wishAmount;

    private ObservableList<Wish> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        completeColumn.setCellValueFactory(new PropertyValueFactory<>("complete"));
        table.setItems(data);

        loadData();

        calcGoal();
    }

    private void calcGoal() {
        String goal = DataUtil.readData(DataUtil.currentUser.getUsername() + "/goal.txt", false);
        setGoalLabel(goal);

        List<Wallet> list = DataUtil.readWallet();
        double totalIncome = 0;
        for (Wallet wallet : list) {
            totalIncome += wallet.getAmount();
        }
        if(totalIncome <0){
            totalIncome = 0;
        }


        if(goal.equals("0")) {
            completeGoalPercent.setText("0.0%");
            progressBar.setProgress(0);
        } else {
            double percent = Math.min(1, totalIncome / Double.parseDouble(goal));
            percent = Math.round(percent * 1000) / 1000.0;

            completeGoalPercent.setText(String.format("%.2f%%", percent * 100));
            progressBar.setProgress(percent);
        }
    }

    private void loadData() {
        data.clear();
        List<Wish> list = WishCalculater.calculateComplete();
        data.addAll(list);
    }

    public void setTarget(MouseEvent mouseEvent) {
        String goal = Dialog.input("Goal: ");
        if("".equals(goal)) {
            Dialog.alert("Goal can't be empty.");
            return;
        }
        DataUtil.saveData("goal.txt", goal);
        calcGoal();
    }

    public void reset(MouseEvent mouseEvent) {
        setGoalLabel(DataUtil.readData(DataUtil.currentUser.getUsername() + "/goal.txt", false));
    }

    private void setGoalLabel(String goal) {
        goalLabel.setText("Amount: " + goal);
    }

    public void addWishlist(MouseEvent mouseEvent) {
        if(!"".equals(wishName.getText().trim()) && !"".equals(wishAmount.getText().trim())){
            DataUtil.saveWish(wishName.getText(), wishAmount.getText());
            data.add(new Wish(wishName.getText(), Double.parseDouble(wishAmount.getText()), DataUtil.getCurrentTime()));
            wishName.clear();
            wishAmount.clear();
        } else {
            Dialog.alert("Wish and amount can be empty.");
        }
    }
}
