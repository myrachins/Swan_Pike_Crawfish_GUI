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

import static settings.AppSettings.MARGIN_FROM_TRUCK;

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
                        double xShift = canvas.getWidth() / 2 - truck.getX();
                        double yShift = canvas.getHeight() / 2 + truck.getY();
                        truckHistory.add(new Pair<>(truck.getX(), truck.getY())); // adding new element to history
                        drawHistory(gc, xShift, yShift);
                        drawTruck(gc, truck, xShift, yShift);
                        creatures.forEach((creature) -> drawCreature(gc, truck, creature, xShift, yShift));
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

    private void drawBorder() {
        synchronized (canvas) {
            GraphicsContext gc = canvas.getGraphicsContext2D() ;
            gc.setStroke(Color.GREY);
            gc.setLineWidth(4);
            gc.strokeRoundRect(0, 0, canvas.getWidth(), canvas.getHeight(), 0, 0);
        }
    }

    private void drawTruck(GraphicsContext gc, Truck truck, double xShift, double yShift) {
        synchronized (truck) {
            gc.setFill(Color.BLACK);
            gc.fillOval(truck.getX() + xShift, -truck.getY() + yShift,
                    AppSettings.TRUCK_WIDTH, AppSettings.TRUCK_HEIGHT);
            truck.notifyAll();
        }
    }

    private void drawCreature(GraphicsContext gc, Truck truck, Creature creature, double xShift, double yShift) {
        synchronized (truck) {
            synchronized (creature) {
                gc.setFill(creature.getColor());
                double x = Math.cos(Math.PI * creature.getAngle() / 180f) * MARGIN_FROM_TRUCK + truck.getX();
                double y = -Math.sin(Math.PI * creature.getAngle() / 180f) * MARGIN_FROM_TRUCK  - truck.getY();
                gc.fillOval(x + xShift, y + yShift,
                        AppSettings.CREATURE_WIDTH, AppSettings.CREATURE_HEIGHT);
                creature.notifyAll();
            }
            truck.notifyAll();
        }
    }

    private void drawHistory(GraphicsContext gc, double xShift, double yShift) {
        gc.setFill(Color.BROWN);
        if (truckHistory.size() == 0) {
            return;
        }
        double xPrev = truckHistory.get(0).getKey() + xShift;
        double yPrev = -truckHistory.get(0).getValue() + yShift;
        for(Pair<Double, Double> coordinate : truckHistory) {
            double x = coordinate.getKey() + xShift;
            double y = -coordinate.getValue() + yShift;
            gc.strokeLine(xPrev, yPrev, x, y);
            gc.fillOval(x, y, AppSettings.TRUCK_WIDTH, AppSettings.TRUCK_HEIGHT);
            xPrev = x;
            yPrev = y;
        }
    }
}
