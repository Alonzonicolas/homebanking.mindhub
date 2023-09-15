package com.mindhub.homebanking.utils;

import java.util.Random;

public final class CardUtils {

    public static String getCardNumber() {
        return (int) ((Math.random() * (9999 - 1000)) + 1000)
                + "-" + (int) ((Math.random() * (9999 - 1000)) + 1000)
                + "-" + (int) ((Math.random() * (9999 - 1000)) + 1000)
                + "-" + (int) ((Math.random() * (9999 - 1000)) + 1000);
    }

    public static int getCVV() {
        return (int) ((Math.random() * (999 - 100)) + 100);
    }

}
