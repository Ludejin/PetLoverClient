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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 扩展的GridView支持: 弹性模式、触摸渗透（如果child没有被点击则ignore事件）.
 */
public class ExtendGridView extends GridView {

    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    private int mNumColumns;

    private boolean mStretchable;
    private boolean mPenetrateTouch;

    public ExtendGridView(Context context) {
        this(context, null);
    }

    public ExtendGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExtendGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendGridView);
        mStretchable = a.getBoolean(R.styleable.ExtendGridView_stretchable, false);
        mPenetrateTouch = a.getBoolean(R.styleable.ExtendGridView_penetrateTouch, false);
        a.recycle();
    }

    @Override
    public void setHorizontalSpacing(int horizontalSpacing) {
        super.setHorizontalSpacing(horizontalSpacing);
        mHorizontalSpacing = horizontalSpacing;
    }

    /**
     * Get the horizontal spacing.
     */
    @Override
    public int getHorizontalSpacing() {
        return mHorizontalSpacing;
    }

    @Override
    public void setVerticalSpacing(int verticalSpacing) {
        super.setVerticalSpacing(verticalSpacing);
        mVerticalSpacing = verticalSpacing;
    }

    /**
     * Get the vertical spacing.
     */
    @Override
    public int getVerticalSpacing() {
        return mVerticalSpacing;
    }

    @Override
    public void setNumColumns(int numColumns) {
        super.setNumColumns(numColumns);
        mNumColumns = numColumns;
    }

    /**
     * Get the number of columns.
     */
    @SuppressLint("NewApi")
    @Override
    public int getNumColumns() {
        return mNumColumns;
    }

    /**
     * Set whether this grid view is stretchable.
     *
     * @param stretchable true if stretchable.
     */
    public void setStretchable(boolean stretchable) {
        if (mStretchable != stretchable) {
            mStretchable = stretchable;
            requestLayout();
        }
    }

    /**
     * Whether this grid view is stretchable.
     */
    public boolean isStretchable() {
        return mStretchable;
    }

    /**
     * Set whether this grid view will ignore touch event (just like touch event penetrate it)
     * if none of children is touched.
     *
     * @param penetrate whether touch event will penetrate if not handled.
     */
    public void setPenetrateTouch(boolean penetrate) {
        mPenetrateTouch = penetrate;
    }

    /**
     * Whether this grid view will penetrate touch event.
     */
    public boolean isPenetrateTouch() {
        return mPenetrateTouch;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final boolean stretchable = mStretchable;
        if (stretchable) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // final int action = ev.getAction();
        final int x = (int) ev.getX();
        final int y = (int) ev.getY();
        int motionPosition = pointToPosition(x, y);
        if (mPenetrateTouch && motionPosition < 0) {
            return false;
        }
        return super.onTouchEvent(ev);
    }
}
