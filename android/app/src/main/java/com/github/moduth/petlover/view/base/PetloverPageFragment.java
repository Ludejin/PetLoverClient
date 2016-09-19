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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.moduth.ext.utils.NetWorkUtils;
import com.github.moduth.uikit.ProgressLayout;
import com.github.moduth.uikit.loadmore.OnLoadMoreListener;
import com.github.moduth.uikit.loadmore.RecyclerViewWithFooter;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Abner on 16/6/6.
 * Email nimengbo@gmail.com
 * GitHub https://github.com/nimengbo
 */
public abstract class PetloverPageFragment extends PetloverFragment {

    public int mCurrentPage = 1;

    public String mLastId = "";

    public boolean mIsFetching = false;

    public boolean isInit = true;

    protected abstract PtrFrameLayout getPtrFrameLayout();

    protected abstract RecyclerViewWithFooter getLoadMoreRecycler();

    protected abstract ProgressLayout getProgressLayout();

    protected abstract void fetchData();

    protected boolean canDoRefresh(PtrFrameLayout frame, View content, View header) {
        return !mIsFetching &&
                PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    public PtrHandler mPtrHandler = new PtrHandler() {
        @Override
        public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
            return canDoRefresh(frame, content, header);
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            mCurrentPage = 1;
            mLastId = "";
            fetchData();
            getLoadMoreRecycler().setPullToLoad();
        }
    };

    public OnLoadMoreListener mLoadMoreListener = () -> {
        mCurrentPage++;
        fetchData();
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPtrFrameLayout().setPtrHandler(mPtrHandler);
        getLoadMoreRecycler().setOnLoadMoreListener(mLoadMoreListener);
    }

    protected void setPtrHandler(PtrHandler ptrHandler) {
        mPtrHandler = ptrHandler;
        getPtrFrameLayout().setPtrHandler(mPtrHandler);
    }

    protected void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
        getLoadMoreRecycler().setOnLoadMoreListener(mLoadMoreListener);
    }

    public void setNoDataText(CharSequence text) {
        getProgressLayout().setNoDataText(text);
    }

    public void clickNoData() {
        fetchData();
    }

    protected void loadEmpty() {
        if (getProgressLayout() != null) {
            getProgressLayout().showNone(v -> clickNoData());
        }
        if (mCurrentPage == 1) {
            getPtrFrameLayout().refreshComplete();
        }
        mIsFetching = false;
    }

    protected void loadFinish() {
        RecyclerView.Adapter adapter = getLoadMoreRecycler().getAdapter();
        if (mCurrentPage == 1 &&
                getLoadMoreRecycler() != null &&
                adapter != null &&
                (adapter.getItemCount() == 0
                        || (adapter.getItemCount() == 1 &&
                        (adapter.getItemViewType(0) == RecyclerViewWithFooter.
                                LoadMoreAdapter.LOAD_MORE_VIEW_TYPE ||
                                adapter.getItemViewType(0) == RecyclerViewWithFooter.
                                        LoadMoreAdapter.EMPTY_VIEW_TYPE)))) {
            isInit = true;
            loadEmpty();
            return;
        }

        if (getProgressLayout() != null) {
            getProgressLayout().showContent();
        }
        if (mCurrentPage == 1) {
            getPtrFrameLayout().refreshComplete();
        }
        mCurrentPage++;
        mIsFetching = false;
    }

    protected void showError() {
        mIsFetching = false;
        getPtrFrameLayout().refreshComplete();
        if (mCurrentPage == 1 && getProgressLayout() != null) {
            if (NetWorkUtils.isNetworkConnected()) {
                getProgressLayout().showFailed(v -> fetchData());
            } else {
                getProgressLayout().showNetError(v -> fetchData());
            }
        }
    }

    protected void load() {
        mIsFetching = true;
        if (mCurrentPage == 1 && isInit) {
            if (getProgressLayout() != null) {
                getProgressLayout().showLoading();
            }
        }
        isInit = false;
    }

}
