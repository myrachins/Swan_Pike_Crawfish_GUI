package utils;

import javafx.scene.control.Alert;

public class AlertFactory {
    private AlertFactory() { }

    public static Alert GetAlert(AlertType type, String title, String header, String content) {
        switch (type) {
            case INFO: return new InfoAlert(title, header, content);
            case ERROR: return new ErrorAlert(title, header, content);
            default: throw new RuntimeException("Unreachable statement");
        }
    }

    public enum AlertType {
        INFO, ERROR
    }
}
