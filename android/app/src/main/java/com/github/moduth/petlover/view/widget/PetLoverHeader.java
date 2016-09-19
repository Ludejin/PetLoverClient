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

package com.github.moduth.petlover.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.github.moduth.petlover.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class PetloverHeader extends FrameLayout implements PtrUIHandler {

    private ImageView mRotateView;
    private ImageView mLoadingView;

    private Animation mAnimation;

    public PetloverHeader(Context context) {
        super(context);
        initViews(null);
    }

    public PetloverHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public PetloverHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(attrs);
    }

    protected void initViews(AttributeSet attrs) {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.widget_petlover_ptr_header, this);

        mRotateView = (ImageView) header.findViewById(R.id.stay_header_rotate_view);
        mLoadingView = (ImageView) header.findViewById(R.id.stay_header_rotate_view_progressbar);
        mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.petlover_ptr_header_loading);
//        mAnimationDrawable = (AnimationDrawable) mLoadingView.getDrawable();

        resetView();
    }

    private void resetView() {
        hideRotateView();
        mLoadingView.setVisibility(INVISIBLE);
        mLoadingView.clearAnimation();
    }

    private void hideRotateView() {
        mRotateView.clearAnimation();
        mRotateView.setVisibility(INVISIBLE);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        resetView();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        mLoadingView.setVisibility(INVISIBLE);
        mRotateView.clearAnimation();
        mRotateView.setVisibility(VISIBLE);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        hideRotateView();

        mLoadingView.setVisibility(VISIBLE);
        mLoadingView.startAnimation(mAnimation);
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        mLoadingView.clearAnimation();
        hideRotateView();
//        mLoadingView.setVisibility(INVISIBLE);
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

//        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
//            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
//                if (mRotateView != null) {
//                    mRotateView.clearAnimation();
//                    mRotateView.startAnimation(mReverseFlipAnimation);
//                }
//            }
//        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
//            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
//                if (mRotateView != null) {
//                    mRotateView.clearAnimation();
//                    mRotateView.startAnimation(mFlipAnimation);
//                }
//            }
//        }
    }
}
