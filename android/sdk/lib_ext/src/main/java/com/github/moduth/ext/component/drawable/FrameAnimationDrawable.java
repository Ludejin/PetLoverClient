/*
 * The GPL License (GPL)
 *
 * Copyright (c) 2016 MarkZhai (http://zhaiyifan.cn)
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

package com.github.moduth.ext.component.drawable;

import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

/**
 * Frame animation drawable.
 */
public class FrameAnimationDrawable extends AnimationDrawable {

    private int mWidth = -1;
    private int mHeight = -1;
    private int mTotalDuration = 0;

    public FrameAnimationDrawable() {
        init();
    }

    public FrameAnimationDrawable(Drawable first, int duration) {
        this();
        addFrame(first, duration);
    }

    private void init() {
        setOneShot(false);
        setVisible(true, false);
    }

    private void computeSizeIfNeeded() {
        if (mWidth <= 0 || mHeight <= 0) {
            Drawable first = getFrame(0);
            if (first != null) {
                mWidth = first.getIntrinsicWidth();
                mHeight = first.getIntrinsicHeight();
            }
        }
    }

    @Override
    public void addFrame(Drawable frame, int duration) {
        super.addFrame(frame, duration);
        computeSizeIfNeeded();
        mTotalDuration += duration;
    }

    /**
     * Get the total duration of this FrameAnimationDrawable.
     *
     * @return Total duration of this FrameAnimationDrawable.
     */
    public int getTotalDuration() {
        return mTotalDuration;
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mHeight;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        start();
    }
}
