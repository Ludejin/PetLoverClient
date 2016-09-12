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
