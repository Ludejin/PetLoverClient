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
package com.github.moduth.ext.component.logger;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Appender for {@link android.util.Log}
 *
 * @author Rolf Kulemann, Pascal Bockhorn
 */
class LogCatAppender extends AppenderSkeleton {

    protected Layout tagLayout;

    public LogCatAppender(final Layout messageLayout, final Layout tagLayout) {
        this.tagLayout = tagLayout;
        setLayout(messageLayout);
    }

    public LogCatAppender(final Layout messageLayout) {
        this(messageLayout, new PatternLayout("%c"));
    }

    public LogCatAppender() {
        this(new PatternLayout("%m%n"));
    }

    @Override
    protected void append(final LoggingEvent le) {
        switch (le.getLevel().toInt()) {
            case Level.TRACE_INT:
                if (le.getThrowableInformation() != null) {
                    Log.v(getTagLayout().format(le), getLayout().format(le), le.getThrowableInformation().getThrowable());
                } else {
                    Log.v(getTagLayout().format(le), getLayout().format(le));
                }
                break;
            case Level.DEBUG_INT:
                if (le.getThrowableInformation() != null) {
                    Log.d(getTagLayout().format(le), getLayout().format(le), le.getThrowableInformation().getThrowable());
                } else {
                    Log.d(getTagLayout().format(le), getLayout().format(le));
                }
                break;
            case Level.INFO_INT:
                if (le.getThrowableInformation() != null) {
                    Log.i(getTagLayout().format(le), getLayout().format(le), le.getThrowableInformation().getThrowable());
                } else {
                    Log.i(getTagLayout().format(le), getLayout().format(le));
                }
                break;
            case Level.WARN_INT:
                if (le.getThrowableInformation() != null) {
                    Log.w(getTagLayout().format(le), getLayout().format(le), le.getThrowableInformation().getThrowable());
                } else {
                    Log.w(getTagLayout().format(le), getLayout().format(le));
                }
                break;
            case Level.ERROR_INT:
                if (le.getThrowableInformation() != null) {
                    Log.e(getTagLayout().format(le), getLayout().format(le), le.getThrowableInformation().getThrowable());
                } else {
                    Log.e(getTagLayout().format(le), getLayout().format(le));
                }
                break;
            case Level.FATAL_INT:
                if (le.getThrowableInformation() != null) {
                    Log.e(getTagLayout().format(le), getLayout().format(le), le.getThrowableInformation().getThrowable());
                } else {
                    Log.e(getTagLayout().format(le), getLayout().format(le));
                }
                break;
        }
    }

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

    public Layout getTagLayout() {
        return tagLayout;
    }

    public void setTagLayout(final Layout tagLayout) {
        this.tagLayout = tagLayout;
    }
}