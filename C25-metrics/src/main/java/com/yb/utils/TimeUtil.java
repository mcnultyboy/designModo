package com.yb.utils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TimeUtil {
    public static Random random = new Random();

    public static void sleepInRandomSeconds(){
        int sec = random.nextInt(500);
        try {
            TimeUnit.MICROSECONDS.sleep((long)sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
