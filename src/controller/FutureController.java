package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Wish;
import utils.DataUtil;
import utils.Dialog;

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

    private ObservableList<Wish> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        progressBar.setProgress(0.5);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        completeColumn.setCellValueFactory(new PropertyValueFactory<>("complete"));
        table.setItems(data);

        loadData();
    }

    private void loadData() {
        data.clear();
        List<Wish> list = new ArrayList<>();
        list.add(new Wish("Tom", 10));
        list.add(new Wish("Bill", 90));
        data.addAll(list);
    }

    public void setTarget(MouseEvent mouseEvent) {
        String goal = Dialog.input("Goal: ");
        setGoalLabel(goal);
        DataUtil.saveData("goal.txt", goal);
    }

    public void reset(MouseEvent mouseEvent) {
        setGoalLabel(DataUtil.readData("goal.txt"));
    }

    private void setGoalLabel(String goal) {
        goalLabel.setText("Amount: " + goal);
    }
}
