package controller;

import model.Vehicle;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WriteToFile {

    protected static ArrayList<Vehicle> cars = new ArrayList<>();
    private static float averageSpeed;

    public static void createFile() {
        int carIndex = 0;
        try {
            //pasul de simulare e 0.1 sec care face 2 px
            //1 pas de simulare face 20m/s
            //20*3600/1000 = 72 km/h
            //1.7 = 25km/s


            // nici un obstacol......50 km/h
            // 5000 m / 3600 sec = 14 m/s la 50 km/h
            // 1.4px la 0.1sec =>
            //1px = 1m
            //1 pas de simulare = 0.1sec
            //timpul = nr pasi de simulare * 0.1

            //un

            //adun distantele si timpul
            //si impart distanta la timp sa iau viteza medie
            FileWriter myWriter = new FileWriter("Results.txt");
            for (Vehicle car : cars) {
                myWriter.write("Car: " + carIndex +
                        ", distance: " +  String.format("%.02f",(float)car.getDistance() /1000) + "km" +
                        ", time: " + String.format("%.02f",(car.getSimulationStep() * 0.1)/60) + "min" + "" +
                        ", steps: " + car.getSimulationStep()+ "\n");

                carIndex++;
            }

            //SUMA TUTUROR TIMPILOR SI FAC MEDIA

            int time = 0;
            int distance = 0;
            for(Vehicle vehicle : cars){
                time+=vehicle.getTime();
                distance+=vehicle.getDistance();
            }
            averageSpeed = (float)distance/time;
            System.out.println("Viteza medie: " + distance/time);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void openFile(){
        try
        {
            File file = new File("Results.txt");
            if(!Desktop.isDesktopSupported())
            {
                System.out.println("not supported");
                return;
            }
            Desktop desktop = Desktop.getDesktop();
            if(file.exists())
                desktop.open(file);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static float getAverageSpeed() {
        return averageSpeed;
    }
}
