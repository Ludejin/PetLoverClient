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

package com.github.moduth.ext.component.info;

/**
 * 网络类型枚举，包含了常用网络类型
 * <p/>
 * Created by zhaiyifan on 2015/7/31.
 */
public enum NetworkType {
    /**
     * 无网络，网络不可用
     */
    NONE("NONE", false),
    /**
     * Wifi网络
     */
    WIFI("WIFI", true),
    /**
     * 2G网络 / 低速移动网络
     */
    MOBILE_2G("2G", true),
    /**
     * 3G网络 / 高速移动网络
     */
    MOBILE_3G("3G", true),

    /**
     * 4G网络 / 超高速移动网络(Yeah!)
     */
    MOBILE_4G("4G", true),
    /**
     * 有线网路
     */
    ETHERNET("ETHERNET", true),
    /**
     * 其他网络，含蓝牙、WIFI P2P等
     */
    OTHERS("UNKNOWN", true),;

    private String name;
    private boolean available;

    NetworkType(String friendlyName, boolean available) {
        setName(friendlyName);
        setAvailable(available);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}

