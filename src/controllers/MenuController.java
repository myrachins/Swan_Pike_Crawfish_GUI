package controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import settings.AppSettings;

public class MenuController {
    public void onExit(ActionEvent actionEvent) {
        // TODO: exit application
    }

    public void onAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("About program");
        alert.setHeaderText(AppSettings.PROGRAM_NAME);
        alert.setContentText(AppSettings.ABOUT_PROGRAM);

        alert.showAndWait();
    }
}
