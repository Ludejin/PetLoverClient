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

import android.animation.ObjectAnimator;

/**
 * This interface might be used to customize {@link ObjectAnimator} behavior during close-up animation
 *
 * @see ObjectAnimator
 * @see InterpolatorCloseUpAnimatorConfigurator
 * Created by Dimitry Ivanov on 22.05.2015.
 */
public interface CloseUpAnimatorConfigurator {

    /**
     * Note that {@link ObjectAnimator#setDuration(long)} would erase current value set by {@link CloseUpIdleAnimationTime} if any present
     *
     * @param animator current {@link ObjectAnimator} object to animate close-up animation of a {@link ScrollableLayout}
     */
    void configure(ObjectAnimator animator);
}
