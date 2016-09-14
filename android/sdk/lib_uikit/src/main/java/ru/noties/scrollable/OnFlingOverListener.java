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
 * Class to listen for `fling` events and translate possible vertical scroll
 * to child view. This functionality would be useful (and actually used only for that)
 * to create effect of scroll continuation when ScrollableLayout completely collapses.
 * Created by Dimitry Ivanov on 13.04.2016.
 */
public interface OnFlingOverListener {
    /**
     * This method will be called, when ScrollableLayout completely collapses,
     * but initial fling event had big velocity. So this method will be called with
     * theoretical vertical scroll value to scroll the underlining child
     *
     * @param y        the final scroll y (theoretical) for underlining scrolling child
     * @param duration theoretical duration of the scroll event based on velocity value
     *                 of touch event
     */
    void onFlingOver(int y, long duration);
}
