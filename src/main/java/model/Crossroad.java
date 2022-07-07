package model;

import view.Traffic;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Crossroad {

    private final int x;
    private final int y;
    private final int WIDTH = 30;
    private final int HEIGHT = 30;

    HashMap<Character, Light> lights;
    HashMap<Road, Integer> roads = new HashMap<>();


    private double time = 100;
    private int mils = 0;
    private int t = 0;

    private final String name;
    private final String lightPosition;

    public Crossroad(String name, int x, int y, String lightPosition, HashMap<Character, Light> lights) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.lightPosition = lightPosition;
        this.lights = lights;
        idle();
    }

    private void idle() {
        for (Light light : lights.values()) {
            light.setColor(Color.red);
        }
    }

    public void run() {
        if (mils == time) {
            changeLight();
            mils = 0;
        } else {
            mils++;
        }
    }

    private void changeLight() {
        if (lights.size() == 4) {
            if (t == 0) {
                time = 100 * lights.get('U').getTime();
                lights.get('U').setColor(Color.green);
                lights.get('D').setColor(Color.red);
                lights.get('L').setColor(Color.red);
                lights.get('R').setColor(Color.red);
                t++;
            } else if (t == 1) {
                time = 100 * lights.get('R').getTime();
                lights.get('R').setColor(Color.green);
                lights.get('D').setColor(Color.red);
                lights.get('L').setColor(Color.red);
                lights.get('U').setColor(Color.red);
                t++;
            } else if (t == 2) {
                time = 100 * lights.get('D').getTime();
                lights.get('D').setColor(Color.green);
                lights.get('U').setColor(Color.red);
                lights.get('L').setColor(Color.red);
                lights.get('R').setColor(Color.red);
                t++;
            } else if (t == 3) {
                time = 100 * lights.get('L').getTime();
                lights.get('L').setColor(Color.green);
                lights.get('D').setColor(Color.red);
                lights.get('U').setColor(Color.red);
                lights.get('R').setColor(Color.red);
                t = 0;
            }
        } else if (lights.size() == 3) {
            if (!lights.containsKey('L')) {
                if (t == 0) {
                    time = 100 * lights.get('U').getTime();
                    lights.get('U').setColor(Color.green);
                    lights.get('R').setColor(Color.red);
                    lights.get('D').setColor(Color.red);
                    t++;
                } else if (t == 1) {
                    time = 100 * lights.get('R').getTime();
                    lights.get('R').setColor(Color.green);
                    lights.get('U').setColor(Color.red);
                    lights.get('D').setColor(Color.red);
                    t++;
                } else if (t == 2) {
                    time = 100 * lights.get('D').getTime();
                    lights.get('D').setColor(Color.green);
                    lights.get('U').setColor(Color.red);
                    lights.get('R').setColor(Color.red);
                    t = 0;
                }
            }
            if (!lights.containsKey('D')) {
                if (t == 0) {
                    time = 100 * lights.get('U').getTime();
                    lights.get('U').setColor(Color.green);
                    lights.get('R').setColor(Color.red);
                    lights.get('L').setColor(Color.red);
                    t++;
                } else if (t == 1) {
                    time = 100 * lights.get('R').getTime();
                    lights.get('R').setColor(Color.green);
                    lights.get('U').setColor(Color.red);
                    lights.get('L').setColor(Color.red);
                    t++;
                } else if (t == 2) {
                    time = 100 * lights.get('L').getTime();
                    lights.get('L').setColor(Color.green);
                    lights.get('U').setColor(Color.red);
                    lights.get('R').setColor(Color.red);
                    t = 0;
                }
            }
            if (!lights.containsKey('R')) {
                if (t == 0) {
                    time = 100 * lights.get('U').getTime();
                    lights.get('U').setColor(Color.green);
                    lights.get('L').setColor(Color.red);
                    lights.get('D').setColor(Color.red);
                    t++;
                } else if (t == 1) {
                    time = 100 * lights.get('D').getTime();
                    lights.get('D').setColor(Color.green);
                    lights.get('L').setColor(Color.red);
                    lights.get('U').setColor(Color.red);
                    t++;
                } else if (t == 2) {
                    time = 100 * lights.get('L').getTime();
                    lights.get('L').setColor(Color.green);
                    lights.get('U').setColor(Color.red);
                    lights.get('D').setColor(Color.red);
                    t = 0;
                }
            }
            if (!lights.containsKey('U')) {
                if (t == 0) {
                    time = 100 * lights.get('R').getTime();
                    lights.get('R').setColor(Color.green);
                    lights.get('D').setColor(Color.red);
                    lights.get('L').setColor(Color.red);
                    t++;
                } else if (t == 1) {
                    time = 100 * lights.get('D').getTime();
                    lights.get('D').setColor(Color.green);
                    lights.get('R').setColor(Color.red);
                    lights.get('L').setColor(Color.red);
                    t++;
                } else if (t == 2) {
                    time = 100 * lights.get('L').getTime();
                    lights.get('L').setColor(Color.green);
                    lights.get('D').setColor(Color.red);
                    lights.get('R').setColor(Color.red);
                    t = 0;
                }
            }
        }
    }


    public ArrayList<Road> getRoadsAsArray() {
        return new ArrayList<>(roads.keySet());
    }

    public HashMap<Road, Integer> getRoads() {
        return roads;
    }

    //inOut = 0 - intra in intersectie
    //inOut = 1 - iese din intersectie
    public void addRoad(Road r, int inOut) {
        roads.put(r, inOut);
    }

    public void paintMe(Graphics g) {
        //desenez intersectia
        g.setColor(Color.white);
        g.fillRect(x, y, WIDTH, HEIGHT);

        g.setColor(Color.darkGray);
        g.fillRect(x + 2, y + 2, WIDTH - 4, HEIGHT - 4);

        if (Traffic.showNames){
            g.setColor(Color.white);
            g.drawString(name,x,y);
        }

        g.setColor(Color.darkGray);

        //desenez semafoarele
        if (lights.size() > 2) {
            if (lightPosition.contains("U")) {
                g.setColor(lights.get('U').getColor());
                g.fillRect(x, y - 3, 16, 3);
            }
            if (lightPosition.contains("R")) {
                g.setColor(lights.get('R').getColor());
                g.fillRect(x + WIDTH, y, 3, 16);
            }
            if (lightPosition.contains("D")) {
                g.setColor(lights.get('D').getColor());
                g.fillRect(x + WIDTH - 16, y + HEIGHT, 16, 3);
            }
            if (lightPosition.contains("L")) {
                g.setColor(lights.get('L').getColor());
                g.fillRect(x - 3, y + WIDTH - 16, 3, 16);
            }
        }
    }

    public String getLightPosition() {
        return lightPosition;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public HashMap<Character, Light> getLights() {
        return lights;
    }
}
