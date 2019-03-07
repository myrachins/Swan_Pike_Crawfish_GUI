package controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import settings.AppSettings;
import utils.AlertFactory;

public class MenuController {
    public void onExit(ActionEvent actionEvent) {
        // TODO: exit application
    }

    public void onAbout(ActionEvent actionEvent) {
        Alert alert = AlertFactory.GetAlert(AlertFactory.AlertType.INFO, "About program",
                AppSettings.PROGRAM_NAME, AppSettings.ABOUT_PROGRAM);
        alert.showAndWait();
    }
}
