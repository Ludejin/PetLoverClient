package com.github.moduth.ext.utils;

import android.os.Build;

/**
 * Rom Utils.
 *
 * @author markzhai on 16/8/28
 * @version 1.3.0
 */
public final class RomUtils {

    private static final String MANUFACTURER_XIAOMI = "Xiaomi";
    private static final String MANUFACTURER_HUAWEI = "HUAWEI";

    public static boolean isHuaWeiPhone() {
        return getManufacturer().contains(MANUFACTURER_HUAWEI);
    }

    public static boolean isXiaomiPhone() {
        return getManufacturer().equals(MANUFACTURER_XIAOMI);
    }

    private static String getEmuiVersion() {
        return PropertyUtils.get("ro.build.version.emui", "");
    }

    private static String getMiuiVersion() {
        return PropertyUtils.get("ro.miui.ui.version.name", "");
    }

    private static String getManufacturer() {
        return Build.MANUFACTURER;
    }
}
