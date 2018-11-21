package gui;

import gui.controllers.ApplicationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gui.dialogs.FxmlUtils;
import lab1.data.frame.DataFrame;
import lab3.DoubleValue;
import lab3.IntegerValue;

import java.io.IOException;
import java.util.Locale;

public class Main extends Application {

    public static final String MAIN_CONTROLLER_FXML = "/fxml/MainController.fxml";

    @Override
    public void start(Stage stage) {
        Locale.setDefault(Locale.ENGLISH);
        Parent root = FxmlUtils.fxmlLoader(MAIN_CONTROLLER_FXML);
        stage.setScene(new Scene(root));
        stage.setTitle("DataFrame");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
