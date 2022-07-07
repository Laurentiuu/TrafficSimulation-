package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Vehicle {

    public enum Directions {
        FORWARD,
        LEFT,
        RIGHT,
        END
    }

    protected float x;
    protected float y;
    protected int width = 0;
    protected int height = 0;
    protected float speed = 0;
    //contor de siimulare
    //timpi de start si timp de stop
    protected int time = 0;
    private Road road;
    private int distance = 0;
    private int simulationStep = 0;

    private Directions direction;

    public Vehicle(Road road, int newX, int newY) {
        this.x = newX + 5;
        this.y = newY + 5;
        this.road = road;

        this.direction = generateRandomDirection(this.road);
    }


    public static Directions generateRandomDirection(Road road) {

        try {
            ArrayList<Directions> aux = new ArrayList<>();
            String lightPositions = road.getCrossroad().getLightPosition();

            if (lightPositions.length() == 4) {
                aux.add(Directions.FORWARD);
                aux.add(Directions.LEFT);
                aux.add(Directions.RIGHT);
            } else if (lightPositions.length() == 3) {
                if (road.getType() == Road.Type.UD) {
                    if (lightPositions.contains("D") && lightPositions.contains("L")) {
                        aux.add(Directions.FORWARD);
                        aux.add(Directions.RIGHT);
                    }
                    if (lightPositions.contains("D") && lightPositions.contains("R")) {
                        aux.add(Directions.FORWARD);
                        aux.add(Directions.LEFT);
                    }
                    if (lightPositions.contains("L") && lightPositions.contains("R")) {
                        aux.add(Directions.RIGHT);
                        aux.add(Directions.LEFT);
                    }
                }
                if (road.getType() == Road.Type.RL) {
                    if (lightPositions.contains("U") && lightPositions.contains("D")) {
                        aux.add(Directions.LEFT);
                        aux.add(Directions.RIGHT);
                    }
                    if (lightPositions.contains("U") && lightPositions.contains("L")) {
                        aux.add(Directions.FORWARD);
                        aux.add(Directions.RIGHT);
                    }
                    if (lightPositions.contains("L") && lightPositions.contains("D")) {
                        aux.add(Directions.FORWARD);
                        aux.add(Directions.LEFT);
                    }
                } else if (road.getType() == Road.Type.DU) {
                    if (lightPositions.contains("U") && lightPositions.contains("L")) {
                        aux.add(Directions.LEFT);
                        aux.add(Directions.FORWARD);
                    }
                    if (lightPositions.contains("U") && lightPositions.contains("R")) {
                        aux.add(Directions.FORWARD);
                        aux.add(Directions.RIGHT);
                    }
                    if (lightPositions.contains("L") && lightPositions.contains("R")) {
                        aux.add(Directions.RIGHT);
                        aux.add(Directions.LEFT);
                    }
                } else if (road.getType() == Road.Type.LR) {
                    if (lightPositions.contains("U") && lightPositions.contains("D")) {
                        aux.add(Directions.LEFT);
                        aux.add(Directions.RIGHT);
                    }
                    if (lightPositions.contains("U") && lightPositions.contains("R")) {
                        aux.add(Directions.FORWARD);
                        aux.add(Directions.LEFT);
                    }
                    if (lightPositions.contains("D") && lightPositions.contains("R")) {
                        aux.add(Directions.RIGHT);
                        aux.add(Directions.FORWARD);
                    }
                }
            } else {
                if (road.getType() == Road.Type.UD) {
                    if (lightPositions.contains("R")) {
                        return Directions.LEFT;
                    }
                    if (lightPositions.contains("D")) {
                        return Directions.FORWARD;
                    }
                    if (lightPositions.contains("L")) {
                        return Directions.RIGHT;
                    }
                }
                if (road.getType() == Road.Type.RL) {
                    if (lightPositions.contains("U")) {
                        return Directions.RIGHT;
                    }
                    if (lightPositions.contains("D")) {
                        return Directions.LEFT;
                    }
                    if (lightPositions.contains("L")) {
                        return Directions.FORWARD;
                    }
                } else if (road.getType() == Road.Type.DU) {
                    if (lightPositions.contains("U")) {
                        return Directions.FORWARD;
                    }
                    if (lightPositions.contains("R")) {
                        return Directions.RIGHT;
                    }
                    if (lightPositions.contains("L")) {
                        return Directions.LEFT;
                    }
                } else if (road.getType() == Road.Type.LR) {
                    if (lightPositions.contains("U")) {
                        return Directions.LEFT;
                    }
                    if (lightPositions.contains("R")) {
                        return Directions.FORWARD;
                    }
                    if (lightPositions.contains("D")) {
                        return Directions.RIGHT;
                    }
                }
            }
            if (!road.isEndPoint()) {
                int rnd = new Random().nextInt(aux.size());
                return aux.get(rnd);
            }
        }catch (NullPointerException e){
            return Directions.END;
        }
        return Directions.END;
    }

    public void paintMe(Graphics g) {
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getSpeed() {
        return speed;
    }


    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
    }

    public Directions getDirection() {
        return direction;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance += distance;
    }


    public int getTime() {
        return time;
    }

    public void incrementTime() {
        this.time++;
    }

    public int getSimulationStep() {
        return simulationStep;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void incrementSimulationStep() {
        this.simulationStep++;
    }
}
