package simulation;

import plate.Plate;

public class Simulation extends Thread {
    private Plate plate;
    private boolean running = true;
    private boolean paused = false;

    public Simulation() {
        plate = new Plate(1000,350);
    }

//    @Override
//    public void handle(long now) {
//        plate.update();
//    }

    @Override
    public void run() {
        while (running){
            plate.update();
        }
    }

    public Plate getPlate() {
        return plate;
    }


}
