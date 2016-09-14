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

package com.github.moduth.uikit;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.RotateDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * @author markzhai on 16/2/28
 * @version 1.0.0
 */
public class DataListFooterView extends TextView {
    private RotateDrawable mDrawable;
    private ObjectAnimator mObjectAnimator;

    /**
     * construct method
     *
     * @param context 上下文环境
     */
    public DataListFooterView(Context context) {
        this(context, null);
    }

    /**
     * construct method
     *
     * @param context 上下文环境
     * @param attrs   构建属性
     */
    public DataListFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    /**
     * 构造函数
     *
     * @param context  上下文
     * @param attrs    属性
     * @param defStyle 默认风格
     */
    public DataListFooterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initViews(context);
    }

    private void initViews(Context context) {
        setPadding(0, (int) getResources().getDimension(R.dimen.footer_padding)
                , 0, (int) getResources().getDimension(R.dimen.footer_padding));

        final int textSize = 12;    //sp
        setTextSize(textSize);
        mDrawable = (RotateDrawable) context.getResources().getDrawable(R.drawable.xml_footer_refresh);
        mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
        setGravity(Gravity.CENTER);
        setVisibility(GONE);

        final int duration = 1000;
        final int maxDegree = 10000;
        mObjectAnimator = ObjectAnimator.ofInt(mDrawable, "level", 0, maxDegree);
        mObjectAnimator.setDuration(duration);
        mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator.setInterpolator(new LinearInterpolator());
    }

    /**
     * 显示刷新动画
     */
    public void showLoadingAnim() {
        setVisibility(VISIBLE);
        setText(R.string.loading);
        setCompoundDrawablesWithIntrinsicBounds(null, mDrawable, null, null);
        mObjectAnimator.start();
    }

    /**
     * 显示列表最后一项提示
     *
     * @param text 字符串
     */
    public void showLastPageText(CharSequence text) {
        setVisibility(VISIBLE);
        clearRefreshDrawable();
        setText(text);
        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    /**
     * 显示加载失败
     */
    public void showLoadFailText() {
        setVisibility(VISIBLE);
        clearRefreshDrawable();
        setText(R.string.load_failed);
        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    /**
     * 隐藏自身
     */
    public void hide() {
        setVisibility(INVISIBLE);
        clearRefreshDrawable();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearRefreshDrawable();
    }

    private void clearRefreshDrawable() {
        mObjectAnimator.cancel();
    }
}

