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
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理fragment回退栈逻辑<br>
 * 该类有别于系统fragment栈管理机制：<br><p></p>
 * 1-系统fragment栈管理机制只有fragment回退逻辑，没有对fragment的生命周期进行管理（生命周期管理委托给了Activity），所有栈内的fragment
 * 都处于显示状态，如果栈里面有很多fragment，会增大系统的绘制压力，影响程序流畅度，影响用户体验。<br><p></p>
 * 2-该类参考系统Activity栈管理机制，除了有fragment回退逻辑外，增加对fragment生命周期管理逻辑(显示/隐藏控制)，栈顶的fragment处于显示状态，
 * 可以绘制，非栈顶的fragment处于隐藏状态，界面刷新时不重绘,大大减轻了系统的绘制压力，让程序更流畅。
 *
 * @author markzhai on 16/2/29
 * @version 1.0.0
 */
public class FragmentBackStackManager {
    private int mContainerViewRes;
    private int mEnterAnimRes;
    private int mPopExitAnimRes;
    private FragmentManager mAttachFragmentManager;
    /**
     * 根Fragment：不会被pop。对于FragmentActivity和Fragment，可以拥有RootFragment；
     * RootFragment的返回操作，与父FragmentActivity或父Fragment一同返回
     */
    private AbstractFragment mRootFragment;
    private List<AbstractFragment> mFragmentList = new ArrayList<>();

    public FragmentBackStackManager(FragmentManager attachFragmentManager) {
        mAttachFragmentManager = attachFragmentManager;
    }

    /**
     * 设置弹出Fragment的默认属性
     *
     * @param containerViewRes 容器View id
     * @param enterAnimRes     进入动画
     * @param popExitAnimRes   退出动画
     */
    public void setLaunchFragmentAttr(int containerViewRes, int enterAnimRes, int popExitAnimRes) {
        mContainerViewRes = containerViewRes;
        mEnterAnimRes = enterAnimRes;
        mPopExitAnimRes = popExitAnimRes;
    }

    /**
     * 弹出新的Fragment
     *
     * @param fragment       待弹出的Fragment
     * @param containId      容器View id
     * @param enterAnimRes   进入动画
     * @param popExitAnimRes 退出动画
     */
    public void launchFragment(AbstractFragment fragment, int containId, int enterAnimRes, int popExitAnimRes) {

        handleClearTopMode();

        // 检查是否为已经按照SingleTop模式处理了Fragment弹出
        if (handleSingleTopMode(fragment)) {
            return;
        }

        handleTopFragment();

        fragment.onNewResume();
        mFragmentList.add(fragment);

        if (containId <= 0) {
            containId = mContainerViewRes;
        }
        mAttachFragmentManager.beginTransaction().setCustomAnimations(enterAnimRes, 0, 0, popExitAnimRes)
                .add(containId, fragment).addToBackStack(null)
                .commitAllowingStateLoss();
    }

    /**
     * 弹出新的Fragment
     *
     * @param fragment 待弹出的Fragment
     */
    public void launchFragment(AbstractFragment fragment) {
        launchFragment(fragment, mContainerViewRes, mEnterAnimRes, mPopExitAnimRes);
    }

    /**
     * 隐藏新启动Fragment下面的Fragment
     *
     * @param fragment 弹出的Fragment
     */
    public void hidePreviousFragment(AbstractFragment fragment) {
        if (fragment != null && (fragment.isHiddenPreviousFragment()) && getTopFragment() == fragment) {
            final AbstractFragment previousFragment = getPreviousFragment();
            if (previousFragment != null && previousFragment.getView() != null) {
                previousFragment.getView().setVisibility(View.GONE);
            }
        }
    }

    /**
     * Fragment堆栈是否为空
     *
     * @return true或false
     */
    public boolean isBackStackEmpty() {
        return mFragmentList.isEmpty();
    }

    /**
     * 清除Fragment堆栈
     */
    public void clear() {
        mRootFragment = null;
        mFragmentList.clear();
    }

    /**
     * 获取顶端的Fragment
     *
     * @return 顶端的Fragment
     */
    public AbstractFragment getTopFragment() {
        if (mFragmentList.isEmpty()) {
            return mRootFragment;
        }
        return mFragmentList.get(mFragmentList.size() - 1);
    }

    /**
     * 弹出根Fragment
     *
     * @param fragment 根Fragment
     */
    public void launchRootFragment(AbstractFragment fragment) {
        if (mContainerViewRes == 0) {
            throw new IllegalArgumentException("you must call setLaunchFragmentAttr(int containerViewRes, int enterAnimRes, int popExitAnimRes) first");
        }
        fragment.onNewResume();
        mRootFragment = fragment;
        mAttachFragmentManager.beginTransaction().add(mContainerViewRes, fragment).commitAllowingStateLoss();
    }

    /**
     * 获取根Fragment
     *
     * @return 根Fragment
     */
    public AbstractFragment getRootFragment() {
        return mRootFragment;
    }

    /**
     * 获取后台fragment堆栈
     *
     * @return fragment堆栈列表
     */
    public List<AbstractFragment> getFragmentList() {
        return mFragmentList;
    }

    /**
     * 如果Fragment，包含子Fragment，那么可以将其中一个子Fragment设置为子根Fragment
     *
     * @param fragment 子根Fragment
     */
    public void setSubRootFragment(AbstractFragment fragment) {
        mRootFragment = fragment;
    }

    /**
     * 销毁Fragment
     *
     * @param fragment 需要销毁的Fragment
     */
    public void destroyFragment(AbstractFragment fragment) {
        // 对于根Fragment，是不允许被销毁的
        if (isRootFragment(fragment)) {
            throw new IllegalArgumentException("Primary fragment must not be finish");
        }

        // 仅仅在栈顶的Fragment才可以被销毁
        try {
            if (mFragmentList.size() > 0 && mFragmentList.get(mFragmentList.size() - 1) == fragment) {
                // 销毁顶部的Fragment
                mAttachFragmentManager.popBackStack();
                AbstractFragment popFragment = pop();
                if (popFragment != null) {
                    popFragment.onNewPause();
                }
                // 次顶部Fragment显示
                AbstractFragment curFragment = getTopFragment();
                if (curFragment != null) {
                    curFragment.onNewResume();
                    if (curFragment.mRequestCode >= 0 && popFragment != null) {
                        curFragment.onActivityResult(curFragment.mRequestCode, popFragment.mResultCode, popFragment.mResultData);
                        curFragment.mRequestCode = -1;
                        popFragment.mResultCode = Activity.RESULT_CANCELED;
                        popFragment.mResultData = null;
                    }
                    View view = curFragment.getView();
                    if (null != view) {
                        view.setVisibility(View.VISIBLE);
                    }
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示顶Fragment的前一个Fragment ? 那顶Fragment呢？
     */
    public final void showPreviousFragment() {
        AbstractFragment previousFragment = getPreviousFragment();
        if (previousFragment != null && previousFragment.getView() != null) {
            previousFragment.getView().setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏顶Fragment的前一个Fragment
     */
    public final void hidePreviousFragment() {
        AbstractFragment previousFragment = getPreviousFragment();
        if (previousFragment != null && previousFragment.getView() != null) {
            previousFragment.getView().setVisibility(View.GONE);
        }
    }

    private AbstractFragment pop() {
        AbstractFragment fragment = mFragmentList.get(mFragmentList.size() - 1);
        mFragmentList.remove(fragment);
        return fragment;
    }

    public boolean isRootFragment(AbstractFragment fragment) {
        return mRootFragment == fragment;
    }

    private AbstractFragment getPreviousFragment() {
        int size = mFragmentList.size();
        if (size > 1) {
            return mFragmentList.get(size - 2);
        } else if (size == 1) {
            return mRootFragment;
        }
        return null;
    }

    protected FragmentManager getAttachFragmentManager() {
        return mAttachFragmentManager;
    }

    private void handleTopFragment() {
        AbstractFragment topFragment = getTopFragment();
        if (topFragment != null) {
            topFragment.onNewPause();
        }
    }

    private void handleClearTopMode() {
        AbstractFragment topFragment = getTopFragment();
        if (topFragment != null) {
            // 如果为FRAGMENT_LAUNCH_CLEAR_TOP模式，那么先清理TopFragment
            if (topFragment.isClearTop()) {
                destroyFragment(topFragment);
            }
        }
    }

    /**
     * 处理SingleTop模式：如果支持SingleTop模式，并且TopFragment与Fragment为同一个类，那么直接复用TopFragment，并将Fragment的参数传入TopFragment
     *
     * @param fragment 待启动的Fragment
     * @return 是否已经处理Fragment启动
     */
    private boolean handleSingleTopMode(AbstractFragment fragment) {
        AbstractFragment top = getTopFragment();
        if (top == null) {
            return false;
        }

        if (fragment.isSingleTop() && fragment.getClass() == top.getClass()) {
            if (top.getActivity() != null) {
                top.onNewBundle(fragment.getArguments());
            }
            fragment.finish();
            return true;
        }
        return false;
    }
}
