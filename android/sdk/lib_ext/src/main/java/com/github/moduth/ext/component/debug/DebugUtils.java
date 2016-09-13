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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <p>Utilities for debug.</p>
 * Created by zhaiyifan on 2015/8/3.
 */
public final class DebugUtils {

    private final static String[] LOGCAT_COMMAND = new String[]{
            "logcat",
            "-d",
            "-v time"
    };

    private static volatile String sLineSeparator;

    private DebugUtils() {
        // static usage.
    }

    /**
     * Dump whole logcat.
     *
     * @return Logcat.
     */
    public static String dumpLogcat() {
        return dumpLogcat(-1);
    }

    /**
     * Dump logcat with corresponding length.
     *
     * @param maxLength Maximum length of logcat to dump.
     * @return Logcat.
     */
    public static String dumpLogcat(int maxLength) {

        final StringBuilder log = new StringBuilder();

        Process process = null;
        BufferedReader reader = null;
        try {
            String[] commandLine = LOGCAT_COMMAND;
            process = Runtime.getRuntime().exec(commandLine);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null && (maxLength < 0 || log.length() < maxLength)) {
                log.append(line);
                log.append(getLineSeparator());
            }
        } catch (Throwable e) {
            // empty.
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // empty.
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
        return log.toString();
    }

    private static String getLineSeparator() {
        if (sLineSeparator == null) {
            sLineSeparator = System.getProperty("line.separator");
        }
        return sLineSeparator;
    }
}
