package com.github.moduth.ext.utils;

/**
 * Created by Abner on 16/6/6.
 * Email nimengbo@gmail.com
 * GitHub https://github.com/nimengbo
 */
public class NumberUtils {


    public static Integer valueOf(String str) {
        try {
            return Integer.valueOf(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String valueOf(Integer value) {
        try {
            if(value > 999){
                int valueInteger = value / 1000;
                return String.valueOf(valueInteger)+"k";
            }
            return String.valueOf(value);
        } catch (Exception e) {
            return "";
        }
    }

}
