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

package in.srain.cube.views.ptr;

/**
 * Run a hook runnable, the runnable will run only once.
 * After the runnable is done, call resume to resume.
 * Once run, call takeover will directory call the resume action
 */
public abstract class PtrUIHandlerHook implements Runnable {

    private Runnable mResumeAction;
    private static final byte STATUS_PREPARE = 0;
    private static final byte STATUS_IN_HOOK = 1;
    private static final byte STATUS_RESUMED = 2;
    private byte mStatus = STATUS_PREPARE;

    public void takeOver() {
        takeOver(null);
    }

    public void takeOver(Runnable resumeAction) {
        if (resumeAction != null) {
            mResumeAction = resumeAction;
        }
        switch (mStatus) {
            case STATUS_PREPARE:
                mStatus = STATUS_IN_HOOK;
                run();
                break;
            case STATUS_IN_HOOK:
                break;
            case STATUS_RESUMED:
                resume();
                break;
        }
    }

    public void reset() {
        mStatus = STATUS_PREPARE;
    }

    public void resume() {
        if (mResumeAction != null) {
            mResumeAction.run();
        }
        mStatus = STATUS_RESUMED;
    }

    /**
     * Hook should always have a resume action, which is hooked by this hook.
     *
     * @param runnable
     */
    public void setResumeAction(Runnable runnable) {
        mResumeAction = runnable;
    }
}