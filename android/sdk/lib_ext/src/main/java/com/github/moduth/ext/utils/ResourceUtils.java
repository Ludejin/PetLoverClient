/*
 * The GPL License (GPL)
 *
 * Copyright (c) 2016 MarkZhai (http://zhaiyifan.cn)
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

package com.github.moduth.ext.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * @author markzhai on 16/6/7
 * @version 1.0.0
 */
public class ResourceUtils {
    public static int getColor(Context context, int resourceId) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(resourceId);
        } else {
            return context.getResources().getColor(resourceId);
        }
    }

    public static Drawable getDrawable(Context context, int resourceId) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(resourceId);
        } else {
            return context.getResources().getDrawable(resourceId);
        }
    }
}
