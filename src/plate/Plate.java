package plate;
import bacteria.Bacteria;
import bacteria.Direction;
import plate.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Plate {
    private List<Bacteria> aliveBacterias;
    private int width;
    private int height;
    private Field[][] fields;
    private  List<Bacteria> bacteriasToAdd;
    private List<Bacteria> deadbacterias;

    public Plate(int width, int height) {
        this.aliveBacterias = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.fields = new Field[width][height];
        this.bacteriasToAdd = new ArrayList<>();
        this.deadbacterias = new ArrayList<>();

        for (int i = 0; i < this.width; i++){
            for (int j = 0; j < this.height; j++){
                this.fields[i][j] = new Field(); } }

        setAntibioticArea(0.00f,1);
        setAntibioticArea(0.2f,2);
        setAntibioticArea(0.40f,3);
        setAntibioticArea(0.60f,4);
        setAntibioticArea(0.95f,5);
        //generateFirstGenerationOfBacterias();
    }

   public void setAntibioticArea(float antibioticValue, int area){
        switch (area){
            case 1:
                for (int i = 0; i < 110; i++) {
                    for (int j = 0; j < this.height; j++) { this.fields[i][j].setAntibiotic(antibioticValue); } }

                for (int i = 890; i < 1000; i++) {
                    for (int j = 0; j < this.height; j++) { this.fields[i][j].setAntibiotic(antibioticValue); } }

                    break;
            case 2:
                for (int i = 110; i < 220; i++) {
                    for (int j = 0; j < this.height; j++) { this.fields[i][j].setAntibiotic(antibioticValue); } }

                for (int i = 780; i < 890; i++) {
                    for (int j = 0; j < this.height; j++) { this.fields[i][j].setAntibiotic(antibioticValue); } }

                    break;
            case 3:
                for (int i = 220; i < 330; i++) {
                    for (int j = 0; j < this.height; j++) { this.fields[i][j].setAntibiotic(antibioticValue); } }

                for (int i = 670; i < 780; i++) {
                    for (int j = 0; j < this.height; j++) { this.fields[i][j].setAntibiotic(antibioticValue); } }

                    break;
            case 4:
                for (int i = 330; i < 440; i++) {
                    for (int j = 0; j < this.height; j++) { this.fields[i][j].setAntibiotic(antibioticValue); } }

                for (int i = 560; i < 670; i++) {
                    for (int j = 0; j < this.height; j++) { this.fields[i][j].setAntibiotic(antibioticValue); } }
                    break;
            case 5:
                for (int i = 440; i < 560; i++) {
                    for (int j = 0; j < this.height; j++) { this.fields[i][j].setAntibiotic(antibioticValue); } }
                    break;
        }
    }

    public void generateFirstGenerationOfBacterias(int startingBacterias) {
        for (int i = 0; i < startingBacterias; i++) {
            int y = ThreadLocalRandom.current().nextInt(350);
            int x = 0;
            Bacteria bacteria = new Bacteria(x, y);
            aliveBacterias.add(bacteria);
            fields[x][y].setBacteria(bacteria); }
        for (int i = 0; i <  startingBacterias; i++) {
            int y = ThreadLocalRandom.current().nextInt(350);
            int x = 999;
            Bacteria bacteria = new Bacteria(x, y);
            aliveBacterias.add(bacteria);
            fields[x][y].setBacteria(bacteria); }
    }

    public void setBacteriasHunger(int hunger){
        aliveBacterias.forEach(bacteria -> bacteria.setHunger(hunger));
    }

    private boolean isFieldEmpty(int x, int y){
        if(x < 0 || x > width - 1 || y < 0 || y > height -1){ return false; }
        if(fields[x][y].getBacteria() == null) { return true; }
        else return false;
    }

    public void update(){
        aliveBacterias.removeIf(x -> !x.isAlive());
        aliveBacterias.addAll(bacteriasToAdd);
        bacteriasToAdd.clear();

        aliveBacterias.forEach(bacteria -> {
            List<Direction> avalibleDirections = new ArrayList<>();
            prepareAvailableDirectionsForBacteria(bacteria, avalibleDirections);

            //reproduce
            if(bacteria.canReproduce()){
                bacteriaReproduce(bacteria, avalibleDirections);
            }//move and eat
            else { bacteriaMoveAndEat(bacteria, avalibleDirections); }

            if((fields[bacteria.getX()][bacteria.getY()].getAntibiotic() > bacteria.getResistance())){
              //  System.out.println(bacteria);
                bacteria.setAlive(false);
               // fields[bacteria.getX()][bacteria.getY()].setBacteria(null);
            }
        });
    }

    public void setPlateFood(int foodValue){
        for (int i = 0; i < this.width; i++){
            for (int j = 0; j < this.height; j++){
                this.fields[i][j] = new Field();
                this.fields[i][j].setFood(foodValue);
            }
        }
    }

    private void bacteriaMoveAndEat(Bacteria bacteria, List<Direction> avalibleDirections) {
        if (!avalibleDirections.isEmpty()) {
            int direction = new Random().nextInt(avalibleDirections.size());
            switch (avalibleDirections.get(direction)) {
                case UP:
                    bacteria.setY(bacteria.getY() - 1);
                    fields[bacteria.getX()][bacteria.getY()].setBacteria(bacteria);
                    fields[bacteria.getX()][bacteria.getY() + 1].setBacteria(null);
                    bacteria.eatFood(fields[bacteria.getX()][bacteria.getY()]);
                    break;
                case LEFT:
                    bacteria.setX(bacteria.getX() - 1);
                    fields[bacteria.getX()][bacteria.getY()].setBacteria(bacteria);
                    fields[bacteria.getX() + 1][bacteria.getY()].setBacteria(null);
                    bacteria.eatFood(fields[bacteria.getX()][bacteria.getY()]);
                    break;
                case RIGHT:
                    bacteria.setX(bacteria.getX() + 1);
                    fields[bacteria.getX()][bacteria.getY()].setBacteria(bacteria);
                    fields[bacteria.getX() - 1][bacteria.getY()].setBacteria(null);
                    bacteria.eatFood(fields[bacteria.getX()][bacteria.getY()]);
                    break;
                case DOWN:
                    bacteria.setY(bacteria.getY() + 1);
                    fields[bacteria.getX()][bacteria.getY()].setBacteria(bacteria);
                    fields[bacteria.getX()][bacteria.getY() - 1].setBacteria(null);
                    bacteria.eatFood(fields[bacteria.getX()][bacteria.getY()]);
                    break;
            }
        }
    }

    private void bacteriaReproduce(Bacteria bacteria, List<Direction> avalibleDirections) {
        if (!avalibleDirections.isEmpty()) {
            int direction = new Random().nextInt(avalibleDirections.size());
            Bacteria tempBacteria = null;
            switch (avalibleDirections.get(direction)) {
                case UP:
                    tempBacteria = new Bacteria(bacteria);
                    tempBacteria.setY(bacteria.getY() - 1);
                    break;
                case LEFT:
                    tempBacteria = new Bacteria(bacteria);
                    tempBacteria.setX(bacteria.getX() - 1);
                    break;
                case RIGHT:
                    tempBacteria = new Bacteria(bacteria);
                    tempBacteria.setX(bacteria.getX() + 1);
                    break;
                case DOWN:
                    tempBacteria = new Bacteria(bacteria);
                    tempBacteria.setY(bacteria.getY() + 1);
                    break;
            }
            fields[tempBacteria.getX()][tempBacteria.getY()].setBacteria(tempBacteria);
            bacteriasToAdd.add(tempBacteria);
        }
    }

    private void prepareAvailableDirectionsForBacteria(Bacteria bacteria, List<Direction> avalibleDirections) {
        if(isFieldEmpty(bacteria.getX(), bacteria.getY() - 1)){
            avalibleDirections.add(Direction.UP); }
        if(isFieldEmpty(bacteria.getX() + 1, bacteria.getY())) {
            avalibleDirections.add(Direction.RIGHT); }
        if(isFieldEmpty(bacteria.getX(), bacteria.getY() + 1)) {
            avalibleDirections.add(Direction.DOWN); }
        if(isFieldEmpty(bacteria.getX() - 1, bacteria.getY())) {
            avalibleDirections.add(Direction.LEFT); }
    }

    public Field[][] getFields() {
        return fields;
    }

    public List<Bacteria> getAliveBacterias() {
        return aliveBacterias;
    }
}