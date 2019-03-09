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
                    synchronized (truck) {
                        drawHistory(gc);
                        drawTruck(gc, truck);
                        truckHistory.add(new Pair<>(truck.getX(), truck.getY())); // adding new element to history
                        creatures.forEach((creature) -> drawCreature(gc, truck, creature));
                        truck.notifyAll();
                    }
                }
            }

            @Override
            public void onStart() {
                synchronized (canvas) {
                    clearCanvas();
                    truckHistory.clear();
                    canvas.notifyAll();
                }
            }

            @Override
            public void onFinish() { }
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
        }
    }

    private void drawTruck(GraphicsContext gc, Truck truck) {
        synchronized (truck) {
            gc.setFill(Color.BLACK);
            gc.fillOval(truck.getX() + getCanvasXCenter(), truck.getY() + getCanvasYCenter(),
                    AppSettings.TRUCK_WIDTH, AppSettings.TRUCK_HEIGHT);
            truck.notifyAll();
        }
    }

    private void drawCreature(GraphicsContext gc, Truck truck, Creature creature) {
        synchronized (truck) {
            synchronized (creature) {
                gc.setFill(creature.getColor());
                double x = Math.cos(Math.PI * creature.getAngle() / 180f) * 100 + truck.getX();
                double y = Math.sin(Math.PI * creature.getAngle() / 180f) * 100 + truck.getY();
                gc.fillOval(x + getCanvasXCenter(), y + getCanvasYCenter(),
                        AppSettings.CREATURE_WIDTH, AppSettings.CREATURE_HEIGHT);
                creature.notifyAll();
            }
            truck.notifyAll();
        }
    }

    private void drawHistory(GraphicsContext gc) {
        gc.setFill(Color.BROWN);
        for(Pair<Double, Double> coordinate : truckHistory) {
            gc.fillOval(coordinate.getKey() + getCanvasXCenter(), coordinate.getValue() + getCanvasYCenter(),
                    AppSettings.TRUCK_WIDTH, AppSettings.TRUCK_HEIGHT);
        }
    }
}
