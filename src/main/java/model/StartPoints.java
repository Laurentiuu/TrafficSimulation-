package model;

public class StartPoints {
    private Road road;
    private double time;

    public StartPoints(Road road, double time) {
        this.road = road;
        this.time = time;
    }

    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
