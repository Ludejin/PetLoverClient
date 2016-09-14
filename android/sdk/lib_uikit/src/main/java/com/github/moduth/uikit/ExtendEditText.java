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
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.github.moduth.uikit.text.LengthConverter;
import com.github.moduth.uikit.text.LengthFilter;


/**
 * 扩展的EditText，支持文字长度限制和回调
 */
public class ExtendEditText extends EditText {

    /**
     * Text limit listener.
     */
    public interface LimitListener {
        /**
         * Called when max length reached, see {@link #setMaxLength} and {@link #setLengthConverter}.
         *
         * @param maxLength max length.
         */
        void onMaxLengthReached(int maxLength);
    }

    private boolean mClearFocusOnBack = false;

    private LimitListener mLimitListener;
    private LengthConverter mLengthConverter = null;

    public ExtendEditText(Context context) {
        super(context);
        init(context, null);
    }

    public ExtendEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExtendEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (context == null || attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendEditText);

        boolean clearFocusOnBack = a.getBoolean(R.styleable.ExtendEditText_clearFocusOnBack, false);
        setClearFocusOnBack(clearFocusOnBack);

        int maxLength = a.getInteger(R.styleable.ExtendEditText_android_maxLength, -1);
        setMaxLength(maxLength);

        a.recycle();
    }

    /**
     * Set whether clear focus when back key is pressed.
     *
     * @param clear whether clear focus on back.
     */
    public void setClearFocusOnBack(boolean clear) {
        mClearFocusOnBack = clear;
    }

    /**
     * Set the max length of text. This actually takes advantage of
     * {@link #setFilters(InputFilter[])} and {@link LengthFilter}.
     *
     * @param maxLength max length.
     */
    public void setMaxLength(int maxLength) {
        if (maxLength >= 0) {
            InnerLengthFilter filter = new InnerLengthFilter(maxLength);
            setFilters(new InputFilter[]{filter});
        }
    }

    /**
     * Set the length converter of this text view. This is used to calculate the actual text length. Also see {@link #setMaxLength}.
     *
     * @param converter length converter.
     */
    public void setLengthConverter(LengthConverter converter) {
        mLengthConverter = converter;
    }

    /**
     * Set the text limit listener.
     *
     * @param listener limit listener.
     */
    public void setLimitListener(LimitListener listener) {
        mLimitListener = listener;
    }

    /**
     * Get the converted length with {@link LengthConverter}.
     *
     * @return converted length.
     */
    public int getConvertedLength() {
        CharSequence text = getText();
        if (TextUtils.isEmpty(text)) {
            return 0;
        }
        LengthConverter converter = mLengthConverter;
        if (converter == null) {
            return text.length();
        } else {
            return converter.convert(text, 0, text.length());
        }
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (mClearFocusOnBack && keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_UP) {
            hideIme();
            clearFocus();
            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    private void hideIme() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    private void notifyMaxLengthReached(int maxLength) {
        LimitListener limitListener = mLimitListener;
        if (limitListener != null) {
            limitListener.onMaxLengthReached(maxLength);
        }
    }

    final class InnerLengthFilter extends LengthFilter {

        public InnerLengthFilter(int maxLength) {
            super(maxLength, ExtendEditText.this::notifyMaxLengthReached);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            // ensure use the latest length converter.
            setLengthConverter(mLengthConverter);
            return super.filter(source, start, end, dest, dstart, dend);
        }
    }
}
