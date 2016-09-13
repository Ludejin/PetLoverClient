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

/**
 * Log4j wrapper
 */
class Log4j {
    private static LoggerFile.Log4jWrapper log4j = LoggerFile.getLog4j(LogConstants.TAG);

    // 带 TAG 标签
    public static void v(String tag, String text) {
        log4j.trace("[" + tag + "]" + text);
    }

    public static void i(String tag, String text) {
        log4j.info("[" + tag + "]" + text);
    }

    public static void d(String tag, String text) {
        log4j.debug("[" + tag + "]" + text);
    }

    public static void w(String tag, String text) {
        log4j.warn("[" + tag + "]" + text);
    }

    public static void e(String tag, String text) {
        log4j.error("[" + tag + "]" + text);
    }

    public static void e(Exception e) {
        log4j.error(e);
    }

    // 不带TAG 标签
    public static void v(String text) {
        log4j.trace("[" + LogConstants.TAG + "]" + text);
    }

    public static void i(String text) {
        log4j.info("[" + LogConstants.TAG + "]" + text);
    }

    public static void d(String text) {
        log4j.debug("[" + LogConstants.TAG + "]" + text);
    }

    public static void w(String text) {
        log4j.warn("[" + LogConstants.TAG + "]" + text);
    }

    public static void e(String text) {
        log4j.error("[" + LogConstants.TAG + "]" + text);
    }
}
