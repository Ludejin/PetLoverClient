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
import com.github.moduth.petlover.internal.di.components.ApplicationComponent;
import com.github.moduth.petlover.internal.di.components.DaggerApplicationComponent;
import com.github.moduth.petlover.internal.di.module.ApplicationModule;
import com.i18n.reactnativei18n.ReactNativeI18n;

import java.util.Arrays;
import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class PetloverApplication extends Application {

    private final static String TAG = "PetloverApplication";

    private ApplicationComponent mApplicationComponent;



    @Override
    public void onCreate() {
        super.onCreate();
        // 如果不是当前主进程,就不注册了.
        if(!ProcessUtils.isMainProcess(this)){
            return ;
        }
        initLog();
        initInjector();
    }

    private void initLog() {
        if (BuildConfig.DEBUG) {
            Logger.init(this, DebugLogger.getInstance());
//            FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        } else {
            Logger.init(this, ReleaseLogger.getInstance());
        }
    }
    private void initInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

}
