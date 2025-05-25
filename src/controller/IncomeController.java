package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Income;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IncomeController {

    @FXML
    public TableView table;
    public BarChart chart;
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
        saveToCSV(); // 自动保存到 CSV 文件
    }

    private void loadData() {
        data.clear();
        List<Income> list = new ArrayList<>();
        list.add(new Income("Tom", 10));
        list.add(new Income("Bill", 90));
        list.add(new Income("Kate", 50));
        data.addAll(list);

        initializeChartData();
    }

    private void initializeChartData() {
        chart.getData().clear();
        javafx.scene.chart.XYChart.Series<String, Number> series = new javafx.scene.chart.XYChart.Series<>();
        series.setName("Income ranking list");
        for (Income income : data) {
            series.getData().add(new javafx.scene.chart.XYChart.Data<>(income.getName(), income.getAmount()));
        }
        chart.getData().add(series);
    }

    // 新增方法：将数据保存到 CSV 文件
    private void saveToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("income.csv"))) {
            // 写入表头
            writer.write("Name,Amount");
            writer.newLine();

            // 写入数据
            for (Income income : data) {
                writer.write(income.getName() + "," + income.getAmount());
                writer.newLine();
            }
            System.out.println("Data saved to income.csv successfully.");
        } catch (IOException e) {
            System.err.println("Error saving data to CSV: " + e.getMessage());
        }
    }
}
