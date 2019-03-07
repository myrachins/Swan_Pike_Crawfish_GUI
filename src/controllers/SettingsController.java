package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
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
import models.Truck;
import settings.AppSettings;
import settings.RunAttributes;
import utils.AlertFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class SettingsController implements Initializable {
    /**
     * If we would use SettingsController from different threads, we should mark it as volatile or add some synchronized blocks
     * By know it's called only from application thread
     */
    private /*volatile*/ ExecuteState state;
    private HashMap<TextField, Boolean> isCorrect;

    public TextField swanAngle;
    public TextField pikeAngle;
    public TextField crawfishAngle;
    public TextField sleepLowBound;
    public TextField sleepUpperBound;
    public TextField forceLowBound;
    public TextField forceUpperBound;
    public TextField workTime;
    public TextField startX;
    public TextField startY;

    public Button start;
    public Button stop;
    public Button reset;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDefaultFields();
        state = ExecuteState.NONWORKING;
        isCorrect = initializeFieldsStates();
        Runner.addTruckListener(new Runner.TruckListener() {
            private long timeOfSimulationStart;

            @Override
            public void truckMoved(Truck truck) { }

            @Override
            public void onStart() {
                start.setDisable(true);
                stop.setDisable(false);
                setDisableAllFields(true);
                state = ExecuteState.WORKING;
                timeOfSimulationStart = System.currentTimeMillis();
            }

            @Override
            public void onFinish() {
                Platform.runLater(() -> {
                    Alert alert = AlertFactory.GetAlert(AlertFactory.AlertType.INFO, "Program dialog",
                            "Simulation has been finished", "Duration of simulation: "
                                    + (System.currentTimeMillis() - timeOfSimulationStart));
                    alert.showAndWait();
                });

                start.setDisable(false);
                stop.setDisable(true);
                setDisableAllFields(false);
                state = ExecuteState.NONWORKING;
            }
        });
    }

    public void setSwanAngle(KeyEvent actionEvent) {
        checkAngle(swanAngle);
    }

    public void setPikeAngle(KeyEvent actionEvent) {
        checkAngle(pikeAngle);
    }

    public void setCrawfishAngle(KeyEvent actionEvent) {
        checkAngle(crawfishAngle);
    }

    public void setForceLowBound(KeyEvent keyEvent) {
        checkPair(forceLowBound, forceUpperBound, text -> isInteger(text), (a, b) -> a <= b);
    }

    public void setForceUpperBound(KeyEvent keyEvent) {
        checkPair(forceUpperBound, forceLowBound, text -> isInteger(text), (a, b) -> a >= b);
    }

    public void setSleepLowBound(KeyEvent keyEvent) {
        checkPair(sleepLowBound, sleepUpperBound, text -> isInteger(text) && Integer.parseInt(text) >= 0, (a, b) -> a <= b);
    }

    public void setSleepUpperBound(KeyEvent keyEvent) {
        checkPair(sleepUpperBound, sleepLowBound, text -> isInteger(text) && Integer.parseInt(text) >= 0, (a, b) -> a >= b);
    }

    public void setWorkTime(KeyEvent keyEvent) {
        checkField(workTime, (text) -> isInteger(text) && Integer.parseInt(text) > 0);
    }

    public void setStartX(KeyEvent keyEvent) {
        checkField(startX, this::isDouble);
    }

    public void setStartY(KeyEvent keyEvent) {
        checkField(startY, this::isDouble);
    }

    public void onStart(ActionEvent actionEvent) {
        if (state == ExecuteState.WORKING) {
            Runner.stop(); // Perfectly it's never gonna happen
        }
        Runner.start(getFields());
    }

    public void onStop(ActionEvent actionEvent) {
        if (state == ExecuteState.WORKING) {
            Runner.stop();
        }
        start.setDisable(false);
        setDisableAllFields(false);
        state = ExecuteState.NONWORKING;
    }

    public void onReset(ActionEvent actionEvent) {
        onStop(actionEvent);
        setDefaultFields();
    }

    private HashMap<TextField, Boolean> initializeFieldsStates() {
        HashMap<TextField, Boolean> result = new HashMap<>();
        result.put(swanAngle, true);
        result.put(pikeAngle, true);
        result.put(crawfishAngle, true);
        result.put(sleepLowBound, true);
        result.put(sleepUpperBound, true);
        result.put(forceLowBound, true);
        result.put(forceUpperBound, true);
        result.put(workTime, true);
        result.put(startX, true);
        result.put(startY, true);

        return result;
    }

    private RunAttributes getFields() {
        return new RunAttributes(
                Integer.parseInt(sleepLowBound.getText()),
                Integer.parseInt(sleepUpperBound.getText()),
                Integer.parseInt(forceLowBound.getText()),
                Integer.parseInt(forceUpperBound.getText()),
                Integer.parseInt(swanAngle.getText()),
                Integer.parseInt(pikeAngle.getText()),
                Integer.parseInt(crawfishAngle.getText()),
                Integer.parseInt(workTime.getText()),
                Double.parseDouble(startX.getText()),
                Double.parseDouble(startY.getText())
        );
    }

    private void setDefaultFields() {
        RunAttributes attributes = new RunAttributes();
        sleepLowBound.setText(Integer.toString(attributes.getSleepLowBound()));
        sleepUpperBound.setText(Integer.toString(attributes.getSleepUpperBound()));
        forceLowBound.setText(Integer.toString(attributes.getForceLowBound()));
        forceUpperBound.setText(Integer.toString(attributes.getForceUpperBound()));
        swanAngle.setText(Integer.toString(attributes.getSwanAngle()));
        pikeAngle.setText(Integer.toString(attributes.getPikeAngle()));
        crawfishAngle.setText(Integer.toString(attributes.getCrawfishAngle()));
        workTime.setText(Integer.toString(attributes.getWorkTime()));
        startX.setText(Double.toString(attributes.getStartX()));
        startY.setText(Double.toString(attributes.getStartY()));
    }

    private void setDisableAllFields(boolean value) {
        swanAngle.setDisable(value);
        pikeAngle.setDisable(value);
        crawfishAngle.setDisable(value);
        sleepLowBound.setDisable(value);
        sleepUpperBound.setDisable(value);
        forceLowBound.setDisable(value);
        forceUpperBound.setDisable(value);
        workTime.setDisable(value);
        startX.setDisable(value);
        startY.setDisable(value);
    }

    private void checkField(TextField textField, Predicate<String> condition) {
        String text = textField.getText().trim();
        if(!condition.test(text)) {
            textField.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            isCorrect.replace(textField, false);
        } else {
            textField.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            isCorrect.replace(textField, true);
        }
        start.setDisable(isCorrect.containsValue(false));
        // TODO: switch to default background
    }

    private void checkAngle(TextField textField) {
        checkField(textField, text -> isInteger(text)
                && Integer.parseInt(text) >= AppSettings.MIN_ANGLE
                && Integer.parseInt(text) <= AppSettings.MAX_ANGLE);
    }

    private void checkPair(TextField changed, TextField sibling, Predicate<String> condition, BiPredicate<Integer, Integer> compare) {
        checkField(changed, condition);
        if (isInteger(changed.getText()) && isInteger(sibling.getText())) {
            Predicate<String> compareCondition = (text) -> compare.test(Integer.parseInt(changed.getText()), Integer.parseInt(sibling.getText()));
            checkField(changed, condition.and(compareCondition));
            checkField(sibling, condition.and(compareCondition));
        }
    }

    private boolean isDouble(String strNumber) {
        return !isThrowing(() -> Double.parseDouble(strNumber));
    }

    private boolean isInteger(String strNumber) {
        return !isThrowing(() -> Integer.parseInt(strNumber));
    }

    private boolean isThrowing(Runnable runnable) {
        try {
            runnable.run();
        } catch(Exception e) {
            return true;
        }
        return false;
    }

    private enum ExecuteState {
        NONWORKING, WORKING
    }
}
