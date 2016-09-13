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

package com.github.moduth.ext.component.cache.file;

import android.support.v4.util.LruCache;

import com.github.moduth.ext.utils.FileUtils;

import java.io.File;

/**
 * 文件缓存，内部使用LRU cache实现，外部使用请通过 {@link FileCacheManager}。
 * <p/>
 * Created by zhaiyifan on 2015/8/4.
 */
final class FileCache<K> {

    private final int mMaxSize;
    private LruCache<K, String> mLruCache;

    public FileCache(int maxSize) {
        mMaxSize = maxSize > 0 ? maxSize : 0;
        mLruCache = new LruCache<K, String>(maxSize > 0 ? maxSize : 1) {
            @Override
            protected int sizeOf(K key, String value) {
                return getFileSize(value);
            }

            @Override
            protected void entryRemoved(boolean evicted, K key, String oldValue, String newValue) {
                if (oldValue == newValue) {
                    return;
                }
                if (oldValue != null && oldValue.equals(newValue)) {
                    return;
                }
                recycle(oldValue);
            }
        };
    }

    public String get(K key) {
        if (mMaxSize <= 0) {
            return null;
        }
        return mLruCache.get(key);
    }

    public void put(K key, String file) {
        if (mMaxSize <= 0) {
            return;
        }
        mLruCache.put(key, file);
    }

    public void remove(K key) {
        if (mMaxSize <= 0) {
            return;
        }
        mLruCache.remove(key);
    }

    public void trimToSize(int maxSize) {
        if (mMaxSize <= 0) {
            return;
        }
        mLruCache.trimToSize(maxSize > 0 ? maxSize : 0);
    }

    public void clear() {
        if (mMaxSize <= 0) {
            return;
        }
        mLruCache.evictAll();
    }

    public int size() {
        if (mMaxSize <= 0) {
            return 0;
        }
        return mLruCache.size();
    }

    public int maxSize() {
        return mMaxSize;
    }

    private void recycle(String path) {
        synchronized (this) {
            if (path != null) {
                FileUtils.delete(new File(path));
            }
        }
    }

    private static int getFileSize(String path) {
        return (path == null || path.length() == 0) ? 0 : 1;
    }
}