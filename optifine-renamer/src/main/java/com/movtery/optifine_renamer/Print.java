package com.movtery.optifine_renamer;

import java.util.Locale;

public class Print {
    private static final String NAME = "OptiFineRenamer";

    public static void printLog(String logText) {
        System.out.printf(Locale.getDefault(), "%s: %s%n", NAME, logText);
    }
}
