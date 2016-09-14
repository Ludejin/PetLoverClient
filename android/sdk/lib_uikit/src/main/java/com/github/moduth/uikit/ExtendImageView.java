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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.ImageView;

/**
 * 扩展的ImageView，提高了性能并支持额外功能如foreground、animation等.
 */
public class ExtendImageView extends ImageView {

    private final static ThreadLocal<Rect> sLocalTmpRect = new ThreadLocal<Rect>() {
        @Override
        protected Rect initialValue() {
            return new Rect();
        }
    };

    // ------------------ performance -------------------
    private boolean mMeasuredExactly = false;
    private boolean mBlockMeasurement = false;

    //-------------------- enhance ----------------------
    private boolean mIgnoreContentBounds = false;
    private boolean mAdjustViewBounds = false;

    // ------------------- foreground -------------------
    private int mForegroundResource = 0;
    private ViewForeground mForeground = new ViewForeground(this, null);

    public ExtendImageView(Context context) {
        super(context);
    }

    public ExtendImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        setImageBitmapInternal(bm);
    }

    private void setImageBitmapInternal(Bitmap bm) {
        mBlockMeasurement = true;
        super.setImageBitmap(bm);
        mBlockMeasurement = false;
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        setImageDrawableInternal(drawable);
    }

    private void setImageDrawableInternal(Drawable drawable) {
        mBlockMeasurement = true;
        super.setImageDrawable(drawable);
        mBlockMeasurement = false;
    }

    @Override
    public void setImageResource(int resId) {
        setImageResourceInternal(resId);
    }

    private void setImageResourceInternal(int resId) {
        mBlockMeasurement = true;
        super.setImageResource(resId);
        mBlockMeasurement = false;
    }

    @Override
    public void setImageURI(Uri uri) {
        setImageURIInternal(uri);
    }

    private void setImageURIInternal(Uri uri) {
        mBlockMeasurement = true;
        super.setImageURI(uri);
        mBlockMeasurement = false;
    }

    /**
     * Sets a Bitmap as the content of this ImageView with in and out animation.
     *
     * @param bm  The bitmap to set
     * @param in  In animation for new content.
     * @param out Out animation for previous content.
     */
    public void setImageBitmap(Bitmap bm, Animation in, Animation out) {
        if (out != null) {
            final Bitmap fbm = bm;
            final Animation fin = in;
            scheduleAnimation(out, new Runnable() {
                @Override
                public void run() {
                    setImageBitmapInternal(fbm);
                    if (fin != null) scheduleAnimation(fin, null);
                }
            });
            return;
        }
        setImageBitmapInternal(bm);
        if (in != null) scheduleAnimation(in, null);
    }

    /**
     * Sets a drawable as the content of this ImageView with in and out animation.
     *
     * @param drawable The drawable to set.
     * @param in       In animation for new content.
     * @param out      Out animation for previous content.
     */
    public void setImageDrawable(Drawable drawable, Animation in, Animation out) {
        if (out != null) {
            final Drawable fdrawable = drawable;
            final Animation fin = in;
            scheduleAnimation(out, new Runnable() {
                @Override
                public void run() {
                    setImageDrawableInternal(fdrawable);
                    if (fin != null) scheduleAnimation(fin, null);
                }
            });
            return;
        }
        setImageDrawableInternal(drawable);
        if (in != null) scheduleAnimation(in, null);
    }

    /**
     * Sets a drawable as the content of this ImageView with in and out animation.
     *
     * @param resId The drawable resource id.
     * @param in    In animation for new content.
     * @param out   Out animation for previous content.
     */
    public void setImageResource(int resId, Animation in, Animation out) {
        if (out != null) {
            final int fresid = resId;
            final Animation fin = in;
            scheduleAnimation(out, new Runnable() {
                @Override
                public void run() {
                    setImageResourceInternal(fresid);
                    if (fin != null) scheduleAnimation(fin, null);
                }
            });
            return;
        }
        setImageResourceInternal(resId);
        if (in != null) scheduleAnimation(in, null);
    }

    /**
     * Sets the content of this ImageView to the specified Uri with in and out animation.
     *
     * @param uri The Uri of an image.
     * @param in  In animation for new content.
     * @param out Out animation for previous content.
     */
    public void setImageURI(Uri uri, Animation in, Animation out) {
        if (out != null) {
            final Uri furi = uri;
            final Animation fin = in;
            scheduleAnimation(out, new Runnable() {
                @Override
                public void run() {
                    setImageURIInternal(furi);
                    if (fin != null) scheduleAnimation(fin, null);
                }
            });
            return;
        }
        setImageURIInternal(uri);
        if (in != null) scheduleAnimation(in, null);
    }

    @Override
    public void setBackgroundDrawable(Drawable drawable) {
        // background's padding will affect view's, if there's one.
        mBlockMeasurement = !isBackgroundHasPadding(getBackground(), drawable);
        super.setBackgroundDrawable(drawable);
        mBlockMeasurement = false;
    }

    @Override
    public void setBackgroundResource(int resId) {
        mBlockMeasurement = true;
        super.setBackgroundResource(resId);
        mBlockMeasurement = false;
    }

    @Override
    public void setBackgroundColor(int color) {
        mBlockMeasurement = true;
        super.setBackgroundColor(color);
        mBlockMeasurement = false;
    }

    private boolean isBackgroundHasPadding(Drawable oldDrawable, Drawable newDrawable) {
        Rect rect = sLocalTmpRect.get();
        boolean hasPadding = oldDrawable != null && oldDrawable.getPadding(rect);
        if (!hasPadding) {
            hasPadding = newDrawable != null && newDrawable.getPadding(rect);
        }
        return hasPadding;
    }

    @Override
    public void requestLayout() {
        if (mBlockMeasurement && mMeasuredExactly) {
            // Ignore request
        } else {
            super.requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMeasuredExactly = isMeasuredExactly(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mIgnoreContentBounds && !mAdjustViewBounds) {
            setMeasuredDimension(getDefaultSize(getMeasuredWidth(), widthMeasureSpec),
                    getDefaultSize(getMeasuredHeight(), heightMeasureSpec));
        }
    }

    private boolean isMeasuredExactly(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMeasureSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMeasureSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        return widthMeasureSpecMode == MeasureSpec.EXACTLY && heightMeasureSpecMode == MeasureSpec.EXACTLY;
    }

    /**
     * Set the foreground of this image view.
     *
     * @param resId foreground res id.
     */
    public void setForeground(int resId) {
        if (resId != 0 && resId == mForegroundResource) {
            return;
        }
        mForegroundResource = resId;
        setForegroundInternal(resId != 0 ? getResources().getDrawable(resId) : null);
    }

    /**
     * Set the foreground of this image view.
     *
     * @param foregroundDrawable foreground drawable.
     */
    public void setForeground(Drawable foregroundDrawable) {
        if (foregroundDrawable == mForeground.getDrawable()) {
            return;
        }
        mForegroundResource = 0;
        setForegroundInternal(foregroundDrawable);
    }

    private void setForegroundInternal(Drawable drawable) {
        mBlockMeasurement = true;
        mForeground.setDrawable(drawable);
        mBlockMeasurement = false;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        ViewForeground foreground = mForeground;
        if (foreground != null) foreground.dispatchBoundsChanged();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ViewForeground foreground = mForeground;
        if (foreground != null) foreground.dispatchBoundsChanged();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        ViewForeground foreground = mForeground;
        if (foreground != null) foreground.dispatchDrawableStateChanged();
    }

    @Override
    protected boolean verifyDrawable(Drawable dr) {
        ViewForeground foreground = mForeground;
        Drawable foregroundDrawable = foreground == null ? null : foreground.getDrawable();
        return foregroundDrawable == dr || super.verifyDrawable(dr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // foreground
        ViewForeground foreground = mForeground;
        if (foreground != null) foreground.dispatchDraw(canvas);
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        super.setAdjustViewBounds(adjustViewBounds);
        mAdjustViewBounds = adjustViewBounds;
    }

    /**
     * True when ImageView is adjusting its bounds
     * to preserve the aspect ratio of its drawable
     *
     * @return whether to adjust the bounds of this view
     * to preserve the original aspect ratio of the drawable
     * @see #setAdjustViewBounds(boolean)
     */
    public boolean getAdjustViewBounds() {
        return mAdjustViewBounds;
    }

    /**
     * Set whether ignore content(drawable) bounds when perform measure.
     * When ignore, this view will try to use the size from measure spec rather than it's content(drawable).
     * Notice, this should work with {@link #setAdjustViewBounds(boolean false)}.
     *
     * @param ignore whether ignore content(drawable) bounds when perform measure.
     */
    public void setIgnoreContentBounds(boolean ignore) {
        if (mIgnoreContentBounds != ignore) {
            mIgnoreContentBounds = ignore;
            requestLayout();
        }
    }

    private void scheduleAnimation(final Animation animation, final Runnable postRunnable) {
        if (animation == null) {
            if (postRunnable != null) {
                postRunnable.run();
            }
            return;
        }
        clearAnimation();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (postRunnable != null) {
                    postRunnable.run();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        startAnimation(animation);
    }
}
