package gui.controllers;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.paint.Color;
import simulation.Field;
import simulation.Simulation;

public class MainWindowController extends AnimationTimer {

    @FXML
    private Canvas canvas;
    private GraphicsContext gc;
    private int alive;
    private int dead;
    private Simulation simulation;

    //  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
//    private Runnable refreshCanvas = this::updateCanvas;


    public void init(Simulation simulation) {
        this.simulation = simulation;
//        gc.setFill(Color.BLACK);
//        gc.fillRect(0,0, 110,350);

//        gc.setFill(Color.OLIVE);
//        gc.fillRect(110,0, 110,350);

//        gc.setFill(Color.ORANGE);
//        gc.fillRect(220,0, 110,350);

//        gc.setFill(Color.BLUE);
//        gc.fillRect(330,0, 110,350);

//        gc.setFill(Color.GREEN);
//        gc.fillRect(440,0, 120,350);
//        gc.setFill(Color.BLUE);
//        gc.fillRect(560,0, 110,350);
//        gc.setFill(Color.ORANGE);
//        gc.fillRect(670,0, 110,350);
//        gc.setFill(Color.OLIVE);
//        gc.fillRect(780,0, 110,350);
//        gc.setFill(Color.BLACK);
//        gc.fillRect(890,0, 110,350);
    }

    private void updateCanvas() {
        alive = 0;
        dead = 0;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Field[][] fields = simulation.getPlate().getFields();

        for (int i = 0; i < canvas.getWidth(); i++) {
            for (int j = 0; j < canvas.getHeight(); j++) {
                Field field = fields[i][j];
                if (field.getBacteria() != null) {
                    if (field.getBacteria().isAlive()) {
                        // field.getBacteria().draw(gc);
                        field = fields[i][j];
                       // float alpha = field.getFood();
                        gc.setFill(Color.rgb(255, 255, 255, 1));
                        gc.fillRect(i, j, 1, 1);
                    }
                    //alive++;
                }
            } //else dead++;
        }
    }


    @Override
    public void handle(long now) {
        updateCanvas();
//        System.out.println("Alive " + alive);
//        System.out.println("Dead " +dead);
//        System.out.println(food);
    }
}



//    Field[][] fields = simulation.getPlate().getFields();
//        for (int i = 0; i < canvas.getWidth(); i++){
//        for (int j = 0; j < canvas.getHeight(); j++){
//        Field field = fields[i][j];
//        float alpha = field.getFood();
//        gc.setFill(Color.rgb(1,1,1, alpha));
//        gc.fillRect(i,j,1,1);
//        }

