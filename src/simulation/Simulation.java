package simulation;

import javafx.animation.AnimationTimer;
import plate.Plate;

public class Simulation extends AnimationTimer {
    private Plate plate;
    private boolean running = true;
    private boolean paused = false;

    public Simulation() {
        plate = new Plate(1000,350);
    }

    @Override
    public void handle(long now) {
        stop();
        plate.update();
        start();
    }

//    @Override
//    public void run() {
//        while (running){
//            plate.update();
//        }
//    }

    public Plate getPlate() {
        return plate;
    }


}
