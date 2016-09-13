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

package com.github.moduth.petlover.data.network;

import com.github.moduth.ext.Ext;
import com.github.moduth.ext.component.cache.sp.ConfigManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlService {

    private static final String TAG = "MrService";

    private static final String API_DEV_URL = "http://";
    private static final String API_PRODUCT_URL = "http://";

    private static final String WEB_DEV_URL = "http://";
    private static final String WEB_PRODUCT_URL = "http://";

    private static final String KEY_ENVIRONMENT = "api_environment";

    public static final boolean INITIAL_ENVIRONMENT_DEV = Ext.g().isDebuggable();
    public static boolean isDevEnvironment = ConfigManager.getBoolean(KEY_ENVIRONMENT, INITIAL_ENVIRONMENT_DEV);
    public static String token = ConfigManager.getString("token", "", ConfigManager.KEY_ACCOUNT);;
    public static boolean wifiHighQuality = false;

    private static PlService mInstance;

    private Retrofit mRetrofit;

    public static void toggleEnvironment() {
        isDevEnvironment = !isDevEnvironment;
        ConfigManager.putBoolean(KEY_ENVIRONMENT, isDevEnvironment);
    }

    public static String getActiveHttpScheme() {
        return isDevEnvironment ? WEB_DEV_URL : WEB_PRODUCT_URL;
    }

    public static PlService getInstance() {
        if (mInstance == null) {
            synchronized (PlService.class) {
                if (mInstance == null) {
                    mInstance = new PlService();
                }
            }
        }
        return mInstance;
    }

    public static String getBaseUrl() {
        return isDevEnvironment ? API_DEV_URL : API_PRODUCT_URL;
    }

    public <T> T createApi(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }

    private PlService() {
        this(true);
    }

    private PlService(boolean useRxJava) {
        final Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getClient());
        if (useRxJava) {
            builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        }
        mRetrofit = builder.build();
    }

    private OkHttpClient getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        if (INITIAL_ENVIRONMENT_DEV) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }

        return new OkHttpClient.Builder()
                .addInterceptor(new HeadInterceptor())
                .addInterceptor(logging)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .hostnameVerifier((hostname, session) -> true)
                .cookieJar(new CookieJar() {
                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(HttpUrl.parse(url.host()), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(HttpUrl.parse(url.host()));
                        return cookies != null ? cookies : new ArrayList<>();
                    }
                })
                .build();
    }

}
