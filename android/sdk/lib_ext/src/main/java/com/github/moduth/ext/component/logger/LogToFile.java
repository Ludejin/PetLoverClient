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

class LogToFile extends LoggerFile.Log4jWrapper {

    private org.apache.log4j.Logger log4j = null;

    public LogToFile(org.apache.log4j.Logger log4j) {
        this.log4j = log4j;
    }

    public void trace(Object message) {
        try {
            log4j.trace(message);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void trace(Object message, Throwable t) {
        try {
            log4j.trace(message, t);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void debug(Object message) {
        try {
            log4j.debug(message);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void debug(Object message, Throwable t) {
        try {
            log4j.debug(message, t);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void info(Object message) {
        try {
            log4j.info(message);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void info(Object message, Throwable t) {
        try {
            log4j.info(message, t);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void warn(Object message) {
        try {
            log4j.warn(message);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void warn(Object message, Throwable t) {
        try {
            log4j.warn(message, t);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void warn(Throwable t) {
        try {
            log4j.warn(t, t);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void error(Object message) {
        try {
            log4j.error(message);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void error(Object message, Throwable t) {
        try {
            log4j.error(message, t);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void error(Throwable t) {
        try {
            log4j.error(t, t);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void fatal(Object message) {
        try {
            log4j.fatal(message);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void fatal(Object message, Throwable t) {
        try {
            log4j.fatal(message, t);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void fatal(Throwable t) {
        try {
            log4j.fatal(t, t);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
}
