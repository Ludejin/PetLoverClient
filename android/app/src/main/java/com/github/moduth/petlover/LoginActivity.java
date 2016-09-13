/*
 * The GPL License (GPL)
 *
 * Copyright (c) 2016 Moduth (https://github.com/moduth)
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
        setContentView(R.layout.activity_login);

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
