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

package com.github.moduth.ext.component.drawable;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;

/**
 * A single drawable container which supports {@link Matrix} transform.
 */
public class MatrixDrawable extends DrawableDecorator {

    private final MatrixState mState;
    private final Matrix mMatrix = new Matrix();

    /**
     * Construct a MatrixDrawable container.
     *
     * @param drawable Drawable to contain.
     */
    public MatrixDrawable(Drawable drawable) {
        this(drawable, null);
    }

    /**
     * Construct a MatrixDrawable with corresponding matrix.
     *
     * @param drawable Drawable to contain.
     * @param matrix   Matrix to apply.
     */
    public MatrixDrawable(Drawable drawable, Matrix matrix) {
        mState = new MatrixState(null, this, null);
        mState.setDrawable(drawable);
        setConstantState(mState);
        setMatrix(matrix);
    }

    /**
     * Return the drawable' matrix. This is applied to it's inner drawable when it is drawn.
     * Do not change this matrix in place. If you want a different matrix
     * applied to the drawable, be sure to call {@link #setMatrix}.
     */
    public Matrix getMatrix() {
        return mMatrix;
    }

    /**
     * Set the drawable' matrix, This is applied to it's inner drawable when it is drawn.
     *
     * @param matrix Matrix to apply.
     */
    public void setMatrix(Matrix matrix) {
        // collaps null and identity to just null
        if (matrix != null && matrix.isIdentity()) {
            matrix = null;
        }

        // don't invalidate unless we're actually changing our matrix
        if (matrix == null && !mMatrix.isIdentity() ||
                matrix != null && !mMatrix.equals(matrix)) {
            mMatrix.set(matrix);
            invalidateSelf();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (mMatrix.isIdentity()) {
            super.draw(canvas);
        } else {
            int saveCount = canvas.getSaveCount();
            canvas.save();

            canvas.concat(mMatrix);
            super.draw(canvas);

            canvas.restoreToCount(saveCount);
        }
    }

    static class MatrixState extends DrawableDecoratorState {

        MatrixState(DrawableDecoratorState orig, DrawableDecorator owner, Resources res) {
            super(orig, owner, res);
        }

        @Override
        public Drawable newDrawable() {
            return new MatrixDrawable(this, null);
        }

        @Override
        public Drawable newDrawable(Resources res) {
            return new MatrixDrawable(this, res);
        }
    }

    private MatrixDrawable(MatrixState state, Resources res) {
        mState = new MatrixState(state, this, res);
        setConstantState(mState);
    }
}
