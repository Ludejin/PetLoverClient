package com.github.moduth.petlover;

import android.app.Activity;
import android.os.Bundle;

import com.github.moduth.ext.component.logger.Logger;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class LoginActivity extends Activity {

    private final static String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SMSSDK.initSDK(this, "17106857745de", "3e108f0e5ba7220cd5a7b227d174ca4f");
        SMSSDK.registerEventHandler(new EventHandler(){

            @Override
            public void onRegister() {
                super.onRegister();
                Logger.d(TAG,"onRegister");
            }

            @Override
            public void afterEvent(int i, int i1, Object o) {
                Logger.d(TAG,"afterEvent" + i);
            }
        });
        SMSSDK.getVerificationCode("86", "your phone");

    }


}
