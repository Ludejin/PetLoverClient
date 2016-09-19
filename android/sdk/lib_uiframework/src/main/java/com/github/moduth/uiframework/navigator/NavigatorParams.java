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

package com.github.moduth.uiframework.navigator;

import android.os.Bundle;

import com.github.moduth.ext.component.logger.Logger;

import java.util.Map;

/**
 * 导航参数类，打开新界面的需要的所有参数都保存在此类中
 *
 * @author markzhai on 16/2/29
 * @version 1.0.0
 */
public class NavigatorParams {

    private static final String TAG = "NavigatorParams";

    /**
     * 界面启动类及默认的静态参数
     */
    private NavigatorOptions mURLNavigatorOptions;
    /**
     * 界面动态参数(用于H5页面参数传递,H5参数无法识别参数类型,只能统一处理成string类型)
     */
    private Map<String, String> mOpenParams;

    /**
     * 界面动态参数(用于Native参数传递,Native参数类型丰富型太强,需要支持Object类型参数)
     */
    private Bundle mBundle;

    /**
     * 获取静态参数类实例
     *
     * @return 静态参数实例
     */
    public NavigatorOptions getOptions() {
        return mURLNavigatorOptions;
    }

    /**
     * 设置静态参数实例
     *
     * @param URLNavigatorOptions 静态参数实例
     */
    public void setOptions(NavigatorOptions URLNavigatorOptions) {
        mURLNavigatorOptions = URLNavigatorOptions;
    }

    /**
     * 获取动态参数实例
     *
     * @return 动态参数map
     */
    public Map<String, String> getParams() {
        return mOpenParams;
    }

    /**
     * 设置动态参数实例
     *
     * @param openParams 动态参数map
     */
    public void setParams(Map<String, String> openParams) {
        this.mOpenParams = openParams;
    }

    /**
     * 获取动态参数(主要用于Native)
     *
     * @return 动态参数
     */
    public Bundle getBundle() {
        return mBundle;
    }

    /**
     * 设置动态参数(主要用于Native)
     *
     * @param bundle 动态参数
     */
    public void setBundle(Bundle bundle) {
        mBundle = bundle;
    }

    /*************************************
     * 以下各项参数获取函数(主要是针对Map<String,String>和Bundle之间的兼容方案)
     * 其他类型的参数获取,如获取ArrayXXX,Parcelable,仍需自行获取Bundle解析
     *
     * @param key          key值
     * @param defaultValue 默认Value值
     **************************************/

    public String getParamString(String key, String defaultValue) {
        if (null != mBundle && mBundle.containsKey(key)) {
            return mBundle.getString(key, defaultValue);
        }
        if (null != mOpenParams && mOpenParams.containsKey(key)) {
            return mOpenParams.get(key);
        }
        return defaultValue;
    }

    public boolean getParamBoolean(String key, boolean defaultValue) {
        if (null != mBundle && mBundle.containsKey(key)) {
            return mBundle.getBoolean(key, defaultValue);
        }
        if (null != mOpenParams && mOpenParams.containsKey(key)) {
            String value = mOpenParams.get(key);
            try {
                return Boolean.parseBoolean(value);
            } catch (Exception e) {
                Logger.w(TAG, e);
            }
        }
        return defaultValue;
    }

    public short getParamShort(String key, short defaultValue) {
        if (null != mBundle && mBundle.containsKey(key)) {
            return mBundle.getShort(key, defaultValue);
        }
        if (null != mOpenParams && mOpenParams.containsKey(key)) {
            String value = mOpenParams.get(key);
            try {
                return Short.parseShort(value);
            } catch (Exception e) {
                Logger.w(TAG, e);
            }
        }
        return defaultValue;
    }

    public int getParamInt(String key, int defaultValue) {
        if (null != mBundle && mBundle.containsKey(key)) {
            return mBundle.getInt(key, defaultValue);
        }
        if (null != mOpenParams && mOpenParams.containsKey(key)) {
            String value = mOpenParams.get(key);
            try {
                return Integer.parseInt(value);
            } catch (Exception e) {
                Logger.w(TAG, e);
            }
        }
        return defaultValue;
    }

    public long getParamLong(String key, long defaultValue) {
        if (null != mBundle && mBundle.containsKey(key)) {
            return mBundle.getLong(key, defaultValue);
        }
        if (null != mOpenParams && mOpenParams.containsKey(key)) {
            String value = mOpenParams.get(key);
            try {
                return Long.parseLong(value);
            } catch (Exception e) {
                Logger.w(TAG, e);
            }
        }
        return defaultValue;
    }

    public float getParamFloat(String key, float defaultValue) {
        if (null != mBundle && mBundle.containsKey(key)) {
            return mBundle.getFloat(key, defaultValue);
        }
        if (null != mOpenParams && mOpenParams.containsKey(key)) {
            String value = mOpenParams.get(key);
            try {
                return Float.parseFloat(value);
            } catch (Exception e) {
                Logger.w(TAG, e);
            }
        }
        return defaultValue;
    }

    public double getParamDouble(String key, double defaultValue) {
        if (null != mBundle && mBundle.containsKey(key)) {
            return mBundle.getDouble(key, defaultValue);
        }
        if (null != mOpenParams && mOpenParams.containsKey(key)) {
            String value = mOpenParams.get(key);
            try {
                return Double.parseDouble(value);
            } catch (Exception e) {
                Logger.w(TAG, e);
            }
        }
        return defaultValue;
    }

}
