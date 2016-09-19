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

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.github.moduth.ext.component.logger.Logger;
import com.github.moduth.ext.utils.StringUtils;
import com.github.moduth.uiframework.navigator.backstack.AbstractFragment;
import com.github.moduth.uiframework.navigator.backstack.IFragmentBackHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * url 导航
 *
 * @author markzhai on 16/2/29
 * @version 1.0.0
 */
public final class Navigator {
    private static final String TAG = "Navigator";

    private static final Map<String, NavigatorOptions> mNavigatorOptionsHashMap = new HashMap<>();
    private static final Map<String, NavigatorParams> mNavigatorParamsHashMap = new HashMap<>();

    private static final Map<String, NavigatorOptions> mSchemeNavigatorOptionsHashMap = new HashMap<>();

    private static Context sGlobalContext;

    private int mLaunchFlag = Intent.FLAG_ACTIVITY_NEW_TASK;
    private NavigatorCloser mNavigatorCloser;
    private IFragmentBackHelper mFragmentIFragmentBackHelper;
    private int mContainerId;
    private int mEnterAnimRes;
    private int mPopExitAnimRes;
    private String mUrl;
    private Bundle mBundle;
    private int mRequestCode = -1;
    private AbstractFragment mFragment;
    private static FragmentBackHelperFactory sFragmentBackHelperFactory;

    private Navigator() {
    }

    /**
     * 注册Fragment导航信息
     *
     * @param fragmentLauncher 该Fragment的启动器
     */
    public static void registerFragment(@NonNull String pageName, FragmentLauncher fragmentLauncher) {
        NavigatorOptions options = new NavigatorOptions();
        options.setFragmentLauncher(fragmentLauncher);
        mNavigatorOptionsHashMap.put(pageName, options);
    }

    /**
     * 注册Fragment导航信息
     *
     * @param clazz fragment的类名
     */
    public static void registerFragment(@NonNull String pageName, Class<? extends AbstractFragment> clazz) {
        registerFragment(pageName, clazz, null);
    }


    /**
     * 注册Activity导航
     *
     * @param launcher Activity的启动器
     */
    public static void registerActivity(@NonNull String pageName, ActivityLauncher launcher) {
        NavigatorOptions options = new NavigatorOptions();
        options.setActivityLauncher(launcher);
        mNavigatorOptionsHashMap.put(pageName, options);
    }

    /**
     * 注册
     *
     * @param schemeName like http
     * @param clazz      activity的类
     */
    public static void registerScheme(@NonNull String schemeName, Class<? extends Activity> clazz) {
        NavigatorOptions navigatorOptions = new NavigatorOptions();
        navigatorOptions.setOpenActivityClass(clazz);
        mSchemeNavigatorOptionsHashMap.put(schemeName, navigatorOptions);
    }

    /**
     * 注册Activity导航信息
     *
     * @param clazz activity的类
     */
    public static void registerActivity(@NonNull String pageName, Class<? extends Activity> clazz) {
        registerActivity(pageName, clazz, null);
    }

    /**
     * 注册Activity导航信息
     *
     * @param clazz activity的类
     */
    public static void registerActivity(@NonNull String pageName, Class<? extends Activity> clazz, NavigatorOptions navigatorOptions) {
        if (navigatorOptions == null) {
            navigatorOptions = new NavigatorOptions();
        }
        navigatorOptions.setOpenActivityClass(clazz);
        mNavigatorOptionsHashMap.put(pageName, navigatorOptions);
    }


    public static void init(Context context, String appSchema) {
        sGlobalContext = context;
        NavigatorConfig.instance().setAppSchema(appSchema);
    }

    public static void setFragmentBackHelperFactory(FragmentBackHelperFactory factory) {
        sFragmentBackHelperFactory = factory;
    }

    /**
     * @return false 表示open出现问题，schema未匹配或其他错误
     */
    public boolean open() {
        if (StringUtils.isEmpty(mUrl)) {
            throw new IllegalArgumentException("the mUrl is empty. should config with Builder first");
        }

        return open(mUrl);
    }

    /**
     * 跳转
     *
     * @param url url地址
     * @return false 表示open出现问题，schema未匹配或其他错误
     */
    public boolean open(String url) {
        Logger.d(TAG, "open url." + url);
        NavigatorURL mNavigatorURL = new NavigatorURL(url);
        NavigatorParams params = createNavigatorParamsFromUrl(url, mNavigatorURL);
        // 增加Bundle参数
        if (null != params) {
            params.setBundle(mBundle);
        } else {
//            Logger.e(TAG, "params create failed, ignore " + url);
//            return false;
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                intent.addFlags(mLaunchFlag <= 0 ? Intent.FLAG_ACTIVITY_NEW_TASK : mLaunchFlag);
                sGlobalContext.startActivity(intent);
                return true;
            } catch (ActivityNotFoundException exception) {
                Logger.e(TAG, "ActivityNotFoundException, ignore " + url);
                Logger.e(TAG, exception.getMessage());
                return false;
            }
        }

        NavigatorOptions options = params.getOptions();

        if (options == null) {
            Logger.e(TAG, "options create failed, ignore " + url);
            return false;
        }

        if (options.getOpenActivityClass() != null || options.getActivityLauncher() != null) {

            if (options.getActivityLauncher() != null) {
                options.getActivityLauncher().open(sGlobalContext, params);
            } else {
                Intent intent = intentFor(params);
                intent.setClass(sGlobalContext, options.getOpenActivityClass());
                if (mRequestCode >= 0) {
                    mFragment.startActivityForResult(intent, mRequestCode);
                } else {
                    intent.addFlags(mLaunchFlag <= 0 ? Intent.FLAG_ACTIVITY_NEW_TASK : mLaunchFlag);
                    sGlobalContext.startActivity(intent);
                }
            }

            if (mNavigatorCloser != null) {
                mNavigatorCloser.close(params);
            }
        } else if ((options.getOpenFragmentClass() != null && mFragmentIFragmentBackHelper != null) || options.getFragmentLauncher() != null) {
            if (options.getFragmentLauncher() != null) {
                options.getFragmentLauncher().open(mFragmentIFragmentBackHelper, params);
            } else {
                try {
                    AbstractFragment fragment = (AbstractFragment) options.getOpenFragmentClass().newInstance();
                    Bundle bundle = bundleFor(params);
                    fragment.setArguments(bundle);
                    if (mContainerId > 0 && mEnterAnimRes > 0 && mPopExitAnimRes > 0 && mLaunchFlag > 0) {
                        mFragmentIFragmentBackHelper.launchFragment(fragment, mContainerId, mEnterAnimRes, mPopExitAnimRes, mLaunchFlag);
                    } else {
                        mFragmentIFragmentBackHelper.launchFragment(fragment);
                    }
                } catch (InstantiationException e) {
                    Logger.e(TAG, "InstantiationException", e);
                    return false;
                } catch (IllegalAccessException e) {
                    Logger.e(TAG, "IllegalAccessException", e);
                    return false;
                }
            }
        }

        return true;
    }

    private static Intent intentFor(NavigatorParams params) {
        NavigatorOptions options = params.getOptions();
        Intent intent = new Intent();
        if (options.getDefaultParams() != null) {
            for (Entry<String, String> entry : options.getDefaultParams().entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }
        for (Entry<String, String> entry : params.getParams().entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        return intent;
    }

    private static Bundle bundleFor(NavigatorParams params) {
        NavigatorOptions options = params.getOptions();
        Bundle intent = new Bundle();
        if (options.getDefaultParams() != null) {
            for (Entry<String, String> entry : options.getDefaultParams().entrySet()) {
                intent.putString(entry.getKey(), entry.getValue());
            }
        }
        for (Entry<String, String> entry : params.getParams().entrySet()) {
            intent.putString(entry.getKey(), entry.getValue());
        }
        return intent;
    }

    /**
     * 解析url
     *
     * @param url           url请求
     * @param mNavigatorURL 导航url
     * @return 参数类
     */
    private static NavigatorParams createNavigatorParamsFromUrl(String url, NavigatorURL mNavigatorURL) {

        if (!mNavigatorURL.isValid()) {
            Logger.w(TAG, "Not valid for url " + url);
            return null;
        }

        if (mNavigatorParamsHashMap.get(url) != null) {
            return mNavigatorParamsHashMap.get(url);
        }

        NavigatorParams navigatorParams = null;

        // 先匹配scheme，如http
        for (Entry<String, NavigatorOptions> entry : mSchemeNavigatorOptionsHashMap.entrySet()) {
            String scheme = entry.getKey();
            NavigatorOptions navigatorOptions = entry.getValue();

            if (!scheme.equalsIgnoreCase(mNavigatorURL.getSchema())) {
                continue;
            }

            navigatorParams = new NavigatorParams();

            // add raw url
            mNavigatorURL.getPageParamMap().put(NavigatorConstansts.URL, url);

            navigatorParams.setParams(mNavigatorURL.getPageParamMap());
            navigatorParams.setOptions(navigatorOptions);
            break;
        }

        // 遍历默认页面的静态参数类
        for (Entry<String, NavigatorOptions> entry : mNavigatorOptionsHashMap.entrySet()) {
            String pageName = entry.getKey();
            NavigatorOptions navigatorOptions = entry.getValue();

            if (!pageName.equalsIgnoreCase(mNavigatorURL.getPageName())) {
                continue;
            }

            navigatorParams = new NavigatorParams();
            navigatorParams.setParams(mNavigatorURL.getPageParamMap());
            navigatorParams.setOptions(navigatorOptions);
            break;
        }

        if (navigatorParams == null) {
            Logger.w(TAG, "No route found for url " + url);
            return null;
        }

        mNavigatorParamsHashMap.put(url, navigatorParams);
        return navigatorParams;
    }

    /**
     * 注册Fragment导航
     *
     * @param pageName 资源名称
     * @param clazz    fragment的类名
     * @param options  选项
     */
    public static void registerFragment(String pageName, Class<? extends AbstractFragment> clazz, NavigatorOptions options) {
        if (options == null) {
            options = new NavigatorOptions();
        }
        options.setOpenFragmentClass(clazz);
        mNavigatorOptionsHashMap.put(pageName, options);
    }

    /**
     * @deprecated use the {@link Builder} API
     */
    public void setLaunchFlag(int launchFlag) {
        mLaunchFlag = launchFlag;
    }

    /**
     * @deprecated use the {@link Builder} API
     */
    public void setRequestCode(AbstractFragment fragment, int requestCode) {
        mRequestCode = requestCode;
        mFragment = fragment;
    }

    /**
     * @deprecated use the {@link Builder} API
     */
    public void setNavigatorCloser(NavigatorCloser navigatorCloser) {
        mNavigatorCloser = navigatorCloser;
    }

    /**
     * @deprecated use the {@link Builder} API
     */
    public void setFragmentIFragmentBackHelper(IFragmentBackHelper fragmentIFragmentBackHelper) {
        mFragmentIFragmentBackHelper = fragmentIFragmentBackHelper;
    }

    /**
     * @deprecated use the {@link Builder} API
     */
    public void setContainerId(int containerId) {
        mContainerId = containerId;
    }

    /**
     * @deprecated use the {@link Builder} API
     */
    public void setEnterAnimRes(int enterAnimRes) {
        mEnterAnimRes = enterAnimRes;
    }

    /**
     * @deprecated use the {@link Builder} API
     */
    public void setPopExitAnimRes(int popExitAnimRes) {
        mPopExitAnimRes = popExitAnimRes;
    }

    /**
     * @deprecated use the {@link Builder} API
     */
    public void setUrl(String url) {
        mUrl = url;
    }

    /**
     * @deprecated use the {@link Builder} API
     */
    public void setBundle(Bundle bundle) {
        mBundle = bundle;
    }

    public static final class Builder {
        private NavigatorCloser mNavigatorCloser;
        private IFragmentBackHelper mFragmentBackHelper;
        private int mContainerId;
        private int mEnterAnimRes;
        private int mPopExitAnimRes;
        private int mLaunchFlag = Intent.FLAG_ACTIVITY_NEW_TASK;
        private String mUrl;
        private Bundle mBundle;
        private int mRequestCode = -1;
        private AbstractFragment mFragment;
        private Uri.Builder mUriBuilder;

        public Builder() {
        }

        private void checkPageIsSet() {
            if (mUriBuilder == null) {
                throw new IllegalAccessError("should call setPageName before you call addParameter");
            }
        }

        public Builder addParameter(@NonNull String key, @NonNull String value) {
            checkPageIsSet();

            mUriBuilder.appendQueryParameter(key, value);
            return this;
        }

        public Builder addParameter(@NonNull String key, @NonNull Number value) {
            checkPageIsSet();

            mUriBuilder.appendQueryParameter(key, value.toString());

            return this;
        }

        public Builder addParameter(@NonNull String key, @NonNull Boolean value) {
            checkPageIsSet();

            mUriBuilder.appendQueryParameter(key, value.toString());

            return this;
        }

        public Builder setPageName(@NonNull String pageName) {
            mUriBuilder = Uri.parse(NavigatorConfig.instance().getAppSchema() + "://page/" + pageName).buildUpon();
            return this;
        }

        public Builder setUrl(@NonNull String url) {
            mUrl = url;
            mUriBuilder = Uri.parse(url).buildUpon();
            return this;
        }

        public void setNavigatorCloser(NavigatorCloser navigatorCloser) {
            mNavigatorCloser = navigatorCloser;
        }

        public Builder setFragmentBackHelper(IFragmentBackHelper fragmentBackHelper) {
            mFragmentBackHelper = fragmentBackHelper;
            return this;
        }

        public void setContainerId(int containerId) {
            mContainerId = containerId;
        }

        public void setEnterAnimRes(int enterAnimRes) {
            mEnterAnimRes = enterAnimRes;
        }

        public void setPopExitAnimRes(int popExitAnimRes) {
            mPopExitAnimRes = popExitAnimRes;
        }

        public void setLaunchFlag(int launchFlag) {
            mLaunchFlag = launchFlag;
        }

        public Builder setRequestCode(AbstractFragment fragment, int requestCode) {
            mFragment = fragment;
            mFragment.setRequestCode(requestCode);
            mRequestCode = requestCode;
            return this;
        }

        /**
         * @param bundle bundle
         * @deprecated should not use this API. it cause ui dependent ui
         */
        public void setBundle(Bundle bundle) {
            mBundle = bundle;
        }

        public Navigator build() {
            if (mFragmentBackHelper == null && sFragmentBackHelperFactory != null) {
                mFragmentBackHelper = sFragmentBackHelperFactory.getCurrentFragmentBackHelper();
            }

            Navigator navigator = new Navigator();
            navigator.setUrl(mUriBuilder.build().toString());
            navigator.setLaunchFlag(mLaunchFlag);
            navigator.setPopExitAnimRes(mPopExitAnimRes);
            navigator.setEnterAnimRes(mEnterAnimRes);
            navigator.setContainerId(mContainerId);
            navigator.setFragmentIFragmentBackHelper(mFragmentBackHelper);
            navigator.setNavigatorCloser(mNavigatorCloser);
            navigator.setBundle(mBundle);
            navigator.setRequestCode(mFragment, mRequestCode);
            return navigator;
        }
    }
}
