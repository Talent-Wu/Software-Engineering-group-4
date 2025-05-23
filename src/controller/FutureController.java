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
    public TableView<Wish> table;
    @FXML
    public TableColumn<Wish, String> nameColumn;
    @FXML
    public TableColumn<Wish, Double> amountColumn;
    @FXML
    public TableColumn<Wish, String> completeColumn;
    @FXML
    public Label goalLabel;
    public Label completeGoalPercent;
    public TextField wishName;
    public TextField wishAmount;
    @FXML
    public Button moveUpButton;
    @FXML
    public Button moveDownButton;

    private ObservableList<Wish> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        completeColumn.setCellValueFactory(new PropertyValueFactory<>("complete"));
        table.setItems(data);

        loadData();
        calcGoal(); // 初始化时计算目标进度
    }

    private void calcGoal() {
        // 读取目标金额（可能为用户设置的目标或默认值）
        String goalStr = DataUtil.readData(DataUtil.currentUser.getUsername() + "/goal.txt", false);
        double goal = goalStr.isEmpty() ? 0 : Double.parseDouble(goalStr);

        // 计算收入总和
        List<Wallet> incomeList = DataUtil.readIncome();
        double totalIncome = incomeList.stream().mapToDouble(Wallet::getAmount).sum();

        // 计算支出总和
        List<Wallet> expenseList = DataUtil.readExpense();
        double totalExpense = expenseList.stream().mapToDouble(Wallet::getAmount).sum();

        // 计算净收入（收入 - 支出）
        double netIncome = totalIncome + totalExpense;
        System.out.println(netIncome);
        // 根据净收入计算完成百分比
        double percent = 0;
        if (goal > 0) { // 目标金额大于0时才计算比例
            if (netIncome <= 0) { // 支出大于等于收入时，百分比为0
                percent = 0;
            } else {
                percent = Math.min(1, netIncome / goal); // 防止超过100%
            }
        }

        // 更新界面显示
        goalLabel.setText("Goal: $" + String.format("%.2f", goal));
        completeGoalPercent.setText(String.format("%.2f%%", percent * 100));
        progressBar.setProgress(percent);
    }

    private void loadData() {
        data.clear();
        List<Wish> list = WishCalculater.calculateComplete(DataUtil.readIncome(), DataUtil.readExpense(), DataUtil.readWishList());
        data.addAll(list);
    }

    public void setTarget(MouseEvent mouseEvent) {
        String goal = Dialog.input("Enter your financial goal:");
        if (!goal.isEmpty()) {
            DataUtil.saveData("goal.txt", goal);
            calcGoal(); // 更新目标后重新计算进度
        }
    }

    public void reset(MouseEvent mouseEvent) {
        // 重置目标为0（可根据需求改为其他默认值）
        DataUtil.saveData("goal.txt", "0");
        calcGoal();
    }

    public void addWishlist(MouseEvent mouseEvent) {
        String name = wishName.getText().trim();
        String amountStr = wishAmount.getText().trim();

        if (!name.isEmpty() && !amountStr.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr);
                DataUtil.saveWish(name, amountStr);
                data.add(new Wish(name, amount, DataUtil.getCurrentTime()));
                wishName.clear();
                wishAmount.clear();
            } catch (NumberFormatException e) {
                Dialog.alert("Invalid amount! Please enter a valid number.");
            }
        } else {
            Dialog.alert("Wish name and amount cannot be empty.");
        }
    }

    @FXML
    public void moveWishUp(MouseEvent mouseEvent) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        if (selectedIndex > 0) {
            Wish selectedWish = table.getSelectionModel().getSelectedItem();
            data.remove(selectedWish);
            data.add(selectedIndex - 1, selectedWish);
            table.getSelectionModel().select(selectedIndex - 1);
            recalibrateWishCompletion();
        }
    }

    @FXML
    public void moveWishDown(MouseEvent mouseEvent) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < data.size() - 1) {
            Wish selectedWish = table.getSelectionModel().getSelectedItem();
            data.remove(selectedWish);
            data.add(selectedIndex + 1, selectedWish);
            table.getSelectionModel().select(selectedIndex + 1);
            recalibrateWishCompletion();
        }
    }

    private void recalibrateWishCompletion() {
        List<Wish> updatedWishList = WishCalculater.calculateComplete(DataUtil.readIncome(), DataUtil.readExpense(), new ArrayList<>(data));
        data.setAll(updatedWishList);
    }
}