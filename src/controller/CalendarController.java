package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.YearMonth;

public class CalendarController {

    @FXML
    private Label yearLabel;

    @FXML
    private GridPane calendarGrid;

    @FXML
    private Button previousMonthButton;

    @FXML
    private Button nextMonthButton;

    private YearMonth currentYearMonth;
    private HomeController homeController;

    @FXML
    public void initialize() {
        currentYearMonth = YearMonth.now();
        updateCalendar();

        previousMonthButton.setOnAction(event -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateCalendar();
            homeController.updateFestivalList(currentYearMonth.toString());
        });

        nextMonthButton.setOnAction(event -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateCalendar();
            homeController.updateFestivalList(currentYearMonth.toString());
        });
    }

    private void updateCalendar() {
        yearLabel.setText(currentYearMonth.getYear() + " - " + currentYearMonth.getMonth().toString());

        // clear calendar
        calendarGrid.getChildren().clear();

        // add title
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < 7; i++) {
            Label dayLabel = new Label(daysOfWeek[i]);
            dayLabel.setStyle("-fx-font-weight: bold;");
            GridPane.setConstraints(dayLabel, i, 0);
            calendarGrid.getChildren().add(dayLabel);
        }

        // Calculate the day of the week for the first day of the current month.
        int firstDayOfWeek = currentYearMonth.atDay(1).getDayOfWeek().getValue();
        int daysInMonth = currentYearMonth.lengthOfMonth();

        // fill all date
        for (int day = 1, row = 1, col = firstDayOfWeek % 7; day <= daysInMonth; day++, col++) {
            if (col == 7) {
                col = 0;
                row++;
            }
            Label dateLabel = new Label(String.valueOf(day));
            GridPane.setConstraints(dateLabel, col, row);
            calendarGrid.getChildren().add(dateLabel);
        }
    }

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
        homeController.updateFestivalList(currentYearMonth.toString());
    }
}
