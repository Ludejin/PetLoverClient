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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Markable interface with a default mark implementation.
 *
 * @author markzhai on 2016/3/11
 */
public interface Markable {

    interface OnMarkerClickListener {
        void onClick(Markable view);
    }

    /**
     * Center position for mark.
     */
    int MARK_CENTER = 1;
    /**
     * Left top position for mark.
     */
    int MARK_LEFT_TOP = 2;
    /**
     * Right top position for mark.
     */
    int MARK_RIGHT_TOP = 3;
    /**
     * Right bottom position for mark.
     */
    int MARK_RIGHT_BOTTOM = 4;
    /**
     * Left bottom position for mark.
     */
    int MARK_LEFT_BOTTOM = 5;

    /**
     * Show none.
     */
    int MARK_SHOW_NONE = 1;
    /**
     * Show red point.
     */
    int MARK_SHOW_IMAGE = 2;
    /**
     * Show red point with new text.
     */
    int MARK_SHOW_TEXT = 3;
    /**
     * Show red point with exact number.
     */
    int MARK_SHOW_NUMBER = 4;

    /**
     * Set whether the marker should be visible. Default is true.
     */
    void setMarkerVisible(boolean visible);

    /**
     * Set whether the marker should be visible when this markable is selected.
     */
    void setMarkerVisibleWhenSelected(boolean visible);

    /**
     * Set the maker drawable.
     */
    void setMarker(Drawable marker);

    /**
     * Set the maker drawable for one-digit text.
     */
    void setMarker1(Drawable marker);

    /**
     * Set the maker drawable for two-digit text.
     */
    void setMarker2(Drawable marker);

    /**
     * Set the maker drawable for three-digit text.
     */
    void setMarker3(Drawable marker);

    /**
     * Set the maker drawable.
     */
    void setMarker(int resId);

    /**
     * Set the maker drawable for one-digit text.
     */
    void setMarker1(int resId);

    /**
     * Set the maker drawable for two-digit text.
     */
    void setMarker2(int resId);

    /**
     * Set the maker drawable for three-digit text.
     */
    void setMarker3(int resId);

    /**
     * Set if enable auto padding to make center alignment.
     */
    void setMarkerAutoPadding(boolean enable);

    /**
     * Set the marker position
     *
     * @param position The marker's position. See {@link #MARK_CENTER},
     *                 {@link #MARK_LEFT_TOP}, {@link #MARK_RIGHT_TOP},
     *                 {@link #MARK_RIGHT_BOTTOM}, {@link #MARK_LEFT_BOTTOM}.
     */
    void setMarkerPosition(int position);

    /**
     * Set the marker position
     *
     * @param markerType The marker's type. See {@link #MARK_SHOW_NONE},
     *                   {@link #MARK_SHOW_IMAGE}, {@link #MARK_SHOW_TEXT},
     *                   {@link #MARK_SHOW_NUMBER}.
     */
    void setMarkerType(int markerType);

    /**
     * Set the marker count.
     */
    void setMarkerCount(int count);

    /**
     * Set the marker text.
     */
    void setMarkerText(String text);

    /**
     * Set the marker text size.
     */
    void setMarkerTextSize(int textSize);

    /**
     * Set the marker text color.
     */
    void setMarkerTextColor(int textColor);

    /**
     * Set the extra padding for marker.
     */
    void setMarkerPaddingOffset(int xOffset, int yOffset);

    /**
     * Set the marker's size.
     */
    void setMarkerSize(int width, int height);

    /**
     * Get the marker's size.
     *
     * @param size an array of two integers in which to hold the size {width, height}.
     */
    void getMarkerSize(int[] size);

    /**
     * Whether the marker is set to be visible.
     */
    boolean isMarkerVisible();

    /**
     * Set the marker click listener.
     */
    void setOnMarkerClickListener(OnMarkerClickListener listener);

    class Marker implements Markable {

        private View mView;
        private Context mContext;
        private Drawable mMarker;
        private Drawable mMarker1;
        private Drawable mMarker2;
        private Drawable mMarker3;
        private boolean mMarkerVisible = false;
        private boolean mMarkerVisibleWhenSelected = false;
        private int mMarkerPosition = MARK_CENTER;
        private String mMarkerText = "";
        private int mMarkerType = MARK_SHOW_IMAGE;
        private int mMarkerPaddingX, mMarkerPaddingY;
        private int mMarkerPaddingXOffset, mMarkerPaddingYOffset;
        private int mMarkerWidth = -1, mMarkerHeight = -1;
        private boolean mAutoPadding = false;

        private boolean mMarkerOrPositionChanged = false;
        private boolean mMarkerPressed = false;
        private OnMarkerClickListener mMarkerClickListener;
        private Markable mMarkerClickListenerCallback;

        private Paint mTextPaint;
        private int mMarkTextSize = 15;
        private int mMarkTextColor = Color.WHITE;

        public Marker(View view) {
            this(view, null);
        }

        public Marker(View view, AttributeSet attrs) {
            mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setColor(mMarkTextColor);
            mTextPaint.setTextSize(mMarkTextSize);

            mView = view;
            mContext = view.getContext();
            if (view instanceof Markable) {
                setMarkerClickListenerCallback((Markable) view);
            }

            TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.Markable);

            Drawable marker = a.getDrawable(R.styleable.Markable_mark);
            Drawable marker1 = a.getDrawable(R.styleable.Markable_mark1);
            Drawable marker2 = a.getDrawable(R.styleable.Markable_mark2);
            Drawable marker3 = a.getDrawable(R.styleable.Markable_mark3);
            boolean markerVisible = a.getBoolean(R.styleable.Markable_markVisible, false);
            boolean markerVisibleWhenSelected = a.getBoolean(R.styleable.Markable_markVisibleWhenSelected, false);
            boolean markerAutoPadding = a.getBoolean(R.styleable.Markable_markAutoPadding, false);
            int markerWidth = a.getDimensionPixelSize(R.styleable.Markable_markWidth, -1);
            int markerHeight = a.getDimensionPixelSize(R.styleable.Markable_markHeight, -1);
            int markerOffsetX = a.getDimensionPixelOffset(R.styleable.Markable_markOffsetX, 0);
            int markerOffsetY = a.getDimensionPixelOffset(R.styleable.Markable_markOffsetY, 0);
            int markerPosition = a.getInt(R.styleable.Markable_markPosition, MARK_CENTER);
            int markerType = a.getInt(R.styleable.Markable_markType, MARK_SHOW_IMAGE);
            int markerTextColor = a.getColor(R.styleable.Markable_markTextColor, mMarkTextColor);
            int markerTextSize = a.getDimensionPixelSize(R.styleable.Markable_markTextSize, mMarkTextSize);

            setMarker(marker);
            setMarker1(marker1);
            setMarker2(marker2);
            setMarker3(marker3);
            setMarkerVisible(markerVisible);
            setMarkerVisibleWhenSelected(markerVisibleWhenSelected);
            setMarkerSize(markerWidth, markerHeight);
            setMarkerPaddingOffset(markerOffsetX, markerOffsetY);
            setMarkerPosition(markerPosition);
            setMarkerType(markerType);
            setMarkerTextColor(markerTextColor);
            setMarkerTextSize(markerTextSize);
            setMarkerAutoPadding(markerAutoPadding);

            a.recycle();
        }

        @Override
        public void setMarkerVisible(boolean visible) {
            if (mMarkerVisible == visible) {
                return;
            }
            mMarkerVisible = visible;
            mView.invalidate();
        }

        public void setMarkerVisibleWhenSelected(boolean visible) {
            if (mMarkerVisibleWhenSelected == visible) {
                return;
            }
            mMarkerVisibleWhenSelected = visible;
            mView.invalidate();
        }

        @Override
        public void setMarker(Drawable marker) {
            if (mMarker == marker) {
                return;
            }
            mMarker = marker;
            mMarkerOrPositionChanged = true;
            mView.invalidate();
        }

        @Override
        public void setMarker1(Drawable marker) {
            mMarker1 = marker;
        }

        @Override
        public void setMarker2(Drawable marker) {
            mMarker2 = marker;
        }

        @Override
        public void setMarker3(Drawable marker) {
            mMarker3 = marker;
        }

        @Override
        public void setMarker(int resId) {
            setMarker(mContext.getResources().getDrawable(resId));
        }

        @Override
        public void setMarker1(int resId) {
            setMarker1(mContext.getResources().getDrawable(resId));
        }

        @Override
        public void setMarker2(int resId) {
            setMarker2(mContext.getResources().getDrawable(resId));
        }

        @Override
        public void setMarker3(int resId) {
            setMarker3(mContext.getResources().getDrawable(resId));
        }

        @Override
        public void setMarkerAutoPadding(boolean enable) {
            mAutoPadding = enable;
        }

        @Override
        public void setMarkerPosition(int position) {
            if (mMarkerPosition == position) {
                return;
            }
            mMarkerPosition = position;
            mMarkerOrPositionChanged = true;
            mView.invalidate();
        }

        @Override
        public void setMarkerType(int markerType) {
            if (mMarkerType == markerType) {
                return;
            }
            mMarkerType = markerType;
            mMarkerOrPositionChanged = true;
            mView.invalidate();
        }

        @Override
        public void setMarkerCount(int count) {
            if (count > 0) {
                setMarkerVisible(true);
                if (mMarkerType == MARK_SHOW_TEXT || mMarkerType == MARK_SHOW_NUMBER) {
                    if (count < 10) {
                        mMarkerText = String.valueOf(count);
                    } else if (count < 100) {
                        mMarkerText = String.valueOf(count);
                    } else {
                        mMarkerText = "99+";
                    }
                    setActiveMarkerByText();
                }
            } else {
                setMarkerVisible(false);
            }
            mMarkerOrPositionChanged = true;
            mView.invalidate();
        }

        @Override
        public void setMarkerText(String text) {
            if (mMarkerText == null || (!mMarkerText.equals(text))) {
                mMarkerText = text;
                setActiveMarkerByText();
                mMarkerOrPositionChanged = true;
                mView.invalidate();
            }
        }

        @Override
        public void setMarkerTextSize(int textSize) {
            if (mMarkTextSize == textSize) {
                return;
            }
            mMarkTextSize = textSize;
            mTextPaint.setTextSize(mMarkTextSize);
            mMarkerOrPositionChanged = true;
            mView.invalidate();
        }

        @Override
        public void setMarkerTextColor(int textColor) {
            if (mMarkTextColor == textColor) {
                return;
            }
            mMarkTextColor = textColor;
            mTextPaint.setColor(mMarkTextColor);
            mMarkerOrPositionChanged = true;
            mView.invalidate();
        }

        @Override
        public void setMarkerPaddingOffset(int xOffset, int yOffset) {
            if (mMarkerPaddingXOffset == xOffset && mMarkerPaddingYOffset == yOffset) {
                return;
            }
            mMarkerPaddingXOffset = xOffset;
            mMarkerPaddingYOffset = yOffset;
            mView.invalidate();
        }

        @Override
        public void setMarkerSize(int width, int height) {
            if (mMarkerWidth == width && mMarkerHeight == height) {
                return;
            }
            mMarkerWidth = width;
            mMarkerHeight = height;

            mMarkerOrPositionChanged = true;
            mView.invalidate();
        }

        @Override
        public boolean isMarkerVisible() {
            return mMarkerVisible || (mMarkerVisibleWhenSelected && mView.isSelected());
        }

        @Override
        public void setOnMarkerClickListener(OnMarkerClickListener listener) {
            mMarkerClickListener = listener;
        }

        protected void setMarkerClickListenerCallback(Markable callback) {
            mMarkerClickListenerCallback = callback;
        }

        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getAction();

            if (isMarkerTouched(event)) {
                if (action == MotionEvent.ACTION_DOWN) {
                    mMarkerPressed = true;
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    if (mMarkerPressed) {
                        notifyMarkerClick();
                        mMarkerPressed = false;
                        return true;
                    }
                }
            }

            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                mMarkerPressed = false;
            }
            return false;
        }

        public void draw(Canvas canvas) {
            if (mMarkerOrPositionChanged) {
                updateMarkerLayout();
                mMarkerOrPositionChanged = false;
            }

            if (isMarkerAvailable()) {
                final int paddingX = getCurrentMarkerPaddingX();
                final int paddingY = getCurrentMarkerPaddingY();

                boolean isTextMode = isTextMode();
                int textLength = 0;
                Rect textBoundRect = null;

                if (isTextMode) {
                    textLength = mMarkerText.length();
                    textBoundRect = new Rect();
                    mTextPaint.getTextBounds(mMarkerText, 0, textLength, textBoundRect);

                    if (textLength > 1 && mAutoPadding) {
                        canvas.translate(textBoundRect.width() / textLength / 2, 0);
                    }
                }

                canvas.save();
                if (paddingX != 0 || paddingY != 0) {
                    canvas.translate(paddingX, paddingY);
                }
                mMarker.draw(canvas);
                canvas.restore();

                if (isTextMode) {
                    if (textLength > 0) {
                        canvas.drawText(mMarkerText,
                                paddingX + getMarkerWidth() / 2,
                                paddingY + (getMarkerHeight() + textBoundRect.height()) / 2,
                                mTextPaint);
                    }
                }
            }
        }

        public void updateMarkerLayout() {
            if (mMarker == null) {
                return;
            }

            final int viewWidth = mView.getWidth();
            final int viewHeight = mView.getHeight();
            final int width = getMarkerWidth();
            final int height = getMarkerHeight();
            mMarkerPaddingX = getMarkerPaddingX(viewWidth, width, mMarkerPosition);
            mMarkerPaddingY = getMarkerPaddingY(viewHeight, height, mMarkerPosition);

            mMarker.setBounds(0, 0, width, height);
        }

        private boolean isTextMode() {
            return mMarkerType == MARK_SHOW_NUMBER || mMarkerType == MARK_SHOW_TEXT;
        }

        private void setActiveMarkerByText() {
            int length = mMarkerText.length();
            if (length == 1) {
                setMarker(mMarker1);
            } else if (length == 2) {
                setMarker(mMarker2);
            } else {
                setMarker(mMarker3);
            }
        }

        private int getMarkerWidth() {
            return mMarkerWidth > 0 ? mMarkerWidth : mMarker.getIntrinsicWidth();
        }

        private int getMarkerHeight() {
            return mMarkerHeight > 0 ? mMarkerHeight : mMarker.getIntrinsicHeight();
        }

        private int getCurrentMarkerPaddingX() {
            return mMarkerPaddingX + mMarkerPaddingXOffset;
        }

        private int getCurrentMarkerPaddingY() {
            return mMarkerPaddingY + mMarkerPaddingYOffset;
        }

        private static int getMarkerPaddingX(int width, int markerWidth, int position) {
            int x = 0;
            switch (position) {
                case MARK_LEFT_TOP:
                    x = 0;
                    break;

                case MARK_RIGHT_TOP:
                    x = width - markerWidth;
                    break;

                case MARK_RIGHT_BOTTOM:
                    x = width - markerWidth;
                    break;

                case MARK_LEFT_BOTTOM:
                    x = 0;
                    break;

                case MARK_CENTER:
                default:
                    x = (width - markerWidth) / 2;
                    break;
            }

            return x;
        }

        private static int getMarkerPaddingY(int height, int markerHeight, int position) {
            int y = 0;
            switch (position) {
                case MARK_LEFT_TOP:
                    y = 0;
                    break;

                case MARK_RIGHT_TOP:
                    y = 0;
                    break;

                case MARK_RIGHT_BOTTOM:
                    y = height - markerHeight;
                    break;

                case MARK_LEFT_BOTTOM:
                    y = height - markerHeight;
                    break;

                case MARK_CENTER:
                default:
                    y = (height - markerHeight) / 2;
                    break;
            }

            return y;
        }

        private void notifyMarkerClick() {
            if (mMarkerClickListener != null) {
                final Markable callback = mMarkerClickListenerCallback != null
                        ? mMarkerClickListenerCallback
                        : this;
                mMarkerClickListener.onClick(callback);
            }
        }

        private boolean isMarkerAvailable() {
            return mMarkerType != MARK_SHOW_NONE && isMarkerVisible() && mMarker != null;
        }

        private boolean isMarkerTouched(MotionEvent event) {
            if (!isMarkerAvailable() || mMarkerClickListener == null) {
                return false;
            }

            final int x = (int) event.getX();
            final int y = (int) event.getY();

            final int lowerX = getCurrentMarkerPaddingX();
            final int upperX = lowerX + getMarkerWidth();
            final int lowerY = getCurrentMarkerPaddingY();
            final int upperY = lowerY + getMarkerHeight();

            return (x >= lowerX && x <= upperX) && (y >= lowerY && y <= upperY);
        }

        @Override
        public void getMarkerSize(int[] size) {
            if (size == null || size.length < 2) {
                throw new IllegalArgumentException("location must be an array of two integers");
            }
            int width = 0, height = 0;
            if (mMarker != null) {
                width = getMarkerWidth();
                height = getMarkerHeight();
            }
            size[0] = width;
            size[1] = height;
        }
    }
}
