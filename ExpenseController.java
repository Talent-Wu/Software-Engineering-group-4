package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Wallet;
import utils.DataUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseController {

    @FXML
    public TableView table;
    public PieChart chart;
    @FXML
    public TableColumn nameColumn;
    @FXML
    public TableColumn amountColumn;
    @FXML
    public TableColumn deleteBtnColumn;
    public TableColumn companyColumn;

    private ObservableList<Wallet> expenseList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // data binding
        companyColumn.setCellValueFactory(new PropertyValueFactory<>("company"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        table.setItems(expenseList);

        initTableData();
        initChartData();
    }

    private void initTableData() {
        expenseList.clear();
        expenseList.addAll(DataUtil.readExpense());
    }

    private void initChartData() {
        Map<String, Double> group = new HashMap<>();
        for (Wallet expense : expenseList) {
            Double amount = group.get(expense.getCategory());
            if(amount == null) {
                amount = 0.0;
            }
            group.put(expense.getCategory(), amount + Math.abs(expense.getAmount()));
        }

        // render pie chart
        chart.getData().clear();
        group.forEach((category, amount) -> chart.getData().add(new PieChart.Data(category, amount)));
    }

}
