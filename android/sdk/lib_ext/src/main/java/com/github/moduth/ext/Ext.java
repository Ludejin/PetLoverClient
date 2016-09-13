/*
 * The GPL License (GPL)
 *
 * Copyright (c) 2016 markzhai
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

package com.github.moduth.ext;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;

import com.github.moduth.ext.component.logger.Logger;
import com.github.moduth.ext.utils.AppUtils;

/**
 * Extension库的环境抽象类，需要外部给出注入实现
 *
 * @author markzhai on 16/3/4
 * @version 1.0.0
 */
public abstract class Ext {

    private static final String TAG = "Ext";

    private static Context sContext = null;
    private static Application sApplication = null;

    private static String sPackageName;
    private static String sVersionName;
    private static String sBuildNumber;
    private static int sVersionCode = 0;

    private static Ext sInstance = null;

    public static Ext g() {
        if (sInstance == null) {
            throw new RuntimeException("Ext 没有初始化!");
        }
        return sInstance;
    }

    public static void init(Application app, Ext instance) {
        sContext = app.getApplicationContext();
        sApplication = app;
        sInstance = instance;
        sPackageName = sApplication.getPackageName();
        initVersionCodeAndName(sApplication);
        initDebugConfig(sApplication);
    }

    private static void initVersionCodeAndName(Context context) {
        String version = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(sPackageName, 0);
            sVersionCode = info.versionCode;
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(TAG, "initVersionCodeAndName", e);
        }
        sVersionName = version.substring(0, version.lastIndexOf('.'));
        sBuildNumber = version.substring(version.lastIndexOf('.') + 1, version.length());
    }

    private static void initDebugConfig(Context context) {
        ApplicationInfo appInfo = AppUtils.getAppInfoWithFlags(context, PackageManager.GET_META_DATA);
        if (appInfo != null) {
            DebugConfig.isDebug = ((appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
        }
    }

    // Debug模式
    public static final class DebugConfig {
        // 是否是调试模式
        public static boolean isDebug = false;
    }

    public static Context getContext() {
        return sContext;
    }

    public static Application getApplication() {
        return sApplication;
    }

    public abstract String getCurOpenId();

    public abstract String getDeviceInfo();

    public abstract String getPackageNameForResource();

    public String getPackageName() {
        return sPackageName;
    }

    public int getVersionCode() {
        return sVersionCode;
    }

    public String getVersionName() {
        return sVersionName;
    }

    public String getBuilderNumber() {
        return sBuildNumber;
    }

    public abstract int getScreenHeight();

    public abstract int getScreenWidth();

    // Network
    public abstract boolean isAvailable();

    public abstract boolean isWap();

    public abstract boolean isMobile();

    public abstract boolean is2G();

    public abstract boolean is3G();

    public abstract boolean isWifi();

    public abstract boolean isEthernet();

    public boolean isDebuggable() {
        return DebugConfig.isDebug;
    }

    /**
     * 拦截系统字体大小设置，并应用给定的字体大小
     */
    public abstract boolean fontInterceptorOnInterceptSetTextSize(View view, float textSize);

    public abstract void showAlertDialog(Context context, String title, String message,
                                         String positive,
                                         DialogInterface.OnClickListener positiveListener,
                                         String negative,
                                         DialogInterface.OnClickListener negativeListener);
}
