package gui.controllers;

import exceptions.ValueOperationException;
import gui.dialogs.DialogUtils;
import gui.dialogs.FxmlUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lab1.data.frame.Column;
import lab1.data.frame.DataFrame;
import values.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ApplicationController {

    private static final String OPERATIONS_CONTROLLER_FXML = "/fxml/OperationsController.fxml";
    private static final String CHART_CONTROLLER_FXML = "/fxml/ChartController.fxml";

    @FXML
    private BorderPane borderPane;
    @FXML
    private TableView<Integer> tableView;
    @FXML
    private HBox hBox;
    @FXML
    private MenuItem closeItem;
    @FXML
    private MenuItem caspian;
    @FXML
    private ToggleButton mainTableToggleButton;
    @FXML
    private ToggleButton operationToggleButton;
    @FXML
    private ToggleButton chartToggleButton;

    private Integer visibleRows;
    private DataFrame dataFrame;
    private Class[] classes;
    private List<TextField> textFields;
    private ObservableList<Integer> observableList;

    public ApplicationController(DataFrame dataFrame) {
        this.dataFrame = dataFrame;
    }

    public void initialize() {
        String[] names = dataFrame.getColumnNames();
        classes = dataFrame.getClasses();
        tableView.setEditable(true);
        visibleRows = 100;
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

        textFields = new ArrayList<>();
        tableView.getColumns().add(intColumn);

        for (String name : names) {
            TableColumn<Integer, Value> tableColumn = new TableColumn<>(name);
            tableColumn.setMinWidth(300);
            tableColumn.setCellValueFactory(cellData -> {
                Integer value = cellData.getValue();
                return new ReadOnlyObjectWrapper<>(dataFrame.getColumn(name).getElement(value));
            });
            list.add(tableColumn);

            TextField textField = new TextField();
            textField.setPromptText("Enter value to " + name + " column");
            textField.setMinWidth(200);
            textFields.add(textField);
            hBox.getChildren().add(textField);
            columns.add(dataFrame.getColumn(name));
        }

        // Menu Items
        close(closeItem);
        caspian.setOnAction(e -> Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN));
        caspian.setOnAction(e -> Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA));

        //ToggleButtons
        mainTableToggleButton.setOnAction(e -> {
            borderPane.setCenter(tableView);
            hBox.setVisible(true);
        });
        operationToggleButton.setOnAction(e -> setOperationContainer(OPERATIONS_CONTROLLER_FXML));
        chartToggleButton.setOnAction(e -> setChartContainer(CHART_CONTROLLER_FXML));

        // Hbox
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10));
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(addRow(new Button("Add Row")));

        tableView.getColumns().addAll(list);
    }

    public Button addRow(Button button) {
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            Value[] value = new Value[textFields.size()];
            for (int i = 0; i < textFields.size(); i++) {
                try {
                    value[i] = (Value) classes[i].getConstructor(String.class).newInstance(textFields.get(i).getText());
                    textFields.get(i).clear();
                } catch (Exception exception) {
                    DialogUtils.errorDialog(exception.toString());
                }
            }
            try {
                dataFrame.addRow(value);
                tableView.getItems().add(dataFrame.size() - 1);
            } catch (ValueOperationException e1) {
                DialogUtils.errorDialog(e.toString());
            }
        });
        return button;
    }

    public void close(MenuItem menuItem) {
        menuItem.setOnAction(e -> {
            Optional<ButtonType> result = DialogUtils.confirmationDialog();
            if (result.get() == ButtonType.OK) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public void setOperationContainer(String string) {
        Parent root = FxmlUtils.fxmlLoader(string);
        OperationsController operationsController = (OperationsController) FxmlUtils.getController(root);
        operationsController.setDataFrame(dataFrame);
        borderPane.setCenter(root);
        hBox.setVisible(false);
    }

    public void setChartContainer(String string) {
        Parent root = FxmlUtils.fxmlLoader(string);
        ChartController chartController = (ChartController) FxmlUtils.getController(root);
        chartController.setDataFrame(dataFrame);
        borderPane.setCenter(root);
        hBox.setVisible(false);
    }
}
