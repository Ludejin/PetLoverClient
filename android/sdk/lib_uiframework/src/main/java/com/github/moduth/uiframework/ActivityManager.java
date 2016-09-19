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
import android.os.Build;
import android.os.Bundle;

import com.github.moduth.ext.component.logger.Logger;
import com.github.moduth.ext.utils.SdkVersionUtils;
import com.github.moduth.uiframework.navigator.backstack.FragmentBackStackManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author markzhai on 16/2/29
 * @version 1.0.0
 */
public class ActivityManager {

    /**
     * 保存在栈里的所有Activity
     */
    private List<Activity> mActivities = new ArrayList<Activity>();
    /**
     * 当前显示的Activity
     */
    private Activity mCurrentActivity = null;
    /**
     * 栈顶Activity
     */
    private Activity mLastActivity = null;
    /**
     * 栈顶Activity的上一个activity，解决跳转到其他应用回来以后LastActivity和CurrentActivity是一样的问题
     */
    private Activity mSavedLastActivity = null;

    private static ActivityManager sInstance;

    private static final String TAG = "ActivityManager";

    /**
     * 获取ActivityManager实例
     *
     * @return ActivityManager实例
     */
    public static ActivityManager instance() {
        if (sInstance == null) {
            sInstance = new ActivityManager();
        }
        return sInstance;
    }

    /**
     * 当Activity执行onCreate时调用 - 保存启动的Activity
     *
     * @param activity 执行onCreate的Activity
     */
    public void onCreate(Activity activity) {
        mActivities.add(activity);
    }

    /**
     * 当Activity执行onDestroy时调用 - 移除销毁的Activity
     *
     * @param activity 执行onDestroy时的Activity
     */
    public void onDestroy(Activity activity) {
        if (mLastActivity == activity) {
            mLastActivity = null;
        }

        if (mSavedLastActivity == activity) {
            mSavedLastActivity = null;
        }

        mActivities.remove(activity);
    }

    /**
     * 关闭所有activity
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void finishActivities() {
        Logger.d(TAG, "will exit app, finishActivities");
        for (Activity activity : mActivities) {
            if (SdkVersionUtils.hasJellyBean()) {
                activity.setResult(Activity.RESULT_CANCELED);
                activity.finishAffinity();
            }
            activity.finish();
        }
        mActivities.clear();
    }

    /**
     * 当Activity执行onResume时调用 - 保存当前显示的activity，更新栈顶Activity
     *
     * @param activity 执行onResume的Activity
     */
    public void onResume(Activity activity) {
//        Logger.i(TAG, "lookLeak onResume new activity is %s", activity.getClass().getSimpleName());
        mCurrentActivity = activity;

        //如果LastActivity和CurrentActivity相同，则说明是从其他应用返回来，此时LastActivity应该取前一次的Activity，否则和当前Activity是一个
        if (mLastActivity == activity) {
            mLastActivity = mSavedLastActivity;
        }
    }

    /**
     * 当Activity执行onPause时调用 - 清除当前显示的Activity
     *
     * @param activity 执行onPause的Activity
     */
    public void onPause(Activity activity) {
        mSavedLastActivity = mLastActivity;
        mLastActivity = activity;
    }

    /**
     * 获取当前显示的Activity
     *
     * @return 当前显示的Activity，可能为空
     */
    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    /**
     * 获取栈顶的Activity
     *
     * @return 栈顶的Activity
     */
    public Activity getLastActivity() {
        return mLastActivity;
    }

    /**
     * 获取所有的Activities
     *
     * @return Activities
     */
    public List<Activity> getActivities() {
        return mActivities;
    }

    /**
     * 判断某个activity是否是当前activity
     *
     * @param activity activity
     * @return 是不是当前显示的activity
     */
    public boolean isCurrentActivity(Activity activity) {
        return activity != null && activity == getCurrentActivity();
    }

    /**
     * @return 最近打开的activity信息， 名称，token，topFragment
     */
    public String dumpLastActivityInfo() {
        return getActivityInfo(mCurrentActivity);
    }

    private String getActivityInfo(Activity activity) {
        if (activity == null) {
            return "null activity has no info";
        }

        StringBuilder builder = new StringBuilder("");

        builder.append(activity.getClass().getSimpleName());

        builder.append(":{");
        try {
            builder.append(", mToken").append(getField(activity, "mToken"));
            builder.append(", isFinished=").append(activity.isFinishing());
            builder.append(", action=").append(activity.getIntent());
            builder.append(", mWindow=").append(activity.getWindow().toString());
            builder.append(", mWindowManager=").append(activity.getWindowManager());
            builder.append(", mCallingActivity=").append(activity.getCallingActivity());
            builder.append(", mWindowAdded=").append(getField(activity, "mWindowAdded"));
            builder.append(", mVisibleFromClient=").append(getField(activity, "mVisibleFromClient"));
            builder.append(", mActivityInfo=").append(getField(activity, "mActivityInfo"));


            Bundle bundle = activity.getIntent().getExtras();
            if (bundle != null) {
                Set<String> keys = bundle.keySet();
                Iterator<String> it = keys.iterator();
                builder.append(", extra=");
                while (it.hasNext()) {
                    String key = it.next();
                    builder.append(key).append(",").append(bundle.get(key)).append("&");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (activity instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) activity;
            FragmentBackStackManager manager = baseActivity.getFragmentBackStackManager();

            if (manager != null && manager.getTopFragment() != null) {
                builder.append(", topFragment=").append(manager.getTopFragment());
            }
        }
        builder.append("}\r\n");
        return builder.toString();
    }

    private String getField(Activity activity, String field) throws Exception {
        Field f = Activity.class.getDeclaredField(field);
        f.setAccessible(true);
        Object token = f.get(activity);

        if (token != null) {
            return token.toString();
        }
        return "null";
    }
}
