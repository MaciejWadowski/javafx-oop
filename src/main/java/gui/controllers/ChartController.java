package gui.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import lab1.data.frame.Column;
import lab1.data.frame.DataFrame;


public class ChartController {

    @FXML
    private BorderPane borderPane;
    @FXML
    private ComboBox<String> xColumn;
    @FXML
    private ComboBox<String> yColumn;
    private DataFrame dataFrame;

    public void setDataFrame(DataFrame dataFrame) {
        this.dataFrame = dataFrame;
        xColumn.setItems(FXCollections.observableArrayList(dataFrame.getColumnNames()));
        yColumn.setItems(FXCollections.observableArrayList(dataFrame.getColumnNames()));
    }

    @FXML
    public void createChart() {
        if(xColumn.getSelectionModel().getSelectedItem() != null && yColumn.getSelectionModel().getSelectedItem() != null) {
            Column firstColumn = dataFrame.getColumn(xColumn.getSelectionModel().getSelectedItem());
            Column secondColumn = dataFrame.getColumn(xColumn.getSelectionModel().getSelectedItem());

            final CategoryAxis xAxis = new CategoryAxis();
            final CategoryAxis yAxis = new CategoryAxis();

            final LineChart<String, String> lineChart = new LineChart<>(xAxis, yAxis);

            lineChart.setTitle("Char of " + firstColumn.getName() + " and " + secondColumn.getName());
            XYChart.Series series = new XYChart.Series();
            series.setName("DataFrame");
            for (int i = 0; i < firstColumn.size(); i++) {
                series.getData().add(new XYChart.Data<>(firstColumn.getElement(i).toString(), secondColumn.getElement(i).toString()));
            }

            lineChart.getData().add(series);
            borderPane.setCenter(lineChart);
        }
    }
}
