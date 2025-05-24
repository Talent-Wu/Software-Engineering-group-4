package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Wallet;
import utils.DataUtil;

public class IncomeController {

    @FXML
    public TableView table;
    @FXML
    public TableColumn nameColumn;
    @FXML
    public TableColumn amountColumn;
    @FXML
    public TableColumn deleteBtnColumn;
    @FXML
    public BarChart chart;
    public TableColumn companyColumn;

    private ObservableList<Wallet> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // data binding
        companyColumn.setCellValueFactory(new PropertyValueFactory<>("company"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        table.setItems(data);

        loadData();
    }

    private void loadData() {
        data.clear();
        data.addAll(DataUtil.readIncome());

        initializeChartData();
    }

    private void initializeChartData() {
        chart.getData().clear();
        javafx.scene.chart.XYChart.Series<String, Number> series = new javafx.scene.chart.XYChart.Series<>();
        series.setName("Income ranking list");
        for (Wallet income : data) {
            series.getData().add(new javafx.scene.chart.XYChart.Data<>(income.getCategory(), income.getAmount()));
        }
        chart.getData().add(series);
    }

}
