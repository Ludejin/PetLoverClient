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

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Environment;

import com.github.moduth.ext.utils.FileUtils;

import org.apache.log4j.Level;

import java.io.File;

/**
 * Log4j configure
 */
public class LoggerFile {

    public static boolean hasConfigured = false;

    public static abstract class Log4jWrapper {

        public abstract void trace(Object message);

        public abstract void trace(Object message, Throwable t);

        public abstract void debug(Object message);

        public abstract void debug(Object message, Throwable t);

        public abstract void info(Object message);

        public abstract void info(Object message, Throwable t);

        public abstract void warn(Object message);

        public abstract void warn(Object message, Throwable t);

        public abstract void warn(Throwable t);

        public abstract void error(Object message);

        public abstract void error(Object message, Throwable t);

        public abstract void error(Throwable t);

        public abstract void fatal(Object message);

        public abstract void fatal(Object message, Throwable t);

        public abstract void fatal(Throwable t);
    }

    public static boolean configure() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }

        try {
            final LogConfigurator logConfigurator = new LogConfigurator();
            logConfigurator.setFileName(LogConstants.PATH + LogConstants.FILE_NAME);
            logConfigurator.setRootLevel(Level.ALL);
            logConfigurator.setFilePattern("%d - [%p::%c] - %m%n");
            int flags = 0;
            // per file 5MB in debug mode, 512KB in release mode
            boolean isDebugMode = (flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            if (isDebugMode) {
                logConfigurator.setMaxFileSize(5 * 1024 * 1024);
            } else {
                logConfigurator.setMaxFileSize(512 * 1024);
            }
            Log.i("isDebugMode", "configure() flags===>>>>" + flags
                    + ",ApplicationInfo.FLAG_DEBUGGABLE===>>>"
                    + ApplicationInfo.FLAG_DEBUGGABLE + ",isDebugMode==>>"
                    + isDebugMode);
            logConfigurator.configure();
            hasConfigured = true;

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean configure(Context context) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }

        try {
            final LogConfigurator logConfigurator = new LogConfigurator();

            FileUtils.mkdirs(new File(LogConstants.PATH));

            logConfigurator.setImmediateFlush(true);
            logConfigurator.setFileName(LogConstants.PATH + LogConstants.FILE_NAME);
            logConfigurator.setRootLevel(Level.ALL);
            logConfigurator.setFilePattern("%d - [%p::%c] - %m%n");
            int flags = 0;
            try {
                flags = context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0).applicationInfo.flags;
            } catch (Exception e) {
                e.printStackTrace();
            }
            // per file 5MB in debug mode, 512KB in release mode
            boolean isDebugMode = (flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            if (isDebugMode) {
                logConfigurator.setMaxFileSize(5 * 1024 * 1024);
            } else {
                logConfigurator.setMaxFileSize(512 * 1024);
            }

            Log.i("isDebugMode", "configure(Context context) flags===>>>>" + flags
                    + ",ApplicationInfo.FLAG_DEBUGGABLE===>>>"
                    + ApplicationInfo.FLAG_DEBUGGABLE + ",isDebugMode==>>"
                    + isDebugMode);
            logConfigurator.configure();
            hasConfigured = true;

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Log4jWrapper getLog4j(String str) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (!hasConfigured) {
                if (configure()) {
                    return new LogToFile(org.apache.log4j.Logger.getLogger(str));
                }
            }
            return new LogToFile(org.apache.log4j.Logger.getLogger(str));
        }
        return null;
    }
}
