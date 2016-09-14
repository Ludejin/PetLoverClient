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
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

public class StateView extends FrameLayout {
    private Animation mAnimation;
    private State mCurrentState;
    private OnConfigStateViewListener mOnConfigStateViewListener;
    private SparseArray<StateProperty> mStateProperties;

    public interface OnConfigStateViewListener {
        void onConfigStateView(View view, State state);
    }

    public interface OnCreateStateViewListener {
        View onCreateStateView(ViewGroup viewGroup, State state);
    }

    public enum State {
        LOADING,
        SUCCESS,
        FAILED,
        NO_DATA,
        ONLY_WIFI,
        NO_COPYRIGHT
    }

    private static class StateProperty {
        private OnCreateStateViewListener mCreateStateViewListener;
        private int mLayoutId;
        private View mView;

        private StateProperty() {
        }
    }

    public StateView(Context context) {
        this(context, null);
    }

    public StateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCurrentState = State.SUCCESS;
        this.mStateProperties = new SparseArray();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StateView);
        int failedId = typedArray.getResourceId(R.styleable.StateView_failed_view, -1);
        int successId = typedArray.getResourceId(R.styleable.StateView_success_view, -1);
        int loadingId = typedArray.getResourceId(R.styleable.StateView_loading_view, R.layout.loadingview_loading);
        int nodataId = typedArray.getResourceId(R.styleable.StateView_nodata_view, -1);
        int onlyWifiId = typedArray.getResourceId(R.styleable.StateView_only_wifi_view, -1);
        int noCopyrightId = typedArray.getResourceId(R.styleable.StateView_no_copyright_view, -1);

        setStateProperty(State.FAILED, failedId);
        setStateProperty(State.SUCCESS, successId);
        setStateProperty(State.LOADING, loadingId);
        setStateProperty(State.NO_DATA, nodataId);
        setStateProperty(State.ONLY_WIFI, onlyWifiId);
        setStateProperty(State.NO_COPYRIGHT, noCopyrightId);
        typedArray.recycle();
        mAnimation = AnimationUtils.loadAnimation(context, R.anim.unlimited_rotate);
        setState(State.SUCCESS);
    }

    private LayoutInflater buildInflater(Context context) {
        if (context instanceof LayoutInflaterProvider) {
            return ((LayoutInflaterProvider) context).requestLayoutInflater();
        }
        return LayoutInflater.from(context);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        View contentView = getChildAt(0);
        if (contentView != null) {
            setStateProperty(State.SUCCESS, contentView, false);
        }
    }

    public void setStateProperty(State state, int layoutId) {
        StateProperty stateProperty = mStateProperties.get(state.ordinal());
        if (stateProperty == null) {
            stateProperty = new StateProperty();
        }
        stateProperty.mLayoutId = layoutId;
        mStateProperties.put(state.ordinal(), stateProperty);
    }

    public void setStateProperty(State state, View view) {
        setStateProperty(state, view, true);
    }

    private void setStateProperty(State state, View view, boolean show) {
        StateProperty stateProperty = mStateProperties.get(state.ordinal());
        if (stateProperty == null) {
            stateProperty = new StateProperty();
        }
        stateProperty.mView = view;
        mStateProperties.put(state.ordinal(), stateProperty);
        if (view.getParent() == null) {
            if (!show) {
                view.setVisibility(INVISIBLE);
            }
            addView(view);
        }
    }

    public void setStateProperty(State state, OnCreateStateViewListener listener) {
        StateProperty stateProperty = mStateProperties.get(state.ordinal());
        if (stateProperty == null) {
            stateProperty = new StateProperty();
        }
        stateProperty.mCreateStateViewListener = listener;
        this.mStateProperties.put(state.ordinal(), stateProperty);
    }

    public View getStateView(State state) {
        StateProperty stateProperty = mStateProperties.get(state.ordinal());
        if (stateProperty == null) {
            return null;
        }
        return stateProperty.mView;
    }

    public void setState(State state) {
        setState(state, true);
    }

    public void setState(State state, boolean animation) {
        this.mCurrentState = state;
        int size = this.mStateProperties.size();
        for (int index = 0; index < size; index++) {
            StateProperty stateProperty = mStateProperties.valueAt(index);
            int key = this.mStateProperties.keyAt(index);
            if (stateProperty != null) {
                View view;
                if (key == state.ordinal()) {
                    view = stateProperty.mView;
                    if (view == null) {
                        OnCreateStateViewListener createStateViewListener = stateProperty.mCreateStateViewListener;
                        int layoutId = stateProperty.mLayoutId;
                        if (createStateViewListener != null) {
                            view = createStateViewListener.onCreateStateView(this, state);
                        } else if (stateProperty.mLayoutId > 0) {
                            view = buildInflater(getContext()).inflate(layoutId, this, false);
                            if (this.mOnConfigStateViewListener != null) {
                                this.mOnConfigStateViewListener.onConfigStateView(view, state);
                            }
                        }
                        if (view != null) {
                            addView(view);
                        }
                        stateProperty.mView = view;
                    } else {
                        if (stateProperty.mLayoutId <= 0
                                && stateProperty.mCreateStateViewListener == null
                                && indexOfChild(view) < 0) {
                            addView(view);
                        }
                        view.setVisibility(View.VISIBLE);
                    }
                    showAnimation(view, animation, key);
                } else if (stateProperty.mView != null) {
                    view = stateProperty.mView;
                    view.setVisibility(View.GONE);
                    showAnimation(view, false, key);
                }
            }
        }
    }

    public State getCurrentState() {
        return this.mCurrentState;
    }

    private void showAnimation(View view, boolean show, int state) {
        if (state == State.LOADING.ordinal() && view != null) {
            View animView = view.findViewById(R.id.online_refresh_animation);
            if (animView != null) {
                if (show) {
                    animView.startAnimation(this.mAnimation);
                } else {
                    animView.clearAnimation();
                }
            }
        }
    }

    public void showAnimation() {
        showAnimation(getStateView(this.mCurrentState), true, mCurrentState.ordinal());
    }

    public void setOnConfigStateViewListener(OnConfigStateViewListener onConfigStateViewListener) {
        this.mOnConfigStateViewListener = onConfigStateViewListener;
    }
}