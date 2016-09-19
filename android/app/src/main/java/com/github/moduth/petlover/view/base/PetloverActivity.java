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

package com.github.moduth.petlover.view.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Window;

import com.github.moduth.petlover.PetloverApplication;
import com.github.moduth.petlover.R;
import com.github.moduth.petlover.data.exception.ResponseException;
import com.github.moduth.petlover.domain.exception.ErrorBundle;
import com.github.moduth.ext.utils.StringUtils;
import com.github.moduth.ext.utils.ToastUtils;
import com.github.moduth.petlover.exception.ErrorMessageFactory;
import com.github.moduth.petlover.internal.di.components.ApplicationComponent;
import com.github.moduth.petlover.internal.di.module.ActivityModule;
import com.github.moduth.petlover.usersystem.UserSystem;
import com.github.moduth.uiframework.BaseActivity;

/**
 * Base {@link BaseActivity} class with DI for every Activity in this application.
 *
 * @author markzhai
 * @version 1.0.0
 */
public abstract class PetloverActivity extends BaseActivity implements ToolbarHelper.ToolbarProvider {

    private ActivityModule mActivityModule;

    protected Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getApplicationComponent().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        StatService.trackBeginPage(this, pageName());
    }

    @Override
    protected void onPause() {
        super.onPause();
//        StatService.trackEndPage(this, pageName());
    }

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link ApplicationComponent}
     */
    protected ApplicationComponent getApplicationComponent() {
        return ((PetloverApplication) getApplication()).getApplicationComponent();
    }

    /**
     * Get an Activity module for dependency injection.
     *
     * @return {@link ActivityModule}
     */
    protected ActivityModule getActivityModule() {
        if (mActivityModule == null) {
            mActivityModule = new ActivityModule(this);
        }
        return mActivityModule;
    }


    public UserSystem getUserSystem() {
        return getApplicationComponent().userSystem();
    }


    // ============= toolbar related =================

    protected ToolbarHelper mToolbarHelper;

    @Override
    public void provideToolbar() {
        mToolbarHelper = new ToolbarHelper((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(mToolbarHelper.getToolbar());
        if (mToolbarHelper.getToolbarTitle() != null) {
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (!isChild()) {
            onTitleChanged(getTitle(), getTitleColor());
        }
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        String titleTemp = title.toString();
        if (!StringUtils.equal(titleTemp, getString(R.string.app_name))) {
            if (mToolbarHelper != null) {
                mToolbarHelper.setTitle(title);
            }
        }
    }

    // ============= data related =================

    public boolean handleCommonResponseError(Exception exception) {
        boolean handled = false;
        if (exception instanceof ResponseException) {
            ResponseException responseException = (ResponseException) exception;
//            switch (responseException.getStatusCode()) {
//                case ResponseException.ERROR_CODE_NEED_LOGIN:
//                    handled = true;
//                    getUserSystem().logout();
//                    if (!(this instanceof LoginActivity)) {
//                        getNavigator().navigateToLoginPage(this);
//                    }
//                    break;
//                case ResponseException.ERROR_CODE_NEED_PERFECT_PROFILE:
//                    handled = true;
//                    if (responseException.getVuser() != null) {
//                        getUserSystem().setVuserWrapper(responseException.getVuser());
//                    }
//                    if (!(this instanceof PerfectProfileActivity)) {
//                        getNavigator().navigateToPerfectProfile(this);
//                    }
//                    break;
//                case ResponseException.ERROR_CODE_NEED_THIRD_PARTY_BIND:
//                    handled = true;
//                    getNavigator().navigateToThirdPartyBind(this);
//                    break;
//            }
        }
        return handled;
    }

    public void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this, errorBundle.getException());
        showErrorMessage(errorMessage);
    }

    protected void showErrorMessage(String errorMessage) {
        if (StringUtils.isNotEmpty(errorMessage)) {
            ToastUtils.show(this, errorMessage);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        MrApplication.getInstance().refWatcher.watch(this);
    }

    protected abstract String pageName();
}
