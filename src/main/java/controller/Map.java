package controller;

import model.Crossroad;
import model.Road;
import model.Vehicle;
import view.Traffic;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Map extends JPanel {

    private final ArrayList<Vehicle> cars = new ArrayList<>();
    private final ArrayList<Road> roads = new ArrayList<>();
    private final ArrayList<Crossroad> crossroads = new ArrayList<>();

    public Map() {
        super();
    }

    public void addCar(Vehicle v) {
        cars.add(v);
    }

    public void addRoad(Road r) {
        roads.add(r);
    }

    public void addCrossroad(Crossroad crossroad) {
        crossroads.add(crossroad);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (Traffic.idle) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("TimesRoman", Font.BOLD, 70));
            g.drawString("TrafficSimulator", 380, getHeight() / 2 - 250);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 17));
            g.drawString("by Galis Laurentiu", 30, getHeight() - 30);
        }

        g.setColor(Color.GRAY);
        //se deseneaza drumurile(conteaza ordinea)
        for (Road road : roads) {
            road.paintMe(g);
        }

        //se deseneaza intersectiile
        for (Crossroad crossroad : crossroads) {
            crossroad.paintMe(g);
        }

        //se deseneaza masinile
        for (Vehicle vehicle : cars) {
            vehicle.paintMe(g);
        }
    }

    //metoda ce face masinile sa se miste
    public void step() {

        for (int i = 0; i < cars.size(); i++) {
            Vehicle v = cars.get(i);
            if (Traffic.mils % 100 == 0) {
                v.incrementTime();
            }
            v.incrementSimulationStep();
            //masinile merg de jos in sus
            if (v.getRoad().getType() == Road.Type.DU) {
                if (carCollision(v.getX(), v.getY() - v.getSpeed(), v) &&
                        lightCollision(v.getX() - v.getSpeed(), v.getY(), v)) {
                    if (!v.getDirection().equals(Vehicle.Directions.END)) {
                        // se afla in intersectie
                        if (v.getY() < v.getRoad().getCrossroad().getY() + 30) {
                            Road r = getNexRoad(v);
                            if (isOnNextRoad(v, r)) {
                                changeRoad(v, r);
                            } else {
                                v.setY(v.getY() - v.getSpeed());
                            }
                        } else {
                            v.setY(v.getY() - v.getSpeed());
                        }
                    } else {
                        if (v.getY() < v.getRoad().getY() + 10) {
                            WriteToFile.cars.add(v);
                            cars.remove(v);
                        } else {
                            v.setY(v.getY() - v.getSpeed());
                        }
                    }
                }
            }

            //masinile merg din sus in jos
            if (v.getRoad().getType() == Road.Type.UD) {
                if (carCollision(v.getX(), v.getY() + v.getSpeed(), v) &&
                        lightCollision(v.getX() + v.getSpeed(), v.getY(), v)) {
                    if (!v.getDirection().equals(Vehicle.Directions.END)) {
                        // se afla in intersectie
                        if (v.getY() > v.getRoad().getCrossroad().getY()) {
                            Road r = getNexRoad(v);
                            if (isOnNextRoad(v, r)) {
                                changeRoad(v, r);
                            } else {
                                v.setY(v.getY() + v.getSpeed());
                            }
                        } else {
                            v.setY(v.getY() + v.getSpeed());
                        }
                    } else {
                        if (v.getY() > v.getRoad().getY() + v.getRoad().getHeight() - 10) {
                            WriteToFile.cars.add(v);
                            cars.remove(v);
                        } else {
                            v.setY(v.getY() + v.getSpeed());
                        }
                    }
                }
            }

            //masinile merg de la stanga la dreapta
            if (v.getRoad().getType() == Road.Type.LR) {
                if (carCollision(v.getX() + v.getSpeed(), v.getY(), v) &&
                        lightCollision(v.getX() + v.getSpeed(), v.getY(), v)) {
                    if (!v.getDirection().equals(Vehicle.Directions.END)) {
                        // se afla in intersectie
                        if (v.getX() > v.getRoad().getCrossroad().getX()) {
                            Road r = getNexRoad(v);
                            if (isOnNextRoad(v, r)) {
                                changeRoad(v, r);
                            } else {
                                v.setX(v.getX() + v.getSpeed());
                            }
                        } else {
                            v.setX(v.getX() + v.getSpeed());
                        }
                    } else {
                        if (v.getX() > v.getRoad().getX() + v.getRoad().getWidth() - 10) {
                            WriteToFile.cars.add(v);
                            cars.remove(v);
                        } else {
                            v.setX(v.getX() + v.getSpeed());
                        }
                    }
                }
            }

            //masinile merg de la dreapta la stanga
            if (v.getRoad().getType() == Road.Type.RL) {
                if (carCollision(v.getX() - v.getSpeed(), v.getY(), v) &&
                        lightCollision(v.getX() - v.getSpeed(), v.getY(), v)) {
                    if (!v.getDirection().equals(Vehicle.Directions.END)) {
                        // se afla in intersectie
                        if (v.getX() < v.getRoad().getCrossroad().getX() + 30) {
                            Road r = getNexRoad(v);
                            if (isOnNextRoad(v, r)) {
                                changeRoad(v, r);
                            } else {
                                v.setX(v.getX() - v.getSpeed());
                            }
                        } else {
                            v.setX(v.getX() - v.getSpeed());
                        }
                    } else {
                        if (v.getX() < v.getRoad().getX() + 10) {
                            WriteToFile.cars.add(v);
                            cars.remove(v);
                        } else {
                            v.setX(v.getX() - v.getSpeed());
                        }
                    }
                }
            }

        }
    }

    private Road getNexRoad(Vehicle v) {
        if (v.getRoad().getType() == Road.Type.LR) {
            Vehicle.Directions dir = v.getDirection();
            if (dir.equals(Vehicle.Directions.RIGHT)) {
                for (Crossroad c : crossroads) {
                    if (c.equals(v.getRoad().getCrossroad())) {
                        for (int i = 0; i < c.getRoadsAsArray().size(); i++) {
                            Road r = c.getRoadsAsArray().get(i);
                            if (r.getType() == Road.Type.UD &&
                                    !(r.equals(v.getRoad()))) {
                                if (c.getRoads().get(r) == 1) {
                                    return c.getRoadsAsArray().get(i);
                                }
                            }
                        }
                    }
                }
            }

            if (dir.equals(Vehicle.Directions.FORWARD)) {
                for (Crossroad c : crossroads) {
                    if (c.equals(v.getRoad().getCrossroad())) {
                        for (int i = 0; i < c.getRoadsAsArray().size(); i++) {
                            Road r = c.getRoadsAsArray().get(i);
                            if (r.getType() == Road.Type.LR &&
                                    !(r.equals(v.getRoad()))) {
                                if (c.getRoads().get(r) == 1) {
                                    return c.getRoadsAsArray().get(i);
                                }
                            }
                        }
                    }
                }
            }


            //aici lucrez a foru cu crossroad
            //o sa folosesc v.getroad.getCrossroad sa fie mai usor
            if (dir.equals(Vehicle.Directions.LEFT)) {
                for (Crossroad c : crossroads) {
                    if (c.equals(v.getRoad().getCrossroad())) {
                        for (int i = 0; i < c.getRoadsAsArray().size(); i++) {
                            Road r = c.getRoadsAsArray().get(i);
                            if (r.getType() == Road.Type.DU &&
                                    !(r.equals(v.getRoad()))) {
                                if (c.getRoads().get(r) == 1) {
                                    return c.getRoadsAsArray().get(i);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (v.getRoad().getType() == Road.Type.UD) {
            Vehicle.Directions dir = v.getDirection();
            if (dir.equals(Vehicle.Directions.RIGHT)) {
                for (Crossroad c : crossroads) {
                    if (c.equals(v.getRoad().getCrossroad())) {
                        for (int i = 0; i < c.getRoadsAsArray().size(); i++) {
                            Road r = c.getRoadsAsArray().get(i);
                            if (r.getType() == Road.Type.RL &&
                                    !(r.equals(v.getRoad()))) {
                                if (c.getRoads().get(r) == 1) {
                                    return c.getRoadsAsArray().get(i);
                                }
                            }
                        }
                    }
                }
            }

            if (dir.equals(Vehicle.Directions.FORWARD)) {
                for (Crossroad c : crossroads) {
                    if (c.equals(v.getRoad().getCrossroad())) {
                        for (int i = 0; i < c.getRoadsAsArray().size(); i++) {
                            Road r = c.getRoadsAsArray().get(i);
                            if (r.getType() == Road.Type.UD &&
                                    !(r.equals(v.getRoad()))) {
                                if (c.getRoads().get(r) == 1) {
                                    return c.getRoadsAsArray().get(i);
                                }
                            }
                        }
                    }
                }
            }

            if (dir.equals(Vehicle.Directions.LEFT)) {
                for (Crossroad c : crossroads) {
                    if (c.equals(v.getRoad().getCrossroad())) {
                        for (int i = 0; i < c.getRoadsAsArray().size(); i++) {
                            Road r = c.getRoadsAsArray().get(i);
                            if (r.getType() == Road.Type.LR &&
                                    !(r.equals(v.getRoad()))) {
                                if (c.getRoads().get(r) == 1) {
                                    return c.getRoadsAsArray().get(i);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (v.getRoad().getType() == Road.Type.RL) {
            Vehicle.Directions dir = v.getDirection();

            if (dir.equals(Vehicle.Directions.RIGHT)) {
                for (Crossroad c : crossroads) {
                    if (c.equals(v.getRoad().getCrossroad())) {
                        for (int i = 0; i < c.getRoadsAsArray().size(); i++) {
                            Road r = c.getRoadsAsArray().get(i);
                            if (r.getType() == Road.Type.DU &&
                                    !(r.equals(v.getRoad()))) {
                                if (c.getRoads().get(r) == 1) {
                                    return c.getRoadsAsArray().get(i);
                                }
                            }
                        }
                    }
                }
            }


            if (dir.equals(Vehicle.Directions.FORWARD)) {
                for (Crossroad c : crossroads) {
                    if (c.equals(v.getRoad().getCrossroad())) {
                        for (int i = 0; i < c.getRoadsAsArray().size(); i++) {
                            Road r = c.getRoadsAsArray().get(i);
                            if (r.getType() == Road.Type.RL &&
                                    !(r.equals(v.getRoad()))) {
                                if (c.getRoads().get(r) == 1) {
                                    return c.getRoadsAsArray().get(i);
                                }
                            }
                        }
                    }
                }
            }

            if (dir.equals(Vehicle.Directions.LEFT)) {
                for (Crossroad c : crossroads) {
                    if (c.equals(v.getRoad().getCrossroad())) {
                        for (int i = 0; i < c.getRoadsAsArray().size(); i++) {
                            Road r = c.getRoadsAsArray().get(i);
                            if (r.getType() == Road.Type.UD &&
                                    !(r.equals(v.getRoad()))) {
                                if (c.getRoads().get(r) == 1) {
                                    return c.getRoadsAsArray().get(i);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (v.getRoad().getType() == Road.Type.DU) {
            Vehicle.Directions dir = v.getDirection();
            if (dir.equals(Vehicle.Directions.RIGHT)) {
                for (Crossroad c : crossroads) {
                    if (c.equals(v.getRoad().getCrossroad())) {
                        for (int i = 0; i < c.getRoadsAsArray().size(); i++) {
                            Road r = c.getRoadsAsArray().get(i);
                            if (r.getType() == Road.Type.LR &&
                                    !(r.equals(v.getRoad()))) {
                                if (c.getRoads().get(r) == 1) {
                                    return c.getRoadsAsArray().get(i);
                                }
                            }
                        }
                    }
                }
            }

            if (dir.equals(Vehicle.Directions.FORWARD)) {
                for (Crossroad c : crossroads) {
                    if (c.equals(v.getRoad().getCrossroad())) {
                        for (int i = 0; i < c.getRoadsAsArray().size(); i++) {
                            Road r = c.getRoadsAsArray().get(i);
                            if (r.getType() == Road.Type.DU &&
                                    !(r.equals(v.getRoad()))) {
                                if (c.getRoads().get(r) == 1) {
                                    return c.getRoadsAsArray().get(i);
                                }
                            }
                        }
                    }
                }
            }

            if (dir.equals(Vehicle.Directions.LEFT)) {
                for (Crossroad c : crossroads) {
                    if (c.equals(v.getRoad().getCrossroad())) {
                        for (int i = 0; i < c.getRoadsAsArray().size(); i++) {
                            Road r = c.getRoadsAsArray().get(i);
                            if (r.getType() == Road.Type.RL &&
                                    !(r.equals(v.getRoad()))) {
                                if (c.getRoads().get(r) == 1) {
                                    return c.getRoadsAsArray().get(i);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean isOnNextRoad(Vehicle v, Road r) {
        try {
            if (r.getType() == Road.Type.LR) {
                return (int) v.getY() == r.getY() + 4 || (int) v.getY() == r.getY() + 5;
            }
            if (r.getType() == Road.Type.RL) {
                return (int) v.getY() == r.getY() + 5 || (int) v.getY() == r.getY() + 6;
            }
            if (r.getType() == Road.Type.UD) {
                return (int) v.getX() == r.getX() + 5 || (int) v.getX() == r.getX() + 6;
            }
            if (r.getType() == Road.Type.DU) {
                return (int) v.getX() == r.getX() + 5 || (int) v.getX() == r.getX() + 6;
            }
            return false;
        } catch (NullPointerException e) {
            cars.remove(v);
            System.out.println("O puscat");
        }
        return false;
    }

    //metoda ce modifica directia masinii si elimina directia din lista de directi
    private void changeRoad(Vehicle v, Road r) {
        v.setRoad(r);
        v.setDirection(Vehicle.generateRandomDirection(r));
        v.setDistance(v.getRoad().getDistance());
    }


    //verifica daca se ajunge la semafor
    private boolean lightCollision(float x, float y, Vehicle v) {
        try {
            if (v.getRoad().getCrossroad().getLightPosition().length() != 2) {
                if (v.getRoad().getType() == Road.Type.UD) {
                    if ((int) y == v.getRoad().getCrossroad().getY() - 11 || (int) y == v.getRoad().getCrossroad().getY() - 12) {
                        return v.getRoad().getCrossroad().getLights().get('U').getColor() != Color.red;
                    }
                }

                if (v.getRoad().getType() == Road.Type.RL) {
                    if ((int) x == v.getRoad().getCrossroad().getX() + 37 || (int) x == v.getRoad().getCrossroad().getX() + 38) {
                        return v.getRoad().getCrossroad().getLights().get('R').getColor() != Color.red;
                    }
                }

                if (v.getRoad().getType() == Road.Type.DU) {
                    if ((int) y == v.getRoad().getCrossroad().getY() + 37 || (int) y == v.getRoad().getCrossroad().getY() + 38) {
                        return v.getRoad().getCrossroad().getLights().get('D').getColor() != Color.red;
                    }
                }

                if (v.getRoad().getType() == Road.Type.LR) {
                    if ((int) x == v.getRoad().getCrossroad().getX() - 11 || (int) x == v.getRoad().getCrossroad().getX() - 12) {
                        return v.getRoad().getCrossroad().getLights().get('L').getColor() != Color.red;
                    }
                }
            }
        } catch (NullPointerException e) {
            return true;
        }
        return true;
    }

    //verifica daca se afla o masina in fata alteia
    private boolean carCollision(float x, float y, Vehicle v) {
        //folosesc iterator sa rezolv problma exceptiei ConcurrentModificationException
        for (Iterator<Vehicle> iterator = cars.iterator(); iterator.hasNext(); ) {
            try {
                Vehicle vehicle = iterator.next();
                if (v.getRoad() == vehicle.getRoad()) {
                    if ((int) y == (int) vehicle.getY() || (int) y == (int) vehicle.getY() - 1 || (int) y == (int) vehicle.getY() + 1) {
                        if (!vehicle.equals(v)) {   //trebuie sa verific alte masini, nu masina curenta
                            //x = partea stanga a masini
                            //x + width = partea dreapta a masini
                            //u = celelalte masini
                            if (((int)x < (int)vehicle.getX() + vehicle.getWidth() - 1 && (int)x + v.getWidth() + 1 > (int)vehicle.getX()) ||
                                    ((int)x < (int)vehicle.getX() + vehicle.getWidth() + 1 && (int)x + v.getWidth() - 1 > (int)vehicle.getX())) {
                                Traffic.running = false;
                                Traffic.ok = false;
                                JOptionPane.showMessageDialog(null,
                                        "BLOCK!!! \n" + "(on road:  " + v.getRoad().getName() + ")",
                                        "BLOCK", JOptionPane.ERROR_MESSAGE);
                            }
                            if (((int)x < (int)vehicle.getX() + vehicle.getWidth() - 4 && (int)x + v.getWidth() + 4 > (int)vehicle.getX()) ||
                                    ((int)x < (int)vehicle.getX() + vehicle.getWidth() + 4 && (int)x + v.getWidth() - 4 > (int)vehicle.getX())) {
                                return false;
                            }
                        }
                    }
                    if ((int) x == (int) vehicle.getX() || (int) x == (int) vehicle.getX() - 1 || (int) x == (int) vehicle.getX() + 1) {
                        if (!vehicle.equals(v)) {   //trebuie sa verifisc alte masini, nu masina curenta
                            //y = partea de sus a masini
                            //x + height = partea de jos a masini
                            //u = celelalte masini

                            if (((int)y < (int)vehicle.getY() + vehicle.getHeight() - 1 && (int)y + v.getHeight() + 1 > (int)vehicle.getY()) ||
                                    ((int)y < (int)vehicle.getY() + vehicle.getHeight() + 1 && (int)y + v.getHeight() - 1 > (int)vehicle.getY())) {
                                Traffic.running = false;
                                Traffic.ok = false;
                                JOptionPane.showMessageDialog(null,
                                        "BLOCK!!! \n" + "(on road:  " + v.getRoad().getName() + ")",
                                        "BLOCK", JOptionPane.ERROR_MESSAGE);
                            }
                            if (((int)y < (int)vehicle.getY() + vehicle.getHeight() - 4 && (int)y + v.getHeight() + 4 >(int) vehicle.getY()) ||
                                    ((int)y < (int)vehicle.getY() + vehicle.getHeight() + 4 && (int)y + v.getHeight() - 4 > (int)vehicle.getY())) {
                                return false;
                            }
                        }
                    }
                }
            } catch (ConcurrentModificationException e) {
                System.out.println("O puscat la threaduri");
                return true;
            }
        }
        return true;
    }

    public void clear() {
        cars.clear();
        roads.clear();
        crossroads.clear();
        WriteToFile.cars.clear();
    }
}
