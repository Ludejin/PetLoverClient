package com.github.moduth.petlover;

import android.app.Application;

import com.burnweb.rnsimplealertdialog.RNSimpleAlertDialogPackage;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.github.moduth.ext.component.logger.DebugLogger;
import com.github.moduth.ext.component.logger.Logger;
import com.github.moduth.ext.component.logger.ReleaseLogger;
import com.github.moduth.ext.utils.ProcessUtils;
import com.i18n.reactnativei18n.ReactNativeI18n;

import java.util.Arrays;
import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class PlApplication extends Application implements ReactApplication {

    private final static String TAG = "PlApplication";

    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        @Override
        protected boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                    new MainReactPackage(),
                    new RNSimpleAlertDialogPackage(),
                    new ReactNativeI18n()
            );
        }
    };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // 如果不是当前主进程,就不注册了.
        if(!ProcessUtils.isMainProcess(this)){
            return ;
        }
        initLog();

    }

    private void initLog() {
        if (BuildConfig.DEBUG) {
            Logger.init(this, DebugLogger.getInstance());
//            FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        } else {
            Logger.init(this, ReleaseLogger.getInstance());
        }
    }
}
