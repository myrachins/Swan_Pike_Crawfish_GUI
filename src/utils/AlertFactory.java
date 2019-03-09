package utils;

import javafx.scene.control.Alert;

public class AlertFactory {
    private AlertFactory() { }

    public static Alert getInfoAlert(String title, String header, String content) {
        return new InfoAlert(title, header, content);
    }

    public static Alert getErrorAlert(String title, String header, String content) {
        return new ErrorAlert(title, header, content);
    }

    public static Alert getErrorAlertForInput(String inputName, String content) {
        return new ErrorAlert("Input error", "Invalid input for " + inputName, content);
    }
}
