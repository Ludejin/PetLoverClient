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

import android.os.StatFs;

import java.io.File;

/**
 * 存储信息
 * <p/>
 * Created by zhaiyifan on 2015/7/31.
 */
public class StorageInfo {
    private File rootPath;
    private long totalSize;
    private long availableSize;

    public File getRootPath() {
        return rootPath;
    }

    public void setRootPath(File rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * 获得存储器总容量
     */
    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    /**
     * 获得存储器可用容量
     */
    public long getAvailableSize() {
        return availableSize;
    }

    public void setAvailableSize(long availableSize) {
        this.availableSize = availableSize;
    }

    /**
     * 从文件对象获得存储器信息
     *
     * @param path 文件对象
     */
    public static StorageInfo fromFile(File path) {
        StorageInfo info = new StorageInfo();

        info.setRootPath(path);

        try {
            StatFs fileSystem = new StatFs(path.getAbsolutePath());

            long blockSize = fileSystem.getBlockSize();
            long totalBlocks = fileSystem.getBlockCount();
            long availableBlocks = fileSystem.getAvailableBlocks();

            info.setTotalSize(totalBlocks * blockSize);
            info.setAvailableSize(availableBlocks * blockSize);
        } catch (Exception e) {
            info.setAvailableSize(0);
            info.setTotalSize(0);
        }

        return info;
    }

    @Override
    public String toString() {
        return String.format("[%s : %d / %d]", (rootPath == null ? "NULL" : rootPath.getAbsolutePath()),
                getAvailableSize(), getTotalSize());
    }
}