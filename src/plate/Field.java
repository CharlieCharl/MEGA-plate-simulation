package plate;

import bacteria.Bacteria;
import bacteria.Direction;

public class Field {

    private Bacteria bacteria;
    private float antibiotic;
    private int food;

    public Field() {
        this.bacteria = null;
        this.food = 100;
        this.antibiotic = 0.0f;
    }

    public void setBacteria(Bacteria bacteria) {
        this.bacteria = bacteria;
        if((this.bacteria != null)){
            if(this.getAntibiotic()> this.bacteria.getResistance()){
                this.bacteria.setAlive(false);
            }else this.bacteria.setAlive(true);
        }
    }

    public void setBacteria(Bacteria bacteria, Direction direction) { this.bacteria = bacteria; }

    public Bacteria getBacteria() { return bacteria; }

    public int getFood() { return this.food; }

    public void removeFood() {
        if(this.food > 0)
            this.food--;
        else this.food = 0; }

    public double getAntibiotic() { return antibiotic; }

    public void setAntibiotic(float antibiotic) { this.antibiotic = antibiotic; }

    public void setFood(int food) { this.food = food; }

    @Override
    public String toString() { return "antibiotic: " + this.antibiotic + ", bacteria: " + this.bacteria + ", food: " +this.food; }
}

