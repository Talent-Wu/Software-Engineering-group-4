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
import utils.StageContainer;
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
    @FXML
    private Button exitButton;
    @FXML
    private Button setTargetButton;
    @FXML
    private Button resetButton;

    private ObservableList<Wish> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        completeColumn.setCellValueFactory(new PropertyValueFactory<>("complete"));
        table.setItems(data);

        loadData();
        calcGoal();
        initUIState(); // 初始化UI状态
        exitButton.setOnAction(e -> {
            if (DataUtil.viewingUser != null && !DataUtil.currentUser.equals(DataUtil.tempUser)) {
                // 如果正在查看关联账户，则切换回自己的资料页面
                DataUtil.currentUser=DataUtil.tempUser;
                StageContainer.switchStage("layout"); // 切换回自己的资料页面
                initUIState(); // 切换后重新初始化UI状态
            } else {
                // 如果已经在自己的资料页面，可以给出提示或者不做任何操作
                Dialog.alert("You are already in your own profile.");
            }
        });
    }

    // 初始化UI状态，根据是否查看关联账户禁用相应按钮
    private void initUIState() {
        boolean isViewingLinkedAccount = DataUtil.viewingUser != null && !DataUtil.currentUser.equals(DataUtil.tempUser);
        moveUpButton.setDisable(isViewingLinkedAccount);
        moveDownButton.setDisable(isViewingLinkedAccount);
        setTargetButton.setDisable(isViewingLinkedAccount);
        resetButton.setDisable(isViewingLinkedAccount);
    }

    private void calcGoal() {
        // Read the target amount (could be user-set goal or default value)
        String goalStr = DataUtil.readData(DataUtil.currentUser.getUsername() + "/goal.txt", false);
        double goal = goalStr.isEmpty() ? 0 : Double.parseDouble(goalStr);

        // Calculate the total income
        List<Wallet> incomeList = DataUtil.readIncome();
        double totalIncome = incomeList.stream().mapToDouble(Wallet::getAmount).sum();

        // Calculate the total expenses
        List<Wallet> expenseList = DataUtil.readExpense();
        double totalExpense = expenseList.stream().mapToDouble(Wallet::getAmount).sum();

        // Calculate the net income (income - expenses)
        double netIncome = totalIncome + totalExpense;
        System.out.println(netIncome);

        // Calculate the completion percentage based on net income
        double percent = 0;
        if (goal > 0) { // Only calculate the ratio when the target amount is greater than 0
            if (netIncome <= 0) { // When expenses are greater than or equal to income, the percentage is 0
                percent = 0;
            } else {
                percent = Math.min(1, netIncome / goal); // Prevent exceeding 100%
            }
        }

        // Update the UI display
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
            calcGoal(); // Recalculate the progress after updating the goal
        }
    }

    public void reset(MouseEvent mouseEvent) {
        // Reset the goal to 0 (can be changed to other default values as needed)
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
        // 清空当前的表格数据
        data.clear();

        // 重新计算每个愿望的完成状态
        List<Wish> updatedWishes = WishCalculater.calculateComplete(
                DataUtil.readIncome(),
                DataUtil.readExpense(),
                DataUtil.readWishList()
        );

        // 更新表格数据
        data.addAll(updatedWishes);

        // 刷新表格显示
        table.refresh();
    }

}