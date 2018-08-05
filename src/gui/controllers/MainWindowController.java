package gui.controllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import plate.Field;
import simulation.Simulation;


public class MainWindowController extends AnimationTimer {

    private int alive;

    @FXML
    private Canvas canvas;

    @FXML
    private Button buttonStart;

    @FXML
    private Button buttonPause;

    @FXML
    private Button restartButton;

    @FXML
    private TextField bacteriasHungerTextField;

    @FXML
    private TextField foodPerFieldTextField;

    @FXML
    private TextField startingPopulationTextfield;

    @FXML
    private TextField startAreaTextField;;

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



    private GraphicsContext gc;
    private Simulation simulation;
    BoxBlur bb = new BoxBlur();

    public void init(Simulation simulation) {
        this.simulation = simulation;
        bb.setWidth(2);
        bb.setHeight(2);
        bb.setIterations(1);

        updateValues();
    }

    public void updateValues(){
        int foodPerField = Integer.parseInt(foodPerFieldTextField.getText());
        int bacteriasHunegr = Integer.parseInt(bacteriasHungerTextField.getText());
        int startBacterias = Integer.parseInt(startingPopulationTextfield.getText());


        float startAreaAntibiotic = Float.parseFloat(startAreaTextField.getText());
        float firstAreaAntibiotic = Float.parseFloat(firstZoneTextField.getText());
        float secondAreaAntibiotic = Float.parseFloat(secondZoneTextField.getText());
        float thirdAreaAntibiotic = Float.parseFloat(thirdZoneTextField.getText());
        float fourthAreaAntibiotic = Float.parseFloat(fourthZoneTextField.getText());


        simulation.getPlate().setPlateFood(foodPerField);
        simulation.getPlate().generateFirstGenerationOfBacterias(startBacterias);
        simulation.getPlate().setBacteriasHunger(bacteriasHunegr);
        simulation.getPlate().setAntibioticArea(startAreaAntibiotic,1);
        simulation.getPlate().setAntibioticArea(firstAreaAntibiotic,2);
        simulation.getPlate().setAntibioticArea(secondAreaAntibiotic,3);
        simulation.getPlate().setAntibioticArea(thirdAreaAntibiotic,4);
        simulation.getPlate().setAntibioticArea(fourthAreaAntibiotic,5);

    }

    @FXML
    public void startButtonEventHandler(){
        simulation.stop();
        stop();
        updateValues();
        start();
        simulation.start();
    }

    @FXML
    public void PauseButtonEventHandler(){
        simulation.stop();
        stop();
    }

    @FXML
    public void restartButtonEventHandler(){
       simulation.stop();
       stop();
       init(new Simulation());
       simulation.start();
       start();
    }

    private void updateCanvas() {
        alive = 0;
        double strongest = simulation.getPlate().getTopResistance();
        double opacity;
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
                    double resistance = field.getBacteria().getResistance();

                    if(resistance <= 0.11f ){
                        opacity = 1.0f;
                      //  System.out.println(opacity);
                    }
                    else if (resistance >= 0.12f && resistance <= 0.29f){
                        opacity = 1.0f - resistance / 2;
                    //    System.out.println(opacity);
                    }
                    else if (resistance >= 0.30f && resistance <= 0.39f){
                        opacity = 1.0f - resistance / 1.5;
                        //    System.out.println(opacity);
                    }
                    else if (resistance >= 0.40f && resistance <= 0.5f ){
                        opacity = 1.0f - resistance / 1.3;
                      //  System.out.println(opacity);
                    }
                    else if (resistance >= 0.51f && resistance <= 0.60f ){
                        opacity = 1.0f - resistance / 1.2;
                        //  System.out.println(opacity);
                    }
                    else if (resistance >= 0.61f && resistance <= 0.7f){
                        opacity = 1.0f - resistance / 1.1 ;
                        //  System.out.println(opacity);
                    }
                    else if (resistance >= 0.71f && resistance <= 0.89f){
                        opacity = 1.0f - resistance;
                        //  System.out.println(opacity);
                    }
                    else opacity = 0.2f;
                    c = new Color(1.0,1.0,1.0,opacity);

                    if (field.getBacteria().getResistance() >= strongest - 0.03)
                        c = Color.RED;
                }
                else{ c = Color.BLACK; }
                pw.setColor(i,j,c);
            }
        }

        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
     //   gc.setEffect(bb);
      //gc.setEffect(new GaussianBlur(20));
        gc.drawImage(img, 0, 0);
        this.aliveBacteriasLabel.setText(String.valueOf(simulation.getPlate().getAliveBacterias().size()));
        avgResistanceLabel.setText(String.format("%.5f",simulation.getPlate().calculateAvgResistance()));
        topResistanceLabel.setText(String.format("%.5f",simulation.getPlate().getTopResistance()));
    }

    @Override
    public void handle(long now) {
        updateCanvas();
        //  System.out.println(simulation.getPlate().getAliveBacterias().size());
    }
}


