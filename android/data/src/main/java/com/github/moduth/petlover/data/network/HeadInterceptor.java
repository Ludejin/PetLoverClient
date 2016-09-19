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

package com.github.moduth.petlover.data.network;

import com.github.moduth.ext.Ext;
import com.github.moduth.ext.utils.ViewUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 设置请求的头拦截器
 */
public class HeadInterceptor implements Interceptor {

    private static final String TAG = "HeadInterceptor";

    public static final String HEADER_APP_ID = "APPID";
    // iOS: 1, Android: 2
    public static final String HEADER_APP_ID_VALUE = "2";
    public static final String HEADER_APP_VER = "APPVER";
    public static final String HEADER_APP_BUILD_NO = "APP-BUILD-NO";
    public static final String HEADER_VUSER = "VUSER";
    public static final String HEADER_NETWORK = "C-NETWORK";
    public static final String HEADER_SCREEN_SCALE = "C-SCREEN-SCALE";
    public static final String HEADER_PIC_MODE = "C-PIC-MODE";
    public static final String HEADER_SCREEN_WIDTH = "C-SCREEN-WIDTH";
    public static final String HEADER_SCREEN_HEIGHT = "C-SCREEN-HEIGHT";
    public static final String HEADER_USER_AGENT = "User-Agent";

    public static final String HEADER_SCREEN_WIDTH_VALUE =
            String.valueOf(Ext.g().getScreenWidth() / ViewUtils.getDensity());
    public static final String HEADER_SCREEN_HEIGHT_VALUE =
            String.valueOf(Ext.g().getScreenHeight() / ViewUtils.getDensity());

    /**
     * APPID	INT	应用ID (iOS: 1, Android: 2)
     * APPVER	STRING	客户端版本号，例如 1.2.1
     * VUSER	STRING	用户凭证（登录、注册后获得的一个哈希字符串）
     * 客户端统一采用 POST 键值对方式提交数据给服务端
     * Content-Type: multipart/form-data;
     * 或者
     * Content-Type: application/x-www-form-urlencoded
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder()
                .method(original.method(), original.body())
                .addHeader(HEADER_APP_ID, HEADER_APP_ID_VALUE)
                .addHeader(HEADER_APP_VER, Ext.g().getVersionName())
                .addHeader(HEADER_APP_BUILD_NO, Ext.g().getBuilderNumber())
                .addHeader(HEADER_VUSER, PetloverService.token)
                .addHeader(HEADER_SCREEN_SCALE, String.valueOf(ViewUtils.getDensity())) //缩放比 1/2/3
                .addHeader(HEADER_SCREEN_WIDTH, HEADER_SCREEN_WIDTH_VALUE)
                .addHeader(HEADER_SCREEN_HEIGHT, HEADER_SCREEN_HEIGHT_VALUE)
                .header(HEADER_USER_AGENT, Ext.g().getDeviceInfo())
                .build();
//        Logger.d(TAG, String.format("Sending request %s", toGetUrl(request)));
        return chain.proceed(request);
    }
}
