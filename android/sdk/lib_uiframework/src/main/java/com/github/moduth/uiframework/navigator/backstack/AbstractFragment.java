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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.github.moduth.uiframework.navigator.Navigator;

/**
 * 支持BACK处理的Fragment抽象类
 *
 * @author markzhai on 16/2/29
 * @version 1.0.0
 */
public abstract class AbstractFragment extends Fragment {
    private FragmentBackHelper mFragmentBackHelper = new FragmentBackHelper(this);

    /**
     * 关闭top的fragment
     */
    public static final int FRAGMENT_LAUNCH_CLEAR_TOP = 0x00000001;

    /**
     * 不隐藏在它下面的fragment
     */
    public static final int FRAGMENT_LAUNCH_UNHIDE = 0x00000002;
    /**
     * 不隐藏在它下面的fragment
     */
    public static final int FRAGMENT_LAUNCH_SINGLE_TOP = 0x00000004;

    private int mLaunchFlag;

    int mRequestCode = -1;
    int mResultCode = Activity.RESULT_CANCELED;
    Intent mResultData;

    /**
     * 获取当前fragment的Fragment退出帮助类
     *
     * @return Fragment的退出帮助类
     */
    public FragmentBackHelper getFragmentBackHelper() {
        return mFragmentBackHelper;
    }


    public void setRequestCode(int requestCode) {
        mRequestCode = requestCode;
    }

    /**
     * 本Fragment是否为 SingleTop : 栈顶Fragment只能保留一个
     *
     * @return true 或 false
     */
    protected boolean isSingleTop() {
        return (mLaunchFlag & FRAGMENT_LAUNCH_SINGLE_TOP) != 0;
    }

    /**
     * 本Fragment是否为 ClearTop ： 启动Fragment时将处于栈顶的Fragment进行清理
     *
     * @return true 或 false
     */
    public boolean isClearTop() {
        return (mLaunchFlag & FRAGMENT_LAUNCH_CLEAR_TOP) != 0;
    }

    /**
     * 启动本Fragment的时候是否隐藏之前的Fragment
     *
     * @return true 或 false
     */
    public boolean isHiddenPreviousFragment() {
        return (mLaunchFlag & FRAGMENT_LAUNCH_UNHIDE) == 0;
    }

    /**
     * 设置弹出Flag：支持 FRAGMENT_LAUNCH_CLEAR_TOP 和 FRAGMENT_LAUNCH_UNHIDE
     */
    public void setLaunchFragmentFlag(int launchFlag) {
        mLaunchFlag = launchFlag;
    }

    /**
     * @deprecated use {@link Navigator.Builder} API
     */
    public final void launchFragment(AbstractFragment fragment) {
        mRequestCode = -1;
        mFragmentBackHelper.launchFragment(fragment);
    }

    public final void launchFragmentForResult(AbstractFragment fragment, int requestCode) {
        mRequestCode = requestCode;
        mFragmentBackHelper.launchFragment(fragment);
    }

    /**
     * Fragment借鉴Activity的singleTop启动方式，这个方法接受新传入的数据
     *
     * @param bundle 参数类
     */
    protected void onNewBundle(Bundle bundle) {

    }

    /**
     * 当Fragment被暂停时
     */
    public void onNewPause() {

    }


    /**
     * 当Fragment被恢复时
     */
    public void onNewResume() {

    }

    /**
     * 关闭Fragment
     */
    public void finish() {
        final FragmentBackStackManager parentFragmentBackStackManager = getFragmentBackHelper().getParentFragmentBackStackManager();
        if (parentFragmentBackStackManager != null) {
            if (parentFragmentBackStackManager.isRootFragment(this)) {
                getActivity().finish();
            } else {
                parentFragmentBackStackManager.destroyFragment(this);
            }
        }
    }

    /**
     * 处理KEY DOWN事件
     *
     * @param keyCode KEY编码
     * @param event   事件
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 返回事件处理
     * <p>
     * 默认实现，是退栈
     * @return false则不要再由activity处理，true则需要activity继续处理
     */
    public boolean onBackPressed() {
        return getFragmentBackHelper().onBackPressed();
    }

    public final void setResult(int resultCode) {
        setResult(resultCode, null);
    }

    public final void setResult(int resultCode, Intent data) {
        mResultCode = resultCode;
        mResultData = data;
        final FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.setResult(resultCode, data);
        }
    }
}
