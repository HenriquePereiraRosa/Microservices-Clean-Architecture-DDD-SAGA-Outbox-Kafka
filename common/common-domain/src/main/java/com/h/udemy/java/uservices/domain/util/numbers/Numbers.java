package com.h.udemy.java.uservices.domain.util.numbers;

public class Numbers {

    private Numbers() {}

    public static boolean isInteger(String pInt) {
        try{
            Integer.parseInt(pInt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean isInteger(int pInt) {
        String lSInt = String.valueOf(pInt);
        return isInteger(lSInt);
    }
}
