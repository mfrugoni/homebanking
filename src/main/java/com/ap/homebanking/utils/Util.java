package com.ap.homebanking.utils;

public class Util {

    public static int getRandomNumber(int min, int max){
        return (int) ((Math.random() * (max - min)) + min);
    }

}
