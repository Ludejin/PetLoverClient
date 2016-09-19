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

package com.github.moduth.uiframework.navigator.backstack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.github.moduth.uiframework.navigator.Navigator;

/**
 * @author markzhai on 16/2/29
 * @version 1.0.0
 */
public class AbstractActivity extends AppCompatActivity {

    private static final String TAG = "AbstractActivity";

    protected FragmentActivityBackHelper mFragmentActivityBackHelper = new FragmentActivityBackHelper(this);

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean result;
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            result = mFragmentActivityBackHelper.onBackPressed();
        } else {
            result = mFragmentActivityBackHelper.onKeyDown(keyCode, event);
        }

        return result || super.onKeyDown(keyCode, event);
    }

    public String getParam(String key) {
        Intent intent = getIntent();
        if (intent != null) {
            return intent.getStringExtra(key);
        }
        return null;
    }

    /**
     * 设置弹出fragment的属性
     *
     * @param containerViewRes containerViewRes
     * @param enterAnimRes     enterAnimRes
     * @param popExitAnimRes   popExitAnimRes
     */
    public void setLaunchFragmentAttr(int containerViewRes, int enterAnimRes, int popExitAnimRes) {
        mFragmentActivityBackHelper.setLaunchFragmentAttr(containerViewRes, enterAnimRes, popExitAnimRes);
    }

    public final void launchRootFragment(AbstractFragment fragment) {
        mFragmentActivityBackHelper.launchRootFragment(fragment);
    }

    public AbstractFragment getRootFragment() {
        return mFragmentActivityBackHelper.getRootFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFragmentActivityBackHelper.clearFragmentBackStack();
    }

    public FragmentActivityBackHelper getFragmentActivityBackHelper() {
        return mFragmentActivityBackHelper;
    }

    /**
     * Fragment回退栈是否为空
     *
     * @return true/false
     */
    protected final boolean isFragmentBackStackEmpty() {
        return mFragmentActivityBackHelper.isFragmentBackStackEmpty();
    }

    /**
     * 启动Fragment，这样启动的Fragment由这个Fragment所在的Activity管理，会加入backStack中
     *
     * @param fragment fragment
     * @deprecated use {@link Navigator.Builder} API
     */
    public final void launchFragment(AbstractFragment fragment) {
        mFragmentActivityBackHelper.launchFragment(fragment);
    }

    /**
     * 启动Fragment，会加入到backstack
     *
     * @param fragment       fragment
     * @param containId      添加fragment的layout id, 0代表使用默认的
     * @param enterAnimRes   进入动画
     * @param popExitAnimRes 退出动画
     * @param launchFlag     flag
     * @deprecated use {@link Navigator.Builder} API
     */
    public final void launchFragment(AbstractFragment fragment, int containId, int enterAnimRes, int popExitAnimRes, int launchFlag) {
        mFragmentActivityBackHelper.launchFragment(fragment, containId, enterAnimRes, popExitAnimRes, launchFlag);
    }

    /**
     * 弹出顶层Fragment
     */
    public final void popTopFragment() {
        mFragmentActivityBackHelper.finishTopFragment();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    /**
     * @return BaseFragment
     */
    public AbstractFragment getTopFragment() {
        return mFragmentActivityBackHelper.getTopFragment();
    }

    public FragmentBackStackManager getFragmentBackStackManager() {
        return mFragmentActivityBackHelper.getFragmentBackStackManager();
    }

    @Override
    public void onBackPressed() {
        if (!mFragmentActivityBackHelper.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
