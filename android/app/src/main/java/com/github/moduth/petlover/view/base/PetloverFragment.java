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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.moduth.ext.component.logger.Logger;
import com.github.moduth.petlover.R;
import com.github.moduth.petlover.internal.di.components.ApplicationComponent;
import com.github.moduth.petlover.internal.di.module.ActivityModule;
import com.github.moduth.petlover.usersystem.UserSystem;
import com.github.moduth.uiframework.BaseFragment;

/**
 * @author markzhai
 * @version 1.0.0
 */
public abstract class PetloverFragment extends BaseFragment implements ToolbarHelper.ToolbarProvider {

    public View mFragmentView;

    protected static final String TAG = "PetloverFragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("onCreate", getClass().getSimpleName());
    }

    /**
     * Gets a component for dependency injection by its type.
     */
//    @SuppressWarnings("unchecked")
//    protected HomeComponent getHomeComponent() {
//        return ((HasLbsComponent) getActivity()).getHomeComponent();
//    }

    /**
     * Gets a component for dependency injection by its type.
     */
//    @SuppressWarnings("unchecked")
//    protected TagComponent getTagComponent() {
//        return ((HasTagComponent) getActivity()).getTagComponent();
//    }

    /**
     * Gets a component for dependency injection by its type.
     */
//    @SuppressWarnings("unchecked")
//    protected <C> C getComponent(Class<C> componentType) {
//        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
//    }

    /**
     * Get navigator.
     *
     * @return {@link StayNavigator}
     */
//    public StayNavigator getNavigator() {
//        PetloverActivity activity = (PetloverActivity) getActivity();
//        return activity.getNavigator();
//    }

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link ApplicationComponent}
     */
    protected ApplicationComponent getApplicationComponent() {
        return ((PetloverActivity) getActivity()).getApplicationComponent();
    }

    /**
     * Get the UserSystem.
     *
     * @return {@link UserSystem}
     */
    protected UserSystem getUserSystem() {
        return getApplicationComponent().userSystem();
    }

    /**
     * Get an Activity module for dependency injection.
     *
     * @return {@link ActivityModule}
     */
    protected ActivityModule getActivityModule() {
        return ((PetloverActivity) getActivity()).getActivityModule();
    }

    // ============= toolbar related =================

    protected ToolbarHelper mToolbarHelper;

    @Override
    public void provideToolbar() {
        mToolbarHelper = new ToolbarHelper((Toolbar) getView().findViewById(R.id.toolbar));
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbarHelper.getToolbar());
        if (mToolbarHelper.getToolbarTitle() != null) {
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        MrApplication.getInstance().refWatcher.watch(this);
    }
}
