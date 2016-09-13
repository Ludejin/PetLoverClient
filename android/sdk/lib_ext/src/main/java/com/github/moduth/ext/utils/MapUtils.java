/*
 * The GPL License (GPL)
 *
 * Copyright (c) 2016 MarkZhai (http://zhaiyifan.cn)
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

package com.github.moduth.ext.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.github.moduth.ext.Ext;

/**
 * Created by Abner on 16/7/28.
 * Email nimengbo@gmail.com
 * GitHub https://github.com/nimengbo
 */
public class MapUtils {

    //高德地图应用包名
    public static final String AMAP_PACKAGENAME = "com.autonavi.minimap";
    //百度地图应用包名
    public static final String BAIDUMAP_PACKAGENAME = "com.baidu.BaiduMap";
    //Google地图应用包名
    public static final String GOOGLEMAP_PACKAGENAME = "com.google.android.apps.maps";

    public static Intent getAMapIntent(String lat, String lng) {
        Context context = Ext.getContext();
        if (AppUtils.isPackageInstalled(context, AMAP_PACKAGENAME)) {
            String appName = context.getString(context.getApplicationInfo().labelRes);
            String uri = String.format("androidamap://viewReGeo?sourceApplication=%s&lat=%s&lon=%s&dev=1"
                    , appName, lat, lng);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage(AMAP_PACKAGENAME);
            return intent;
        }
        return null;
    }

    public static Intent getBaiDuMapIntent(String lat, String lng) {
        Context context = Ext.getContext();
        if (AppUtils.isPackageInstalled(context, BAIDUMAP_PACKAGENAME)) {
            String appName = context.getString(context.getApplicationInfo().labelRes);
            String uri = String.format("bdapp://map/geocoder?location=%s,%s&" +
                            "coord_type=gcj02&src=%s"
                    , lat, lng, appName);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage(BAIDUMAP_PACKAGENAME);
            return intent;
        }
        return null;
    }

    public static Intent getGoogleMapIntent(String lat, String lng) {
        Context context = Ext.getContext();
        if (AppUtils.isPackageInstalled(context, AMAP_PACKAGENAME)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.streetview:cbll=" + lat + "," + lng));
            intent.setPackage(GOOGLEMAP_PACKAGENAME);
            return intent;
        }
        return null;
    }


}
