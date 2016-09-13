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

import java.util.LinkedList;

/**
 * 一个抽象的池，定义了固定的大小，可以获得或回收对应object
 * <p>
 * Created by zhaiyifan on 2015/8/3.
 */
public abstract class Pool<T> {

    private final int mCapacity;

    /**
     * Construct a pool with corresponding capacity.
     *
     * @param capacity Capacity of this pool.
     */
    public Pool(int capacity) {
        mCapacity = capacity;
    }

    /**
     * Get capacity of this pool.
     *
     * @return Capacity of this pool.
     */
    public final int capacity() {
        return mCapacity;
    }

    /**
     * Put (recycle) item into this pool.
     *
     * @param item Item to put.
     */
    public abstract void put(T item);

    /**
     * Get item out from this pool
     */
    public abstract T get();

    /**
     * Create item if needed. This is called when pool is empty and {@link #get()} is required.
     *
     * @return A newly created item.
     */
    protected T create() {
        return null;
    }

    /**
     * Create a simple pool.
     *
     * @param capacity Capacity of this pool.
     * @return A newly created simple pool, which is not thread-safe.
     */
    public static <T> Pool<T> simplePool(int capacity) {
        return new SimplePool<T>(capacity);
    }

    /**
     * Create a synchronized thread-safe pool.
     *
     * @param capacity Capacity of this pool.
     * @return A newly created synchronized thread-safe pool.
     */
    public static <T> Pool<T> synchronizedPool(int capacity) {
        return new SynchronizedPool<T>(capacity);
    }

    /**
     * Create a thread local thread-safe pool.
     *
     * @param capacity Capacity of this pool.
     * @return A newly created thread local thread-safe pool.
     */
    public static <T> Pool<T> threadLocalPool(int capacity) {
        return new ThreadLocalPool<T>(capacity);
    }

    public static class SimplePool<T> extends Pool<T> {

        private final LinkedList<T> mList = new LinkedList<T>();

        public SimplePool(int capacity) {
            super(capacity);
        }

        @Override
        public void put(T item) {
            if (item == null) {
                return;
            }
            int capacity = capacity();
            if (capacity <= 0 || size() < capacity) {
                mList.offer(item);
            }
        }

        @Override
        public T get() {
            return size() <= 0 ? create() : mList.poll();
        }

        private int size() {
            return mList.size();
        }
    }

    /**
     * A synchronized thread-safe pool.
     */
    public static class SynchronizedPool<T> extends SimplePool<T> {

        private final Object mLock = new Object();

        public SynchronizedPool(int capacity) {
            super(capacity);
        }

        @Override
        public void put(T item) {
            synchronized (mLock) {
                super.put(item);
            }
        }

        @Override
        public T get() {
            synchronized (mLock) {
                return super.get();
            }
        }
    }

    /**
     * A thread local thread-safe pool.
     */
    public static class ThreadLocalPool<T> extends Pool<T> {

        private final ThreadLocal<LinkedList<T>> mThreadLocalList
                = new ThreadLocal<LinkedList<T>>() {
            @Override
            protected LinkedList<T> initialValue() {
                return new LinkedList<T>();
            }
        };

        public ThreadLocalPool(int capacity) {
            super(capacity);
        }

        @Override
        public void put(T item) {
            if (item == null) {
                return;
            }
            LinkedList<T> list = mThreadLocalList.get();
            int capacity = capacity();
            if (capacity <= 0 || list.size() < capacity) {
                list.offer(item);
            }
        }

        @Override
        public T get() {
            LinkedList<T> array = mThreadLocalList.get();
            return array.size() <= 0 ? create() : array.poll();
        }
    }
}
