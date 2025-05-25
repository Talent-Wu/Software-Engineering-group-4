package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Income;

import java.util.ArrayList;
import java.util.List;

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

    private ObservableList<Income> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // data binding
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        table.setItems(data);

        loadData();
    }

    private void loadData() {
        data.clear();
        List<Income> list = new ArrayList<>();
        list.add(new Income("张三", 10));
        list.add(new Income("李四", 90));
        list.add(new Income("wa", 50));
        data.addAll(list);

        initializeChartData();
    }

    private void initializeChartData() {
        chart.getData().clear();
        for (Income expense : data) {
            chart.getData().add(new PieChart.Data(expense.getName(), expense.getAmount()));
        }
    }

}
