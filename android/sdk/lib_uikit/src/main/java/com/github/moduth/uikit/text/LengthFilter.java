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

package com.github.moduth.uikit.text;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * A input length filter with {@link LengthConverter} support.
 */
public class LengthFilter implements InputFilter {

    /**
     * Text limit listener.
     */
    public interface LengthCallback {
        /**
         * Called when max length reached, see {@link #setLengthConverter}.
         *
         * @param maxLength max length.
         */
        void onMaxLengthReached(int maxLength);
    }

    private final int mMaxLength;
    private final LengthCallback mLengthCallback;
    private LengthConverter mLengthConverter = null;

    public LengthFilter(int maxLength) {
        this(maxLength, null);
    }

    public LengthFilter(int maxLength, LengthCallback callback) {
        mMaxLength = maxLength;
        mLengthCallback = callback;
    }

    /**
     * Set the length converter of this filter. This is used to calculate the actual text length when do filter.
     *
     * @param converter Length converter.
     */
    public void setLengthConverter(LengthConverter converter) {
        mLengthConverter = converter;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        LengthConverter lengthConverter = mLengthConverter;
        final int destReserveLen;
        if (lengthConverter == null) {
            destReserveLen = dest.length() - (dend - dstart);
        } else {
            destReserveLen = lengthConverter.convert(dest, 0, dstart) + lengthConverter.convert(dest, dend, dest.length());
        }

        final int srcLen;
        if (lengthConverter == null) {
            srcLen = end - start;
        } else {
            srcLen = lengthConverter.convert(source, start, end);
        }

        int keep = mMaxLength - destReserveLen;

        if (keep <= 0) {
            notifyMaxLengthReached();
            return "";
        } else if (keep >= srcLen) {
            return null; // keep original
        } else {
            notifyMaxLengthReached();
            if (lengthConverter != null) {
                keep = lengthConverter.reverse(source, start, start + keep);
                if (keep <= 0) {
                    return "";
                }
            }
            keep += start;
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }
            return source.subSequence(start, keep);
        }
    }

    private void notifyMaxLengthReached() {
        if (mLengthCallback != null) {
            mLengthCallback.onMaxLengthReached(mMaxLength);
        }
    }
}
