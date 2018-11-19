package gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import java.util.Optional;
import java.util.ResourceBundle;

public class DialogUtils {

    private static ResourceBundle resourceBundle = FxmlUtils.getResourceBundle();

    public static void dialogAboutApplication() {
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle(resourceBundle.getString("about.title"));
        informationAlert.setHeaderText(resourceBundle.getString("about.header"));
        informationAlert.setHeaderText(resourceBundle.getString("about.content"));
        informationAlert.showAndWait();
    }

    public static Optional<ButtonType> confirmationDialog() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle(resourceBundle.getString("exit.title"));
        confirmationDialog.setHeaderText(resourceBundle.getString("exit.header"));
        return confirmationDialog.showAndWait();
    }

    public static void errorDialog(String error) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(resourceBundle.getString("error.title"));
        errorAlert.setHeaderText(resourceBundle.getString("error.header"));

        TextArea textArea = new TextArea(error);
        errorAlert.getDialogPane().setContent(textArea); //Node is everything in javaFX
        //errorAlert.getDialogPane().setContent(FxmlUtils.fxmlLoader(fxmlPath);
        errorAlert.showAndWait();
    }
}
