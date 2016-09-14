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

package in.srain.cube.views.ptr.indicator;

public class PtrTensionIndicator extends PtrIndicator {

    private float DRAG_RATE = 0.5f;
    private float mDownY;
    private float mDownPos;
    private float mOneHeight = 0;

    private float mCurrentDragPercent;

    private int mReleasePos;
    private float mReleasePercent = -1;

    @Override
    public void onPressDown(float x, float y) {
        super.onPressDown(x, y);
        mDownY = y;
        mDownPos = getCurrentPosY();
    }

    @Override
    public void onRelease() {
        super.onRelease();
        mReleasePos = getCurrentPosY();
        mReleasePercent = mCurrentDragPercent;
    }

    @Override
    public void onUIRefreshComplete() {
        mReleasePos = getCurrentPosY();
        mReleasePercent = getOverDragPercent();
    }

    @Override
    public void setHeaderHeight(int height) {
        super.setHeaderHeight(height);
        mOneHeight = height * 4f / 5;
    }

    @Override
    protected void processOnMove(float currentX, float currentY, float offsetX, float offsetY) {

        if (currentY < mDownY) {
            super.processOnMove(currentX, currentY, offsetX, offsetY);
            return;
        }

        // distance from top
        final float scrollTop = (currentY - mDownY) * DRAG_RATE + mDownPos;
        final float currentDragPercent = scrollTop / mOneHeight;

        if (currentDragPercent < 0) {
            setOffset(offsetX, 0);
            return;
        }

        mCurrentDragPercent = currentDragPercent;

        // 0 ~ 1
        float boundedDragPercent = Math.min(1f, Math.abs(currentDragPercent));
        float extraOS = scrollTop - mOneHeight;

        // 0 ~ 2
        // if extraOS lower than 0, which means scrollTop lower than onHeight, tensionSlingshotPercent will be 0.
        float tensionSlingshotPercent = Math.max(0, Math.min(extraOS, mOneHeight * 2) / mOneHeight);

        float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow((tensionSlingshotPercent / 4), 2)) * 2f;
        float extraMove = (mOneHeight) * tensionPercent / 2;
        int targetY = (int) ((mOneHeight * boundedDragPercent) + extraMove);
        int change = targetY - getCurrentPosY();

        setOffset(currentX, change);
    }

    private float offsetToTarget(float scrollTop) {

        // distance from top
        final float currentDragPercent = scrollTop / mOneHeight;

        mCurrentDragPercent = currentDragPercent;

        // 0 ~ 1
        float boundedDragPercent = Math.min(1f, Math.abs(currentDragPercent));
        float extraOS = scrollTop - mOneHeight;

        // 0 ~ 2
        // if extraOS lower than 0, which means scrollTop lower than mOneHeight, tensionSlingshotPercent will be 0.
        float tensionSlingshotPercent = Math.max(0, Math.min(extraOS, mOneHeight * 2) / mOneHeight);

        float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow((tensionSlingshotPercent / 4), 2)) * 2f;
        float extraMove = (mOneHeight) * tensionPercent / 2;
        int targetY = (int) ((mOneHeight * boundedDragPercent) + extraMove);

        return 0;
    }

    @Override
    public int getOffsetToKeepHeaderWhileLoading() {
        return getOffsetToRefresh();
    }

    @Override
    public int getOffsetToRefresh() {
        return (int) mOneHeight;
    }

    public float getOverDragPercent() {
        if (isUnderTouch()) {
            return mCurrentDragPercent;
        } else {
            if (mReleasePercent <= 0) {
                return 1.0f * getCurrentPosY() / getOffsetToKeepHeaderWhileLoading();
            }
            // after release
            return mReleasePercent * getCurrentPosY() / mReleasePos;
        }
    }
}
