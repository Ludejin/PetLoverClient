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

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;

/**
 * 比如底下冒出来的分享组件可以用这个
 *
 * @author markzhai on 16/3/14
 * @version 1.0.0
 */
public class GridMenu extends Dialog {

    /**
     * Interface definition for a callback to be invoked when an item in this
     * view has been clicked.
     */
    public interface OnItemClickListener {
        /**
         * Called when an item in this menu has been clicked.
         *
         * @param menu   The menu where click happened.
         * @param itemId The id of item that was clicked.
         * @return true if the callback consumed the click, false otherwise.
         */
        boolean onItemClick(GridMenu menu, int itemId);
    }

    /**
     * Mode for this menu, see {@link #setMode(Mode)}.
     */
    public enum Mode {
        /**
         * Disable stretching.
         */
        NO_STRETCH,

        /**
         * Creates as many columns as can fit on screen.
         */
        AUTO_FIT
    }

    private final static int DEFAULT_WINDOW_COLOR = 0x66000000;
    private final static int DEFAULT_NUM_COLUMNS = 4;

    private ViewGroup mWindowView;
    private ViewGroup mContentView;
    private CustomGridLayout mGridLayout;
    private OnItemClickListener mItemClickListener;
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (performItemClick(v)) {
                dismiss();
            }
        }
    };

    private Mode mMode = Mode.AUTO_FIT;
    private int mGravity = Gravity.BOTTOM;
    private int mNumColumns = DEFAULT_NUM_COLUMNS;

    private final HashSet<Integer> mItemIds = new HashSet<Integer>();
    private final HashSet<Integer> mVisibleItemIds = new HashSet<Integer>();

    public GridMenu(Context context) {
        this(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

    protected GridMenu(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public GridMenu(Context context, int theme) {
        super(context, theme);
        init();
    }

    @SuppressLint("InlinedApi")
    private void init() {
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        // content view.
        mContentView = findContentView();
        mGridLayout = findGridLayout();
        mGridLayout.setStretchMode(CustomGridLayout.STRETCH_COLUMN_WIDTH);

        mWindowView = new FrameLayout(getContext());
        mWindowView.setBackgroundColor(DEFAULT_WINDOW_COLOR);
        mWindowView.addView(mContentView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, mGravity));
        setContentView(mWindowView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            cancel();
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            this.dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Add a menu item.
     *
     * @param id       Item id.
     * @param titleRes Item title resource id.
     * @param iconRes  Item icon resource id.
     */
    public void add(int id, int titleRes, int iconRes) {
        performAdd(id, titleRes, iconRes);
    }

    /**
     * Add a menu item.
     *
     * @param id      Item id.
     * @param title   Item title.
     * @param iconRes Item icon resource id.
     */
    public void add(int id, CharSequence title, int iconRes) {
        performAdd(id, title, iconRes);
    }

    protected Item performAdd(int id, int titleRes, int iconRes) {
        return performAdd(id, getContext().getResources().getString(titleRes), iconRes);
    }

    protected Item performAdd(int id, CharSequence title, int iconRes) {
        if (mItemIds.contains(id)) {
            throw new RuntimeException("item already added with id " + id);
        }
        mItemIds.add(id);
        mVisibleItemIds.add(id);
        Item item = generateItem();
        item.getView().setId(id);
        item.setTitle(title);
        item.setIcon(getContext().getResources().getDrawable(iconRes));
        item.getView().setOnClickListener(mClickListener);
        mGridLayout.addView(item.getView(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mGridLayout.setNumColumns(calculateNumColumns());
        return item;
    }

    /**
     * Remove a menu item.
     *
     * @param id Item id.
     */
    public void remove(int id) {
        performRemove(id);
    }

    protected void performRemove(int id) {
        if (mItemIds.remove(id)) {
            mVisibleItemIds.remove(id);
            View itemView = mGridLayout.findViewById(id);
            if (itemView != null) {
                mGridLayout.removeView(itemView);
                mGridLayout.setNumColumns(calculateNumColumns());
            }
        }
    }

    /**
     * Set the visibility of a menu item.
     *
     * @param id      Item id.
     * @param visible Whether this item should be visible, or not.
     */
    public void setVisible(int id, boolean visible) {
        if (!mItemIds.contains(id)) {
            return;
        }
        if (visible) {
            mVisibleItemIds.add(id);
        } else {
            mVisibleItemIds.remove(id);
        }
        View itemView = mGridLayout.findViewById(id);
        itemView.setVisibility(visible ? View.VISIBLE : View.GONE);
        mGridLayout.setNumColumns(calculateNumColumns());
    }

    /**
     * Set the number of columns in this menu.
     *
     * @param numColumns Number of columns.
     */
    public void setNumColumns(int numColumns) {
        if (mNumColumns != numColumns) {
            mNumColumns = numColumns;
            mGridLayout.setNumColumns(calculateNumColumns());
        }
    }

    private int calculateNumColumns() {
        int num = mNumColumns;
        switch (mMode) {
            case AUTO_FIT:
                num = Math.min(mNumColumns, getVisibleCount());
                break;

            case NO_STRETCH:
                num = mNumColumns;
                break;
        }
        return num;
    }

    /**
     * Set the stretch mode of this menu.
     *
     * @param mode Stretch mode, default is {@link Mode#AUTO_FIT}.
     */
    public void setMode(Mode mode) {
        if (mMode != mode) {
            mMode = mode;
            mGridLayout.setNumColumns(calculateNumColumns());
        }
    }

    /**
     * Set the gravity of this menu displayed in screen, see constant
     * defined in {@link Gravity} for more details.
     *
     * @param gravity Gravity of this menu displayed in screen.
     */
    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            mGravity = gravity;
            FrameLayout.LayoutParams contentParams = (FrameLayout.LayoutParams) mContentView.getLayoutParams();
            contentParams.gravity = gravity;
            mContentView.setLayoutParams(contentParams);
        }
    }

    /**
     * Set the window background of this menu dialog.
     *
     * @param background Window background.
     */
    public void setWindowBackground(Drawable background) {
        mWindowView.setBackgroundDrawable(background);
    }

    /**
     * Set the view background of this menu.
     *
     * @param background View background.
     */
    public void setBackground(Drawable background) {
        mContentView.setBackgroundDrawable(background);
    }

    /**
     * Set the {@link OnItemClickListener} of this menu, which will
     * be called when one item is clicked.
     *
     * @param itemClickListener Item click listener.
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    /**
     * Returns the item count of this menu.
     *
     * @return Item count of this menu.
     */
    public int getCount() {
        return mItemIds.size();
    }

    /**
     * Returns the visible item count of this menu.
     *
     * @return Visible item count of this menu.
     */
    public int getVisibleCount() {
        return mVisibleItemIds.size();
    }

    private boolean performItemClick(View itemView) {
        OnItemClickListener itemClickListener = mItemClickListener;
        if (itemClickListener != null) {
            return itemClickListener.onItemClick(this, itemView.getId());
        }
        return false;
    }

    /**
     * Find the content view of this menu.
     *
     * @return Content view.
     */
    protected ViewGroup findContentView() {
        if (mContentView != null) {
            return mContentView;
        }
        LinearLayout contentView = new LinearLayout(getContext());
        contentView.setOrientation(LinearLayout.VERTICAL);
        return contentView;
    }

    /**
     * Find the inner grid layout of this menu.
     *
     * @return Inner grid layout.
     */
    protected CustomGridLayout findGridLayout() {
        if (mGridLayout != null) {
            return mGridLayout;
        }
        CustomGridLayout gridLayout = new CustomGridLayout(getContext());
        mContentView.addView(gridLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return gridLayout;
    }

    /**
     * Called to generate menu item.
     *
     * @return Newly generated menu item.
     */
    protected Item generateItem() {
        return new ItemView(getContext());
    }

    /**
     * Menu item definition.
     */
    protected static interface Item {
        public View getView();

        public void setIcon(Drawable drawable);

        public void setTitle(CharSequence title);
    }

    private static class ItemView extends LinearLayout implements Item {

        private ImageView mIconView;
        private TextView mTitleView;

        ItemView(Context context) {
            super(context);
            setOrientation(VERTICAL);
            init();
        }

        private void init() {
            ImageView iconView = new ImageView(getContext());
            LayoutParams iconParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            iconParams.gravity = Gravity.CENTER_HORIZONTAL;
            addView(iconView, iconParams);

            TextView titleView = new TextView(getContext());
            LayoutParams titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            titleParams.gravity = Gravity.CENTER_HORIZONTAL;
            addView(titleView, titleParams);

            mIconView = iconView;
            mTitleView = titleView;
        }

        @Override
        public View getView() {
            return this;
        }

        public void setIcon(Drawable drawable) {
            mIconView.setImageDrawable(drawable);
        }

        public void setTitle(CharSequence title) {
            mTitleView.setText(title);
        }
    }
}
