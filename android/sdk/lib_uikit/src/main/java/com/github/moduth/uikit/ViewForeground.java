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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * View的前景
 */
public class ViewForeground {

    private Context mContext;
    private View mView;

    private Drawable mDrawable;
    private int mDrawableWidth;
    private int mDrawableHeight;

    private boolean mForegroundBoundsChanged = false;
    private boolean mForegroundInPadding = true;

    /**
     * Construct a ViewForeground for a view.
     *
     * @param view View that host this foreground.
     */
    public ViewForeground(View view) {
        mContext = view.getContext();
        mView = view;
    }

    /**
     * Construct a ViewForeground for a view.
     *
     * @param view     View that host this foreground.
     * @param drawable Drawable as content of this foreground.
     */
    public ViewForeground(View view, Drawable drawable) {
        this(view);
        setDrawable(drawable);
    }

    /**
     * Construct a ViewForeground for a view.
     *
     * @param view  View that host this foreground.
     * @param resId Drawable resource id as content of this foreground.
     */
    public ViewForeground(View view, int resId) {
        this(view);
        setDrawable(resId);
    }

    /**
     * Set the drawable as the content of this foreground.
     *
     * @param resId Drawable resource id.
     */
    public void setDrawable(int resId) {
        setDrawable(mContext.getResources().getDrawable(resId));
    }

    /**
     * Set the drawable as the content of this foreground.
     *
     * @param drawable Drawable.
     */
    public void setDrawable(Drawable drawable) {
        if (mDrawable != drawable) {
            final View view = mView;
            final int oldWidth = mDrawableWidth;
            final int oldHeight = mDrawableHeight;

            updateDrawable(drawable);

            if (oldWidth != mDrawableWidth || oldHeight != mDrawableHeight) {
                view.requestLayout();
            }
            view.invalidate();
        }
    }

    /**
     * Returns current content of this foreground.
     */
    public Drawable getDrawable() {
        return mDrawable;
    }

    /**
     * Set whether foreground should be in padding of it's host view.
     *
     * @param inPadding Whether foreground should be in padding of it's host view.
     */
    public void setForegroundInPadding(boolean inPadding) {
        if (mForegroundInPadding != inPadding) {
            mForegroundInPadding = inPadding;
            dispatchBoundsChanged();
        }
    }

    /**
     * Should be called by it's host view to indicate drawable state has changed.
     */
    public void dispatchDrawableStateChanged() {
        if (mDrawable != null && mDrawable.isStateful()) {
            mDrawable.setState(mView.getDrawableState());
        }
    }

    /**
     * Should be called by it's host view to indicate bounds has changed.
     */
    public void dispatchBoundsChanged() {
        mForegroundBoundsChanged = true;
    }

    /**
     * Should be called by it's host view to draw this foreground.
     */
    public void dispatchDraw(Canvas canvas) {
        final Drawable foreground = mDrawable;
        if (foreground != null) {
            final View view = mView;
            // handle foreground bounds change.
            if (mForegroundBoundsChanged) {
                mForegroundBoundsChanged = false;

                final int w = view.getWidth();
                final int h = view.getHeight();
                final int left, right, top, bottom;

                if (mForegroundInPadding) {
                    left = view.getPaddingLeft();
                    right = w - view.getPaddingRight();
                    top = view.getPaddingTop();
                    bottom = h - view.getPaddingBottom();
                } else {
                    left = 0;
                    right = w;
                    top = 0;
                    bottom = h;
                }
                foreground.setBounds(left, top, right, bottom);
            }
            foreground.draw(canvas);
        }
    }

    private void updateDrawable(Drawable d) {
        final View view = mView;
        if (mDrawable != null) {
            mDrawable.setCallback(null);
            view.unscheduleDrawable(mDrawable);
        }

        mDrawable = d;

        if (d != null) {
            d.setCallback(view);
            if (d.isStateful()) {
                d.setState(view.getDrawableState());
            }
            mDrawableWidth = d.getIntrinsicWidth();
            mDrawableHeight = d.getIntrinsicHeight();
        } else {
            mDrawableWidth = mDrawableHeight = -1;
        }
    }
}
