package controller;

public class GenerateVehicles {

    private final int ONE_SECOND = 100;
    private int mils = 0;

    public boolean run(double time) {
        if (mils == ONE_SECOND * time) {
            mils = 0;
            return true;
        } else {
            mils++;
            return false;
        }
    }

    public void setMils(int mils) {
        this.mils = mils;
    }
}