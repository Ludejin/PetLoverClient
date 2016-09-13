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

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;

import com.github.moduth.ext.component.logger.Logger;

/**
 * 时间追踪
 * <p/>
 * Created by zhaiyifan on 2015/8/3.
 */
public final class TimeTracer extends Tracer {

    private final static String TAG = "TimeTracer";

    // independent debug switch.
    private final static boolean DEBUG = true;

    private TimeTracer() {
    }

    public static TimeRecord start(String msg) {
        return DEBUG ? new TimeRecord(now(), msg) : null;
    }

    public static void stop(TimeRecord record) {
        if (record != null) {
            long start = record.time;
            long end = now();
            Logger.d(TAG, record.msg + "(start:" + start + " end:" + end + " cost:" + (end - start) + ")");
        }
    }

    private static long now() {
        return SystemClock.uptimeMillis();
    }

    public static class TimeRecord implements Parcelable {

        final long time;
        String msg;

        TimeRecord(long time, String msg) {
            this.time = time;
            this.msg = msg;
        }

        public String getMessage() {
            return msg;
        }

        public TimeRecord setMessage(String msg) {
            this.msg = msg;
            return this;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        private TimeRecord(Parcel parcel) {
            this.time = parcel.readLong();
            this.msg = parcel.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(time);
            dest.writeString(msg);
        }

        public final static Creator<TimeRecord> CREATOR = new Creator<TimeRecord>() {
            @Override
            public TimeRecord createFromParcel(Parcel source) {
                return new TimeRecord(source);
            }

            @Override
            public TimeRecord[] newArray(int size) {
                return new TimeRecord[size];
            }
        };
    }
}
