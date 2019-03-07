package utils;

import javafx.scene.control.Alert;

class ErrorAlert extends Alert {
    ErrorAlert(String title, String header, String content) {
        super(AlertType.ERROR);
        setTitle(title);
        setHeaderText(header);
        setContentText(content);
    }
}
