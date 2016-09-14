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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.moduth.ext.utils.StringUtils;


/**
 * Created by Abner on 16/6/7.
 * Email nimengbo@gmail.com
 * GitHub https://github.com/nimengbo
 */
public class AnimationTagView extends RelativeLayout {

    private AlphaAnimation mAnimation;
    private TextView mTextView;

    public AnimationTagView(Context context) {
        super(context);
        initView();
    }

    public AnimationTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {

        LayoutInflater.from(getContext()).inflate(R.layout.tag_layout, this, true);
        mAnimation = new AlphaAnimation(0.0f, 1);
        mAnimation.setStartOffset(800);
        mAnimation.setDuration(1000);
        mTextView = (TextView) findViewById(R.id.tag_text);
    }

    public void setText(String text) {
        if (StringUtils.isEmpty(text)) {
            setVisibility(INVISIBLE);
        } else {
            setVisibility(VISIBLE);
        }
        mTextView.setText(text);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTextView.getText() != null && !StringUtils.isEmpty(mTextView.getText().toString())) {
            startAnimation(mAnimation);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAnimation.cancel();
        clearAnimation();
    }
}
