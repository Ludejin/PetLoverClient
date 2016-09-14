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
import android.widget.TabHost;


import com.github.moduth.ext.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 扩展的TabHost，支持tab查询
 *
 * @author markzhai on 16/3/14
 * @version 1.0.0
 */
public class ExtendTabHost extends TabHost {

    private List<TabSpec> mTabSpecs = new ArrayList<TabSpec>();

    public ExtendTabHost(Context context) {
        super(context);
    }

    public ExtendTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addTab(TabSpec tabSpec) {
        super.addTab(tabSpec);
        if (tabSpec != null) {
            mTabSpecs.add(tabSpec);
        }
    }

    @Override
    public void clearAllTabs() {
        super.clearAllTabs();
        mTabSpecs.clear();
    }

    /**
     * Get the count of all tabs.
     *
     * @return count of all tabs.
     */
    public int getTabCount() {
        return mTabSpecs.size();
    }

    /**
     * Get tab by index.
     *
     * @param index tab index.
     * @return corresponding tab.
     */
    public TabSpec getTabAt(int index) {
        if (index < 0 || index >= mTabSpecs.size()) {
            return null;
        }
        return mTabSpecs.get(index);
    }

    /**
     * Get all tabs.
     *
     * @return all tabs.
     */
    public List<TabSpec> getAllTabs() {
        return new ArrayList<TabSpec>(mTabSpecs);
    }

    public TabSpec getTabByTag(String tag) {
        for (TabSpec spec : mTabSpecs) {
            if (ObjectUtils.equals(tag, spec.getTag())) {
                return spec;
            }
        }
        return null;
    }
}
