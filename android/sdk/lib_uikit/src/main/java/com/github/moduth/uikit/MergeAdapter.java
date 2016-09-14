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

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import java.util.ArrayList;

/**
 * 支持多个adapter合在一起的adapter（为什么不用viewtype呢）
 *
 * @author markzhai on 16/3/14
 * @version 1.0.0
 */
public class MergeAdapter implements ListAdapter {

    private final DataSetObservable mDataSetObservable = new DataSetObservable();
    private final ArrayList<ListAdapter> mAdapters = new ArrayList<ListAdapter>();

    /**
     * Add a new adapter.
     *
     * @param adapter Adapter to add.
     */
    public void addAdapter(ListAdapter adapter) {
        if (!mAdapters.contains(adapter)) {
            mAdapters.add(adapter);
        }
    }

    /**
     * Remove a adapter previously been added.
     *
     * @param adapter Adapter to remove.
     */
    public void removeAdapter(ListAdapter adapter) {
        if (mAdapters.contains(adapter)) {
            mAdapters.remove(adapter);
        }
    }

    /**
     * Returns the total count of adapters.
     *
     * @return The total count of adapters.
     */
    public int getAdapterCount() {
        return mAdapters.size();
    }

    public ListAdapter getAdapterAt(int index) {
        return mAdapters.get(index);
    }

    @Override
    public boolean areAllItemsEnabled() {
        for (ListAdapter adapter : mAdapters) {
            if (!adapter.areAllItemsEnabled()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position < 0) {
            throw new ArrayIndexOutOfBoundsException("invalid position " + position);
        }
        int pos = position;
        for (ListAdapter adapter : mAdapters) {
            int count = adapter.getCount();
            if (pos < count) {
                return adapter.isEnabled(pos);
            }
            pos -= count;
        }
        throw new ArrayIndexOutOfBoundsException("invalid position " + position);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
        for (ListAdapter adapter : mAdapters) {
            adapter.registerDataSetObserver(observer);
        }
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
        for (ListAdapter adapter : mAdapters) {
            adapter.unregisterDataSetObserver(observer);
        }
    }

    /**
     * Notifies the attached observers that the underlying data has been changed
     * and any View reflecting the data set should refresh itself.
     */
    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    /**
     * Notifies the attached observers that the underlying data is no longer valid
     * or available. Once invoked this adapter is no longer valid and should
     * not report further data set changes.
     */
    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    @Override
    public int getCount() {
        int count = 0;
        for (ListAdapter adapter : mAdapters) {
            count += adapter.getCount();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        if (position < 0) {
            throw new ArrayIndexOutOfBoundsException("invalid position " + position);
        }
        int pos = position;
        for (ListAdapter adapter : mAdapters) {
            int count = adapter.getCount();
            if (pos < count) {
                return adapter.getItem(pos);
            }
            pos -= count;
        }
        throw new ArrayIndexOutOfBoundsException("invalid position " + position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        if (mAdapters.isEmpty()) {
            return false;
        }
        for (ListAdapter adapter : mAdapters) {
            if (!adapter.hasStableIds()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position < 0) {
            throw new ArrayIndexOutOfBoundsException("invalid position " + position);
        }
        int pos = position;
        for (ListAdapter adapter : mAdapters) {
            int count = adapter.getCount();
            if (pos < count) {
                return adapter.getView(pos, convertView, parent);
            }
            pos -= count;
        }
        throw new ArrayIndexOutOfBoundsException("invalid position " + position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 0) {
            throw new ArrayIndexOutOfBoundsException("invalid position " + position);
        }
        int pos = position;
        int offset = 0;
        for (ListAdapter adapter : mAdapters) {
            int count = adapter.getCount();
            if (pos < count) {
                return offset + adapter.getItemViewType(pos);
            }
            pos -= count;
            offset += adapter.getViewTypeCount();
        }
        throw new ArrayIndexOutOfBoundsException("invalid position " + position);
    }

    @Override
    public int getViewTypeCount() {
        int count = 0;
        for (ListAdapter adapter : mAdapters) {
            count += adapter.getViewTypeCount();
        }
        return max(count, 1);
    }

    @Override
    public boolean isEmpty() {
        for (ListAdapter adapter : mAdapters) {
            if (!adapter.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static int max(int value1, int value2) {
        return value1 > value2 ? value1 : value2;
    }
}
