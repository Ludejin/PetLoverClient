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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.ShapeDrawable;

/**
 * A drawable container which supports xfermode.
 */
public class XfermodeDrawable extends DrawableDecorator {

    private XfermodeState mState;

    /**
     * Construct a XfermodeDrawable with null xfermode to apply.
     *
     * @param drawable Drawable to contain.
     */
    public XfermodeDrawable(Drawable drawable) {
        this(drawable, null);
    }

    /**
     * Construct a XfermodeDrawable with corresponding xfermode to apply.
     *
     * @param drawable Drawable to contain.
     * @param xfermode Xfermode to apply.
     */
    public XfermodeDrawable(Drawable drawable, Xfermode xfermode) {
        if (!support(drawable)) {
            throw new RuntimeException("No xfermode support for " + drawable.getClass().getSimpleName());
        }
        mState = new XfermodeState(null, this, null);
        mState.mXfermode = xfermode;
        mState.setDrawable(drawable);
        setConstantState(mState);
    }

    /**
     * Set the xfermode apply to this drawable container.
     *
     * @param xfermode Xfermode to apply.
     */
    public void setXfermode(Xfermode xfermode) {
        if (mState.mXfermode != xfermode) {
            mState.mXfermode = xfermode;
            invalidateSelf();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = obtainPaint(mState.mDrawable);
        Xfermode xfermode = mState.mXfermode;
        if (paint != null && paint.getXfermode() != xfermode) {
            paint.setXfermode(xfermode);
        }

        super.draw(canvas);
    }

    public static Drawable create(Drawable drawable, Xfermode xfermode) {
        if (drawable != null && support(drawable)) {
            return new XfermodeDrawable(drawable, xfermode);
        }
        return drawable;
    }

    /**
     * Whether xfermode is supported on this kind of drawable.
     */
    public static boolean support(Drawable drawable) {
        return drawable == null || obtainPaint(drawable) != null;
    }

    private static Paint obtainPaint(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        // deny immediately.
        if (drawable instanceof XfermodeDrawable) {
            return null;
        }

        Paint paint = null;
        if (drawable instanceof BitmapDrawable) {
            paint = ((BitmapDrawable) drawable).getPaint();
        } else if (drawable instanceof NinePatchDrawable) {
            paint = ((NinePatchDrawable) drawable).getPaint();
        } else if (drawable instanceof ShapeDrawable) {
            paint = ((ShapeDrawable) drawable).getPaint();
        } else if (drawable instanceof DrawableDecorator) {
            // drawable decorator.
            paint = obtainPaint(((DrawableDecorator) drawable).getDecor());
        }
        return paint;
    }

    static class XfermodeState extends DrawableDecoratorState {

        Xfermode mXfermode;

        XfermodeState(XfermodeState orig, DrawableDecorator owner, Resources res) {
            super(orig, owner, res);
            if (orig != null) {
                mXfermode = orig.mXfermode;
            }
        }

        @Override
        public Drawable newDrawable() {
            return new XfermodeDrawable(this, null);
        }

        @Override
        public Drawable newDrawable(Resources res) {
            return new XfermodeDrawable(this, res);
        }
    }

    private XfermodeDrawable(XfermodeState state, Resources res) {
        mState = new XfermodeState(state, this, res);
        setConstantState(mState);
    }
}
