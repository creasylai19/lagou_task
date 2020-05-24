package com.creasy.util;

import java.util.Random;

public class StringUtils {

    public static String getRamdomString(int bits){
        Random random = new Random();
        int loop = 0;
        StringBuilder sb = new StringBuilder();
        while (loop < bits){
            loop++;
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(getRamdomString(6));
    }

}
