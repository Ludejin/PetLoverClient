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
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Markable ImageView
 *
 * @author markzhai on 2016/3/11
 */
public class MarkImageView extends ExtendImageView implements Markable {

    private Marker mMarker;

    public MarkImageView(Context context) {
        super(context);
        init(null);
    }

    public MarkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MarkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mMarker = new Marker(this, attrs);
    }

    public void setMarkerVisible(boolean visible) {
        mMarker.setMarkerVisible(visible);
    }

    public void setMarkerVisibleWhenSelected(boolean visible) {
        mMarker.setMarkerVisibleWhenSelected(visible);
    }

    public boolean isMarkerVisible() {
        return mMarker.isMarkerVisible();
    }

    public void setMarker(int resId) {
        mMarker.setMarker(resId);
    }

    @Override
    public void setMarker1(int resId) {
        mMarker.setMarker1(resId);
    }

    @Override
    public void setMarker2(int resId) {
        mMarker.setMarker2(resId);
    }

    @Override
    public void setMarker3(int resId) {
        mMarker.setMarker3(resId);
    }

    @Override
    public void setMarkerAutoPadding(boolean enable) {
        mMarker.setMarkerAutoPadding(enable);
    }

    public void setMarker(Drawable marker) {
        mMarker.setMarker(marker);
    }

    @Override
    public void setMarker1(Drawable marker) {
        mMarker.setMarker1(marker);
    }

    @Override
    public void setMarker2(Drawable marker) {
        mMarker.setMarker2(marker);
    }

    @Override
    public void setMarker3(Drawable marker) {
        mMarker.setMarker3(marker);
    }

    public void setMarkerPosition(int position) {
        mMarker.setMarkerPosition(position);
    }

    @Override
    public void setMarkerType(int markerType) {
        mMarker.setMarkerType(markerType);
    }

    @Override
    public void setMarkerCount(int count) {
        mMarker.setMarkerCount(count);
    }

    @Override
    public void setMarkerText(String text) {
        mMarker.setMarkerText(text);
    }

    @Override
    public void setMarkerTextSize(int textSize) {
        mMarker.setMarkerTextSize(textSize);
    }

    @Override
    public void setMarkerTextColor(int textColor) {
        mMarker.setMarkerTextColor(textColor);
    }

    public void setMarkerPaddingOffset(int xOffset, int yOffset) {
        mMarker.setMarkerPaddingOffset(xOffset, yOffset);
    }

    public void setMarkerSize(int width, int height) {
        mMarker.setMarkerSize(width, height);
    }

    @Override
    public void getMarkerSize(int[] size) {
        mMarker.getMarkerSize(size);
    }

    public void setOnMarkerClickListener(Markable.OnMarkerClickListener listener) {
        mMarker.setOnMarkerClickListener(listener);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mMarker.updateMarkerLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mMarker.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mMarker.onTouchEvent(event) || super.onTouchEvent(event);
    }
}
