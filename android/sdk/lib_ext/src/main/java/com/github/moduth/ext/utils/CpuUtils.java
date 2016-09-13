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

package com.github.moduth.ext.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import com.github.moduth.ext.component.logger.Logger;

/**
 * CPU工具类
 */
public final class CpuUtils {

    private final static String TAG = "CpuUtils";

    private static volatile Integer sCoreNum;
    private static volatile Long sMaxFrequency;

    private CpuUtils() {
        // static usage.
    }

    /**
     * Get the number of cpu cores.
     *
     * @return The number of cpu cores.
     */
    public static int getNumCores() {
        if (sCoreNum != null) {
            return sCoreNum;
        }
        synchronized (CpuUtils.class) {
            if (sCoreNum != null) {
                return sCoreNum;
            }
            return sCoreNum = obtainNumCores();
        }
    }

    private static int obtainNumCores() {
        // Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                // Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        int coreNum = 1;
        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            // Return the number of cores (virtual CPU devices)
            coreNum = files.length;
        } catch (Exception e) {
            Logger.i(TAG, "fail to obtain cpu core num", e);
        }
        return coreNum;
    }

    /**
     * Get the maximum cpu frequency.
     *
     * @return The maximum cpu frequency.
     */
    public static long getCpuMaxFrequency() {
        if (sMaxFrequency != null) {
            return sMaxFrequency;
        }
        synchronized (CpuUtils.class) {
            if (sMaxFrequency != null) {
                return sMaxFrequency;
            }
            return sMaxFrequency = obtainCpuMaxFrequency();
        }
    }

    private static long obtainCpuMaxFrequency() {
        long freq = 0;
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            freq = Long.parseLong(line);
        } catch (Throwable e) {
            Logger.i(TAG, "fail to obtain cpu max frequency", e);
        }
        return freq;
    }
}
