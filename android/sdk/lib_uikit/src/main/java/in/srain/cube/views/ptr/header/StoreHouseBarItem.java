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

package in.srain.cube.views.ptr.header;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.util.Random;

/**
 * Created by srain on 11/6/14.
 */
public class StoreHouseBarItem extends Animation {

    public PointF midPoint;
    public float translationX;
    public int index;

    private final Paint mPaint = new Paint();
    private float mFromAlpha = 1.0f;
    private float mToAlpha = 0.4f;
    private PointF mCStartPoint;
    private PointF mCEndPoint;

    public StoreHouseBarItem(int index, PointF start, PointF end, int color, int lineWidth) {
        this.index = index;

        midPoint = new PointF((start.x + end.x) / 2, (start.y + end.y) / 2);

        mCStartPoint = new PointF(start.x - midPoint.x, start.y - midPoint.y);
        mCEndPoint = new PointF(end.x - midPoint.x, end.y - midPoint.y);

        setColor(color);
        setLineWidth(lineWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    public void setLineWidth(int width) {
        mPaint.setStrokeWidth(width);
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void resetPosition(int horizontalRandomness) {
        Random random = new Random();
        int randomNumber = -random.nextInt(horizontalRandomness) + horizontalRandomness;
        translationX = randomNumber;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float alpha = mFromAlpha;
        alpha = alpha + ((mToAlpha - alpha) * interpolatedTime);
        setAlpha(alpha);
    }

    public void start(float fromAlpha, float toAlpha) {
        mFromAlpha = fromAlpha;
        mToAlpha = toAlpha;
        super.start();
    }

    public void setAlpha(float alpha) {
        mPaint.setAlpha((int) (alpha * 255));
    }

    public void draw(Canvas canvas) {
        canvas.drawLine(mCStartPoint.x, mCStartPoint.y, mCEndPoint.x, mCEndPoint.y, mPaint);
    }
}