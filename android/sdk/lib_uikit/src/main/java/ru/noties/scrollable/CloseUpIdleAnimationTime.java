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
 * This interface might be used to dynamically compute close-up animation time of a {@link ScrollableLayout}
 *
 * @see ScrollableLayout#setCloseUpIdleAnimationTime(CloseUpIdleAnimationTime)
 * @see SimpleCloseUpIdleAnimationTime
 * Created by Dimitry Ivanov on 22.05.2015.
 */
public interface CloseUpIdleAnimationTime {

    /**
     * @param layout {@link ScrollableLayout}
     * @param nowY   current scroll y of the *layout*
     * @param endY   scroll y value to which *layout* would scroll to
     * @param maxY   current max scroll y value of the *layout*
     * @return animation duration for a close-up animation
     */
    long compute(ScrollableLayout layout, int nowY, int endY, int maxY);
}
