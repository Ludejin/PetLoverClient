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
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import java.util.HashSet;

/**
 * 扩展的ScrollView，支持scrollable child.
 */
public class ExtendScrollView extends ScrollView {

    private HashSet<Integer> mScrollableChildren;

    private Rect mTmpRect = new Rect();

    public ExtendScrollView(Context context) {
        super(context);
    }

    public ExtendScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Add ru.noties.ru.noties.scrollable child which can be scrolled within this ScrollView.
     *
     * @param childId id of child.
     */
    public void addScrollableChild(int childId) {
        if (childId == 0) {
            return;
        }
        if (mScrollableChildren == null) {
            mScrollableChildren = new HashSet<Integer>();
        }
        mScrollableChildren.add(childId);
    }

    /**
     * Remove ru.noties.ru.noties.scrollable child previously added through {@link #addScrollableChild(int)}.
     *
     * @param childId id of child.
     */
    public void removeScrollableChild(int childId) {
        if (mScrollableChildren != null) {
            mScrollableChildren.remove(childId);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (shouldDeliverToChild(event)) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

    private boolean shouldDeliverToChild(MotionEvent event) {
        if (mScrollableChildren != null) {
            Rect hitRect = mTmpRect;
            int x = (int) (event.getX() + getScrollX());
            int y = (int) (event.getY() + getScrollY());

            for (Integer id : mScrollableChildren) {
                if (id == null)
                    continue;

                View child = findViewById(id);
                if (child == null)
                    continue;

                if (child.getVisibility() != VISIBLE)
                    continue;

                child.getHitRect(hitRect);
                if (hitRect.contains(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }
}
