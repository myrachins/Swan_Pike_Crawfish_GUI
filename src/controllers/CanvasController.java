package controllers;

import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import models.Creature;
import models.Runner;
import models.Truck;
import settings.AppSettings;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class CanvasController implements Initializable {
    public Canvas canvas;
    private ArrayList<Pair<Double, Double>> truckHistory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        truckHistory = new ArrayList<>();
        drawBorder();
        Runner.addTruckListener(new Runner.TruckListener() {
            @Override
            public void truckMoved(Truck truck, Collection<Creature> creatures) {
                synchronized (canvas) {
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    clearCanvas();
                    setContext(gc);
                    synchronized (truck) {
                        truckHistory.add(new Pair<>(truck.getX(), truck.getY())); // adding new element to history
                        drawHistory(gc);
                        creatures.forEach((creature) -> drawCreature(gc, truck, creature));
                        truck.notifyAll();
                    }
                }
            }

            @Override
            public void onStart() { }

            @Override
            public void onFinish() {
                synchronized (canvas) {
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    clearCanvas();
                    truckHistory.clear();
                    canvas.notifyAll();
                }
            }
        });
    }

    private void clearCanvas() {
        synchronized (canvas) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // clearing canvas
            drawBorder();
        }
    }

    private double getCanvasXCenter() {
        return canvas.getWidth() / 2;
    }

    private double getCanvasYCenter() {
        return canvas.getHeight() / 2;
    }

    private void drawBorder() {
        synchronized (canvas) {
            GraphicsContext gc = canvas.getGraphicsContext2D() ;
            gc.setStroke(Color.GREY);
            gc.setLineWidth(4);
            gc.strokeRoundRect(0, 0, canvas.getWidth(), canvas.getHeight(), 0, 0);
            //gc.stroke();
        }
    }

    private void setContext(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
    }

    private void drawTruck(GraphicsContext gc, Truck truck) {
        synchronized (truck) {
            gc.fillOval(truck.getX() + getCanvasXCenter(), truck.getY() + getCanvasYCenter(),
                    AppSettings.TRUCK_WIDTH, AppSettings.TRUCK_HEIGHT);
            truck.notifyAll();
        }
    }

    private void drawCreature(GraphicsContext gc, Truck truck, Creature creature) {
        synchronized (truck) {
            synchronized (creature) {
                double x = Math.cos(Math.PI * creature.getAngle() / 180f) * 100 + truck.getX();
                double y = Math.sin(Math.PI * creature.getAngle() / 180f) * 100 + truck.getY();
                gc.fillOval(x + getCanvasXCenter(), y + getCanvasYCenter(), 10, 10);
                creature.notifyAll();
            }
            truck.notifyAll();
        }
    }

    private void drawHistory(GraphicsContext gc) {
        for(Pair<Double, Double> coordinate : truckHistory) {
            gc.fillOval(coordinate.getKey() + getCanvasXCenter(), coordinate.getValue() + getCanvasYCenter(),
                    AppSettings.TRUCK_WIDTH, AppSettings.TRUCK_HEIGHT);
        }
    }
}
