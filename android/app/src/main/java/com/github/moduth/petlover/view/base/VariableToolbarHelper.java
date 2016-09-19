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

package com.github.moduth.petlover.view.base;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amokie.stay.R;
import com.github.moduth.ext.utils.ResourceUtils;

import java.util.LinkedList;

import ru.noties.scrollable.OnScrollChangedListener;
import ru.noties.scrollable.ScrollableLayout;

/**
 * Created by Abner on 16/6/28.
 * Email nimengbo@gmail.com
 * GitHub https://github.com/nimengbo
 */
public class VariableToolbarHelper extends ToolbarHelper {

    // ============================================================================
    // Scroll animation
    // ============================================================================

    // store original color
    private static int ORIGIN_COLOR;

    private static int ORIGIN_ICON_COLOR;

    private static int ORIGIN_ACTION_COLOR;

    private boolean mTransparentEnabled = false;
    // start position of fading animation
    private int mStartFadePosition = 80;
    // end position of fading animation
    private int mEndFadePosition = 380;
    // Max alpha, by default 247(0.97*255)
    private int mMaxAlpha = 247;
    // fade area
    private int mFadeDuration = mEndFadePosition - mStartFadePosition;
    // text shadow when completely transparent

    // View needs to do gradual change during scroll animation
    private LinkedList<View> mFadeViewList = null;
    private ColorStateList mOriginBarTitleColor = null;
    private int mScrollY;
    protected Drawable mBackground;
    private Context mContext;
    private boolean mNeedBackgroundTrans = true;   //是否需要背景色渐变

    public VariableToolbarHelper(Toolbar toolbar) {
        super(toolbar);
        mBackground = mToolbar.getBackground();
        mContext = mToolbar.getContext();
        setTitleTextView(mToolbarTitleTextView);
        ORIGIN_COLOR = ResourceUtils.getColor(mContext, R.color.textColorPrimary);
        ORIGIN_ICON_COLOR = ResourceUtils.getColor(mContext, R.color.colorPrimary);
        ORIGIN_ACTION_COLOR = ResourceUtils.getColor(mContext, R.color.colorAction);
    }

    /**
     * Set transparent title bar anim.
     *
     * @param enabled whether to open anim
     */
    public void setTransparentEnabled(boolean enabled) {
        setTransparentEnabled(enabled, mStartFadePosition, mEndFadePosition, mMaxAlpha);
    }

    /**
     * Set transparent title bar anim.
     *
     * @param enabled whether to open anim
     * @param start   start position of fading animation
     * @param end     end position of fading animation
     */
    public void setTransparentEnabled(boolean enabled, int start, int end) {
        setTransparentEnabled(enabled, start, end, mMaxAlpha);
    }

    /**
     * Set transparent title bar anim.
     *
     * @param enabled  whether to open anim
     * @param start    start position of fading animation
     * @param end      end position of fading animation
     * @param maxAlpha max alpha(0-255)
     */
    public void setTransparentEnabled(boolean enabled, int start, int end, int maxAlpha) {
        mTransparentEnabled = enabled;
        if (mTransparentEnabled) {
            mStartFadePosition = start;
            mEndFadePosition = end;
            mMaxAlpha = maxAlpha;
            mFadeDuration = mEndFadePosition - mStartFadePosition;
        }
    }

    /**
     * Add view that needs gradual change
     */
    public void addViewToFadeList(View view) {
        if (mFadeViewList == null) {
            mFadeViewList = new LinkedList<>();
        }
        if (view != null) {
            mFadeViewList.add(view);
        }
    }

    /**
     * remove view that has been added by addViewToFadeList
     */
    public void removeViewFromFadeList(View view) {
        if (mFadeViewList != null && view != null) {
            mFadeViewList.remove(view);
        }
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title) && mToolbarTitleTextView != null) {
            mToolbarTitleTextView.setText(title);
        }
    }

    public void setTitleTextView(TextView textView) {
        mToolbarTitleTextView = textView;
        if (mToolbarTitleTextView != null) {
            mOriginBarTitleColor = mToolbarTitleTextView.getTextColors();
        }
    }

    private void setTitleBarTranslate(int alpha) {
        if (mBackground == null) {
            return;
        }
        if (mNeedBackgroundTrans) {
            mBackground.mutate().setAlpha(alpha);
            mDividerLine.getBackground().mutate().setAlpha(alpha);
        }
        if (mFadeViewList == null || mToolbarTitleTextView == null) {
            return;
        }
        if (alpha >= mMaxAlpha) {
            setTitleBarColor(ORIGIN_COLOR, ORIGIN_ICON_COLOR, ORIGIN_ACTION_COLOR);
        } else {
            int color = interpolateColor(Color.WHITE, ORIGIN_COLOR, alpha, mMaxAlpha);
            int iconColor = interpolateColor(Color.WHITE, ORIGIN_ICON_COLOR, alpha, mMaxAlpha);
            int actionColor = interpolateColor(Color.WHITE, ORIGIN_ACTION_COLOR, alpha, mMaxAlpha);
            setTitleBarColor(color, iconColor, actionColor);
        }
    }

    private void setTitleBarColor(int color, int iconColor, int actionColor) {
        for (View view : mFadeViewList) {
            if (view == mToolbarBack ||
                    view == mToolbarTitleTextView) {
                setViewColor(view, color);
            } else if (view == mToolbarFollowTextView) {
                setViewColor(view, actionColor);
            } else {
                setViewColor(view, iconColor);
            }
        }
    }

    private void setViewColor(View view, int color) {
        if (view == null || view.getVisibility() != View.VISIBLE) {
            return;
        }
        if (view instanceof TextView) {
            setViewColor((TextView) view, color);
        }
        if (view instanceof ImageView) {
            setViewColor((ImageView) view, color);
        }
    }

    private void setViewColor(TextView view, int color) {
        if (color == ORIGIN_COLOR) {
            view.setTextColor(mOriginBarTitleColor);
            if (view.getBackground() != null) {
                view.getBackground().clearColorFilter();
            }
        } else {
            view.setTextColor(color);
            if (view.getBackground() != null) {
                view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    private void setViewColor(ImageView view, int color) {
        if (color == ORIGIN_COLOR) {
            view.clearColorFilter();
        } else {
            view.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    /**
     * onScroll callback interface for ScrollView
     */
    public void onScroll(int y) {
        if (mTransparentEnabled) {
            setTitleBarTranslate(interpolate(mEndFadePosition - y));
        }
    }

    /**
     * @param delta distance to ending position
     * @return alpha value at current position
     */
    private int interpolate(int delta) {
        if (delta > mFadeDuration) {
            return 0;
        } else if (delta <= 0) {
            return mMaxAlpha;
        } else {
            float temp = ((float) delta) / mFadeDuration;
            return (int) ((1 - temp) * mMaxAlpha);
        }
    }

    public static int interpolateColor(int colorFrom, int colorTo, int posFrom, int posTo) {
        float delta = posTo - posFrom;
        int red = (int) ((Color.red(colorFrom) - Color.red(colorTo)) * delta / posTo + Color.red(colorTo));
        int green = (int) ((Color.green(colorFrom) - Color.green(colorTo)) * delta / posTo + Color.green(colorTo));
        int blue = (int) ((Color.blue(colorFrom) - Color.blue(colorTo)) * delta / posTo) + Color.blue(colorTo);
        return Color.argb(255, red, green, blue);
    }

    /**
     * 滑动监听
     */
    public void setScrollView(View scrollView) {
        if (scrollView instanceof RecyclerView) {
            ((RecyclerView) scrollView).addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (mScrollY < 0) {
                        mScrollY = 0;
                    }
                    mScrollY += dy;
                    onScroll(mScrollY);
                }
            });
        } else if (scrollView instanceof ScrollableLayout) {
            ((ScrollableLayout) scrollView).addOnScrollChangedListener(new OnScrollChangedListener() {
                @Override
                public void onScrollChanged(int y, int oldY, int maxY) {
                    if (mScrollY < 0) {
                        mScrollY = 0;
                    }
                    mScrollY = y;
//                    Log.d("setScrollView onscroll", "y " + y + " oldY " + oldY + " maxY " + maxY);
                    onScroll(mScrollY);
                }
            });
        }
    }

    public void setNeedBackgroundTrans(boolean needBackgroundTrans) {
        mNeedBackgroundTrans = needBackgroundTrans;
        if (mBackground != null) {
            mBackground.mutate().setAlpha(0);
        }
        if (mDividerLine.getBackground() != null) {
            mDividerLine.getBackground().mutate().setAlpha(0);
        }
    }
}
