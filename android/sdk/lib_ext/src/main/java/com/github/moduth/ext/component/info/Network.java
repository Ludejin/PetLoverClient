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

import android.support.annotation.RequiresPermission;

import com.github.moduth.ext.Ext;
import com.github.moduth.ext.utils.NetWorkUtils;

/**
 * 网络信息，包含了通用信息(Network.)、
 * 代理信息(Network.Proxy.)、
 * Wifi信息{@link Network.Wifi}
 * DNS信息
 * <p/>
 * Created by zhaiyifan on 2015/7/31.
 */
public class Network extends NetworkDash {
    /**
     * 系统代理信息
     */
    public static abstract class Proxy {
        public static int getPort() {
            return android.net.Proxy.getDefaultPort();
        }

        public static String getHost() {
            return android.net.Proxy.getDefaultHost();
        }
    }

    /**
     * WIFI网卡信息
     */
    public static class Wifi extends WifiDash {

    }

    public static class Dns {
        @RequiresPermission(android.Manifest.permission.ACCESS_WIFI_STATE)
        public static NetWorkUtils.DNS getDNS() {
            return NetWorkUtils.getDNS(Ext.getContext());
        }
    }
}