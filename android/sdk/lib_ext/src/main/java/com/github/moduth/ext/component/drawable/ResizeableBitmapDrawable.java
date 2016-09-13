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
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.io.InputStream;

/**
 * Bitmap drawable which can and explicitly determine and re-determine it's size.
 */
public class ResizeableBitmapDrawable extends BitmapDrawable {

    private int mWidth = -1;
    private int mHeight = -1;

    public ResizeableBitmapDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
    }

    public ResizeableBitmapDrawable(Resources res, InputStream is) {
        super(res, is);
    }

    public ResizeableBitmapDrawable(Resources res, String filepath) {
        super(res, filepath);
    }

    /**
     * Resize this drawable.
     *
     * @param width  Drawable width. <= 0 means use the Bitmap' original width.
     * @param height Drawable height. <= 0 means use the Bitmap' original width.
     */
    public void resize(int width, int height) {
        if (mWidth != width || mHeight != height) {
            mWidth = width;
            mHeight = height;
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth > 0 ? mWidth : super.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mHeight > 0 ? mHeight : super.getIntrinsicHeight();
    }
}
