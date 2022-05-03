package com.example.myapplication;

import java.util.Random;

public class Helper {

    static public int INIT = 9;
    static public double FILL_PROB = (double) 1/2;
    static public double SHOW_PROB = (double) 7/12;

    static public boolean DECISION(double probability)
    {
        Random rand = new Random();
        double number = rand.nextDouble();
        return number <= probability;
    }

}