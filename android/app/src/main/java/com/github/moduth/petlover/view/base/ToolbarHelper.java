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
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amokie.stay.R;
import com.amokie.stay.listener.OnThrottleClickListener;
import com.github.moduth.ext.Ext;
import com.github.moduth.ext.utils.KeyBoardUtils;
import com.github.moduth.uiframework.navigator.backstack.AbstractActivity;
import com.github.moduth.uikit.MarkImageView;
import com.github.moduth.uikit.tablayout.SlidingTabLayout;

/**
 * @author markzhai on 16/3/16
 * @version 1.0.0
 */
public class ToolbarHelper {

    interface ToolbarProvider {
        void provideToolbar();
    }

    protected Toolbar mToolbar = null;
    protected TextView mToolbarTitleTextView = null;
    protected MarkImageView mToolbarMessageImageView = null;
    protected View mToolbarSort = null;
    protected ImageView mToolbarSetting = null;
    protected ImageView mToolbarEdit = null;
    protected View mToolbarEditTitle = null;
    protected View mToolbarMore = null;
    protected ImageView mToolbarBack = null;
    protected TextView mToolbarLeftTextButton = null;
    protected TextView mToolbarRightTextButton = null;
    protected SlidingTabLayout mToolbarTab = null;
    protected View mToolbarCollect;
    protected View mToolbarShare;
    protected ImageView mToolbarPublish = null;
    protected View mToolbarSearchContainer = null;
    protected TextView mToolbarFollowTextView = null;

    protected EditText mToolbarSearchEditText = null;
    protected ImageView mToolbarSearchIconSearch = null;
    protected ImageView mToolbarSearchIconDelete = null;

    protected View mDividerLine;

    private SearchBarListener mOnSearchBarClickListener;

    private boolean mIsSearchShowingHint = true;

    public interface SearchBarListener {
        void onSearchBarFocusChange(boolean hasFocus);
    }

    public ToolbarHelper(Toolbar toolbar) {
        mToolbar = toolbar;
        if (mToolbar == null) {
            throw new IllegalStateException("Layout is required to include a Toolbar with id toolbar");
        }

        mToolbarTitleTextView = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mDividerLine = mToolbar.findViewById(R.id.divider_line);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public TextView getToolbarTitle() {
        return mToolbarTitleTextView;
    }

    public void enableBack(AbstractActivity activity) {
        if (mToolbar != null) {
            mToolbarBack = (ImageView) mToolbar.findViewById(R.id.toolbar_back);
        }
        if (mToolbarBack != null) {
            mToolbarBack.setVisibility(View.VISIBLE);
            mToolbarBack.setOnClickListener(v -> activity.onBackPressed());
        }
    }

    public void setTitle(CharSequence title) {
        if (mToolbarTitleTextView != null) {
            mToolbarTitleTextView.setText(title);
            if (mToolbarTitleTextView.getVisibility() != View.VISIBLE) {
                mToolbarTitleTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void hideTitle() {
        if (mToolbarTitleTextView != null) {
            mToolbarTitleTextView.setVisibility(View.GONE);
        }
    }

    public void showTitle() {
        if (mToolbarTitleTextView != null) {
            mToolbarTitleTextView.setVisibility(View.VISIBLE);
        }
    }

    public void initToolbarSort(View.OnClickListener listener) {
        if (mToolbar != null) {
            mToolbarSort = mToolbar.findViewById(R.id.toolbar_sort);
        }
        if (mToolbarSort != null) {
            mToolbarSort.setVisibility(View.VISIBLE);
            mToolbarSort.setOnClickListener(listener);
        }
    }

    public void initToolbarSetting(View.OnClickListener listener) {
        if (mToolbar != null) {
            mToolbarSetting = (ImageView) mToolbar.findViewById(R.id.toolbar_setting);
        }
        if (mToolbarSetting != null) {
            mToolbarSetting.setVisibility(View.VISIBLE);
            mToolbarSetting.setOnClickListener(listener);
        }
    }

    public void initToolbarMore(View.OnClickListener listener) {
        if (mToolbar != null) {
            mToolbarMore = mToolbar.findViewById(R.id.toolbar_more);
        }
        if (mToolbarMore != null) {
            mToolbarMore.setVisibility(View.VISIBLE);
            mToolbarMore.setOnClickListener(listener);
        }
    }

    public void initToolbarEdit(View.OnClickListener listener) {
        if (mToolbar != null) {
            mToolbarEdit = (ImageView) mToolbar.findViewById(R.id.toolbar_edit);
        }
        if (mToolbarEdit != null) {
            mToolbarEdit.setVisibility(View.VISIBLE);
            mToolbarEdit.setOnClickListener(listener);
        }
    }

    public void initToolbarEditTitle(View.OnClickListener listener) {
        if (mToolbar != null) {
            mToolbarEditTitle = mToolbar.findViewById(R.id.toolbar_edit_title);
        }
        if (mToolbarEditTitle != null) {
//            mToolbarEditTitle.setVisibility(View.VISIBLE);
            mToolbarEditTitle.setOnClickListener(listener);
        }
    }

    public void initToolbarLeftTextButton(String text, View.OnClickListener listener) {
        if (mToolbar != null) {
            mToolbarLeftTextButton = (TextView) mToolbar.findViewById(R.id.toolbar_left_text_button);
        }
        if (mToolbarLeftTextButton != null) {
            mToolbarLeftTextButton.setText(text);
            mToolbarLeftTextButton.setOnClickListener(listener);
            mToolbarLeftTextButton.setVisibility(View.VISIBLE);
        }
    }

    public void initToolbarRightTextButton(String text, View.OnClickListener listener) {
        if (mToolbar != null) {
            mToolbarRightTextButton = (TextView) mToolbar.findViewById(R.id.toolbar_right_text_button);
        }
        if (mToolbarRightTextButton != null) {
            mToolbarRightTextButton.setText(text);
            mToolbarRightTextButton.setOnClickListener(listener);
            mToolbarRightTextButton.setVisibility(View.VISIBLE);
        }
    }

    public void setToolbarRightTextButton(String text) {
        if (mToolbar != null) {
            mToolbarRightTextButton = (TextView) mToolbar.findViewById(R.id.toolbar_right_text_button);
        }
        if (mToolbarRightTextButton != null) {
            mToolbarRightTextButton.setText(text);
        }
    }

    public TextView getToolbarRightTextButton() {
        if (mToolbarRightTextButton != null) {
            return mToolbarRightTextButton;
        } else {
            if (mToolbar != null) {
                mToolbarRightTextButton = (TextView) mToolbar
                        .findViewById(R.id.toolbar_right_text_button);
                return mToolbarRightTextButton;
            }
        }
        return null;
    }

    public void hideRightTextButton() {
        if (mToolbarRightTextButton != null) {
            mToolbarRightTextButton.setVisibility(View.GONE);
        }
    }

    public void hideLeftTextButton() {
        if (mToolbarLeftTextButton != null) {
            mToolbarLeftTextButton.setVisibility(View.GONE);
        }
    }

    public void showLeftTextButton() {
        if (mToolbarLeftTextButton != null) {
            mToolbarLeftTextButton.setVisibility(View.VISIBLE);
        }
    }

    public void setEditMode(boolean editMode) {
        if (mToolbarEditTitle != null) {
            mToolbarEditTitle.setVisibility(editMode ? View.VISIBLE : View.GONE);
        }
        if (mToolbarTitleTextView != null) {
            mToolbarTitleTextView.setVisibility(editMode ? View.VISIBLE : View.GONE);
        }
        if (mToolbarSetting != null) {
            mToolbarSetting.setVisibility(editMode ? View.GONE : View.VISIBLE);
        }
        if (mToolbarEdit != null) {
            mToolbarEdit.setVisibility(editMode ? View.GONE : View.VISIBLE);
        }
        if (mToolbarRightTextButton != null) {
            mToolbarRightTextButton.setVisibility(editMode ? View.VISIBLE : View.GONE);
        }
    }

    public void setSearchBarListener(SearchBarListener onSearchBarClickListener) {
        mOnSearchBarClickListener = onSearchBarClickListener;
    }

    public void showSearchBar() {
        hideTitle();
        if (mToolbarSearchContainer == null) {
            mToolbarSearchContainer = mToolbar.findViewById(R.id.toolbar_search_container);
            mToolbarSearchEditText = (EditText) mToolbarSearchContainer.findViewById(R.id.toolbar_search_text);
            mToolbarSearchIconSearch = (ImageView) mToolbarSearchContainer.findViewById(R.id.toolbar_search_icon_search);
            mToolbarSearchIconDelete = (ImageView) mToolbarSearchContainer.findViewById(R.id.toolbar_search_icon_delete);

            mToolbarSearchIconDelete.setOnClickListener(v -> mToolbarSearchEditText.setText(""));
            mToolbarSearchContainer.setOnClickListener(v -> mToolbarSearchEditText.requestFocus());

            mToolbarSearchEditText.setOnFocusChangeListener((v, hasFocus) -> {
                if (mOnSearchBarClickListener != null) {
                    mOnSearchBarClickListener.onSearchBarFocusChange(hasFocus);
                }
                if (hasFocus) {
                    mToolbarSearchEditText.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    mToolbarSearchIconSearch.setVisibility(View.VISIBLE);
                    mToolbarSearchIconDelete.setVisibility(View.VISIBLE);
                    mToolbarRightTextButton.setVisibility(View.VISIBLE);
//                    mToolbarMessageImageView.setVisibility(View.GONE);
//                    mToolbarSearchEditText.setCompoundDrawables(null, null, null, null);
                    mToolbarSearchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    mToolbarSearchEditText.setSelection(mToolbarSearchEditText.getText().toString().length());
                    if (mIsSearchShowingHint) {
                        mToolbarSearchEditText.setText("");
                        mIsSearchShowingHint = false;
                    }
                    InputMethodManager inputMethodManager = (InputMethodManager) Ext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    v.requestFocus();
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
                } else {
                    mToolbarSearchEditText.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    mToolbarSearchIconSearch.setVisibility(View.GONE);
                    mToolbarSearchIconDelete.setVisibility(View.GONE);
                    mToolbarRightTextButton.setVisibility(View.GONE);
//                    mToolbarMessageImageView.setVisibility(View.VISIBLE);
                    mToolbarSearchEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_search, 0, 0, 0);
                    mToolbarSearchEditText.setText(mToolbarSearchEditText.getText());
                    if (!mIsSearchShowingHint && mToolbarSearchEditText.getText().length() == 0) {
                        mToolbarSearchEditText.setText(R.string.toolbar_search_default);
                        mIsSearchShowingHint = true;
                    }
                }
            });

            // 取消按钮
            initToolbarRightTextButton(Ext.getContext().getString(R.string.action_cancel), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mToolbarSearchEditText.clearFocus();
                    KeyBoardUtils.hideInputMethod(mToolbarSearchEditText.getContext(), mToolbarSearchEditText);
                }
            });
            mToolbarRightTextButton.setVisibility(View.GONE);
        }
        mToolbarSearchContainer.setVisibility(View.VISIBLE);
    }

    public void initToolbarMessage(View.OnClickListener listener) {
        if (mToolbar != null) {
            mToolbarMessageImageView = (MarkImageView) mToolbar.findViewById(R.id.toolbar_message);
        }
        if (mToolbarMessageImageView != null) {
            mToolbarMessageImageView.setVisibility(View.VISIBLE);
            mToolbarMessageImageView.setOnClickListener(listener);
        }
    }

    public void setToolbarMessageCount(int count) {
        if (mToolbarMessageImageView != null) {
            mToolbarMessageImageView.setMarkerCount(count);
        }
    }

    public void initToolbarTab(ViewPager viewPager, String[] tabTitles) {
        if (mToolbar != null) {
            mToolbarTab = (SlidingTabLayout) mToolbar.findViewById(R.id.toolbar_tabs);
        }
        if (mToolbarTab != null) {
            mToolbarTab.setVisibility(View.VISIBLE);
            mToolbarTab.setViewPager(viewPager, tabTitles);
        }
    }

    public void initToolbarCollect(OnThrottleClickListener listener) {
        if (mToolbar != null) {
            mToolbarCollect = mToolbar.findViewById(R.id.toolbar_collect);
        }
        if (mToolbarCollect != null) {
            mToolbarCollect.setOnClickListener(listener);
        }
    }

    public void showToolbarCollect() {
        if (mToolbarCollect != null) {
            mToolbarCollect.setVisibility(View.VISIBLE);
        }
    }

    public void initToolbarShare(OnThrottleClickListener listener) {
        if (mToolbar != null) {
            mToolbarShare = mToolbar.findViewById(R.id.toolbar_share);
        }
        if (mToolbarShare != null) {
            mToolbarShare.setVisibility(View.VISIBLE);
            mToolbarShare.setOnClickListener(listener);
        }
    }

    public void showToolbarShare() {
        if (mToolbar != null && mToolbarShare == null) {
            mToolbarShare = mToolbar.findViewById(R.id.toolbar_share);
        }
        if (mToolbarShare != null) {
            mToolbarShare.setVisibility(View.VISIBLE);
        }
    }

    public void hideToolbarShare() {
        if (mToolbar != null && mToolbarShare == null) {
            mToolbarShare = mToolbar.findViewById(R.id.toolbar_share);
        }
        if (mToolbarShare != null) {
            mToolbarShare.setVisibility(View.GONE);
        }
    }

    public void setToolbarCollect(boolean isCollected) {

        if (mToolbar != null) {
            mToolbarCollect = mToolbar.findViewById(R.id.toolbar_collect);
        }
        if (mToolbarCollect != null) {
            mToolbarCollect.setVisibility(View.VISIBLE);
            mToolbarCollect.setSelected(isCollected);
        }
    }

    public void initToolbarPublish(OnThrottleClickListener listener) {
        if (mToolbar != null) {
            mToolbarPublish = (ImageView) mToolbar.findViewById(R.id.toolbar_publish);
        }
        if (mToolbarPublish != null) {
            mToolbarPublish.setVisibility(View.VISIBLE);
            mToolbarPublish.setOnClickListener(listener);
        }
    }

    public void initToolbarFollow(OnThrottleClickListener listener) {
        if (mToolbar != null) {
            mToolbarFollowTextView = (TextView) mToolbar.findViewById(R.id.toolbar_follow);
        }
        if (mToolbarFollowTextView != null) {
            mToolbarFollowTextView.setVisibility(View.VISIBLE);
            mToolbarFollowTextView.setOnClickListener(listener);
        }
    }

    public ImageView getToolbarBack() {
        return mToolbarBack;
    }

    public TextView getToolbarTitleTextView() {
        return mToolbarTitleTextView;
    }

    public ImageView getToolbarPublish() {
        return mToolbarPublish;
    }

    public View getDividerLine() {
        return mDividerLine;
    }

    public ImageView getToolbarEdit() {
        return mToolbarEdit;
    }

    public ImageView getToolbarSetting() {
        return mToolbarSetting;
    }

    public TextView getToolbarFollowTextView() {
        return mToolbarFollowTextView;
    }

    public EditText getToolbarSearchEditText() {
        return mToolbarSearchEditText;
    }

    public void setToolbarSearchContainerListener(View.OnClickListener listener) {
        mToolbarSearchContainer.setOnClickListener(listener);
        mToolbarSearchEditText.setOnClickListener(listener);
        mToolbarSearchEditText.setFocusable(false);
    }
}
