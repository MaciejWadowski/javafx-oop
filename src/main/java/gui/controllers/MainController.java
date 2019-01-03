package gui.controllers;

import gui.dialogs.DialogUtils;
import gui.dialogs.FxmlUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ResourceBundle;


public class MainController {

    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox vBox;

    private FileOpenController fileOpenController;
    private Pane pane;

    @FXML
    public void initialize() {
        try {
            FXMLLoader loader = FxmlUtils.load("/fxml/FileOpenController.fxml");
            loader.setResources(FxmlUtils.getResourceBundle());
            pane = loader.load();
            fileOpenController = loader.getController();
            fileOpenController.setMainController(this);
        } catch (Exception e) {
            DialogUtils.errorDialog(e.toString() + e.getMessage());
        }
    }

    @FXML
    public void loadFile() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open source file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Data file", "*.csv"));
        Stage stage = (Stage) borderPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            fileOpenController.setFile(file);
            borderPane.setCenter(pane);
        }
    }

    @FXML
    public void createEmpty() {
        borderPane.setCenter(pane);
    }

    public void setMenu() {
        borderPane.setCenter(vBox);
    }

    public BorderPane getBorderPane() {
        return this.borderPane;
    }
}
