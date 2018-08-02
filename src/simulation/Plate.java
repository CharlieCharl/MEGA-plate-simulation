package simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
                this.fields[i][j] = new Field();
                this.fields[i][j].setAntibiotic(0.1f);
            }
        }

        // 2 area
        for (int i = 100; i < 220; i++) {
            for (int j = 0; j < this.height; j++) {
                this.fields[i][j].setAntibiotic(0.3f);
            }
        }

        // 3 area
        for (int i = 220; i < 330; i++) {
            for (int j = 0; j < this.height; j++) {
                this.fields[i][j].setAntibiotic(0.45f);
            }
        }

        //4 area
        for (int i = 330; i < 440; i++) {
            for (int j = 0; j < this.height; j++) {
                this.fields[i][j].setAntibiotic(0.6f);
            }
        }

        //5 area
        for (int i = 440; i < 560; i++) {
            for (int j = 0; j < this.height; j++) {
                this.fields[i][j].setAntibiotic(0.9f);
            }
        }

        //4 area
        for (int i = 560; i < 670; i++) {
            for (int j = 0; j < this.height; j++) {
                this.fields[i][j].setAntibiotic(1.9f);
            }
        }

        //3 area
        for (int i = 670; i < 780; i++) {
            for (int j = 0; j < this.height; j++) {
                this.fields[i][j].setAntibiotic(1.9f);
            }
        }

        //2 area
        for (int i = 780; i < 890; i++) {
            for (int j = 0; j < this.height; j++) {
                this.fields[i][j].setAntibiotic(1.09f);
            }

        }
        for (int i = 890; i < 1000; i++) {
            for (int j = 0; j < this.height; j++) {
                this.fields[i][j].setAntibiotic(1.2f);
            }
        }
        System.out.println(foodLeft);
        generateFirstGenerationOfBacterias();
    }

    public void checkFieldAntibiotic(int i, int j){

        System.out.println("field [" + i + "]" +"[" + j + "]" + " antibiotic: " + this.fields[i][j].getAntibiotic());

    }

    private void generateFirstGenerationOfBacterias() {

     for (int i = 0; i < 50; i++) {
//            int x = ThreadLocalRandom.current().nextInt(0, 50);
            int y = ThreadLocalRandom.current().nextInt(350);
            int x = 0;
           // int y = i;
            Bacteria bacteria = new Bacteria(x, y);
            fields[x][y].setBacteria(bacteria);
       }

        for (int i = 0; i <  5; i++) {
          // int x = ThreadLocalRandom.current().nextInt(900, 1000);
           int y = ThreadLocalRandom.current().nextInt(350);
           int x = 999;
          //  int y = i;
            Bacteria bacteria = new Bacteria(x, y);
            fields[x][y].setBacteria(bacteria);
          //  System.out.println(fields[x][y].getBacteria().isAlive());
         //   aliveBacterias.add(fields[x][y].getBacteria());

        }
    }

    private boolean isFieldEmpty(int x, int y) {
        if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
            return false;
        }
        if (fields[x][y].getBacteria() == null) {
            return true;
        } else return false;
    }

    public void update() {
        calculateFoodLeft();
        // System.out.println(aliveBacterias.size());

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (fields[i][j].getBacteria() != null) {

//                    if (fields[i][j].getBacteria().getMovesWithoutFood() > 9) {
//                        System.out.println(fields[i][j].getBacteria());
//                    }

                    List<Direction> availableDirections = getDirections(fields[i][j]);

                    if (fields[i][j].getBacteria().canReproduce() && !availableDirections.isEmpty()) {
                        addBacteriasChild(i, j, availableDirections);
                    }

                    if (!availableDirections.isEmpty()) {
                        move(i, j, availableDirections);
                    }

                    if ((fields[i][j].getBacteria().getResistance() > fields[i][j].getAntibiotic())) {
                        fields[i][j].getBacteria().setAlive(true);
                    } else {
                        fields[i][j].getBacteria().setAlive(false);
                    }

                    //if (((fields[i][j].getBacteria().getMovesWithoutFood() >= 10000) || !(fields[i][j].getBacteria().isAlive()))) {
                    if (!(fields[i][j].getBacteria().isAlive())) {

                            fields[i][j].setBacteria(null);
                    }

//                    if(!(fields[i][j].getBacteria().isAlive())){
//                        fields[i][j].setBacteria(null);
//                    }
                    //  System.out.println(fields[i][j] + " " + i + " " + j);
                }
            }
        }
        //  System.out.println(foodLeft);
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

//    private void move(int i, int j, List<Direction> avalibleDirections) {
//
//       int direction = ThreadLocalRandom.current().nextInt(avalibleDirections.size());
//        System.out.println(avalibleDirections);
//        switch (avalibleDirections.get(direction)) {
//           case UP:
//               fields[i][j].getBacteria().setY(j-1);
//              // fields[i][j].getBacteria().eatFood(fields[fields[i][j].getBacteria().getX()][fields[i][j].getBacteria().getY()]);
//           //    fields[i][j].getBacteria().eatFood(fields[i][j]);
//               eat(i,j);
//               break;
//            case LEFT:
//                fields[i][j].getBacteria().setX(i-1);
//               // fields[i][j].getBacteria().eatFood(fields[i][j]);
//             //  fields[i][j].getBacteria().eatFood(fields[i][j]);
//                eat(i,j);
//                break;
//            case RIGHT:
//                fields[i][j].getBacteria().setX(i+1);
//                //fields[i][j].getBacteria().eatFood(fields[fields[i][j].getBacteria().getX()][fields[i][j].getBacteria().getY()]);
//           //     fields[i][j].getBacteria().eatFood(fields[i][j]);
//                eat(i,j);
//                break;
//           case DOWN:
//               fields[i][j].getBacteria().setY(j+1);
//               //fields[i][j].getBacteria().eatFood(fields[fields[i][j].getBacteria().getX()][fields[i][j].getBacteria().getY()]);
//             //  fields[i][j].getBacteria().eatFood(fields[i][j]);
//               eat(i,j);
//               break;
//        }
////        for (Bacteria b: aliveBacterias)
////        { System.out.println(b);
////        }
//    }

    public void calculateFoodLeft(){
        this.foodLeft = 0;
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                this.foodLeft += this.fields[i][j].getFood();
            }
        }
    }
    private void move(int i, int j, List<Direction> avalibleDirections) {

        int direction = ThreadLocalRandom.current().nextInt(avalibleDirections.size());
       // System.out.println(avalibleDirections);
        switch (avalibleDirections.get(direction)) {
            case UP:
                fields[i][j].getBacteria().setY(fields[i][j].getBacteria().getY()-1);
                // fields[i][j].getBacteria().eatFood(fields[fields[i][j].getBacteria().getX()][fields[i][j].getBacteria().getY()]);
                //    fields[i][j].getBacteria().eatFood(fields[i][j]);
                eat(i,j);
                break;
            case LEFT:
                fields[i][j].getBacteria().setX(fields[i][j].getBacteria().getX()-1);
                // fields[i][j].getBacteria().eatFood(fields[i][j]);
                //  fields[i][j].getBacteria().eatFood(fields[i][j]);
                eat(i,j);
                break;
            case RIGHT:
                fields[i][j].getBacteria().setX(fields[i][j].getBacteria().getX() + 1);
                //fields[i][j].getBacteria().eatFood(fields[fields[i][j].getBacteria().getX()][fields[i][j].getBacteria().getY()]);
                //     fields[i][j].getBacteria().eatFood(fields[i][j]);
                eat(i,j);
                break;
            case DOWN:
                fields[i][j].getBacteria().setY(fields[i][j].getBacteria().getY()+1);
                //fields[i][j].getBacteria().eatFood(fields[fields[i][j].getBacteria().getX()][fields[i][j].getBacteria().getY()]);
                //  fields[i][j].getBacteria().eatFood(fields[i][j]);
                eat(i,j);
                break;
        }
//        for (Bacteria b: aliveBacterias)
//        { System.out.println(b);
//        }
    }

    private void eat(int i, int j){
        if (fields[i][j].getFood() >= 0){
            fields[i][j].getBacteria().eatFood(fields[fields[i][j].getBacteria().getX()][fields[i][j].getBacteria().getY()]);
          //  fields[i][j].setFood(fields[i][j].getFood()-1);
            fields[i][j].removeFood();
        }
    }


//    private void eat(int i, int j){
//        if (fields[i][j].getFood() > 0){
//              fields[i][j].getBacteria().eatFood(fields[i][j]);
//              if(foodLeft > 0)
//              foodLeft--;
//              else foodLeft = 0;
//        }
//    }

    private List<Direction> getDirections(Field field) {
        List<Direction> avalibleDirections = new ArrayList<>();

        if(field.getBacteria() != null){
            if (isFieldEmpty(field.getBacteria().getX(), field.getBacteria().getY() - 1)) {
                avalibleDirections.add(Direction.UP);
            }
            if (isFieldEmpty(field.getBacteria().getX() + 1, field.getBacteria().getY())) {
                avalibleDirections.add(Direction.RIGHT);
            }
            if (isFieldEmpty(field.getBacteria().getX(), field.getBacteria().getY() + 1)) {
                avalibleDirections.add(Direction.DOWN);
            }
            if (isFieldEmpty(field.getBacteria().getX() - 1, field.getBacteria().getY())) {
                avalibleDirections.add(Direction.LEFT);
            }
        }

        return avalibleDirections;
    }

    public Field[][] getFields() {
        return fields;
    }

}