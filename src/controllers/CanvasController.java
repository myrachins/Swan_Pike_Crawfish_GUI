package controllers;

import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import models.Runner;
import models.Truck;

import java.net.URL;
import java.util.ResourceBundle;

public class CanvasController implements Initializable {
    public Canvas canvas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Runner.addTruckListener(new Runner.TruckListener() {
            @Override
            public void truckMoved(Truck truck) {
                synchronized (canvas) {
                    // TODO: Write
                }
            }

            @Override
            public void onStart() { }

            @Override
            public void onFinish() { }
        });
    }
}
