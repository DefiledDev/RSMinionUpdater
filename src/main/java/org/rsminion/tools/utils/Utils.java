package org.rsminion.tools.utils;

import java.util.Arrays;

public class Utils {

    public static final String[] EMPTY_ARRAY = new String[0];

    public static boolean checkIntArrayMatch(int[] arr1, int... arr2) {
        return Arrays.equals(arr1, arr2);
    }

    public static boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String formatAsClass(String name) {
        return String.format("L%s;", name);
    }

}
