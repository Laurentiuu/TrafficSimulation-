package controller;

import model.Crossroad;
import model.Light;
import model.Road;
import model.StartPoints;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import view.Traffic;


import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;


public class ReadFromFile {
    private final ArrayList<Road> roads = new ArrayList<>();
    private final HashMap<String, Crossroad> hashMap = new HashMap<>();
    ArrayList<StartPoints> startPoints = new ArrayList<>();
    private int flowTime;

    public ReadFromFile(String mapName, String flowName) {
        clear();
        generateMap(mapName);
        generateFlow(flowName);
    }

    public ReadFromFile(String mapName) {
        generateMap(mapName);
    }

    private void clear() {
        if (this.startPoints != null)
            this.startPoints.clear();
        this.roads.clear();
        this.hashMap.clear();

    }

    private void generateMap(String mapName) {

        StringBuilder content = new StringBuilder();

        try {
            File myObj = new File(mapName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                content.append(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND!!!");
            e.printStackTrace();
        }
        try {
            JSONObject json = new JSONObject(content.toString());
            JSONArray crossroadsFromFile = json.getJSONArray("Crossroads");
            for (int i = 0; i < crossroadsFromFile.length(); i++) {
                createCrossroad((JSONObject) crossroadsFromFile.get(i));
            }
            JSONArray roadsFromFile = json.getJSONArray("Roads");
            for (int i = 0; i < roadsFromFile.length(); i++) {
                createRoads((JSONObject) roadsFromFile.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void generateFlow(String flowName) {

        StringBuilder content = new StringBuilder();

        try {
            File myObj = new File(flowName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                content.append(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND!!!");
            e.printStackTrace();
        }

        try {
            JSONObject json = new JSONObject(content.toString());
            this.flowTime = json.getInt("flowTime");
            //this.flowTime = Integer.MAX_VALUE;
            JSONArray startPointsArray = json.getJSONArray("startPoints");
            for (int i = 0; i < startPointsArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) startPointsArray.get(i);
                for (Road road : roads) {
                    if (road.getName().equals(jsonObject.getString("roadName"))) {
                        double time = jsonObject.getDouble("carGenerationTime");
                        startPoints.add(new StartPoints(road, time));
                    }
                }
            }
            if (startPoints.isEmpty()) {
                Traffic.startPointsNotFound();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createCrossroad(JSONObject arrayObject) throws JSONException {
        String name = arrayObject.getString("name");
        int x = arrayObject.getInt("x");
        int y = arrayObject.getInt("y");
        String type = (String) arrayObject.get("type");

        HashMap<Character, Light> lights = new HashMap<>();
        JSONArray lightsFromFile = arrayObject.getJSONArray("lightTimes");
        for (int i = 0; i < lightsFromFile.length(); i++) {
            JSONObject jsonObject = (JSONObject) lightsFromFile.get(i);

            if (type.contains("U")) {
                double upTime = jsonObject.getDouble("up");
                lights.put('U', new Light('U', upTime));
            }
            if (type.contains("R")) {
                double rightTime = jsonObject.getDouble("right");
                lights.put('R', new Light('R', rightTime));
            }
            if (type.contains("D")) {
                double downTime = jsonObject.getDouble("down");
                lights.put('D', new Light('D', downTime));
            }
            if (type.contains("L")) {
                double leftTime = jsonObject.getDouble("left");
                lights.put('L', new Light('L', leftTime));
            }

        }

        Crossroad crossroad = new Crossroad(name, x, y, type, lights);
        this.hashMap.put(name, crossroad);
    }

    private void createRoads(JSONObject arrayObject) throws JSONException {

        String name = arrayObject.getString("name");
        String start = arrayObject.getString("startCrossroad");
        String end = arrayObject.getString("endCrossroad");
        String t = arrayObject.getString("type");

        Crossroad startCrossroad = hashMap.get(start);
        Crossroad endCrossroad = hashMap.get(end);

        if (startCrossroad != null && endCrossroad != null) {
            int distance;
            if (startCrossroad.getX() == endCrossroad.getX()) {
                distance = (endCrossroad.getY() > startCrossroad.getY())
                        ? endCrossroad.getY() - startCrossroad.getY() : startCrossroad.getY() - endCrossroad.getY();
            } else {
                distance = (endCrossroad.getX() > startCrossroad.getX())
                        ? endCrossroad.getX() - startCrossroad.getX() : startCrossroad.getX() - endCrossroad.getX();
            }
            int auxDistance = distance;
            if (auxDistance % 2 == 1)
                auxDistance += 1;

            if (t.equals("UD")) {
                Road r = new Road(name, distance, startCrossroad.getX() + 7 - 7, startCrossroad.getY(), 16, auxDistance + 30, Road.Type.UD, endCrossroad, false);
                startCrossroad.addRoad(r, 1);
                roads.add(r);
                if (startCrossroad.getY() + auxDistance > Traffic.getScrollPaneHeight()) {
                    Traffic.changePanel(Traffic.getScrollPaneWidth(), startCrossroad.getY() + auxDistance + 100);
                }
            }
            if (t.equals("DU")) {
                Road r = new Road(name, distance, startCrossroad.getX() + 7 + 7, startCrossroad.getY() - auxDistance, 16, auxDistance + 30, Road.Type.DU, endCrossroad, false);
                startCrossroad.addRoad(r, 1);
                roads.add(r);
                if (startCrossroad.getY() > Traffic.getScrollPaneHeight()) {
                    Traffic.changePanel(Traffic.getScrollPaneWidth(), startCrossroad.getY());
                }
            }
            if (t.equals("RL")) {
                Road r = new Road(name, distance, startCrossroad.getX() - auxDistance, startCrossroad.getY() + 7 - 7, auxDistance + 30, 16, Road.Type.RL, endCrossroad, false);
                startCrossroad.addRoad(r, 1);
                roads.add(r);
                if (startCrossroad.getX() > Traffic.getScrollPaneWidth()) {
                    Traffic.changePanel(startCrossroad.getX(), Traffic.getScrollPaneHeight());
                }
            }
            if (t.equals("LR")) {
                Road r = new Road(name, distance, startCrossroad.getX(), startCrossroad.getY() + 7 + 7, auxDistance + 30, 16, Road.Type.LR, endCrossroad, false);
                startCrossroad.addRoad(r, 1);
                roads.add(r);
                if (startCrossroad.getX() + auxDistance > Traffic.getScrollPaneWidth()) {
                    Traffic.changePanel(startCrossroad.getX() + auxDistance + 100, Traffic.getScrollPaneHeight());
                }
            }


        } else if (startCrossroad == null) {
            int distance = arrayObject.getInt("distance");
            int auxDistance = distance;
            if (auxDistance % 2 == 1)
                auxDistance += 1;
            if (t.equals("UD")) {
                Road r = new Road(name, distance, endCrossroad.getX() + 7 - 7, endCrossroad.getY() - auxDistance, 16, auxDistance + 30, Road.Type.UD, endCrossroad, true);
                endCrossroad.addRoad(r, 0);
                roads.add(r);
                if (endCrossroad.getY() + auxDistance > Traffic.getScrollPaneHeight()) {
                    Traffic.changePanel(Traffic.getScrollPaneWidth(), endCrossroad.getY() + auxDistance + 100);
                }
            }
            if (t.equals("DU")) {
                Road r = new Road(name, distance, endCrossroad.getX() + 7 + 7, endCrossroad.getY(), 16, auxDistance + 30, Road.Type.DU, endCrossroad, true);
                endCrossroad.addRoad(r, 0);
                roads.add(r);
                if (endCrossroad.getY() > Traffic.getScrollPaneHeight()) {
                    Traffic.changePanel(Traffic.getScrollPaneWidth(), endCrossroad.getY());
                }
            }
            if (t.equals("RL")) {
                Road r = new Road(name, distance, endCrossroad.getX(), endCrossroad.getY() + 7 - 7, auxDistance + 30, 16, Road.Type.RL, endCrossroad, true);
                endCrossroad.addRoad(r, 0);
                roads.add(r);
                if (endCrossroad.getX() > Traffic.getScrollPaneWidth()) {
                    Traffic.changePanel(endCrossroad.getX(), Traffic.getScrollPaneHeight());
                }
            }
            if (t.equals("LR")) {
                Road r = new Road(name, distance, endCrossroad.getX() - auxDistance, endCrossroad.getY() + 7 + 7, auxDistance + 30, 16, Road.Type.LR, endCrossroad, true);
                endCrossroad.addRoad(r, 0);
                roads.add(r);
                if (endCrossroad.getX() + auxDistance > Traffic.getScrollPaneWidth()) {
                    Traffic.changePanel(endCrossroad.getX() + auxDistance + 100, Traffic.getScrollPaneHeight());
                }
            }
        } else if (endCrossroad == null) {
            int distance = arrayObject.getInt("distance");
            int auxDistance = distance;
            if (auxDistance % 2 == 1)
                auxDistance += 1;
            if (t.equals("UD")) {
                Road r = new Road(name, distance, startCrossroad.getX() + 7 - 7, startCrossroad.getY(), 16, auxDistance + 30, Road.Type.UD, null, false);
                r.setEndPoint(true);
                startCrossroad.addRoad(r, 1);
                roads.add(r);
                if (startCrossroad.getY() + auxDistance > Traffic.getScrollPaneHeight()) {
                    Traffic.changePanel(Traffic.getScrollPaneWidth(), startCrossroad.getY() + auxDistance + 100);
                }
            }
            if (t.equals("DU")) {
                Road r = new Road(name, distance, startCrossroad.getX() + 7 + 7, startCrossroad.getY() - auxDistance, 16, auxDistance + 30, Road.Type.DU, null, false);
                r.setEndPoint(true);
                startCrossroad.addRoad(r, 1);
                roads.add(r);
                if (startCrossroad.getY() > Traffic.getScrollPaneHeight()) {
                    Traffic.changePanel(Traffic.getScrollPaneWidth(), startCrossroad.getY());
                }
            }
            if (t.equals("RL")) {
                Road r = new Road(name, distance, startCrossroad.getX() - auxDistance, startCrossroad.getY() + 7 - 7, auxDistance + 30, 16, Road.Type.RL, null, false);
                r.setEndPoint(true);
                startCrossroad.addRoad(r, 1);
                roads.add(r);
                if (startCrossroad.getX() > Traffic.getScrollPaneWidth()) {
                    Traffic.changePanel(startCrossroad.getX(), Traffic.getScrollPaneHeight());
                }
            }
            if (t.equals("LR")) {
                Road r = new Road(name, distance, startCrossroad.getX(), startCrossroad.getY() + 7 + 7, auxDistance + 30, 16, Road.Type.LR, null, false);
                r.setEndPoint(true);
                startCrossroad.addRoad(r, 1);
                roads.add(r);
                if (startCrossroad.getX() + auxDistance > Traffic.getScrollPaneWidth()) {
                    Traffic.changePanel(startCrossroad.getX() + auxDistance + 100, Traffic.getScrollPaneHeight());
                }
            }
        }
    }

    public ArrayList<Crossroad> getCrossroads() {
        Collection<Crossroad> aux = hashMap.values();
        return new ArrayList<>(aux);
    }


    public ArrayList<StartPoints> getStartPoints() {
        return startPoints;
    }

    public ArrayList<Road> getRoads() {
        return roads;
    }

    public int getFlowTime() {
        return flowTime;
    }
}