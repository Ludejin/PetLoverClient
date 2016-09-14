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

package me.kaede.tagview.tagview;

import android.graphics.Color;

class Constants {


    //use dp and sp, not px

    //----------------- separator TagView-----------------//
    public static final float DEFAULT_LINE_MARGIN = 5;
    public static final float DEFAULT_TAG_MARGIN = 5;
    public static final float DEFAULT_TAG_TEXT_PADDING_LEFT = 8;
    public static final float DEFAULT_TAG_TEXT_PADDING_TOP = 5;
    public static final float DEFAULT_TAG_TEXT_PADDING_RIGHT = 8;
    public static final float DEFAULT_TAG_TEXT_PADDING_BOTTOM = 5;

    public static final float LAYOUT_WIDTH_OFFSET = 2;

    //----------------- separator Tag Item-----------------//
    public static final float DEFAULT_TAG_TEXT_SIZE = 14f;
    public static final float DEFAULT_TAG_DELETE_INDICATOR_SIZE = 14f;
    public static final float DEFAULT_TAG_LAYOUT_BORDER_SIZE = 0f;
    public static final float DEFAULT_TAG_RADIUS = 100;
    public static final int DEFAULT_TAG_LAYOUT_COLOR = Color.parseColor("#00BFFF");
    public static final int DEFAULT_TAG_LAYOUT_COLOR_PRESS = Color.parseColor("#88363636");
    public static final int DEFAULT_TAG_TEXT_COLOR = Color.parseColor("#ffffff");
    public static final int DEFAULT_TAG_DELETE_INDICATOR_COLOR = Color.parseColor("#ffffff");
    public static final int DEFAULT_TAG_LAYOUT_BORDER_COLOR = Color.parseColor("#ffffff");
    public static final String DEFAULT_TAG_DELETE_ICON = "×";
    public static final boolean DEFAULT_TAG_IS_DELETABLE = false;
}
