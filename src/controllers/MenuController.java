package controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import settings.AppSettings;
import utils.AlertFactory;

public class MenuController {
    public void onExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void onAbout(ActionEvent actionEvent) {
        Alert alert = AlertFactory.getInfoAlert("About program",
                AppSettings.PROGRAM_NAME, AppSettings.ABOUT_PROGRAM);
        alert.showAndWait();
    }
}
