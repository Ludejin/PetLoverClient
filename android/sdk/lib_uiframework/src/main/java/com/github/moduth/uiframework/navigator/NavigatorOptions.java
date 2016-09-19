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
import android.support.v4.app.Fragment;

import com.github.moduth.uiframework.navigator.backstack.AbstractFragment;

import java.util.Map;

/**
 * 导航选项类，包含界面类，及启动界面的默认桉树
 *
 * @author alimusic.wireless
 * @version 0.0.1
 */
public class NavigatorOptions {
    /**
     * Activity类
     */
    private Class<? extends Activity> mActivityClazz;
    /**
     * Fragment类
     */
    private Class<? extends AbstractFragment> mFragmentClazz;
    /**
     * 启动界面的默认参数
     */
    private Map<String, String> mDefaultParams;

    private FragmentLauncher mFragmentLauncher;
    private ActivityLauncher mActivityLauncher;

    /**
     * 设置activity界面类
     *
     * @param clazz activity类
     */
    public void setOpenActivityClass(Class<? extends Activity> clazz) {
        this.mActivityClazz = clazz;
    }

    /**
     * 设置Fragment界面类
     *
     * @param clazz fragment类
     */
    public void setOpenFragmentClass(Class<? extends AbstractFragment> clazz) {
        this.mFragmentClazz = clazz;
    }

    /**
     * 获取Activity界面类
     *
     * @return activity类
     */
    public Class<? extends Activity> getOpenActivityClass() {
        return this.mActivityClazz;
    }

    /**
     * 获取Fragment界面类
     *
     * @return fragment类
     */
    public Class<? extends Fragment> getOpenFragmentClass() {
        return this.mFragmentClazz;
    }

    /**
     * 设置默认界面启动参数
     *
     * @param defaultParams map参数
     */
    public void setDefaultParams(Map<String, String> defaultParams) {
        this.mDefaultParams = defaultParams;
    }

    /**
     * 获取默认界面启动参数
     *
     * @return map参数
     */
    public Map<String, String> getDefaultParams() {
        return this.mDefaultParams;
    }


    public FragmentLauncher getFragmentLauncher() {
        return mFragmentLauncher;
    }

    public void setFragmentLauncher(FragmentLauncher fragmentLauncher) {
        this.mFragmentLauncher = fragmentLauncher;
    }

    public ActivityLauncher getActivityLauncher() {
        return mActivityLauncher;
    }

    public void setActivityLauncher(ActivityLauncher activityLauncher) {
        this.mActivityLauncher = activityLauncher;
    }

}
