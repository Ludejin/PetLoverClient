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

package in.srain.cube.views.ptr;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.moduth.uikit.R;

import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class CrewHeader extends FrameLayout implements PtrUIHandler {

    private ImageView mRotateView;
    private ImageView mLoadingView;

    private AnimationDrawable mAnimationDrawable;

    public CrewHeader(Context context) {
        super(context);
        initViews(null);
    }

    public CrewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public CrewHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(attrs);
    }

    protected void initViews(AttributeSet attrs) {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.crew_ptr_header, this);

        mRotateView = (ImageView) header.findViewById(R.id.crew_header_rotate_view);
        mLoadingView = (ImageView) header.findViewById(R.id.crew_header_rotate_view_progressbar);

        mAnimationDrawable = (AnimationDrawable) mLoadingView.getDrawable();

        resetView();
    }

    private void resetView() {
        hideRotateView();
        mLoadingView.setVisibility(INVISIBLE);
        mAnimationDrawable.stop();
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
        mAnimationDrawable.stop();
        mRotateView.setVisibility(VISIBLE);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        hideRotateView();

        mLoadingView.setImageDrawable(mAnimationDrawable);
        mLoadingView.setVisibility(VISIBLE);

        mAnimationDrawable.start();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        mLoadingView.setImageResource(R.drawable.ptr_header_load_finish);
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
