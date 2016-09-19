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

import com.github.moduth.ext.component.logger.Logger;
import com.github.moduth.ext.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * 导航url类
 *
 * @author markzhai on 16/2/29
 * @version 1.0.0
 */
public class NavigatorURL {

    private static final String TAG = "Navigator.URL";

    private boolean mIsValid;
    private String mSchema;
    private String mHost;
    private String mPort;
    private String mPageType;
    private String mPageName;
    private String mPageParams;
    private Map<String, String> mPageParamMap = null;

    /**
     * 获取schema字符串
     *
     * @return schema字符串
     */
    public String getSchema() {
        return mSchema;
    }

    /**
     * 获取主机名称
     *
     * @return 主机名称
     */
    public String getHost() {
        return mHost;
    }

    /**
     * 获取页面类型
     *
     * @return 页面类型
     */
    public String getPageType() {
        return mPageType;
    }

    /**
     * 获取页面名称
     *
     * @return 页面名称
     */
    public String getPageName() {
        return mPageName;
    }

    /**
     * 获取参数字符串
     *
     * @return 参数字符串
     */
    public String getPageParams() {
        return mPageParams;
    }

    /**
     * 获取参数map
     *
     * @return 参数map
     */
    public Map<String, String> getPageParamMap() {
        return mPageParamMap;
    }

    /**
     * url 是否有效
     *
     * @return true or false
     */
    public boolean isValid() {
        return mIsValid;
    }

    /**
     * 构造函数
     *
     * @param url url字符串
     */
    public NavigatorURL(String url) {
        String requestURL = transformToUtf8(url);

        if (url.startsWith(NavigatorConfig.instance().getDefaultHttpSchema())) {
            Matcher matcher = NavigatorConfig.instance().getHttpUrlPattern().matcher(requestURL);
            if (!matcher.matches()) {
                Matcher matcherForOutSide = NavigatorConfig.instance().getOutHttpUrlPattern().matcher(requestURL);
                if (!matcherForOutSide.matches()) {
                    mIsValid = false;
                } else {
                    mIsValid = true;
                    mSchema = "http";
                    mPageParamMap = new HashMap<>();
                }
            } else {
                mIsValid = true;
                int position = 1;
                mSchema = "http";
                mHost = matcher.group(position++);
                mPort = matcher.group(position++);
                mPageType = matcher.group(position++);
                mPageName = matcher.group(position++);
                mPageParams = matcher.group(position++);
                if (mPageParams != null) {
                    mPageParamMap = convertToParamsMap(mPageParams);
                }
            }
        } else {
            Matcher matcher = NavigatorConfig.instance().getAppUrlPattern().matcher(requestURL);
            if (!matcher.matches()) {
                mIsValid = false;
            } else {
                mIsValid = true;
                int position = 1;
                mSchema = NavigatorConfig.instance().getAppSchema();
                mPageType = matcher.group(position++);
                mPageName = matcher.group(position++);
                mPageParams = matcher.group(position++);
                if (mPageParams != null) {
                    mPageParamMap = convertToParamsMap(mPageParams);
                }
            }
        }
    }

    private Map<String, String> convertToParamsMap(String requestParams) {
        Map<String, String> formatParams = new HashMap<>();
        String[] params = requestParams.split("&");
        for (String param : params) {
            int pos = param.indexOf('=');

            if (pos >= 0) {
                String key = param.substring(0, pos);
                String value = param.substring(pos + 1, param.length());
                formatParams.put(key, value);
            }
        }

        return formatParams;
    }

    /**
     * 将输入的字符串转换为utf-8格式字符串
     *
     * @param urlString url原始值
     * @return 转换后的字符串
     */
    public static String transformToUtf8(String urlString) {
        if (StringUtils.isEmpty(urlString)) {
            return "";
        }

        try {
            return URLDecoder.decode(new String(urlString.getBytes(), "UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException exception) {
            Logger.w(TAG, "decode url string error.", exception);
        }

        return "";
    }
}
