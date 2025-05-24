package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import model.Wallet;
import utils.DataUtil;
import utils.FestivalUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HomeController {
    @FXML
    public StackPane calendarPane;
    public Label percent;
    public VBox festivalList;

    private LayoutController layoutController;

    @FXML
    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/calendar.fxml"));
            GridPane calendarPaneRoot = loader.load();
            calendarPane.getChildren().add(calendarPaneRoot);

            CalendarController calendarController = loader.getController();
            calendarController.setHomeController(this);

            // 调用loadFestivals方法动态加载节日内容
            loadFestivals();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFestivals() {
        // 获取当前年月（格式：yyyy-MM）
        String yearMonth = java.time.YearMonth.now().toString();

        // 从文件读取节日列表
        List<String> festivals = FestivalUtil.read(yearMonth);

        // 获取festivalContent容器
        VBox festivalContent = (VBox) festivalList.lookup("#festivalContent");
        for (String festival : festivals) {
            Label label = new Label(festival);
            label.setStyle("-fx-text-fill: #666; -fx-font-size: 13px; -fx-padding: 3px 0;");
            // 设置每个节日标签的文字水平居中
            label.setTextAlignment(TextAlignment.CENTER);
            festivalContent.getChildren().add(label);
        }
    }

    public void handleCsvImport(MouseEvent mouseEvent) {
        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) mouseEvent.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Csv File");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                List<String> lines = new ArrayList<>();
                String line;
                int lineNumber = 0;
                while ((line = reader.readLine()) != null) {
                    if (lineNumber++ == 0) continue;
                    String[] split = line.split(",");
                    String company = split[0];
                    String category = split[2];
                    double amount = Double.parseDouble(split[3]);
                    String time = split[4];
                    if (split[1].equalsIgnoreCase("expenditure")) {
                        amount *= -1;
                    }
                    lines.add(amount + "," + category + "," + company + "," + time);
                }

                String filepath = "data/" + DataUtil.currentUser.getUsername() + "/wallet.csv";
                try (FileWriter writer = new FileWriter(filepath, true)) {
                    for (String str : lines) {
                        writer.write(str + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void navigateToAI(MouseEvent mouseEvent) {
        layoutController.loadFXML("ai");
    }

    public void switchWritingPane(MouseEvent mouseEvent) {
        layoutController.loadFXML("inputForm");
    }

    public void setLayoutController(LayoutController layoutController) {
        this.layoutController = layoutController;
    }

    /**
     * update festival list
     *
     * @param yearMonth 2025-05
     */
    public void updateFestivalList(String yearMonth) {
        System.out.println(yearMonth);
        festivalList.getChildren().clear();

        List<String> list = FestivalUtil.read(yearMonth);
        for (String line : list) {
            String[] split = line.replaceFirst(" ", "#").split("#");
            Label timeLabel = new Label(split[0]);
            festivalList.getChildren().add(timeLabel);

            Label nameLabel = new Label(split[1]);
            nameLabel.setWrapText(true);
            nameLabel.setTextAlignment(TextAlignment.CENTER);
            festivalList.getChildren().add(nameLabel);
        }
    }
}