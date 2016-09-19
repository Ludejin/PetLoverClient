/*
 * The GPL License (GPL)
 *
 * Copyright (c) 2016 MarkZhai (http://zhaiyifan.cn/)
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

package com.github.moduth.uiframework.callback;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Fragment生命周期
 * <p/>
 * Created by zhaiyifan on 2015/7/31.
 */
public interface IFragmentLifecycleCallback {
    void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks callback);

    void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks callback);

    void registerFragmentViewCallbacks(FragmentViewCallbacks callback);

    void unregisterFragmentViewCallbacks(FragmentViewCallbacks callback);

    // fragment lifecycle
    interface FragmentLifecycleCallbacks {
        void onFragmentAttached(Fragment fragment, Activity activity);

        void onFragmentCreated(Fragment fragment, Bundle savedInstanceState);

        void onFragmentStarted(Fragment fragment);

        void onFragmentResumed(Fragment fragment);

        void onFragmentPaused(Fragment fragment);

        void onFragmentStopped(Fragment fragment);

        void onFragmentSaveInstanceState(Fragment fragment, Bundle outState);

        void onFragmentDestroyed(Fragment fragment);

        void onFragmentDetached(Fragment fragment);
    }

    interface FragmentViewCallbacks {
        void onFragmentViewCreated(Fragment fragment, View view, Bundle savedInstanceState);
    }
}
