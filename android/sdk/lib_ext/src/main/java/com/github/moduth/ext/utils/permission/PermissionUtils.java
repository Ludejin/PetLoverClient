package com.github.moduth.ext.utils.permission;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abner on 16/8/1.
 * Email nimengbo@gmail.com
 * GitHub https://github.com/nimengbo
 */
public class PermissionUtils {

    public static void checkLocationPermission(Context context , CheckPermissionCallBack callBack) {
        int checkWifiCode = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int checkGpsCode = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (checkWifiCode != PackageManager.PERMISSION_GRANTED ||
                checkGpsCode != PackageManager.PERMISSION_GRANTED) {
            Map<String ,Boolean> disallowedMap = new HashMap<>();
            disallowedMap.put(Manifest.permission.ACCESS_COARSE_LOCATION,
                    checkWifiCode == PackageManager.PERMISSION_GRANTED);
            disallowedMap.put(Manifest.permission.ACCESS_FINE_LOCATION,
                    checkGpsCode == PackageManager.PERMISSION_GRANTED);
            callBack.disallowed(disallowedMap);
        } else {
            callBack.allowed();
        }
    }

}
