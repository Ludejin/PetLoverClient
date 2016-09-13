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

package com.github.moduth.ext.component.debug;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * <p>Configuration for debug.</p>
 * Created by zhaiyifan on 2015/8/3.
 */
public final class DebugConfig {

    private static boolean sRuntimeDebuggable = false;
    private static volatile Boolean sPackageDebuggable = null;

    private DebugConfig() {
        // static usage.
    }

    /**
     * Whether <em>RUNTIME</em> or <em>PACKAGE</em> is debuggable.
     *
     * @param context package context.
     * @return true if runtime or package is debuggable.
     */
    public static boolean isDebuggable(Context context) {
        return isRuntimeDebuggable() || isPackageDebuggable(context);
    }

    /**
     * Whether <em>RUNTIME</em> is debuggable.
     *
     * @return true if runtime is debuggable.
     */
    public static boolean isRuntimeDebuggable() {
        return sRuntimeDebuggable;
    }

    /**
     * Whether this <em>PACKAGE</em> is debuggable.
     *
     * @param context package context.
     * @return true if this package is debuggable.
     */
    public static boolean isPackageDebuggable(Context context) {
        if (sPackageDebuggable != null) {
            return sPackageDebuggable;
        }
        synchronized (DebugConfig.class) {
            if (sPackageDebuggable != null) {
                return sPackageDebuggable;
            }
            ApplicationInfo appInfo = context != null ? context.getApplicationInfo() : null;
            if (appInfo == null) {
                return false;
            }
            return sPackageDebuggable = ((appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
        }
    }

    /**
     * Set whether <em>RUNTIME</em> is debuggable.
     *
     * @param debuggable whether runtime is debuggable.
     */
    public static void setRuntimeDebuggable(boolean debuggable) {
        sRuntimeDebuggable = debuggable;
    }
}
