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

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * A drawable container with specified size.
 */
public class SpecifiedDrawable extends DrawableDecorator {

    private SpecificState mState;

    /**
     * Construct a SpecifiedDrawable with no specified size.
     *
     * @param drawable Drawable to contain.
     */
    public SpecifiedDrawable(Drawable drawable) {
        this(drawable, -1, -1);
    }

    /**
     * Construct a SpecifiedDrawable with corresponding specified size.
     *
     * @param drawable Drawable to contain.
     * @param width    Width of this container.
     * @param height   Height of this container.
     */
    public SpecifiedDrawable(Drawable drawable, int width, int height) {
        mState = new SpecificState(null, this, null);
        mState.setDrawable(drawable);
        mState.mWidth = width;
        mState.mHeight = height;
        setConstantState(mState);
    }

    /**
     * Resize this drawable container.
     *
     * @param width  Width of this container.
     * @param height Height of this container.
     */
    public void resize(int width, int height) {
        if (mState.mWidth != width || mState.mHeight != height) {
            mState.mWidth = width;
            mState.mHeight = height;
            invalidateSelf();
        }
    }

    @Override
    public int getIntrinsicWidth() {
        int width = mState.mWidth;
        return width > 0 ? width : super.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        int height = mState.mHeight;
        return height > 0 ? height : super.getIntrinsicHeight();
    }

    /**
     * Get the original width of this drawable (or child drawable to contain).
     *
     * @return Original width.
     */
    public int getOrginalWidth() {
        return super.getIntrinsicWidth();
    }

    /**
     * Get the original height of this drawable (or child drawable to contain).
     *
     * @return Original height.
     */
    public int getOriginalHeight() {
        return super.getIntrinsicHeight();
    }

    static class SpecificState extends DrawableDecoratorState {

        int mWidth, mHeight;

        SpecificState(SpecificState orig, DrawableDecorator owner, Resources res) {
            super(orig, owner, res);
            if (orig != null) {
                mWidth = orig.mWidth;
                mHeight = orig.mHeight;
            }
        }

        @Override
        public Drawable newDrawable() {
            return new SpecifiedDrawable(this, null);
        }
    }

    private SpecifiedDrawable(SpecificState state, Resources res) {
        mState = new SpecificState(state, this, res);
        setConstantState(mState);
    }
}
