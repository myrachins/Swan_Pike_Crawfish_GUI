package utils;

import javafx.scene.control.Alert;

class InfoAlert extends Alert {
    InfoAlert(String title, String header, String content) {
        super(AlertType.INFORMATION);
        setTitle(title);
        setHeaderText(header);
        setContentText(content);
    }
}
