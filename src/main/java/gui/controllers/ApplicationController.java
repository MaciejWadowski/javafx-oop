package gui.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import lab1.data.frame.Column;
import lab1.data.frame.DataFrame;
import lab3.Value;

import java.util.ArrayList;
import java.util.List;


public class ApplicationController {

    @FXML
    private BorderPane borderPane;
    @FXML
    private TableView<Integer> tableView;

    private DataFrame dataFrame;

    public ApplicationController(DataFrame dataFrame) {
        this.dataFrame = dataFrame;
    }

    public ApplicationController() {}

    public void initialize() {
       String[] names = dataFrame.getColumnNames();
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

        tableView.getColumns().add(intColumn);

        for (String name: names) {
            TableColumn<Integer, Value> tableColumn = new TableColumn<>(name);
            tableColumn.setMinWidth(300);
            tableColumn.setCellValueFactory(cellData ->{
                Integer value = cellData.getValue();
                return new ReadOnlyObjectWrapper<>(dataFrame.getColumn(name).getElement(value));
            });
            list.add(tableColumn);
            columns.add(dataFrame.getColumn(name));
        }

        tableView.getColumns().addAll(list);
    }

}
