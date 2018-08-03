package gui.controllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import plate.Field;
import simulation.Simulation;

public class MainWindowController extends AnimationTimer {

    @FXML
    private Canvas canvas;
    private GraphicsContext gc;
    private Simulation simulation;
    BoxBlur bb = new BoxBlur();

    public void init(Simulation simulation) {
        this.simulation = simulation;
        bb.setWidth(5);
        bb.setHeight(5);
        bb.setIterations(3);
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
                    c = new Color(1.0,1.0,1.0,1.0 - field.getBacteria().getResistance());
                }
                else{ c = Color.BLACK;
                }
                pw.setColor(i,j,c);
            }
        }

        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setEffect(bb);
       // gc.setEffect(new GaussianBlur(20));
        gc.drawImage(img, 0, 0);
    }

    @Override
    public void handle(long now) {
        updateCanvas();
        //  System.out.println(simulation.getPlate().getAliveBacterias().size());
    }
}


