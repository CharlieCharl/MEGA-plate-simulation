package gui.controllers;

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
    private BoxBlur bb = new BoxBlur();
    private XYChart.Series series;
    private Integer test;
    private Timeline timeline;

    public void init(Simulation simulation){

        timeline = new Timeline();
        this.simulation = simulation;
        this.test = 0;

        bb.setWidth(2);
        bb.setHeight(2);
        bb.setIterations(1);

        updateValues();
        series = new XYChart.Series();
        lineChart.getXAxis().setLabel("Time");
        lineChart.getYAxis().setLabel("Average resistance");
        lineChart.setAnimated(false);

        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(2000), (ActionEvent actionEvent) -> {
                    double data = simulation.getPlate().calculateAvgResistance();
                        series.getData().add(new XYChart.Data(test.toString(), data));
                        test += 2;
                }));
        timeline.setCycleCount(Animation.INDEFINITE);
      //  timeline.setAutoReverse(true);  //!?

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
       timeline.play();
       simulation.start();
       start();
       lineChart.getData().addAll(series);
    }

    private void updateCanvas() {
        double strongest = simulation.getPlate().getTopResistance();
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

                    c = new Color(1.0,1.0,1.0,1);
                    if (field.getBacteria().getResistance() >= strongest - 0.05f)
                        c = Color.RED;
                }
                else{ c = Color.BLACK; }
                pw.setColor(i,j,c);
            }
        }

        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
      //  gc.setEffect(bb);
    //  gc.setEffect(new GaussianBlur(20));
        gc.drawImage(img, 0, 0);
        this.aliveBacteriasLabel.setText(String.valueOf(simulation.getPlate().getAliveBacterias().size()));
        avgResistanceLabel.setText(String.format("%.5f",simulation.getPlate().calculateAvgResistance()));
        topResistanceLabel.setText(String.format("%.5f",simulation.getPlate().getTopResistance()));
    }

    @Override
    public void handle(long now) {
        updateCanvas();
    }


}


