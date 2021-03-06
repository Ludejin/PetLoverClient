/*
 * The GPL License (GPL)
 *
 * Copyright (c) 2016 MarkZhai (http://zhaiyifan.cn/)
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

package com.github.moduth.uiframework.callback;

import android.content.Context;
import android.content.Intent;

/**
 * 全局事件处理器接口
 * <p/>
 * Created by zhaiyifan on 2015/8/6.
 */
public interface IGlobalEventHandler {
    void onEnterForeground();

    void onEnterBackground();

    void onScreenOn();

    void onScreenOff();

    void onUSBConnected();

    void onUSBDisconnected();

    void onCreate(final Context context);

    void onTerminate(final Context context);

    void onLowMemory();

    void onLogin(long uin);

    void onLogout();

    void onClearData();

    void onPhoneBoot(Context context, Intent intent);
}