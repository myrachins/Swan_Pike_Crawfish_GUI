package controllers;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import models.Runner;
import settings.AppSettings;
import settings.RunAttributes;

public class SettingsController {
    public TextField swanAngle;
    public TextField pikeAngle;
    public TextField crawfishAngle;

    public Button start;
    public Button stop;
    public Button reset;

    public void setSwanAngle(KeyEvent actionEvent) {
        checkAngle(swanAngle);
    }

    public void setPikeAngle(KeyEvent actionEvent) {
        checkAngle(pikeAngle);
    }

    public void setCrawfishAngle(KeyEvent actionEvent) {
        checkAngle(crawfishAngle);
    }

    private void checkAngle(TextField textField) {
        String text = textField.getText().trim();
        if(!isInteger(text) || Integer.parseInt(text) < AppSettings.MIN_ANGLE
                || Integer.parseInt(text) > AppSettings.MAX_ANGLE) {
            textField.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            textField.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        // TODO: switch to default background
        // TODO: Unable reset
    }

    private boolean isInteger(String strNumber) {
        try {
            Integer.parseInt(strNumber);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    public void onStart(ActionEvent actionEvent) {
        // TODO: Implement method with custom attributes
        try {
            Runner.start(new RunAttributes());
            // TODO: unable stop
            // TODO: give to start function runnable object to print on this canvas
        } catch (InterruptedException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Error occurred while running");
            alert.setHeaderText("Main thread was interrupted");
            alert.setContentText(e.getLocalizedMessage());

            alert.showAndWait();
        }
    }

    public void onStop(ActionEvent actionEvent) {
        Runner.stop();
        // TODO: unable start
    }

    public void onReset(ActionEvent actionEvent) {
        // TODO: Implement method
    }
}
