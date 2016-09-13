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

package com.github.moduth.ext.component.info;

import android.os.Environment;

import com.github.moduth.ext.Ext;
import com.github.moduth.ext.component.logger.Logger;

/**
 * 存储器信息收集类
 * <p/>
 * Created by zhaiyifan on 2015/7/31.
 */
public class StorageDash {

    private static final String TAG = "StorageDash";

    /**
     * 是否有外部存储
     */
    public static boolean hasExternal() {
        try {
            return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        } catch (Exception e) {
            Logger.w(TAG, "hasExternal exception" + e.toString());
        }
        return false;
    }

    /**
     * 是否有只读的外部存储
     */
    public static boolean hasExternalReadable() {
        try {
            String state = Environment.getExternalStorageState();

            return Environment.MEDIA_MOUNTED.equals(state) || (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
        } catch (Exception e) {
            Logger.w(TAG, "hasExternalReadable exception" + e.toString());
        }
        return false;
    }

    /**
     * 获得外部存储器的信息
     */
    public static StorageInfo getExternalInfo() {
        if (!hasExternalReadable()) {
            return null;
        }

        return StorageInfo.fromFile(Environment.getExternalStorageDirectory());
    }

    /**
     * 获得内部存储器的信息
     */
    public static StorageInfo getInnerInfo() {
        return StorageInfo.fromFile(Ext.getContext().getFilesDir());
    }
}
