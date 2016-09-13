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

import com.github.moduth.ext.utils.OomUtils;
import com.github.moduth.ext.utils.ToastUtils;

/**
 * Tracer for exception. This is used to trace all exceptions reported via {@link #trace(Throwable)}.
 * <p>
 * Created by zhaiyifan on 2015/8/3.
 */
public final class ExceptionTracer {

    private final static String TAG = "ExceptionTracer";

    private volatile Context mContext;
    private volatile ExceptionInterceptor mExceptionInterceptor;

    private ExceptionTracer() {
    }

    /**
     * Install this exception manager, this should be called before everything.
     *
     * @param context application context.
     */
    public void install(Context context) {
        if (context == null) {
            return;
        }
        // assign only once.
        if (mContext != null) {
            return;
        }
        synchronized (this) {
            if (mContext != null) {
                return;
            }
            mContext = context.getApplicationContext();
        }
    }

    /**
     * Trace corresponding exception.
     *
     * @param e exception.
     */
    public void trace(Throwable e) {
        if (e == null) {
            return;
        }
        // hprof handle before everything.
        dumpHprofIfNeeded(e);

        // exception interceptor.
        final ExceptionInterceptor interceptor = mExceptionInterceptor;
        if (interceptor != null && interceptor.onInterceptException(e)) {
            // handled by interceptor.
            return;
        }

        // exception handling detail.
        if (e instanceof OutOfMemoryError) {
            handleOOM((OutOfMemoryError) e);
        }
    }

    /**
     * Set the exception interceptor.
     *
     * @param interceptor Exception interceptor.
     */
    public void setInterceptor(ExceptionInterceptor interceptor) {
        mExceptionInterceptor = interceptor;
    }

    // ------------ specific handle ------------
    private void handleOOM(OutOfMemoryError e) {
        // notify vm to gc.
        System.gc();
        System.gc();
    }

    // ------------ oom dump & notify -----------
    private void dumpHprofIfNeeded(Throwable e) {
        final Context context = mContext;
        if (context == null) {
            // no valid context.
            return;
        }
        if (!DebugConfig.isDebuggable(context)) {
            // dump only in debug mode.
            return;
        }
        if (OomUtils.dumpHprofIfNeeded(context, e)) {
            if (DebugConfig.isPackageDebuggable(context)) ToastUtils.show(context, "OOM occurs!!!");
        }
    }

    /**
     * Exception interceptor. This is used to intercept exception before handled by {@link ExceptionTracer}.
     */
    public interface ExceptionInterceptor {
        /**
         * Called when exception occurs.
         *
         * @param e exception.
         * @return true if this exception is handled, then {@link ExceptionTracer} will do nothing for this exception.
         */
        boolean onInterceptException(Throwable e);
    }

    // --------------- singleton ----------------
    static final class InstanceHolder {
        final static ExceptionTracer INSTANCE = new ExceptionTracer();
    }

    /**
     * Get the single instance of {@link ExceptionTracer}.
     */
    public static ExceptionTracer getInstance() {
        return InstanceHolder.INSTANCE;
    }
}
