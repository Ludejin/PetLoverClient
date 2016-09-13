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

package com.github.moduth.ext.utils.byteunits;

/**
 * A {@code ByteUnit} represents a size at a given unit of granularity which can be converted
 * into bytes. A {@code ByteUnit} does not maintain byte size information, but only helps use byte
 * size representations that may be maintained separately across various contexts.
 *
 * @see DecimalByteUnit
 * @see BinaryByteUnit
 * @see BitUnit
 */
public interface ByteUnit {
    /**
     * Converts the given size in the given unit to bytes. Conversions with arguments that would
     * numerically overflow saturate to {@code Long.MIN_VALUE} if negative or {@code Long.MAX_VALUE}
     * if positive.
     *
     * @param count the bit count
     * @return the converted count, or {@code Long.MIN_VALUE} if conversion would negatively
     * overflow, or {@code Long.MAX_VALUE} if it would positively overflow.
     */
    long toBytes(long count);
}
