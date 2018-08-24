package plate;
import bacteria.Bacteria;
import bacteria.Direction;
import utilities.Util;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Plate {

    private List<Bacteria> aliveBacterias;
    private int width;
    private int height;
    private Field[][] fields;
    private List<Bacteria> bacteriasToAdd;
    private int fixFood;
    private Random random;
    private int fixReproduceWithoutFood;

    public Plate(int width, int height) {
        this.random = new Random();
        this.aliveBacterias = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.fields = new Field[width][height];
        this.bacteriasToAdd = new ArrayList<>();

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                this.fields[i][j] = new Field();
            }
        }

        setAntibioticArea(0.00f, 1);
        setAntibioticArea(0.2f, 2);
        setAntibioticArea(0.40f, 3);
        setAntibioticArea(0.60f, 4);
        setAntibioticArea(0.95f, 5);
    }

    public void setAntibioticArea(float antibioticValue, int area) {
        switch (area) {
            case 1:
                for (int i = 0; i <= 110; i++) {
                    for (int j = 0; j < this.height; j++) {
                        this.fields[i][j].setAntibiotic(antibioticValue);
                    }
                }

                for (int i = 890; i < 1000; i++) {
                    for (int j = 0; j < this.height; j++) {
                        this.fields[i][j].setAntibiotic(antibioticValue);
                    }
                }
                break;
            case 2:
                for (int i = 111; i <= 220; i++) {
                    for (int j = 0; j < this.height; j++) {
                        this.fields[i][j].setAntibiotic(antibioticValue);
                    }
                }

                for (int i = 781; i <= 890; i++) {
                    for (int j = 0; j < this.height; j++) {
                        this.fields[i][j].setAntibiotic(antibioticValue);
                    }
                }

                break;
            case 3:
                for (int i = 221; i <= 330; i++) {
                    for (int j = 0; j < this.height; j++) {
                        this.fields[i][j].setAntibiotic(antibioticValue);
                    }
                }

                for (int i = 671; i <= 780; i++) {
                    for (int j = 0; j < this.height; j++) {
                        this.fields[i][j].setAntibiotic(antibioticValue);
                    }
                }

                break;
            case 4:
                for (int i = 331; i <= 440; i++) {
                    for (int j = 0; j < this.height; j++) {
                        this.fields[i][j].setAntibiotic(antibioticValue);
                    }
                }

                for (int i = 561; i <= 670; i++) {
                    for (int j = 0; j < this.height; j++) {
                        this.fields[i][j].setAntibiotic(antibioticValue);
                    }
                }
                break;
            case 5:
                for (int i = 441; i <= 560; i++) {
                    for (int j = 0; j < this.height; j++) {
                        this.fields[i][j].setAntibiotic(antibioticValue);
                    }
                }
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
        this.fixFood = hunger;
    }

    public void setBacteriasToAddHunger(int hunger){
        bacteriasToAdd.forEach(bacteria -> bacteria.setHunger(hunger));
    }

    public void setBacteriasToAddReproduceWithoutHunger(int fixReproduceWithoutFood){
        bacteriasToAdd.forEach(bacteria -> bacteria.setStepsWithoutFoodToReproduce(fixReproduceWithoutFood));
    }

    public void setReproduceWithoutFood(int reproduceWithoutFood) {
        aliveBacterias.forEach(bacteria -> bacteria.setStepsWithoutFoodToReproduce(reproduceWithoutFood));
        this.fixReproduceWithoutFood = reproduceWithoutFood;
    }

    private boolean isFieldEmpty(int x, int y){
        if(x < 0 || x > width - 1 || y < 0 || y > height -1){ return false; }
        if(fields[x][y].getBacteria() == null) { return true; }
        else return false;
    }

    private void tryToKill(){
        Random random = new Random();
        aliveBacterias.forEach(bacteria -> {
            double deathProbability = random.nextDouble();
            if(deathProbability < 0.000000001f){
                bacteria.setAlive(false);
            }
        });
    }

    public void update(){
        tryToKill();
        aliveBacterias.removeIf(x -> !x.isAlive());
        aliveBacterias.addAll(bacteriasToAdd);
        bacteriasToAdd.clear();

        aliveBacterias.forEach(bacteria -> {
            List<Direction> avalibleDirections = new ArrayList<>();
            prepareAvailableDirectionsForBacteria(bacteria, avalibleDirections);
            //System.out.println(bacteria);

            if(bacteria.canReproduce()){
                bacteriaReproduce(bacteria, avalibleDirections);
            }
            else { bacteriaMoveAndEat(bacteria, avalibleDirections); }

            if((fields[bacteria.getX()][bacteria.getY()].getAntibiotic() > bacteria.getResistance())) {
                bacteria.setAlive(false);
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

    private void bacteriaReproduce(Bacteria bacteria, List<Direction> availableDirections) {
        if (!availableDirections.isEmpty()) {
           int direction = new Random().nextInt(availableDirections.size());

            double resistance = calculateChildResistance(bacteria);
            Bacteria tempBacteria = new Bacteria(bacteria,resistance);
            System.out.println("Parent: " + bacteria.getResistance() + " Child: " + resistance );
            switch (availableDirections.get(direction)) {
                case UP:
                    tempBacteria.setY(bacteria.getY() - 1);
                    break;
                case LEFT:
                    tempBacteria.setX(bacteria.getX() - 1);
                    break;
                case RIGHT:
                    tempBacteria.setX(bacteria.getX() + 1);
                    break;
                case DOWN:
                    tempBacteria.setY(bacteria.getY() + 1);
                    break;
            }
            fields[tempBacteria.getX()][tempBacteria.getY()].setBacteria(tempBacteria);
            bacteriasToAdd.add(tempBacteria);
            setBacteriasToAddHunger(fixFood);
            setBacteriasToAddReproduceWithoutHunger(fixReproduceWithoutFood);
        }
    }

    private double calculateChildResistance(Bacteria bacteria){
       double resistance = 0;
       double[] resistanceTable = new double[aliveBacterias.size()];

        for (int i = 0; i < resistanceTable.length; i++) {
            resistanceTable[i] = aliveBacterias.get(i).getResistance();
        }

       do{
           resistance = random.nextGaussian() * (Util.populationStandardDeviation(resistanceTable)/2) + bacteria.getResistance();
       } while (resistance > 1.0d || resistance < 0.0d);

        return resistance;
    }

    private void prepareAvailableDirectionsForBacteria(Bacteria bacteria, List<Direction> availableDirections) {
        if(isFieldEmpty(bacteria.getX(), bacteria.getY() - 1)){
            availableDirections.add(Direction.UP); }
        if(isFieldEmpty(bacteria.getX() + 1, bacteria.getY())) {
            availableDirections.add(Direction.RIGHT); }
        if(isFieldEmpty(bacteria.getX(), bacteria.getY() + 1)) {
            availableDirections.add(Direction.DOWN); }
        if(isFieldEmpty(bacteria.getX() - 1, bacteria.getY())) {
            availableDirections.add(Direction.LEFT); }
    }

    public Field[][] getFields() {
        return fields;
    }

    public double calculateAvgResistance(){
       double avgResistance = 0;
        for(Bacteria b : aliveBacterias){
            avgResistance += b.getResistance();
        }
        avgResistance = avgResistance / aliveBacterias.size();
        return avgResistance;
    }

    public double getTopResistance(){
        double topResistance = 0;
        if(!aliveBacterias.isEmpty()){
            Bacteria bacteria =  Collections.max(aliveBacterias, Comparator.comparing(s -> s.getResistance()));
            topResistance = bacteria.getResistance();
        }
            return topResistance;
    }

    public List<Bacteria> getAliveBacterias() {
        return aliveBacterias;
    }
}