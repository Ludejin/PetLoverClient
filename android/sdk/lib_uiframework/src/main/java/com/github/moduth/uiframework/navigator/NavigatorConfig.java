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

import java.util.regex.Pattern;

/**
 * 导航配置类
 *
 * @author markzhai on 16/2/29
 * @version 1.0.0
 */
public final class NavigatorConfig {
    private static final String APP_URL_FORMAT = "://(\\w+)/(\\w+)\\??(.*)?";

    private static final Pattern HTTP_URL_PATTERN = Pattern.compile("http://(.*):?(\\d+)?/(\\w+)/(\\w+)\\??(.*)?");

    private static final Pattern OUT_HTTP_URL_PATTERN = Pattern.compile("^(http|https|ftp)://.*$");

    private static final String DEFAULT_HTTP_SCHEMA = "http";

    private static NavigatorConfig sConfig;

    private Pattern mAppPattern = null;
    private String mAppSchema = "morecruit";

    private NavigatorConfig() {
        mAppPattern = Pattern.compile(mAppSchema + APP_URL_FORMAT);
    }

    /**
     * 获取单例
     *
     * @return 单例
     */
    public static NavigatorConfig instance() {
        if (sConfig == null) {
            synchronized (NavigatorConfig.class) {
                if (sConfig == null) {
                    sConfig = new NavigatorConfig();
                }
            }
        }

        return sConfig;
    }

    /**
     * 设置app的schema，如http,ttpod,xiami
     *
     * @param appSchema app schema
     */
    public void setAppSchema(String appSchema) {
        mAppSchema = appSchema;
        mAppPattern = Pattern.compile(mAppSchema + APP_URL_FORMAT);
    }

    protected String getAppSchema() {
        return mAppSchema;
    }

    protected String getDefaultHttpSchema() {
        return DEFAULT_HTTP_SCHEMA;
    }

    protected Pattern getHttpUrlPattern() {
        return HTTP_URL_PATTERN;
    }

    public Pattern getOutHttpUrlPattern() {
        return OUT_HTTP_URL_PATTERN;
    }

    protected Pattern getAppUrlPattern() {
        return mAppPattern;
    }
}
