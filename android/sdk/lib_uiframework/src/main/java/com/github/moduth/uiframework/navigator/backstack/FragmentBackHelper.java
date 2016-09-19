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

import android.view.KeyEvent;

import com.github.moduth.ext.Ext;

/**
 * Fragment的 返回 帮助类
 *
 * @author markzhai on 16/2/29
 * @version 1.0.0
 */
public class FragmentBackHelper implements IFragmentBackHelper {
    private AbstractFragment mFragment;
    private FragmentBackStackManager mChildFragmentBackStackManager;
    private FragmentBackStackManager mParentFragmentBackStackManager;
    private AbstractFragment mCurrentChildFragment;

    public FragmentBackHelper(AbstractFragment fragment) {
        mFragment = fragment;
    }

    /**
     * 清理Fragment堆栈及RootFragment
     */
    public void clearChildFragmentBackStackManager() {
        if (mChildFragmentBackStackManager != null) {
            mChildFragmentBackStackManager.clear();
        }
    }

    /**
     * 设置弹出fragment的属性
     *
     * @param containerViewRes containerViewRes
     * @param enterAnimRes     enterAnimRes
     * @param popExitAnimRes   popExitAnimRes
     */
    public final void setLaunchChildFragmentAttr(int containerViewRes, int enterAnimRes, int popExitAnimRes) {
        if (mChildFragmentBackStackManager == null || mChildFragmentBackStackManager.getAttachFragmentManager() != mFragment.getChildFragmentManager()) {
            mChildFragmentBackStackManager = new FragmentBackStackManager(mFragment.getChildFragmentManager());
        }
        mChildFragmentBackStackManager.setLaunchFragmentAttr(containerViewRes, enterAnimRes, popExitAnimRes);
    }

    /**
     * 启动子页面，这样启动的Fragment由这个Fragment管理，会加入backStack中
     *
     * @param childFragment childFragment
     */
    public final void launchChildFragment(AbstractFragment childFragment) {
        assertChildFragmentBackStackManager();
        childFragment.getFragmentBackHelper().setFragmentBackStackManager(mChildFragmentBackStackManager);
        mChildFragmentBackStackManager.launchFragment(childFragment);
    }



    /**
     * 设置FragmentHandler
     *
     * @param fragmentBackStackManager fragmentBackStackManager
     */
    public void setFragmentBackStackManager(FragmentBackStackManager fragmentBackStackManager) {
        mParentFragmentBackStackManager = fragmentBackStackManager;
    }

    /**
     * 子Fragment的回退栈是否为空
     *
     * @return true/false
     */
    public final boolean isChildFragmentBackStackEmpty() {
        return mChildFragmentBackStackManager == null || mChildFragmentBackStackManager.isBackStackEmpty();
    }

    /**
     * 获取子Fragment管理FragmentBackStackManager
     *
     * @return FragmentBackStackManager
     */
    public final FragmentBackStackManager getChildFragmentBackStackManager() {
        return mChildFragmentBackStackManager;
    }

    /**
     * BACK事件处理。
     */
    public boolean onBackPressed() {
        boolean handled;
//        // TODO mCurrentChildFragment 逻辑待验证
//        if (mCurrentChildFragment != null) {
//            mCurrentChildFragment.getFragmentBackHelper().onBackPressed();
//        }
        if (mChildFragmentBackStackManager == null) {
            mFragment.finish();
            handled = true;
        } else if (mChildFragmentBackStackManager.isBackStackEmpty()) {
            mFragment.finish();
            handled = true;
        } else {
            handled = mChildFragmentBackStackManager.getTopFragment().getFragmentBackHelper().onBackPressed();
        }
        return handled;
    }

    /**
     * 处理KEY DOWN事件
     *
     * @param keyCode KEY编码
     * @param event   事件
     * @return true or false
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isChildFragmentBackStackEmpty() && getChildFragmentBackStackManager().getTopFragment() != null) {
            return getChildFragmentBackStackManager().getTopFragment().onKeyDown(keyCode, event);
        } else {
            return mFragment.onKeyDown(keyCode, event);
        }
    }

    /**
     * 启动Fragment，这样启动的Fragment由这个Fragment所在的Activity管理，会加入backStack中
     *
     * @param fragment fragment
     */
    public final void launchFragment(AbstractFragment fragment) {
        if (!mFragment.isAdded()) {
            return;
        }
        if (mParentFragmentBackStackManager == null) {
            mParentFragmentBackStackManager = ((AbstractActivity)mFragment.getActivity()).getFragmentBackStackManager();
        }
        fragment.getFragmentBackHelper().setFragmentBackStackManager(mParentFragmentBackStackManager);
        mParentFragmentBackStackManager.launchFragment(fragment);
    }

    /**
     * 启动Fragment，会加入到堆栈
     *
     * @param fragment       fragment
     * @param containId      添加fragment的layout id, 0代表使用默认的
     * @param enterAnimRes   进入动画
     * @param popExitAnimRes 退出动画
     * @param launchFlag     flag
     */
    public final void launchFragment(AbstractFragment fragment, int containId, int enterAnimRes, int popExitAnimRes, int launchFlag) {
        if (!mFragment.isAdded()) {
            return;
        }
        fragment.getFragmentBackHelper().setFragmentBackStackManager(mParentFragmentBackStackManager);
        fragment.setLaunchFragmentFlag(launchFlag);
        mParentFragmentBackStackManager.launchFragment(fragment, containId, enterAnimRes, popExitAnimRes);
    }

    /**
     * 获取父Fragment/或父Activity的Fragment返回栈管理器
     *
     * @return Fragment返回栈管理器
     */
    public FragmentBackStackManager getParentFragmentBackStackManager() {
        return mParentFragmentBackStackManager;
    }

    /**
     * 显示之前一个fragment
     */
    public final void showPreviousFragment() {
        if (null != mParentFragmentBackStackManager) {
            mParentFragmentBackStackManager.showPreviousFragment();
        }
    }

    /**
     * 隐藏之前一个fragment
     */
    public final void hidePreviousFragment() {
        if (null != mParentFragmentBackStackManager) {
            mParentFragmentBackStackManager.hidePreviousFragment();
        }
    }

    private void assertChildFragmentBackStackManager() {
        if (Ext.g().isDebuggable()) {
            if (mChildFragmentBackStackManager == null) {
                throw new IllegalStateException("you must call setLaunchChildFragmentAttr(" +
                        "int containerViewRes, int enterAnimRes, int popExitAnimRes)" +
                        " or attachChildFragmentBackStackManager(AbstractFragment fragment) first");
            }
        }
    }
}
