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

import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

/**
 * FragmentActivity的 Fragment返回 帮助类
 *
 * @author markzhai on 16/2/29
 * @version 1.0.0
 */
public class FragmentActivityBackHelper implements IFragmentBackHelper {

    private FragmentBackStackManager mFragmentBackStackManager;

    public FragmentActivityBackHelper(FragmentActivity activity) {
        mFragmentBackStackManager = new FragmentBackStackManager(activity.getSupportFragmentManager());
    }

    /**
     * 处理BACK事件
     *
     * @return 是否已经处理
     */
    public boolean onBackPressed() {
        // 如果栈为空，那么无法进行BACK处理
        if (mFragmentBackStackManager.isBackStackEmpty()) {
            return false;
        } else {
            // 如果栈不为空，那么对Fragment进行BACK处理（备注：RootFragment自身是不会进行BACK处理的，它交给父FragmentActivity或父Fragment）
            mFragmentBackStackManager.getTopFragment().onBackPressed();
            return true;
        }
    }

    /**
     * 处理KEY DOWN事件
     *
     * @param keyCode KEY编码
     * @param event   事件
     * @return true or false
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mFragmentBackStackManager.getTopFragment() != null &&
                mFragmentBackStackManager.getTopFragment().getFragmentBackHelper().onKeyDown(keyCode, event);
    }

    /**
     * 设置弹出fragment的属性
     *
     * @param containerViewRes containerViewRes
     * @param enterAnimRes     enterAnimRes
     * @param popExitAnimRes   popExitAnimRes
     */
    public final void setLaunchFragmentAttr(int containerViewRes, int enterAnimRes, int popExitAnimRes) {
        mFragmentBackStackManager.setLaunchFragmentAttr(containerViewRes, enterAnimRes, popExitAnimRes);
    }

    /**
     * 启动Fragment，会加入到堆栈
     *
     * @param fragment       fragment
     * @param containId      添加fragment的layout id, 0代表使用默认的
     * @param enterAnimRes   进入动画
     * @param popExitAnimRes 退出动画
     * @param launchFlag     flag   {@link AbstractFragment#FRAGMENT_LAUNCH_CLEAR_TOP}
     */
    public final void launchFragment(AbstractFragment fragment, int containId, int enterAnimRes, int popExitAnimRes, int launchFlag) {
        fragment.getFragmentBackHelper().setFragmentBackStackManager(mFragmentBackStackManager);
        fragment.setLaunchFragmentFlag(launchFlag);
        mFragmentBackStackManager.launchFragment(fragment, containId, enterAnimRes, popExitAnimRes);
    }

    /**
     * 清理Fragment栈
     */
    public void clearFragmentBackStack() {
        if (mFragmentBackStackManager != null) {
            mFragmentBackStackManager.clear();
        }
    }

    /**
     * Fragment栈是否为空
     *
     * @return true/false
     */
    public final boolean isFragmentBackStackEmpty() {
        return mFragmentBackStackManager == null || (mFragmentBackStackManager.isBackStackEmpty());

    }

    /**
     * 启动Fragment，这样启动的Fragment由这个Fragment所在的Activity管理，会加入backStack中
     *
     * @param fragment fragment
     */
    public final void launchFragment(AbstractFragment fragment) {
        fragment.getFragmentBackHelper().setFragmentBackStackManager(mFragmentBackStackManager);
        mFragmentBackStackManager.launchFragment(fragment);
    }

    /**
     * 退出顶层Fragment
     */
    public final void finishTopFragment() {
        AbstractFragment topFragment = getTopFragment();
        if (mFragmentBackStackManager != null && topFragment != null) {
            mFragmentBackStackManager.destroyFragment(topFragment);
        }
    }

    /**
     * 获取顶Fragment
     *
     * @return 顶Fragment
     */
    public AbstractFragment getTopFragment() {
        return mFragmentBackStackManager != null ? mFragmentBackStackManager.getTopFragment() : null;
    }

    /**
     * 获取Fragment栈管理器
     *
     * @return Fragment栈管理器
     */
    public FragmentBackStackManager getFragmentBackStackManager() {
        return mFragmentBackStackManager;
    }

    /**
     * 设置最底层的Fragment，这个fragment不会被pop
     *
     * @param fragment fragment
     */
    public final void launchRootFragment(AbstractFragment fragment) {
        fragment.getFragmentBackHelper().setFragmentBackStackManager(this.mFragmentBackStackManager);
        mFragmentBackStackManager.launchRootFragment(fragment);
    }

    /**
     * 获取最底层的Fragment
     */
    public final AbstractFragment getRootFragment() {
        return mFragmentBackStackManager.getRootFragment();
    }

    private void assertFragmentBackStackManager() {
        if (mFragmentBackStackManager == null) {
            throw new IllegalArgumentException("you must call setLaunchFragmentAttr(int replaceResId, int enterAnimRes, int popExitAnimRes) first");
        }
    }
}
