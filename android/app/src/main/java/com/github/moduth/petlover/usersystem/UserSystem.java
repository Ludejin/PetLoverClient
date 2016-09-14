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

package com.github.moduth.petlover.usersystem;

import com.github.moduth.ext.component.cache.sp.ConfigManager;
import com.github.moduth.ext.utils.StringUtils;
import com.github.moduth.petlover.data.network.PlService;
import com.github.moduth.petlover.domain.model.TokenEntity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserSystem {

    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "uid";
    private static final String KEY_MOBILE = "mobile";


    private String mToken;
    private String mUserId;
    private String mMobileNumber;


    @Inject
    public UserSystem() {
       
    }
    public void logout() {
        setUserId("");
        setToken("");
    }

    private void setUserId(String userId) {
        if (StringUtils.equal(userId, mUserId)) {
            return;
        }
        ConfigManager.putString(KEY_USER_ID, userId, ConfigManager.KEY_ACCOUNT);
        mUserId = userId;
    }

    private void setToken(String token) {
        if (StringUtils.equal(token, mToken)) {
            return;
        }
        mToken = token;
        PlService.token = token;
        ConfigManager.putString(KEY_TOKEN, token, ConfigManager.KEY_ACCOUNT);
    }

    public String getUserId() {
        if (StringUtils.isEmpty(mUserId)) {
            String userId = ConfigManager.getString(KEY_USER_ID, "", ConfigManager.KEY_ACCOUNT);
            setUserId(userId);
        }
        return mUserId;
    }

    public String getToken() {
        if (StringUtils.isEmpty(mToken)) {
            String token = ConfigManager.getString(KEY_TOKEN, "", ConfigManager.KEY_ACCOUNT);
            String userId = ConfigManager.getString(KEY_USER_ID, "", ConfigManager.KEY_ACCOUNT);
            setTokenWrapper(new TokenEntity(userId, token));
        }
        return mToken;
    }

    public void setTokenWrapper(TokenEntity token) {
        if (token != null) {
            setUserId(token.getUid());
            setToken(token.getToken());
        }
    }

    public String getMobileNumber() {
        if (StringUtils.isEmpty(mMobileNumber)) {
            String mobile = ConfigManager.getString(KEY_MOBILE, "", ConfigManager.KEY_ACCOUNT);
            setMobileNumber(mobile);
        }
        return mMobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        if (StringUtils.equal(mobileNumber, mMobileNumber)) {
            return;
        }
        mMobileNumber = mobileNumber;
        ConfigManager.putString(KEY_MOBILE, mMobileNumber, ConfigManager.KEY_ACCOUNT);
    }
}
