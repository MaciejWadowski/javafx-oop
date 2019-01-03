package gui.controllers;

import gui.dialogs.DialogUtils;
import gui.dialogs.FxmlUtils;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lab1.data.frame.DataFrame;
import values.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileOpenController {

    private static final String APPLICATION_CONTROLLER_FXML = "/fxml/ApplicationController.fxml";

    @FXML
    private Button nextButton;
    @FXML
    private TextField textField;
    @FXML
    private ComboBox<Class<? extends Value>> comboBox;
    @FXML
    private TextArea textArea;

    private ArrayList<Class<? extends Value>> dataFrameClasses;
    private ArrayList<String> dataFrameNames;
    private MainController mainController;
    private File file;
    private DataFrame dataFrame;

    @FXML
    public void initialize() {
        dataFrameClasses = new ArrayList<>();
        dataFrameNames = new ArrayList<>();
        List<Class<? extends Value>> arrayList = new ArrayList<>();
        arrayList.add(StringValue.class);
        arrayList.add(IntegerValue.class);
        arrayList.add(DoubleValue.class);
        arrayList.add(DateTimeValue.class);
        arrayList.add(FloatValue.class);

        ListProperty<Class<? extends Value>> listProperty = new SimpleListProperty<>();
        ObservableList<Class<? extends Value>> classList = FXCollections.observableArrayList(arrayList);
        listProperty.set(classList);

        comboBox.itemsProperty().bindBidirectional(listProperty);
        textField.setText("");
        textField.setPromptText("Column name:");
    }

    @FXML
    public void back() {
        mainController.setMenu();
        textArea.setText("");
        dataFrameClasses.clear();
    }

    @FXML
    public void next() {

        if (file != null) {
            try {
                dataFrame = new DataFrame(file.getAbsolutePath(), dataFrameClasses.toArray(Class[]::new));
            } catch (Exception e) {
                DialogUtils.errorDialog(e.toString());
            }
        } else {
            dataFrame = new DataFrame(dataFrameNames.toArray(String[]::new), dataFrameClasses.toArray(Class[]::new));
        }

        if (dataFrame != null) {
            FXMLLoader loader = new FXMLLoader(FileOpenController.class.getResource(APPLICATION_CONTROLLER_FXML));
            loader.setController(new ApplicationController(dataFrame));
            loader.setResources(FxmlUtils.getResourceBundle());
            Stage stage = (Stage) mainController.getBorderPane().getScene().getWindow();
            stage.setResizable(true);
            try {
                stage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                DialogUtils.errorDialog(e.toString());
            }
        }
    }


    @FXML
    public void add() {
        if (comboBox.getSelectionModel().getSelectedItem() != null && file != null) {
            dataFrameClasses.add(comboBox.getSelectionModel().getSelectedItem());
            textArea.setText(dataFrameClasses.toString());
        } else if (comboBox.getSelectionModel().getSelectedItem() != null && !textField.getText().equals("")) {
            dataFrameClasses.add(comboBox.getSelectionModel().getSelectedItem());
            dataFrameNames.add(textField.getText());
            textField.setText("");
            textArea.setText(dataFrameClasses.toString() + '\n' + dataFrameNames.toString());
        }
    }

    @FXML
    public void remove() {
        if (file == null && dataFrameNames.contains(textField.getText())) {
            dataFrameClasses.remove(comboBox.getSelectionModel().getSelectedItem());
            dataFrameNames.remove(textField.getText());
        } else {
            dataFrameClasses.remove(comboBox.getSelectionModel().getSelectedItem());
        }
        textArea.setText(dataFrameClasses.toString());
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setFile(File file) {
        this.file = file;
        textField.setEditable(false);
    }

    private Class[] classGuess(File file) {
        ArrayList<Class> classList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            bufferedReader.readLine();
            String[] string = bufferedReader.readLine().split(",");
            for (int i = 0; i < string.length; i++) {
                if (string[i].matches("^([+-]?\\d+)$")) {
                    classList.add(IntegerValue.class);
                } else if (string[i].matches("^([+-]?\\d*\\.?\\d*)$")) {
                    classList.add(DoubleValue.class);
                } else if (string[i].matches("^\\d{4}-\\d{2}-\\d{3}$")) {
                    classList.add(DateTimeValue.class);
                } else {
                    classList.add(StringValue.class);
                }
            }
        } catch (IOException e) {
            DialogUtils.errorDialog(e.toString());
        }
        return classList.toArray(Class[]::new);
    }
}
