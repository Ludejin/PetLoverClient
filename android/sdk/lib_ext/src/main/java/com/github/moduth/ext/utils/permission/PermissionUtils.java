/*
 * The GPL License (GPL)
 *
 * Copyright (c) 2016 Abner (http://http://abner-nimengbo.cn/)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
