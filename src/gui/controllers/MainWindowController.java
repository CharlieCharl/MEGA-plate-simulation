package gui.controllers;

import bacteria.Bacteria;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import plate.Field;
import simulation.Simulation;

import java.sql.Time;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


public class MainWindowController extends AnimationTimer {
    @FXML
    private Canvas canvas;

    @FXML
    private Button buttonStart;

    @FXML
    private Button buttonPause;

    @FXML
    private Button restartButton;

    @FXML
    private Button continueButton;

    @FXML
    private TextField bacteriasHungerTextField;

    @FXML
    private TextField foodPerFieldTextField;

    @FXML
    private TextField startingPopulationTextfield;

    @FXML
    private TextField startAreaTextField;

    @FXML
    private TextField firstZoneTextField;

    @FXML
    private TextField secondZoneTextField;

    @FXML
    private TextField thirdZoneTextField;

    @FXML
    private TextField fourthZoneTextField;

    @FXML
    private Label aliveBacteriasLabel;

    @FXML
    private Label avgResistanceLabel;

    @FXML
    private Label topResistanceLabel;

    @FXML
    private LineChart<Integer, Double> lineChart;

    private GraphicsContext gc;
    private Simulation simulation;
    private XYChart.Series series;
    private Integer test;
    private Timeline timeline;

    public void init(Simulation simulation){

        timeline = new Timeline();
        this.simulation = simulation;
        this.test = 0;

        //updateValues();
        series = new XYChart.Series();
        lineChart.getXAxis().setLabel("Time");
        lineChart.getYAxis().setLabel("Average resistance");
        lineChart.setAnimated(false);

        series.getData().add(new XYChart.Data("0", 0));

        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(5000), (ActionEvent actionEvent) -> {
                    double data = simulation.getPlate().calculateAvgResistance();
                    test += 5;
                    series.getData().add(new XYChart.Data(test.toString(), data));
                }));
        timeline.setCycleCount(Animation.INDEFINITE);

    }

    private Color pickColor(Bacteria bacteria){
        if (bacteria.getResistance() < 0.1) {
            return Color.WHITE;
        }
        if (bacteria.getResistance() < 0.25) {
            return Color.ANTIQUEWHITE;
        }
        if (bacteria.getResistance() < 0.5) {
            return Color.BURLYWOOD;
        }
        if (bacteria.getResistance() < 0.7) {
            return Color.CORAL;
        }
        if (bacteria.getResistance() < 0.85) {
            return Color.CHOCOLATE;
        }
        if (bacteria.getResistance() < 0.9) {
            return Color.GOLD;
        }
        else return Color.LIME;
    }

    private void updateValues(){
        int foodPerField = Integer.parseInt(foodPerFieldTextField.getText());
        int bacteriaHunger = Integer.parseInt(bacteriasHungerTextField.getText());
        int startBacterias = Integer.parseInt(startingPopulationTextfield.getText());

        float startAreaAntibiotic = Float.parseFloat(startAreaTextField.getText());
        float firstAreaAntibiotic = Float.parseFloat(firstZoneTextField.getText());
        float secondAreaAntibiotic = Float.parseFloat(secondZoneTextField.getText());
        float thirdAreaAntibiotic = Float.parseFloat(thirdZoneTextField.getText());
        float fourthAreaAntibiotic = Float.parseFloat(fourthZoneTextField.getText());

        simulation.getPlate().setPlateFood(foodPerField);
        simulation.getPlate().generateFirstGenerationOfBacterias(startBacterias);
        simulation.getPlate().setBacteriasHunger(bacteriaHunger);
        simulation.getPlate().setAntibioticArea(startAreaAntibiotic,1);
        simulation.getPlate().setAntibioticArea(firstAreaAntibiotic,2);
        simulation.getPlate().setAntibioticArea(secondAreaAntibiotic,3);
        simulation.getPlate().setAntibioticArea(thirdAreaAntibiotic,4);
        simulation.getPlate().setAntibioticArea(fourthAreaAntibiotic,5);
    }

    @FXML
    public void PauseButtonEventHandler(){
        simulation.stop();
        stop();
        timeline.stop();
    }

    public void continueButtonEventHandler(){
      timeline.play();
      start();
      simulation.start();
    }

    @FXML
    public void startButtonEventHandler(){
        simulation.stop();
        stop();
        updateValues();
        start();
        timeline.play();
        simulation.start();
        lineChart.getData().addAll(series);
    }

    @FXML
    public void restartButtonEventHandler(){
       timeline.stop();
       simulation.stop();
       stop();
       series.getData().clear();
       init(new Simulation());
       updateValues();
       timeline.play();
       simulation.start();
       start();
       lineChart.getData().addAll(series);
    }

    private void updateCanvas() {
        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Field[][] fields = simulation.getPlate().getFields();

        WritableImage img = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        PixelWriter pw = img.getPixelWriter();

        for (int i = 0; i < canvas.getWidth(); i++) {
            for (int j = 0; j < canvas.getHeight(); j++) {
                Field field = fields[i][j];
                Color c;
                if (field.getBacteria() != null && field.getBacteria().isAlive()) {
                    c = pickColor(field.getBacteria());
                }
                else{ c = Color.BLACK; }
                pw.setColor(i,j,c);
            }
        }
        this.gc = canvas.getGraphicsContext2D();
        this.gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        this.gc.drawImage(img, 0, 0);
        this.aliveBacteriasLabel.setText(String.valueOf(simulation.getPlate().getAliveBacterias().size()));
        this.avgResistanceLabel.setText(String.format("%.5f",simulation.getPlate().calculateAvgResistance()));
        this.topResistanceLabel.setText(String.format("%.5f",simulation.getPlate().getTopResistance()));
    }

    @Override
    public void handle(long now) {
        updateCanvas();
    }


}


