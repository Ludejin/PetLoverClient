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
import android.graphics.drawable.Drawable;

/**
 * Tag Entity
 */
public class Tag {

    public String id;
    public String text;
    public int tagTextColor;
    public float tagTextSize;
    public int layoutColor;
    public int layoutColorPress;
    public boolean isDeletable;
    public int deleteIndicatorColor;
    public float deleteIndicatorSize;
    public float radius;
    public String deleteIcon;
    public float layoutBorderSize;
    public int layoutBorderColor;
    public Drawable background;


    public Tag(String text) {
        init("0", text, Constants.DEFAULT_TAG_TEXT_COLOR, Constants.DEFAULT_TAG_TEXT_SIZE, Constants.DEFAULT_TAG_LAYOUT_COLOR, Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS,
                Constants.DEFAULT_TAG_IS_DELETABLE, Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR, Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE, Constants.DEFAULT_TAG_RADIUS, Constants.DEFAULT_TAG_DELETE_ICON, Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE, Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR);
    }

    public Tag(String text, int color) {
        init("0", text, Constants.DEFAULT_TAG_TEXT_COLOR, Constants.DEFAULT_TAG_TEXT_SIZE, color, Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS, Constants.DEFAULT_TAG_IS_DELETABLE,
                Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR, Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE, Constants.DEFAULT_TAG_RADIUS, Constants.DEFAULT_TAG_DELETE_ICON, Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE, Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR);

    }

    public Tag(String text, String color) {
        init("0", text, Constants.DEFAULT_TAG_TEXT_COLOR, Constants.DEFAULT_TAG_TEXT_SIZE, Color.parseColor(color), Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS,
                Constants.DEFAULT_TAG_IS_DELETABLE, Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR, Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE, Constants.DEFAULT_TAG_RADIUS, Constants.DEFAULT_TAG_DELETE_ICON, Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE, Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR);

    }

    public Tag(String id, String text, int color) {
        init(id, text, Constants.DEFAULT_TAG_TEXT_COLOR, Constants.DEFAULT_TAG_TEXT_SIZE, color, Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS, Constants.DEFAULT_TAG_IS_DELETABLE,
                Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR, Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE, Constants.DEFAULT_TAG_RADIUS, Constants.DEFAULT_TAG_DELETE_ICON, Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE, Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR);

    }

    public Tag(String id, String text, String color) {
        init(id, text, Constants.DEFAULT_TAG_TEXT_COLOR, Constants.DEFAULT_TAG_TEXT_SIZE, Color.parseColor(color), Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS,
                Constants.DEFAULT_TAG_IS_DELETABLE, Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR, Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE, Constants.DEFAULT_TAG_RADIUS, Constants.DEFAULT_TAG_DELETE_ICON, Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE, Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR);

    }

    private void init(String id, String text, int tagTextColor, float tagTextSize, int layout_color, int layout_color_press, boolean isDeletable, int deleteIndicatorColor,
                      float deleteIndicatorSize, float radius, String deleteIcon, float layoutBorderSize, int layoutBorderColor) {
        this.id = id;
        this.text = text;
        this.tagTextColor = tagTextColor;
        this.tagTextSize = tagTextSize;
        this.layoutColor = layout_color;
        this.layoutColorPress = layout_color_press;
        this.isDeletable = isDeletable;
        this.deleteIndicatorColor = deleteIndicatorColor;
        this.deleteIndicatorSize = deleteIndicatorSize;
        this.radius = radius;
        this.deleteIcon = deleteIcon;
        this.layoutBorderSize = layoutBorderSize;
        this.layoutBorderColor = layoutBorderColor;
    }
}
