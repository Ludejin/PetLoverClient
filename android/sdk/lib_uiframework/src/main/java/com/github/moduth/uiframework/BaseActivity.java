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
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.moduth.ext.utils.SdkVersionUtils;
import com.github.moduth.uiframework.navigator.backstack.AbstractActivity;
import com.github.moduth.uikit.LayoutInflaterProvider;

/**
 * Base {@link android.app.Activity} class for every Activity in this application.
 */
public abstract class BaseActivity extends AbstractActivity implements LayoutInflaterProvider {

    private View mBusinessView;

    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment        The fragment to be added.
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.instance().onCreate(this);
    }

    /**
     * 设置一个放业务主体内容的view,用来统一处理背景等
     *
     * @param businessView businessView
     */
    protected void setBusinessView(View businessView) {
        mBusinessView = businessView;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityManager.instance().onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager.instance().onPause(this);
    }

    protected void setFullscreen(boolean fullscreen) {
        Window window = getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        if (fullscreen) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
    }

    @Override
    public LayoutInflater requestLayoutInflater() {
        return getLayoutInflater();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.instance().onDestroy(this);
        setViewBackgroundDrawable(mBusinessView != null ? mBusinessView : getWindow().getDecorView(), null);
        mBusinessView = null;
    }

    /**
     * 设置一个背景图,为节省内存,此图应该各UI页面共用
     *
     * @param drawable 背景大图
     */
    public void setBackgroundDrawable(Drawable drawable) {
        setViewBackgroundDrawable(mBusinessView != null ? mBusinessView : getWindow().getDecorView(), drawable);
    }

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
    public void finish() {
        super.finish();
        if (getExitAnimation() > 0) {
            overridePendingTransition(0, getExitAnimation());
        }
    }

    protected int getExitAnimation() {
        return 0;
    }
}
