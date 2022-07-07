package model;

import java.awt.*;

public class Light {

    private Color color;
    private double time;
    private char position;

    public Light(char position, double time) {
        this.time = time;
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getTime() {
        return time;
    }
}
