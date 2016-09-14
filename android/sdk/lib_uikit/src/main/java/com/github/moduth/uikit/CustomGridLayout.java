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
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 轻量级的Grid layout，支持和 {@link android.widget.GridView} 同样的功能.
 */
public class CustomGridLayout extends ViewGroup {

    /**
     * Disables stretching.
     *
     * @see #setStretchMode(int)
     */
    public static final int NO_STRETCH = 0;
    /**
     * Stretches the spacing between columns.
     *
     * @see #setStretchMode(int)
     */
    public static final int STRETCH_SPACING = 1;
    /**
     * Stretches columns.
     *
     * @see #setStretchMode(int)
     */
    public static final int STRETCH_COLUMN_WIDTH = 2;
    /**
     * Stretches the spacing between columns. The spacing is uniform.
     *
     * @see #setStretchMode(int)
     */
    public static final int STRETCH_SPACING_UNIFORM = 3;

    /**
     * Disables stretching and disallows exceeding.
     */
    public static final int NO_STRETCH_NO_EXCEED = 4;

    /**
     * Creates as many columns as can fit on screen.
     *
     * @see #setNumColumns(int)
     */
    public static final int AUTO_FIT = -1;

    private int mNumColumns = AUTO_FIT;
    private int mColumnWidth;
    private int mRequestedColumnWidth;
    private int mRequestedNumColumns;

    private int mStretchMode = STRETCH_COLUMN_WIDTH;

    private int mHorizontalSpacing = 0;
    private int mRequestedHorizontalSpacing;
    private int mVerticalSpacing = 0;

    public CustomGridLayout(Context context) {
        this(context, null);
    }

    public CustomGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomGridLayout, defStyle, 0);

        int hSpacing = a.getDimensionPixelOffset(
                R.styleable.CustomGridLayout_android_horizontalSpacing, 0);
        setHorizontalSpacing(hSpacing);

        int vSpacing = a.getDimensionPixelOffset(
                R.styleable.CustomGridLayout_android_verticalSpacing, 0);
        setVerticalSpacing(vSpacing);

        int index = a.getInt(R.styleable.CustomGridLayout_android_stretchMode, STRETCH_COLUMN_WIDTH);
        if (index >= 0) {
            setStretchMode(index);
        }

        int columnWidth = a.getDimensionPixelOffset(R.styleable.CustomGridLayout_android_columnWidth, -1);
        if (columnWidth > 0) {
            setColumnWidth(columnWidth);
        }

        int numColumns = a.getInt(R.styleable.CustomGridLayout_android_numColumns, 1);
        setNumColumns(numColumns);

        a.recycle();
    }

    /**
     * Control how items are stretched to fill their space.
     *
     * @param stretchMode Either {@link #NO_STRETCH},
     *                    {@link #STRETCH_SPACING}, {@link #STRETCH_SPACING_UNIFORM}, or {@link #STRETCH_COLUMN_WIDTH}.
     */
    public void setStretchMode(int stretchMode) {
        if (stretchMode != mStretchMode) {
            mStretchMode = stretchMode;
            requestLayoutIfNecessary();
        }
    }

    /**
     * Get the current stretch mode.
     */
    public int getStretchMode() {
        return mStretchMode;
    }

    /**
     * Set the number of columns in the grid
     *
     * @param numColumns The desired number of columns.
     */
    public void setNumColumns(int numColumns) {
        if (numColumns != mRequestedNumColumns) {
            mRequestedNumColumns = numColumns;
            requestLayoutIfNecessary();
        }
    }

    /**
     * Get the current number of columns in the grid.
     */
    public int getNumColumns() {
        return mNumColumns;
    }

    /**
     * Get requested number of columns in the grid.
     */
    public int getRequestedNumColumns() {
        return mRequestedNumColumns;
    }

    /**
     * Set the width of columns in the grid.
     *
     * @param columnWidth The column width, in pixels.
     */
    public void setColumnWidth(int columnWidth) {
        if (columnWidth != mRequestedColumnWidth) {
            mRequestedColumnWidth = columnWidth;
            requestLayoutIfNecessary();
        }
    }

    /**
     * Get the width of columns.
     */
    public int getColumnWidth() {
        return mColumnWidth;
    }

    /**
     * Set the amount of horizontal (x) spacing to place between each item
     * in the grid.
     *
     * @param horizontalSpacing The amount of horizontal space between items,
     *                          in pixels.
     */
    public void setHorizontalSpacing(int horizontalSpacing) {
        if (horizontalSpacing != mRequestedHorizontalSpacing) {
            mRequestedHorizontalSpacing = horizontalSpacing;
            requestLayoutIfNecessary();
        }
    }

    /**
     * Returns the amount of horizontal spacing currently used between each item in the grid.
     * <p/>
     * <p>This is only accurate for the current layout. If {@link #setHorizontalSpacing(int)}
     * has been called but layout is not yet complete, this method may return a stale value.
     * To get the horizontal spacing that was explicitly requested use
     * {@link #getRequestedHorizontalSpacing()}.</p>
     *
     * @return Current horizontal spacing between each item in pixels
     * @see #setHorizontalSpacing(int)
     * @see #getRequestedHorizontalSpacing()
     */
    public int getHorizontalSpacing() {
        return mHorizontalSpacing;
    }

    /**
     * Returns the requested amount of horizontal spacing between each item in the grid.
     * <p/>
     * <p>The value returned may have been supplied during inflation as part of a style,
     * the default GridView style, or by a call to {@link #setHorizontalSpacing(int)}.
     * If layout is not yet complete or if GridView calculated a different horizontal spacing
     * from what was requested, this may return a different value from
     * {@link #getHorizontalSpacing()}.</p>
     *
     * @return The currently requested horizontal spacing between items, in pixels
     * @see #setHorizontalSpacing(int)
     * @see #getHorizontalSpacing()
     */
    public int getRequestedHorizontalSpacing() {
        return mRequestedHorizontalSpacing;
    }

    /**
     * Set the amount of vertical (y) spacing to place between each item
     * in the grid.
     *
     * @param verticalSpacing The amount of vertical space between items,
     *                        in pixels.
     * @see #getVerticalSpacing()
     */
    public void setVerticalSpacing(int verticalSpacing) {
        if (verticalSpacing != mVerticalSpacing) {
            mVerticalSpacing = verticalSpacing;
            requestLayoutIfNecessary();
        }
    }

    /**
     * Returns the amount of vertical spacing between each item in the grid.
     *
     * @return The vertical spacing between items in pixels
     * @see #setVerticalSpacing(int)
     */
    public int getVerticalSpacing() {
        return mVerticalSpacing;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        final int parentLeft = getPaddingLeft();
        final int parentTop = getPaddingTop();

        if (count > 0) {
            int columnCount = mNumColumns;
            int columnIndex = 0;

            int prevLeft = parentLeft;
            int prevTop = parentTop;

            int maxChildHeight = 0;
            for (int index = 0; index < count; index++) {
                final View child = getChildAt(index);
                if (child.getVisibility() == View.GONE) continue;

                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();

                child.layout(prevLeft, prevTop, prevLeft + width, prevTop + height);

                maxChildHeight = maxChildHeight < height ? height : maxChildHeight;

                prevLeft += width + mHorizontalSpacing;

                // update column index.
                columnIndex++;
                if (columnIndex >= columnCount) {
                    // new row.
                    columnIndex = 0;
                    prevLeft = parentLeft;
                    prevTop += maxChildHeight + mVerticalSpacing;
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.UNSPECIFIED) {
            if (mColumnWidth > 0) {
                widthSize = mColumnWidth + getPaddingLeft() + getPaddingRight();
            } else {
                widthSize = getPaddingLeft() + getPaddingRight();
            }
            widthSize += getVerticalScrollbarWidth();
        }

        int availableChildWidth = widthSize - getPaddingLeft() - getPaddingRight();
        boolean didNotInitiallyFit = determineColumns(availableChildWidth);

        int childrenHeight = 0;

        final int count = getChildCount();
        if (count > 0) {
            int columnCount = mNumColumns;
            int columnIndex = 0;
            int rowIndex = 0;
            int rowHeight = 0;
            for (int index = 0; index < count; index++) {
                final View child = getChildAt(index);
                if (child.getVisibility() == View.GONE) continue;

                LayoutParams p = (LayoutParams) child.getLayoutParams();
                if (p == null) {
                    p = generateDefaultLayoutParams();
                    child.setLayoutParams(p);
                }

                int layoutWidth = p.width;
                int layoutHeight = p.height;
                if (mStretchMode == NO_STRETCH_NO_EXCEED &&
                        ((layoutWidth > mColumnWidth && count > 1) || layoutWidth > availableChildWidth)) {
                    if (layoutWidth > 0) {
                        if (layoutHeight > 0) {
                            layoutHeight = (int) ((float) mColumnWidth / layoutWidth * layoutHeight);
                        }
                        layoutWidth = LayoutParams.MATCH_PARENT;
                    }
                }
                int childHeightSpec = getChildMeasureSpec(
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 0, layoutHeight);
                int childWidthSpec = getChildMeasureSpec(
                        MeasureSpec.makeMeasureSpec(mColumnWidth, MeasureSpec.EXACTLY), 0, layoutWidth);
                child.measure(childWidthSpec, childHeightSpec);

                int childHeight = child.getMeasuredHeight();
                rowHeight = rowHeight < childHeight ? childHeight : rowHeight;

                // update column index.
                columnIndex++;
                if (columnIndex >= columnCount || index == count - 1) {
                    // new row or end of last row.
                    columnIndex = 0;
                    // compute children height.
                    childrenHeight += rowHeight;
                    if (rowIndex > 0) childrenHeight += mVerticalSpacing;
                    // update row index and height.
                    rowIndex++;
                    rowHeight = 0;
                }
            }
        }

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = getPaddingTop() + getPaddingBottom() + childrenHeight +
                    getVerticalFadingEdgeLength() * 2;
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            int ourSize = getPaddingTop() + getPaddingBottom() + childrenHeight +
                    getVerticalFadingEdgeLength() * 2;
            if (heightSize > ourSize) {
                heightSize = ourSize;
            }
        }

        if (widthMode == MeasureSpec.AT_MOST && mRequestedNumColumns != AUTO_FIT) {
            int ourSize = (mRequestedNumColumns * mColumnWidth)
                    + ((mRequestedNumColumns - 1) * mHorizontalSpacing)
                    + getPaddingLeft() + getPaddingRight();
            if (ourSize > widthSize || didNotInitiallyFit) {
                widthSize |= MEASURED_STATE_TOO_SMALL;
            }
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    final void requestLayoutIfNecessary() {
        if (getChildCount() > 0) {
            requestLayout();
            invalidate();
        }
    }

    private boolean determineColumns(int availableSpace) {
        final int requestedHorizontalSpacing = mRequestedHorizontalSpacing;
        final int stretchMode = mStretchMode;
        final int requestedColumnWidth = mRequestedColumnWidth;
        boolean didNotInitiallyFit = false;

        if (mRequestedNumColumns == AUTO_FIT) {
            if (requestedColumnWidth > 0) {
                // Client told us to pick the number of columns
                mNumColumns = (availableSpace + requestedHorizontalSpacing) /
                        (requestedColumnWidth + requestedHorizontalSpacing);
            } else {
                // Just make up a number if we don't have enough info
                mNumColumns = 2;
            }
        } else {
            // We picked the columns
            mNumColumns = mRequestedNumColumns;
        }

        if (mNumColumns <= 0) {
            mNumColumns = 1;
        }

        switch (stretchMode) {
            case NO_STRETCH:
                // Nobody stretches
                mColumnWidth = requestedColumnWidth;
                mHorizontalSpacing = requestedHorizontalSpacing;
                break;

            case NO_STRETCH_NO_EXCEED:
                // Nobody stretches and no exceeding.
                int maxColumnWidth = (availableSpace - (mNumColumns - 1) * requestedHorizontalSpacing) / mNumColumns;
                mColumnWidth = requestedColumnWidth <= maxColumnWidth ? requestedColumnWidth : maxColumnWidth;
                mHorizontalSpacing = requestedHorizontalSpacing;
                break;

            default:
                int spaceLeftOver = availableSpace - (mNumColumns * requestedColumnWidth) -
                        ((mNumColumns - 1) * requestedHorizontalSpacing);

                if (spaceLeftOver < 0) {
                    didNotInitiallyFit = true;
                }

                switch (stretchMode) {
                    case STRETCH_COLUMN_WIDTH:
                        // Stretch the columns
                        mColumnWidth = requestedColumnWidth + spaceLeftOver / mNumColumns;
                        mHorizontalSpacing = requestedHorizontalSpacing;
                        break;

                    case STRETCH_SPACING:
                        // Stretch the spacing between columns
                        mColumnWidth = requestedColumnWidth;
                        if (mNumColumns > 1) {
                            mHorizontalSpacing = requestedHorizontalSpacing +
                                    spaceLeftOver / (mNumColumns - 1);
                        } else {
                            mHorizontalSpacing = requestedHorizontalSpacing + spaceLeftOver;
                        }
                        break;

                    case STRETCH_SPACING_UNIFORM:
                        // Stretch the spacing between columns
                        mColumnWidth = requestedColumnWidth;
                        if (mNumColumns > 1) {
                            mHorizontalSpacing = requestedHorizontalSpacing +
                                    spaceLeftOver / (mNumColumns + 1);
                        } else {
                            mHorizontalSpacing = requestedHorizontalSpacing + spaceLeftOver;
                        }
                        break;
                }

                break;
        }
        return didNotInitiallyFit;
    }

    public void attachRecycleableView(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = generateDefaultLayoutParams();
            if (params == null) {
                throw new IllegalArgumentException("generateDefaultLayoutParams() cannot return null");
            }
        }
        attachRecycleableView(child, -1, params);
    }

    public void attachRecycleableView(View child, ViewGroup.LayoutParams params) {
        attachRecycleableView(child, -1, params);
    }

    public void attachRecycleableView(View child, int index, ViewGroup.LayoutParams params) {
        if (child == null) {
            return;
        }
        child.onFinishTemporaryDetach();
        attachViewToParent(child, index, params);
        requestLayout();
    }

    public void detachRecycleableView(View child) {
        if (child == null) {
            return;
        }
        child.onStartTemporaryDetach();
        detachViewFromParent(child);
    }

    private static int ceil(float value) {
        int newValue = (int) value;
        return value == newValue ? newValue : newValue + 1;
    }

    public static class LayoutParams extends MarginLayoutParams {

        /**
         * {@inheritDoc}
         */
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(int width, int height) {
            super(width, height);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }


    //========================================
    // dividers
    //========================================

    /**
     * Don't show any dividers.
     */
    public static final int SHOW_DIVIDER_NONE = 0;
    /**
     * Show a divider at the beginning of the group.
     */
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    /**
     * Show dividers between each item in the group.
     */
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    /**
     * Show a divider at the end of the group.
     */
    public static final int SHOW_DIVIDER_END = 4;

    private Paint mDividerPaint;
    private Drawable mDivider;
    private int mDividerColor;
    private int mDividerWidth;
    private int mDividerHeight;
    private int mShowDividers = SHOW_DIVIDER_NONE;
    private boolean mFixLastRowDivider = true;

    public void setFixLastRowDivider(boolean fixLastRowDivider) {
        this.mFixLastRowDivider = fixLastRowDivider;
    }

    /**
     * Set how dividers should be shown between items in this layout
     *
     * @param showDividers One or more of {@link #SHOW_DIVIDER_BEGINNING},
     *                     {@link #SHOW_DIVIDER_MIDDLE}, or {@link #SHOW_DIVIDER_END},
     *                     or {@link #SHOW_DIVIDER_NONE} to show no dividers.
     */
    public void setShowDividers(int showDividers) {
        if (showDividers != mShowDividers) {
            requestLayout();
        }
        mShowDividers = showDividers;
    }


    /**
     * @return A flag set indicating how dividers should be shown around items.
     * @see #setShowDividers(int)
     */
    public int getShowDividers() {
        return mShowDividers;
    }

    public void setDividerColor(int color) {
        float density = getResources().getDisplayMetrics().density;
        setDividerColor(color, ceil(1 * density), ceil(1 * density));
    }

    public void setDividerColor(int color, int dividerWidth, int dividerHeight) {
        if (mDividerColor == color) {
            return;
        }
        mDividerColor = color;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mDivider = new ColorDrawable(mDividerColor);
        } else {
            mDividerPaint = new Paint();
            mDividerPaint.setColor(mDividerColor);
        }

        float density = getResources().getDisplayMetrics().density;

        mDividerWidth = Math.max(dividerWidth, ceil(1 * density));
        mDividerHeight = Math.max(dividerHeight, ceil(1 * density));

        setWillNotDraw(false);
        requestLayout();
    }


    /**
     * Get the width of the current divider drawable.
     *
     * @hide Used internally by framework.
     */
    public int getDividerWidth() {
        return mDividerWidth;
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawDividerInner(canvas);
    }


    private void drawDividerInner(Canvas canvas) {
        if (mShowDividers == SHOW_DIVIDER_NONE || (mDivider == null && mDividerPaint == null))
            return;

        final int parentLeft = getPaddingLeft();
        final int parentTop = getPaddingTop();

        final int count = getChildCount();

        if (count > 0) {
            int columnCount = mNumColumns;
            int rowCount = ceil((float) count / columnCount);

            int prevLeft = parentLeft;
            int prevTop = parentTop;
            for (int row = 0; row < rowCount; row++) {
                int maxChildHeight = 0;
                for (int column = 0; column < columnCount; column++) {
                    int index = row * columnCount + column;
                    if (index >= count) break;

                    final View child = getChildAt(index);
                    if (child.getVisibility() == View.GONE) continue;

                    final int width = child.getMeasuredWidth();
                    final int height = child.getMeasuredHeight();

                    maxChildHeight = maxChildHeight < height ? height : maxChildHeight;

                    // 绘制横向分割线
                    if (hasRowDividerBeforeChildAt(row, column, rowCount)) {
                        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                        final int top = child.getTop() - lp.topMargin - mDividerHeight;
                        final int left = prevLeft - lp.leftMargin;
                        drawHorizontalDividerInner(canvas, top, left, width);
                    }

                    // 绘制纵向分割线
                    if (hasColumnDividerBeforeChildAt(row, column)) {
                        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                        final int left = child.getLeft() - lp.leftMargin - mDividerWidth;
                        final int top = prevTop - lp.topMargin;
                        drawVerticalDividerInner(canvas, top, left, height);
                    }

                    prevLeft += width + mHorizontalSpacing;
                }
                prevLeft = parentLeft;
                prevTop += maxChildHeight + mVerticalSpacing;
            }

            fixLastRowDivider(canvas, rowCount);

        }
    }

    private void fixLastRowDivider(Canvas canvas, int rowCount) {
        if (mFixLastRowDivider && rowCount > 1) {
            int columnCount = mNumColumns;
            int childCount = getChildCount();
            if ((childCount % columnCount) != 0) {
                int startColumn = childCount % columnCount;
                View lastChild = getChildAt(childCount - 1);
                int prevLeft = getPaddingLeft();

                final int width = lastChild.getMeasuredWidth();
                final int height = lastChild.getMeasuredHeight();
                prevLeft += startColumn * (width + mHorizontalSpacing);

                // 绘制纵向分割线
                LayoutParams lp = (LayoutParams) lastChild.getLayoutParams();
                int left = prevLeft - lp.leftMargin - mDividerWidth;
                int top = lastChild.getTop() - lp.topMargin;
                drawVerticalDividerInner(canvas, top, left, height);

                for (; startColumn < columnCount; startColumn++) {
                    // 绘制横向分割线
                    top = lastChild.getTop() - lp.topMargin - mDividerHeight;
                    left = prevLeft - lp.leftMargin;
                    drawHorizontalDividerInner(canvas, top, left, width);

                    prevLeft += width + mHorizontalSpacing;
                }
            }
        }
    }

    private boolean hasColumnDividerBeforeChildAt(int rowIndex, int columnIdex) {
        if (columnIdex == 0) {
            return (mShowDividers & SHOW_DIVIDER_BEGINNING) != 0;
        } else if (columnIdex == mNumColumns) {
            return (mShowDividers & SHOW_DIVIDER_END) != 0;
        } else if ((mShowDividers & SHOW_DIVIDER_MIDDLE) != 0) {
            return true;
        }
        return false;
    }

    private boolean hasRowDividerBeforeChildAt(int rowIndex, int columnIdex, int rowCount) {
        if (rowIndex == 0) {
            return (mShowDividers & SHOW_DIVIDER_BEGINNING) != 0;
        } else if (rowIndex == rowCount) {
            return (mShowDividers & SHOW_DIVIDER_END) != 0;
        } else if ((mShowDividers & SHOW_DIVIDER_MIDDLE) != 0) {
            return true;
        }
        return false;
    }

    private void drawHorizontalDividerInner(Canvas canvas, int top, int left, int width) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mDivider.setBounds(left, top, left + width, top + mDividerHeight);
            mDivider.draw(canvas);
        } else {
            canvas.drawRect(left, top, left + width, top + mDividerHeight, mDividerPaint);
        }
    }

    private void drawVerticalDividerInner(Canvas canvas, int top, int left, int height) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mDivider.setBounds(left, top, left + mDividerWidth, top + height);
            mDivider.draw(canvas);
        } else {
            canvas.drawRect(left, top, left + mDividerWidth, top + height, mDividerPaint);
        }
    }
}
