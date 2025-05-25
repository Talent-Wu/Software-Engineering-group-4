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


        // Initialize the exit button
        exitButton.setOnAction(e -> {
            DataUtil.viewingUser = null; // Clear the viewing state
            StageContainer.switchStage("layout"); // Return to your own profile
        });
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
        List<Wish> updatedWishList = WishCalculater.calculateComplete(DataUtil.readIncome(), DataUtil.readExpense(), new ArrayList<>(data));
        data.setAll(updatedWishList);
    }
}