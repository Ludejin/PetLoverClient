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
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

import java.util.ArrayList;

/**
 * 灵活支持list header和footer的Adapter
 */
public class HeaderAdapter<T extends ListAdapter> implements WrapperListAdapter, Filterable {

    private final T mAdapter;

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    // These two ArrayList are assumed to NOT be null.
    // They are indeed created when declared in ListView and then shared.
    ArrayList<FixedViewInfo> mHeaderViewInfos = new ArrayList<FixedViewInfo>();
    ArrayList<FixedViewInfo> mFooterViewInfos = new ArrayList<FixedViewInfo>();

    boolean mAreAllFixedViewsSelectable;

    private final boolean mIsFilterable;

    private boolean mHeaderFooterVisibleWhenEmpty = true;

    /**
     * Construct a HeaderAdapter with a base wrapped adapter.
     *
     * @param adapter Base wrapped adapter.
     */
    public HeaderAdapter(T adapter) {
        mAdapter = adapter;
        mIsFilterable = adapter instanceof Filterable;
    }

    /**
     * Returns the header count.
     *
     * @return Header count.
     */
    public int getHeadersCount() {
        return mHeaderViewInfos.size();
    }

    /**
     * Returns the footer count.
     *
     * @return Footer count.
     */
    public int getFootersCount() {
        return mFooterViewInfos.size();
    }

    /**
     * Check whether this adapter is empty.
     *
     * @return Whether this adapter is empty.
     */
    public boolean isEmpty() {
        boolean wrappedEmpty = mAdapter == null || mAdapter.isEmpty();
        return mHeaderFooterVisibleWhenEmpty ? wrappedEmpty && (getHeadersCount() + getFootersCount() == 0) : wrappedEmpty;
    }

    private boolean areAllListInfosSelectable(ArrayList<FixedViewInfo> infos) {
        if (infos != null) {
            for (FixedViewInfo info : infos) {
                if (!info.isSelectable) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Add a header.
     *
     * @param v            Header view to add.
     * @param data         Associate date of the header.
     * @param isSelectable Whether the header is selectable.
     * @param isPersist    Whether the header is persist. Persist header will be kept
     *                     even is scrolled out of screen, others are not.
     */
    public void addHeader(View v, Object data, boolean isSelectable, boolean isPersist) {
        FixedViewInfo info = new FixedViewInfo();
        info.view = v;
        info.data = data;
        info.isSelectable = isSelectable;
        info.isPersist = isPersist;

        mHeaderViewInfos.add(info);

        notifyDataSetChanged();
    }

    /**
     * Add a header which is persist.
     *
     * @param v            Header view to add.
     * @param data         Associate date of the header.
     * @param isSelectable Whether the header is selectable.
     */
    public void addHeader(View v, Object data, boolean isSelectable) {
        addHeader(v, data, isSelectable, true);
    }

    /**
     * Add a header.
     *
     * @param v            Header view to add.
     * @param isSelectable Whether the header is selectable.
     * @param isPersist    Whether the header is persist. Persist header will be kept
     *                     even is scrolled out of screen, others are not.
     */
    public void addHeader(View v, boolean isSelectable, boolean isPersist) {
        addHeader(v, null, isSelectable, isPersist);
    }

    /**
     * Add a header which is persist.
     *
     * @param v            Header view to add.
     * @param isSelectable Whether the header is selectable.
     */
    public void addHeader(View v, boolean isSelectable) {
        addHeader(v, null, isSelectable, true);
    }

    /**
     * Add a header which is selectable and persist.
     *
     * @param v Header view to add.
     */
    public void addHeader(View v) {
        addHeader(v, null, true, true);
    }

    /**
     * Add a footer.
     *
     * @param v            Footer view to add.
     * @param data         Associate date of the footer.
     * @param isSelectable Whether the footer is selectable.
     * @param isPersist    Whether the footer is persist. Persist footer will be kept
     *                     even is scrolled out of screen, others are not.
     */
    public void addFooter(View v, Object data, boolean isSelectable, boolean isPersist) {
        FixedViewInfo info = new FixedViewInfo();
        info.view = v;
        info.data = data;
        info.isSelectable = isSelectable;
        info.isPersist = isPersist;

        mFooterViewInfos.add(info);

        notifyDataSetChanged();
    }

    /**
     * Add a footer which is persist.
     *
     * @param v            Footer view to add.
     * @param data         Associate date of the footer.
     * @param isSelectable Whether the footer is selectable.
     */
    public void addFooter(View v, Object data, boolean isSelectable) {
        addFooter(v, data, isSelectable, true);
    }

    /**
     * Add a footer.
     *
     * @param v            Footer view to add.
     * @param isSelectable Whether the footer is selectable.
     * @param isPersist    Whether the footer is persist. Persist footer will be kept
     *                     even is scrolled out of screen, others are not.
     */
    public void addFooter(View v, boolean isSelectable, boolean isPersist) {
        addFooter(v, null, isSelectable, isPersist);
    }

    /**
     * Add a footer which is persist.
     *
     * @param v            Footer view to add.
     * @param isSelectable Whether the footer is selectable.
     */
    public void addFooter(View v, boolean isSelectable) {
        addFooter(v, null, isSelectable, true);
    }

    /**
     * Add a footer which is selectable and persist.
     *
     * @param v Footer view to add.
     */
    public void addFooter(View v) {
        addFooter(v, null, true, true);
    }

    /**
     * Remove a header previous added.
     *
     * @param v Header view to remove.
     * @return Whether the header is removed successfully.
     */
    public boolean removeHeader(View v) {
        for (int i = 0; i < mHeaderViewInfos.size(); i++) {
            FixedViewInfo info = mHeaderViewInfos.get(i);
            if (info.view == v) {
                mHeaderViewInfos.remove(i);

                mAreAllFixedViewsSelectable =
                        areAllListInfosSelectable(mHeaderViewInfos)
                                && areAllListInfosSelectable(mFooterViewInfos);
                notifyDataSetChanged();

                return true;
            }
        }

        return false;
    }

    /**
     * Remove a footer previous added.
     *
     * @param v Footer view to remove.
     * @return Whether the footer is removed successfully.
     */
    public boolean removeFooter(View v) {
        for (int i = 0; i < mFooterViewInfos.size(); i++) {
            FixedViewInfo info = mFooterViewInfos.get(i);
            if (info.view == v) {
                mFooterViewInfos.remove(i);

                mAreAllFixedViewsSelectable =
                        areAllListInfosSelectable(mHeaderViewInfos)
                                && areAllListInfosSelectable(mFooterViewInfos);
                notifyDataSetChanged();

                return true;
            }
        }

        return false;
    }

    /**
     * Notifies the attached observers that the underlying data has been changed
     * and any View reflecting the data set should refresh itself.
     */
    public void notifyDataSetChanged() {
        if (mAdapter != null && mAdapter instanceof BaseAdapter) {
            ((BaseAdapter) mAdapter).notifyDataSetChanged();
        } else {
            mDataSetObservable.notifyChanged();
        }
    }

    /**
     * Notifies the attached observers that the underlying data is no longer valid
     * or available. Once invoked this adapter is no longer valid and should
     * not report further data set changes.
     */
    public void notifyDataSetInvalidated() {
        if (mAdapter != null && mAdapter instanceof BaseAdapter) {
            ((BaseAdapter) mAdapter).notifyDataSetInvalidated();
        } else {
            mDataSetObservable.notifyInvalidated();
        }
    }

    @Override
    public int getCount() {
        if (mAdapter != null) {
            return getFootersCount() + getHeadersCount() + mAdapter.getCount();
        } else {
            return getFootersCount() + getHeadersCount();
        }
    }

    @Override
    public boolean areAllItemsEnabled() {
        if (mAdapter != null) {
            return mAreAllFixedViewsSelectable && mAdapter.areAllItemsEnabled();
        } else {
            return true;
        }
    }

    @Override
    public boolean isEnabled(int position) {
        // Header (negative positions will throw an ArrayIndexOutOfBoundsException)
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return mHeaderViewInfos.get(position).isSelectable;
        }

        // Adapter
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getCount();
            if (adjPosition < adapterCount) {
                return mAdapter.isEnabled(adjPosition);
            }
        }

        // Footer (off-limits positions will throw an ArrayIndexOutOfBoundsException)
        return mFooterViewInfos.get(adjPosition - adapterCount).isSelectable;
    }

    @Override
    public Object getItem(int position) {
        // Header (negative positions will throw an ArrayIndexOutOfBoundsException)
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return mHeaderViewInfos.get(position).data;
        }

        // Adapter
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItem(adjPosition);
            }
        }

        // Footer (off-limits positions will throw an ArrayIndexOutOfBoundsException)
        return mFooterViewInfos.get(adjPosition - adapterCount).data;
    }

    @Override
    public long getItemId(int position) {
        int numHeaders = getHeadersCount();
        if (mAdapter != null && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = mAdapter.getCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    @Override
    public boolean hasStableIds() {
        if (mAdapter != null) {
            return mAdapter.hasStableIds();
        }
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Header (negative positions will throw an ArrayIndexOutOfBoundsException)
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return mHeaderViewInfos.get(position).view;
        }

        // Adapter
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getView(adjPosition, convertView, parent);
            }
        }

        // Footer (off-limits positions will throw an ArrayIndexOutOfBoundsException)
        return mFooterViewInfos.get(adjPosition - adapterCount).view;
    }

    @Override
    public int getItemViewType(int position) {
        int numHeaders = getHeadersCount();
        if (mAdapter != null && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = mAdapter.getCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            }
        }
        FixedViewInfo fixedViewInfo = null;
        if (position < numHeaders) {
            fixedViewInfo = mHeaderViewInfos.get(position);
        } else {
            int index = position - numHeaders - (mAdapter == null ? 0 : mAdapter.getCount());
            if (index >= 0 && index < getFootersCount()) {
                fixedViewInfo = mFooterViewInfos.get(index);
            }
        }

        return (fixedViewInfo == null || fixedViewInfo.isPersist)
                ? android.widget.AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER
                : android.widget.AdapterView.ITEM_VIEW_TYPE_IGNORE;
    }

    @Override
    public int getViewTypeCount() {
        if (mAdapter != null) {
            return mAdapter.getViewTypeCount();
        }
        return 1;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(observer);
        }
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(observer);
        }
    }

    @Override
    public Filter getFilter() {
        if (mIsFilterable) {
            return ((Filterable) mAdapter).getFilter();
        }
        return null;
    }

    @Override
    public T getWrappedAdapter() {
        return mAdapter;
    }

    /**
     * Set whether header or footer will be visible when wrapped adapter is empty. Default is {@code true}.
     *
     * @param visible whether header of footer will be visible.
     */
    public void setHeaderFooterVisibleWhenEmpty(boolean visible) {
        mHeaderFooterVisibleWhenEmpty = visible;
    }

    private class FixedViewInfo {
        /**
         * The view to add to the list
         */
        public View view;
        /**
         * The data backing the view. This is returned from {@link ListAdapter#getItem(int)}.
         */
        public Object data;
        /**
         * <code>true</code> if the fixed view should be selectable in the list
         */
        public boolean isSelectable;
        /**
         * <code>true</code> if the fixed view should be persist in the list
         */
        public boolean isPersist;
    }
}
