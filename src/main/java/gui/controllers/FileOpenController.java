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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lab1.data.frame.DataFrame;
import lab3.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileOpenController {

    @FXML
    private ComboBox<Class<? extends Value>> comboBox;
    @FXML
    private TextArea textArea;

    private ArrayList<Class<? extends Value>> dataFrameClasses;
    private MainController mainController;
    private File file;

    @FXML
    public void initialize() {
        dataFrameClasses = new ArrayList<>();
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
    }

    @FXML
    public void back() {
        mainController.setMenu();
        textArea.setText("");
        dataFrameClasses.clear();
    }

    @FXML
    public void next() {
        Class[] classes = new Class[dataFrameClasses.size()];
        for (int i = 0; i < dataFrameClasses.size(); i++) {
            classes[i] = dataFrameClasses.get(i);
        }

        if(file != null) {
            try {
                DataFrame dataFrame = new DataFrame(file.getAbsolutePath(), classes);
                FXMLLoader loader = new FXMLLoader(FileOpenController.class.getResource("/fxml/ApplicationController.fxml"));
                loader.setController(new ApplicationController(dataFrame));
                loader.setResources(FxmlUtils.getResourceBundle());
                Stage stage = (Stage) mainController.getBorderPane().getScene().getWindow();
                stage.setResizable(true);
                stage.setScene(new Scene(loader.load()));

            } catch (Exception e) {
                DialogUtils.errorDialog(e.toString());
            }
        } else {
            //code for empty DataFrame
        }
    }

    @FXML
    public void add() {
        if(comboBox.getSelectionModel().getSelectedItem() != null) {
            dataFrameClasses.add(comboBox.getSelectionModel().getSelectedItem());
            textArea.setText(dataFrameClasses.toString());
        }
    }

    @FXML
    public void remove() {
        dataFrameClasses.remove(comboBox.getSelectionModel().getSelectedItem());
        textArea.setText(dataFrameClasses.toString());
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setFile(File file) {
        this.file = file;
    }

    private Class[] classGuess(File file) {
        ArrayList<Class> classList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            bufferedReader.readLine();
            String[] string = bufferedReader.readLine().split(",");
            for (int i = 0; i < string.length; i++) {
                if(string[i].matches("^([+-]?\\d+)$")) {
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
