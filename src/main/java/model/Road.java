package model;


import view.Traffic;

import java.awt.*;

public class Road {

    public enum Type {
        RL,
        LR,
        UD,
        DU
    }

    private final String name;
    private final int distance;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    private int startX;
    private int startY;

    private final Type type;
    private final boolean isStartPoint;
    private boolean isEndPoint;
    private final Crossroad crossroad;


    public Road(String name, int distance, int x, int y, int width, int height, Type t, Crossroad crossroad, boolean isStartPoint) {
        this.name = name;
        this.distance = distance;
        this.x = x;
        this.y = y;
        this.crossroad = crossroad;
        this.width = width;
        this.height = height;
        this.type = t;
        this.isStartPoint = isStartPoint;

        if (t == Type.LR) {
            this.startX = x;
            this.startY = y;
        } else if (t == Type.DU) {
            this.startX = x;
            this.startY = y + height;
        } else if (t == Type.UD) {
            this.startX = x;
            this.startY = y;
        } else if (t == Type.RL) {
            this.startX = x + width - 40;
            this.startY = y;
        }

        this.isEndPoint = crossroad == null;

    }

    public void paintMe(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(x, y, width, height);

        g.setColor(Color.black);
        g.fillRect(x + 2, y + 2, width - 4, height - 4);
        g.setColor(Color.white);
        g.setFont(new Font("Helvetica", Font.PLAIN, 9));

        String s = name;
        int v = g.getFontMetrics(new Font("Helvetica", Font.PLAIN, 10)).getHeight() - 5;

        if(Traffic.showNames) {
            if (type == Type.RL) {
                if (isEndPoint) {
                    g.drawString(name, x + width - 38 - 100, y + 12);
                } else {
                    g.drawString(name, crossroad.getX() + 12 + 100, y + 12);
                }

            }
            if (type == Type.LR) {
                if (isEndPoint) {
                    g.drawString(name, x + 12 + 100, y + 12);
                } else {
                    g.drawString(name, crossroad.getX() + 12 - 100, y + 12);
                }

            }
            if (type == Type.DU) {
                int j = 0;
                int k = s.length();
                if (isEndPoint) {
                    while (j < k + 1) {
                        int y = this.y + height - 38 - 100 + (j * v);
                        if (j == k)
                            g.drawString(s.substring(j), x + 5, y);
                        else
                            g.drawString(s.substring(j, j + 1), x + 5, y);
                        j++;
                    }
                } else {
                    while (j < k + 1) {
                        if (j == k)
                            g.drawString(s.substring(j), x + 5, crossroad.getY() + 12 + 100 + (j * v));
                        else
                            g.drawString(s.substring(j, j + 1), x + 5, crossroad.getY() + 12 + 100 + (j * v));
                        j++;
                    }
                }
            }

            if (type == Type.UD) {
                int j = 0;
                int k = s.length();
                if (isEndPoint) {
                    while (j < k + 1) {
                        if (j == k)
                            g.drawString(s.substring(j), x + 5, y + 12 + 100 + (j * v));
                        else
                            g.drawString(s.substring(j, j + 1), x + 5, y + 12 + 100 + (j * v));
                        j++;
                    }
                } else {
                    while (j < k + 1) {
                        if (j == k)
                            g.drawString(s.substring(j), x + 5, crossroad.getY() + 12 - 100 + (j * v));
                        else
                            g.drawString(s.substring(j, j + 1), x + 5, crossroad.getY() + 12 - 100 + (j * v));
                        j++;
                    }
                }
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Type getType() {
        return type;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setEndPoint(boolean endPoint) {
        isEndPoint = endPoint;
    }

    public boolean isEndPoint() {
        return isEndPoint;
    }

    public Crossroad getCrossroad() {
        return crossroad;
    }

    public String getName() {
        return name;
    }

    public int getDistance() {
        return distance;
    }
}
