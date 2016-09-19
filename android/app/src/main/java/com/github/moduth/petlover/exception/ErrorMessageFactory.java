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

package com.github.moduth.petlover.exception;

import android.content.Context;


import com.github.moduth.ext.component.logger.Logger;
import com.github.moduth.ext.utils.StringUtils;
import com.github.moduth.petlover.R;
import com.github.moduth.petlover.data.exception.NetworkConnectionException;
import com.github.moduth.petlover.data.exception.NotFoundException;
import com.github.moduth.petlover.data.exception.ResponseException;
import com.github.moduth.petlover.data.network.PetloverService;

import java.net.UnknownHostException;
import java.security.cert.CertPathValidatorException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Factory used to create error messages from an Exception as a condition.
 */
public class ErrorMessageFactory {

    private static final String TAG = "ErrorMessageFactory";

    private ErrorMessageFactory() {
        //empty
    }

    /**
     * Creates a String representing an error message.
     *
     * @param context   Context needed to retrieve string resources.
     * @param exception An exception used as a condition to retrieve the correct error message.
     * @return {@link String} an error message.
     */
    public static String create(Context context, Exception exception) {
        if (StringUtils.isNotEmpty(exception.getMessage())) {
            Logger.e(TAG, exception.getMessage());
            Logger.e(exception);
        }

        String message = context.getString(R.string.exception_message_generic);

        if (exception instanceof NetworkConnectionException ||
                exception instanceof UnknownHostException) {
            message = context.getString(R.string.exception_message_no_connection);
        } else if (exception instanceof SSLHandshakeException || exception instanceof CertPathValidatorException) {
            message = ""; // hide ssl exceptions on some phone
        } else if (exception instanceof NotFoundException) {
            message = context.getString(R.string.exception_message_not_found);
        } else if (exception instanceof ResponseException) {
            message = exception.getMessage();
        } else if (exception instanceof HttpException) {
            message = exception.getMessage();
        } else if (PetloverService.INITIAL_ENVIRONMENT_DEV) {
            message = exception.getMessage();
        }
        return message;
    }
}
