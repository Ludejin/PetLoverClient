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

package com.github.moduth.ext.component.logger;

import com.github.moduth.ext.utils.Singleton;

import static com.github.moduth.ext.component.logger.LogConstants.TAG;

/**
 * Debug-mode com.github.moduth.ext.component.logger. e/w/d write file and logcat，v/d only logcat
 * <p/>
 * Created by zhaiyifan on 2015/7/31.
 */
public class DebugLogger implements ILog {

    private DebugLogger() {
    }

    private static Singleton<DebugLogger, Void> sSingleton = new Singleton<DebugLogger, Void>() {
        @Override
        protected DebugLogger create(Void aVoid) {
            return new DebugLogger();
        }
    };

    public static DebugLogger getInstance() {
        return sSingleton.get(null);
    }

    @Override
    public void v(String tag, String text) {
        Log.v(tag, text);
    }

    @Override
    public void v(String text) {
        Log.v(TAG, text);
    }

    @Override
    public void d(String tag, String text) {
        Log.d(tag, text);
    }

    @Override
    public void d(String text) {
        Log.d(TAG, text);
    }

    @Override
    public void i(String tag, String text) {
        Log.i(tag, text);
        Log4j.i(tag, text);
    }

    @Override
    public void i(String text) {
        Log.i(TAG, text);
        Log4j.i(text);
    }

    @Override
    public void w(String tag, String text) {
        Log.w(tag, text);
        Log4j.w(tag, text);
    }

    @Override
    public void w(String text) {
        Log.w(TAG, text);
        Log4j.w(text);
    }

    @Override
    public void e(String tag, String text) {
        Log.e(tag, text);
        Log4j.e(tag, text);
    }

    @Override
    public void e(Exception e) {
        Log.e(TAG, e);
        Log4j.e(e);
    }

    @Override
    public void e(String text) {
        Log.e(TAG, text);
        Log4j.e(text);
    }
}