package simulation;

import javafx.animation.AnimationTimer;
import plate.Plate;

public class Simulation extends AnimationTimer {
    private Plate plate;

    public Simulation() {
        plate = new Plate(1000,350);
    }

    @Override
    public void handle(long now) {
        plate.update();
    }

    public Plate getPlate() { return plate; }
}
