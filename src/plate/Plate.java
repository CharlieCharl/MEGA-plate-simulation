package plate;

import bacteria.Bacteria;
import bacteria.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class Plate{
    private int width;
    private int height;
    private Field[][] fields;
    private ArrayList<Bacteria> aliveBacterias;
    private long foodLeft = 0;

    public long getFoodLeft() {
        return foodLeft;
    }

    public Plate(int width, int height) {
        this.width = width;
        this.height = height;
        this.fields = new Field[width][height];
        this.aliveBacterias = new ArrayList<>();

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                this.fields[i][j] = new Field();} }
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
        //4 area
        for (int i = 560; i < 670; i++) {
            for (int j = 0; j < this.height; j++) {
                this.fields[i][j].setAntibiotic(1.9f); } }
        //3 area
        for (int i = 670; i < 780; i++) {
            for (int j = 0; j < this.height; j++) {
                this.fields[i][j].setAntibiotic(1.9f); } }
        //2 area
        for (int i = 780; i < 890; i++) {
            for (int j = 0; j < this.height; j++) {
                this.fields[i][j].setAntibiotic(1.09f); } }
        //start area right
        for (int i = 890; i < 1000; i++) {
            for (int j = 0; j < this.height; j++) {
                this.fields[i][j].setAntibiotic(1.2f); } }
        generateFirstGenerationOfBacterias();
    }

    private void generateFirstGenerationOfBacterias() {

     for (int i = 0; i < 50; i++) {
            int y = ThreadLocalRandom.current().nextInt(350);
            int x = 0;
            Bacteria bacteria = new Bacteria(x, y);
            fields[x][y].setBacteria(bacteria);
       }
        for (int i = 0; i <  5; i++) {
           int y = ThreadLocalRandom.current().nextInt(350);
           int x = 999;
            Bacteria bacteria = new Bacteria(x, y);
            fields[x][y].setBacteria(bacteria);
        }
    }

    private boolean isFieldEmpty(int x, int y) {
        if (x < 0 || x > width - 1 || y < 0 || y > height - 1) { return false; }
        if (fields[x][y].getBacteria() == null) { return true; } else return false; }

    public void update() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (fields[i][j].getBacteria() != null) {
                    List<Direction> availableDirections = getDirections(fields[i][j]);

                    if (fields[i][j].getBacteria().canReproduce() && !availableDirections.isEmpty()) {
                        addBacteriasChild(i, j, availableDirections); }

                    if (!availableDirections.isEmpty()) {
                        move(i, j, availableDirections); }

                    if ((fields[i][j].getBacteria().getResistance() > fields[i][j].getAntibiotic())) {
                        fields[i][j].getBacteria().setAlive(true);
                    } else { fields[i][j].getBacteria().setAlive(false); }

                    //if (((fields[i][j].getBacteria().getMovesWithoutFood() >= 10000) || !(fields[i][j].getBacteria().isAlive()))) {
                    if (!(fields[i][j].getBacteria().isAlive())) {
                            fields[i][j].setBacteria(null);
                    }
                }
            }
        }
    }

    private void addBacteriasChild(int i, int j, List<Direction> directionList) {
        Bacteria tempBacteria = new Bacteria(i, j, fields[i][j].getBacteria().calculateChildsResistance());
        int direction = ThreadLocalRandom.current().nextInt(directionList.size());
        switch (directionList.get(direction)) {
            case UP:
                if (isFieldEmpty(i, j - 1))
                    fields[i][j - 1].setBacteria(tempBacteria);
                break;
            case LEFT:
                if (isFieldEmpty(i - 1, j))
                    fields[i - 1][j].setBacteria(tempBacteria);
                break;
            case RIGHT:
                if (isFieldEmpty(i + 1, j))
                    fields[i + 1][j].setBacteria(tempBacteria);
                break;
            case DOWN:
                if (isFieldEmpty(i, j + 1))
                    fields[i][j + 1].setBacteria(tempBacteria);
                break;
        }
        if (fields[i][j].getBacteria().isAlive())
            aliveBacterias.add(fields[i][j].getBacteria());
    }

    private void move(int i, int j, List<Direction> avalibleDirections) {
        int direction = ThreadLocalRandom.current().nextInt(avalibleDirections.size());
        switch (avalibleDirections.get(direction)) {
            case UP:
                fields[i][j].getBacteria().setY(fields[i][j].getBacteria().getY()-1);
                eat(i,j);
                break;
            case LEFT:
                fields[i][j].getBacteria().setX(fields[i][j].getBacteria().getX()-1);

                eat(i,j);
                break;
            case RIGHT:
                fields[i][j].getBacteria().setX(fields[i][j].getBacteria().getX() + 1);
                eat(i,j);
                break;
            case DOWN:
                fields[i][j].getBacteria().setY(fields[i][j].getBacteria().getY()+1);
                eat(i,j);
                break;
        }
    }

    private void eat(int i, int j){
        if (fields[i][j].getFood() >= 0){
            fields[i][j].getBacteria().eatFood(fields[fields[i][j].getBacteria().getX()][fields[i][j].getBacteria().getY()]);
            fields[i][j].removeFood(); } }

    private List<Direction> getDirections(Field field) {
        List<Direction> avalibleDirections = new ArrayList<>();
        if(field.getBacteria() != null){
            if (isFieldEmpty(field.getBacteria().getX(), field.getBacteria().getY() - 1)) {
                avalibleDirections.add(Direction.UP); }
            if (isFieldEmpty(field.getBacteria().getX() + 1, field.getBacteria().getY())) {
                avalibleDirections.add(Direction.RIGHT); }
            if (isFieldEmpty(field.getBacteria().getX(), field.getBacteria().getY() + 1)) {
                avalibleDirections.add(Direction.DOWN); }
            if (isFieldEmpty(field.getBacteria().getX() - 1, field.getBacteria().getY())) {
                avalibleDirections.add(Direction.LEFT); }
        }
        return avalibleDirections;
    }

    public Field[][] getFields() { return fields; }
}