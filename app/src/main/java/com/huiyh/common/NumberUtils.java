package com.huiyh.common;

import java.math.BigDecimal;

/**
 * @author : 惠远航
 * @date : 2019-07-30 10:15
 */
public class NumberUtils {
    public NumberUtils() {
    }

    public static int parseInt(String str) {
        return parseInt(str, 0);
    }

    public static int parseInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception var3) {
            var3.printStackTrace();
            return defValue;
        }
    }

    public static long parseLong(String str) {
        return parseLong(str, 0L);
    }

    public static long parseLong(String str, long defValue) {
        try {
            return Long.parseLong(str);
        } catch (Exception var4) {
            var4.printStackTrace();
            return defValue;
        }
    }

    public static double parseDouble(String str) {
        return parseDouble(str, 0.0D);
    }

    public static double parseDouble(String str, double defValue) {
        try {
            return Double.parseDouble(str);
        } catch (Exception var4) {
            var4.printStackTrace();
            return defValue;
        }
    }

    public static float parseFloat(String str) {
        return parseFloat(str, 0.0F);
    }

    public static float parseFloat(String str, float defValue) {
        try {
            return Float.parseFloat(str);
        } catch (Exception var3) {
            var3.printStackTrace();
            return defValue;
        }
    }

    public static double round(double value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        return bd.doubleValue();
    }
}
