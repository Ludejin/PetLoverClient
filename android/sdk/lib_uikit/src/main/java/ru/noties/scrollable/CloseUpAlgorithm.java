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

package ru.noties.scrollable;

/**
 * Use this interface to handle specific *close-up* logic for {@link ScrollableLayout},
 * use with {@link ScrollableLayout#setCloseUpAlgorithm(CloseUpAlgorithm)}
 *
 * @see DefaultCloseUpAlgorithm
 * Created by Dimitry Ivanov on 22.05.2015.
 */
public interface CloseUpAlgorithm {

    /**
     * This method computes end scroll y after fling event was detected
     *
     * @param layout            {@link ScrollableLayout}
     * @param isScrollingBottom whether {@link ScrollableLayout} would scroll to top or bottom
     * @param nowY              current scroll y of the *layout*
     * @param suggestedY        scroll y that is suggested
     * @param maxY              current max scroll y of the *layout*
     * @return end scroll y value for the *layout* to animate to
     */
    int getFlingFinalY(ScrollableLayout layout, boolean isScrollingBottom, int nowY, int suggestedY, int maxY);

    /**
     * This method will be fired after scroll state of a {@link ScrollableLayout} would be considered idle
     *
     * @param layout {@link ScrollableLayout}
     * @param nowY   current scroll y of the *layout*
     * @param maxY   current max scroll y of the *layout*
     * @return end scroll y value for the *layout* to animate to
     * @see ScrollableLayout#getConsiderIdleMillis()
     * @see ScrollableLayout#setConsiderIdleMillis(long)
     */
    int getIdleFinalY(ScrollableLayout layout, int nowY, int maxY);
}
