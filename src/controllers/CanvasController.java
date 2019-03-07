package controllers;

import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import models.Runner;
import models.Truck;
import settings.AppSettings;

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
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    setContext(gc);
                    //drawGrid(gc, 300, 300, true);
                    int xShift = (int) canvas.getWidth() / 2;
                    int yShift = (int) canvas.getHeight() / 2;
                    drawTruck(gc, (int) truck.getX() + xShift, (int) truck.getY() + yShift,
                            AppSettings.TRUCK_WIDTH, AppSettings.TRUCK_HEIGHT);
                }
            }

            @Override
            public void onStart() { }

            @Override
            public void onFinish() { }
        });
    }

    private void setContext(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
    }

    private void drawTruck(GraphicsContext gc, int x, int y, int width, int height) {
        gc.fillOval(x, y, width, height);
    }

    private void drawGrid(GraphicsContext gc, int width, int height, boolean sharp) {
        gc.setLineWidth(1.0);
        for (int x = 0; x < width; x+=10) {
            double x1 ;
            if (sharp) {
                x1 = x + 0.5 ;
            } else {
                x1 = x ;
            }
            gc.moveTo(x1, 0);
            gc.lineTo(x1, height);
            gc.stroke();
        }

        for (int y = 0; y < height; y+=10) {
            double y1 ;
            if (sharp) {
                y1 = y + 0.5 ;
            } else {
                y1 = y ;
            }
            gc.moveTo(0, y1);
            gc.lineTo(width, y1);
            gc.stroke();
        }
    }
}
