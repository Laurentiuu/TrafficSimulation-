package model;

import view.Traffic;

import java.awt.*;

public class Car extends Vehicle {

    public Car(Road r, int newX, int newY) {
        super(r, newX, newY);
        width = 5;
        height = 5;
        speed = 1.4f;
    }

    public void paintMe(Graphics g) {
        g.setColor(Color.yellow);
        g.fillRect((int) x, (int) y, width, height);
        if (Traffic.showNames) {
            g.setColor(Color.red);
            g.setFont(new Font("Helvetica", Font.PLAIN, 9));
            if (this.getDirection().equals(Directions.FORWARD)) {
                g.drawString("F", (int) x, (int) y);
            }
            if (this.getDirection().equals(Directions.RIGHT)) {
                g.drawString("R", (int) x, (int) y);
            }
            if (this.getDirection().equals(Directions.LEFT)) {
                g.drawString("L", (int) x, (int) y);
            }
            if (this.getDirection().equals(Directions.END)) {
                g.drawString("X", (int) x, (int) y);
            }
        }
    }


}
