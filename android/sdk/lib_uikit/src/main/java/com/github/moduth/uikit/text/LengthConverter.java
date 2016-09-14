/*
 * The GPL License (GPL)
 *
 * Copyright (c) 2016 Moduth (https://github.com/moduth)
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

package com.github.moduth.uikit.text;

/**
 * Length converter to determine actual length of text.
 */
public interface LengthConverter {
    /**
     * Get the converted length of source char sequence.
     *
     * @param src      source char sequence.
     * @param srcStart start index of source.
     * @param srcEnd   end index of source.
     * @return converted length.
     */
    int convert(CharSequence src, int srcStart, int srcEnd);

    /**
     * Get the original length of source char sequence.
     *
     * @param src      source char sequence.
     * @param srcStart start index of source.
     * @param dstEnd   end index of converted.
     * @return reversed length.
     */
    int reverse(CharSequence src, int srcStart, int dstEnd);

    /**
     * Default {@link LengthConverter} instance.
     */
    LengthConverter DEFAULT_CONVERTER = new LengthConverter() {

        @Override
        public int convert(CharSequence src, int srcStart, int srcEnd) {
            int length = srcEnd - srcStart;
            return length >= 0 ? length : -length;
        }

        @Override
        public int reverse(CharSequence src, int srcStart, int dstEnd) {
            int length = dstEnd - srcStart;
            return length >= 0 ? length : -length;
        }
    };

    /**
     * Chinese {@link LengthConverter} instance.
     */
    LengthConverter CHINESE_CONVERTER = new LengthConverter() {
        @Override
        public int convert(CharSequence src, int srcStart, int srcEnd) {
            int s = srcStart <= srcEnd ? srcStart : srcEnd;
            int e = srcStart <= srcEnd ? srcEnd : srcStart;
            int chineseCount = 0;
            for (int i = s; i < e && i < src.length(); i++) {
                if (isChinese(src.charAt(i))) {
                    chineseCount++;
                }
            }
            return e - s + chineseCount;
        }

        @Override
        public int reverse(CharSequence src, int srcStart, int dstEnd) {
            int s = srcStart <= dstEnd ? srcStart : dstEnd;
            int e = srcStart <= dstEnd ? dstEnd : srcStart;
            int length = 0;
            int chineseCount = 0;
            for (int i = s; i < src.length(); i++) {
                if (isChinese(src.charAt(i))) {
                    chineseCount++;
                }
                if (i + chineseCount >= e) {
                    length = i - s;
                    break;
                }
            }
            return length;
        }

        private boolean isChinese(char c) {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
            if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS                     // 中日韩统一表意文字
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS        // 中日韩兼容字符
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  // 中日韩统一表意文字扩充A
                    || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION                 // 一般标点符号, 判断中文的“号
                    || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION         // 符号和标点, 判断中文的。号
                    || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS       // 半角及全角字符, 判断中文的，号
                    ) {

                return true;
            }
            return false;
        }
    };
}
