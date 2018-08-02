package bacteria;

import plate.Field;

import java.util.concurrent.ThreadLocalRandom;

public class Bacteria{

    private int x;
    private int y;
    private double resistance;
    private int hunger;
    private boolean alive;
    private int movesWithoutFood = 0;

    public Bacteria(int x, int y) {
        this.x = x;
        this.y = y;
        this.resistance = ThreadLocalRandom.current().nextDouble() /2 ;
        this.hunger = 1;
    }

    public double getResistance() {
        return resistance;
    }

    public Bacteria(int x, int y, double resistance){
        this.x = x;
        this.y = y;
        this.resistance = calculateChildsResistance();
        this.hunger = 1;
    }

    public boolean canReproduce(){
        if(this.hunger == 0){
            this.hunger = 1;
            return true;
        }
        return false;
    }

    public double calculateChildsResistance(){
        return ThreadLocalRandom.current().nextDouble() /2 + 0.02f;
    }

    public void eatFood(Field field){
        if(field.getFood() > 0){
            this.hunger--;
            field.removeFood();
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

    @Override
    public String toString() {
        return "x: " + x + ", y: " + y + " " +  this.resistance + "  resistance, " + this.hunger + " hunger " + this.movesWithoutFood + " moves without food"; }

}



