package gui.controllers;

import exceptions.ValueOperationException;
import gui.dialogs.DialogUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import lab1.data.frame.Column;
import lab1.data.frame.DataFrame;
import values.Value;

import java.util.ArrayList;
import java.util.List;

public class OperationsController {

    @FXML
    private Label label;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private BorderPane borderPane;
    private DataFrame dataFrame;
    private List<String> list;

    public void setDataFrame(DataFrame dataFrame) {
        this.dataFrame = dataFrame;
        list = new ArrayList<>();
        comboBox.setItems(FXCollections.observableArrayList(List.of(dataFrame.getColumnNames())));
    }

    @FXML
    public void max() {
        try {
            setTableView(dataFrame.groupBy(list.toArray(String[]::new)).max());
        } catch (ValueOperationException e) {
            DialogUtils.errorDialog(e.toString());
        }
    }

    @FXML
    public void min() {
        try {
            setTableView(dataFrame.groupBy(list.toArray(String[]::new)).min());
        } catch (ValueOperationException e) {
            DialogUtils.errorDialog(e.toString());
        }
    }

    @FXML
    public void sum() {
        try {
            setTableView(dataFrame.groupBy(list.toArray(String[]::new)).sum());
        } catch (ValueOperationException e) {
            DialogUtils.errorDialog(e.toString());
        }
    }

    @FXML
    public void mean() {
        try {
            setTableView(dataFrame.groupBy(list.toArray(String[]::new)).mean());
        } catch (ValueOperationException e) {
            DialogUtils.errorDialog(e.toString());
        }
    }

    @FXML
    public void var() {
        try {
            setTableView(dataFrame.groupBy(list.toArray(String[]::new)).var());
        } catch (ValueOperationException e) {
            DialogUtils.errorDialog(e.toString());
        }
    }

    @FXML
    public void std() {
        try {
            setTableView(dataFrame.groupBy(list.toArray(String[]::new)).std());
        } catch (ValueOperationException e) {
            DialogUtils.errorDialog(e.toString());
        }
    }

    @FXML
    public void add() {
        if (comboBox.getSelectionModel().getSelectedItem() != null) {
            list.add(comboBox.getSelectionModel().getSelectedItem());
            label.setText(label.getText() + " " + comboBox.getSelectionModel().getSelectedItem());
        }
    }

    public void setTableView(DataFrame dataFrame) {
        String[] names = dataFrame.getColumnNames();
        TableView<Integer> tableView = new TableView<>();
        tableView.setEditable(true);
        List<TableColumn<Integer, Value>> list = new ArrayList<>(names.length);
        List<Column> columns = new ArrayList<>(names.length);
        for (int i = 0; i < dataFrame.size(); i++) {
            tableView.getItems().add(i);
        }

        TableColumn<Integer, Integer> intColumn = new TableColumn<>("nr");
        intColumn.setMinWidth(100);
        intColumn.setCellValueFactory(cellData -> {
            Integer value = cellData.getValue();
            return new ReadOnlyObjectWrapper<>(value);
        });
        intColumn.setVisible(false);

        tableView.getColumns().add(intColumn);

        for (String name : names) {
            TableColumn<Integer, Value> tableColumn = new TableColumn<>(name);
            tableColumn.setMinWidth(300);
            tableColumn.setCellValueFactory(cellData -> {
                Integer value = cellData.getValue();
                return new ReadOnlyObjectWrapper<>(dataFrame.getColumn(name).getElement(value));
            });
            list.add(tableColumn);
            columns.add(dataFrame.getColumn(name));
        }
        tableView.getColumns().addAll(list);
        borderPane.setCenter(tableView);
    }
}
