package gui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gui.dialogs.FxmlUtils;
import java.util.Locale;

public class Main extends Application {

    public static final String MAIN_CONTROLLER_FXML = "/fxml/MainController.fxml";

    @Override
    public void start(Stage stage) {
        Locale.setDefault(Locale.ENGLISH);
        Parent root = FxmlUtils.fxmlLoader(MAIN_CONTROLLER_FXML);
        stage.setScene(new Scene(root));
        stage.setTitle("DataFrame");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
