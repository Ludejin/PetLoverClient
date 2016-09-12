package com.github.moduth.ext.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * @author markzhai on 16/6/7
 * @version 1.0.0
 */
public class ResourceUtils {
    public static int getColor(Context context, int resourceId) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(resourceId);
        } else {
            return context.getResources().getColor(resourceId);
        }
    }

    public static Drawable getDrawable(Context context, int resourceId) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(resourceId);
        } else {
            return context.getResources().getDrawable(resourceId);
        }
    }
}
