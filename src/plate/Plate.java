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
    private List<Float> amtibiotics;
    private  List<Bacteria> bacteriasToAdd;


    public Plate(int width, int height) {
        this.aliveBacterias = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.fields = new Field[width][height];
        this.amtibiotics = amtibiotics;
        this.bacteriasToAdd = new ArrayList<>();


        for (int i = 0; i < this.width; i++){
            for (int j = 0; j < this.height; j++){
                this.fields[i][j] = new Field();
            }
        }
        for (int i = 0; i < 1000; i++){
            int x = 0;
            int y = ThreadLocalRandom.current().nextInt(height);
            Bacteria bacteria = new Bacteria(x, y);
            fields[x][y].setBacteria(bacteria);
            aliveBacterias.add(bacteria);
        }
        // 2 area
        for (int i = 100; i < 220; i++) {
            for (int j = 0; j < this.height; j++) {
                this.fields[i][j].setAntibiotic(0.3f); } }
        // 3 area
        for (int i = 220; i < 330; i++) {
            for (int j = 0; j < this.height; j++) {
                this.fields[i][j].setAntibiotic(0.45f); } }
        //4 area
        for (int i = 330; i < 440; i++) {
            for (int j = 0; j < this.height; j++) {
                this.fields[i][j].setAntibiotic(0.6f); } }
        //5 area
        for (int i = 440; i < 560; i++) {
            for (int j = 0; j < this.height; j++) { this.fields[i][j].setAntibiotic(0.9f); } }
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
            else {
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
            if(fields[bacteria.getX()][bacteria.getY()].getAntibiotic() > bacteria.getResistance()){
                bacteria.setAlive(false);
            }
        });
    }

    private void bacteriaReproduce(Bacteria bacteria, List<Direction> avalibleDirections) {
        if (!avalibleDirections.isEmpty()) {
            int direction = new Random().nextInt(avalibleDirections.size());
            Bacteria tempBacteria = null;
            switch (avalibleDirections.get(direction)) {
                case UP:
                    tempBacteria = new Bacteria(bacteria.getX(), bacteria.getY() - 1);
                    break;
                case LEFT:
                    tempBacteria = new Bacteria(bacteria.getX() - 1, bacteria.getY());
                    break;
                case RIGHT:
                    tempBacteria = new Bacteria(bacteria.getX() + 1, bacteria.getY());
                    break;
                case DOWN:
                    tempBacteria = new Bacteria(bacteria.getX(), bacteria.getY() + 1);
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