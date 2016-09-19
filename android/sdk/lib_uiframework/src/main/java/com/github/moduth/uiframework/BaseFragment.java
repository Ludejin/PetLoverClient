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

package com.github.moduth.uiframework;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.github.moduth.ext.Ext;
import com.github.moduth.ext.utils.KeyBoardUtils;
import com.github.moduth.ext.utils.SdkVersionUtils;
import com.github.moduth.uiframework.navigator.backstack.AbstractFragment;
import com.github.moduth.uiframework.navigator.backstack.FragmentBackStackManager;
import com.github.moduth.uikit.LayoutInflaterProvider;

import java.util.List;

/**
 * Base {@link android.app.Fragment} class for every fragment in this application.
 */
public abstract class BaseFragment extends AbstractFragment implements LayoutInflaterProvider, ILoadFinished {

    private enum LoadState {
        NONE,
        DOING_ANIM,
        WAITING_PARENT,
        FINISHED
    }

    private static final int ANIMATION_END_DELAY = 100; //ms
    private LoadState mLoadState = LoadState.NONE;

    private View mBusinessView;

    private static Drawable sGlobalBackgroundDrawable;

    /**
     * @return 全局背景图
     */
    public static Drawable getGlobalBackgroundDrawable() {
        return sGlobalBackgroundDrawable;
    }

    /**
     * 设置全局背景图,仅仅记录,新fragment打开的时候自动应用
     *
     * @param globalBackgroundDrawable 全局背景图
     */
    public static void setGlobalBackgroundDrawable(Drawable globalBackgroundDrawable) {
        sGlobalBackgroundDrawable = globalBackgroundDrawable;
    }

    @Override
    public void onDestroy() {
        getFragmentBackHelper().clearChildFragmentBackStackManager();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        setViewBackgroundDrawable(mBusinessView != null ? mBusinessView : getView(), null);
        super.onDetach();
        mBusinessView = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLoadState == LoadState.NONE && canLoadDataWhenResume()) {
            checkLoadFinished();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (sGlobalBackgroundDrawable != null) {
            setBackgroundDrawable(sGlobalBackgroundDrawable);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Shows a {@link Toast} message.
     *
     * @param message An string representing a message to be shown.
     */
    protected void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置一个放业务主体内容的view,用来统一处理背景等
     *
     * @param businessView businessView
     */
    protected void setBusinessView(View businessView) {
        mBusinessView = businessView;
    }

    /**
     * 设置一个背景图,为节省内存,此图应该各UI页面共用
     *
     * @param drawable 背景大图
     */
    public void setBackgroundDrawable(Drawable drawable) {
        if (mBusinessView != null) {
            setViewBackgroundDrawable(mBusinessView, drawable);
        }
    }

    /**
     * 给某个view设置一个background
     *
     * @param view     view
     * @param drawable drawable
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setViewBackgroundDrawable(View view, Drawable drawable) {
        if (view != null) {
            if (SdkVersionUtils.hasJellyBean()) {
                view.setBackground(drawable);
            } else {
                view.setBackgroundDrawable(drawable);
            }
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            try {
                if (nextAnim != 0) {
                    Animation anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        public void onAnimationStart(Animation animation) {
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationEnd(Animation animation) {
                            new Handler().postDelayed(() -> {
                                if (mLoadState == LoadState.DOING_ANIM) {
                                    BaseFragment.this.checkLoadFinished();
                                }
                            }, ANIMATION_END_DELAY);
                        }
                    });
                    mLoadState = LoadState.DOING_ANIM;
                    return anim;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else {
            onExitAnimationStart();
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public boolean isLoadFinished() {
        return mLoadState == LoadState.FINISHED;
    }

    @Override
    public void onLoadFinished() {

    }

    protected boolean canLoadDataWhenResume() {
        return true;
    }

    /**
     * 退出动画开始
     */
    protected void onExitAnimationStart() {

    }

    private void onParentFragmentLaunchFinished() {
        if (mLoadState == LoadState.WAITING_PARENT) {
            checkLoadFinished();
        }
    }

    protected void checkLoadFinished() {
        if (isResumed() && getUserVisibleHint()) {
            BaseFragment parentFragment = (BaseFragment) getParentFragment();
            if (parentFragment == null || parentFragment.isLoadFinished()) {
                if (getFragmentBackHelper().getParentFragmentBackStackManager() != null) {
                    getFragmentBackHelper().getParentFragmentBackStackManager().hidePreviousFragment(this);
                }
                mLoadState = LoadState.FINISHED;

                BaseFragment.this.onLoadFinished();

                List<Fragment> fragments = getChildFragmentManager().getFragments();
                if (fragments != null) {
                    for (Fragment fragment : fragments) {
                        if (fragment instanceof BaseFragment) {
                            ((BaseFragment) fragment).onParentFragmentLaunchFinished();
                        }
                    }
                }
            } else {
                mLoadState = LoadState.WAITING_PARENT;
            }
        }
    }

    @Override
    public LayoutInflater requestLayoutInflater() {
        return getActivity().getLayoutInflater();
    }

    @Override
    public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        return getActivity().getLayoutInflater();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mLoadState == LoadState.NONE) {
            checkLoadFinished();
        }
    }

    public void hideInputMethod() {
        Activity activity = getActivity();
        if (activity != null) {
            KeyBoardUtils.hideInputMethod(activity, activity.getCurrentFocus());
        }
    }

    public void showInputMethod(EditText editText) {
        Activity activity = getActivity();
        KeyBoardUtils.showInputMethod(activity, editText);
    }

    public boolean isTopFragment() {
        FragmentBackStackManager stackManager = getFragmentBackHelper().getParentFragmentBackStackManager();
        return null != stackManager && stackManager.getTopFragment() == this;
    }

    public String getMrString(int resId) {
        return Ext.getContext().getString(resId);
    }
}
