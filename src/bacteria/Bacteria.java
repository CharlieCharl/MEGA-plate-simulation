package bacteria;


import plate.Field;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Bacteria{

    private int x;
    private int y;
    private double resistance;
    private int hunger;
    private boolean alive;
    private int movesWithoutFood = 0;
    private int stepsWithoutFoodToReproduce;
    private int initialhunger;
    private Random random;

    public Bacteria(int x, int y) {
        this.x = x;
        this.y = y;
        this.resistance = ThreadLocalRandom.current().nextDouble(0.01,0.09);
        this.hunger = 35;
        random = new Random();
    }

    public Bacteria(Bacteria parent,double resistance){
        this.x = parent.getX();
        this.y = parent.getY();
        this.resistance = resistance;
        this.hunger = 35;
        this.movesWithoutFood = 0;
        random = new Random();
    }

    public double getResistance() {
        return resistance;
    }

    public boolean canReproduce(){
        double tryToReproduceChance = random.nextDouble();
        if((this.hunger > 0 && this.movesWithoutFood >= stepsWithoutFoodToReproduce) && tryToReproduceChance < 0.00001f){
            this.movesWithoutFood = 0;
            return true;
        }

        if(this.hunger == 0){
            this.hunger = initialhunger;
            return true;
        }

        return false;
    }



    public void eatFood(Field field){
        if(field.getFood() > 0){
            this.hunger--;
            field.removeFood();
            this.movesWithoutFood = 0;
        } else this.movesWithoutFood++;
    }

    public int getMovesWithoutFood() {
        return movesWithoutFood;
    }

    public int getX() { return x; }

    public void setX(int x) { this.x = x; }

    public int getY() { return y; }

    public void setY(int y) { this.y = y; }

    public boolean isAlive() { return alive; }

    public void setAlive(boolean alive) { this.alive = alive; }

    public void setHunger(int hunger){
        this.hunger = hunger;
        this.initialhunger = hunger;
    }

    @Override
    public String toString() {
        return "x: " + x + ", y: " + y + " " +  this.resistance + "  resistance, " + this.hunger + " hunger " + this.movesWithoutFood + " moves without food"; }

    public int getHunger() {
        return hunger;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }

    public void setMovesWithoutFood(int movesWithoutFood) {
        this.movesWithoutFood = movesWithoutFood;
    }

    public int getStepsWithoutFoodToReproduce() {
        return stepsWithoutFoodToReproduce;
    }

    public void setStepsWithoutFoodToReproduce(int stepsWithoutFoodToReproduce) {
        this.stepsWithoutFoodToReproduce = stepsWithoutFoodToReproduce;
    }

}



