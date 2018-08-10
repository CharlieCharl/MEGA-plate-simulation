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
        //this.resistance = 0.01f;
        this.resistance = ThreadLocalRandom.current().nextDouble(0.01,0.09);
    //   this.resistance = ThreadLocalRandom.current().nextGaussian() + 0.01f;
//        while (this.resistance < 0 || this.resistance > 1.0){
//        this.resistance = ThreadLocalRandom.current().nextGaussian() + 0.5f;}
        this.hunger = 35;
    }

    public Bacteria(Bacteria parent,double resistance){
        this.x = parent.getX();
        this.y = parent.getY();
        //this.resistance = parent.getResistance() + (ThreadLocalRandom.current().nextDouble(-0.03f,0.03f) );
        this.resistance = resistance;
       // while (this.resistance < 0 || this.resistance > 1.0){
        //    this.resistance = parent.getResistance() + (ThreadLocalRandom.current().nextDouble(-0.03f,0.03f) );}
        this.hunger = 35;
    }

    public double getResistance() {
        return resistance;
    }

    public boolean canReproduce(){
        if(this.hunger == 0){
            this.hunger = 35;
            return true;
        }
        return false;
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

    public void setHunger(int hunger){
        this.hunger = hunger;
    }

    @Override
    public String toString() {
        return "x: " + x + ", y: " + y + " " +  this.resistance + "  resistance, " + this.hunger + " hunger " + this.movesWithoutFood + " moves without food"; }

    public int getHunger() {
        return hunger;
    }
}



