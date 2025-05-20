package com.example.glimmerheaven.utils.Ratings;

import java.util.ArrayList;

public class CalculateAverageRate {
    public static float getRoundedAverage(float totalRtCount, ArrayList<Float> ratings){
        float total = 0f;

        for(Float rate : ratings){
            total += rate;
        }

        float average = total/totalRtCount;

        return Math.round(average);
    }
}
