package com.ingic.caristocrat.webhelpers.services;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.helpers.BasePreferenceHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 */
public class WebServiceFactory {
    public static Webservice webservice;
    private static String BASE_URL = "";

    public static Webservice Instance(Context activity) {
        if (webservice == null) {
            final BasePreferenceHelper preferenceHelper = new BasePreferenceHelper(activity);
            BASE_URL = AppConstants.BASE_URL;
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.connectTimeout(3, TimeUnit.MINUTES);
            httpClient.readTimeout(3, TimeUnit.MINUTES);

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = null;
                    try {
                        Request.Builder requestBuilder = original.newBuilder()
                                .addHeader("Authorization", "Bearer " + preferenceHelper.getUserToken())
                                .addHeader("Accept", "application/json")
                                .addHeader("Content-Type", "application/json");
                        request = requestBuilder.build();
                    } catch (Exception ex) {
                        Request.Builder requestBuilder = original.newBuilder()
                                .addHeader("Authorization", "Bearer " + preferenceHelper.getUserToken())
                                .addHeader("Content-Type", "application/json")
                                .addHeader("Accept", "application/json");
                        request = requestBuilder.build();
                    }

                    return chain.proceed(request);
                }
            });

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            httpClient.addInterceptor(logging);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();

            webservice = retrofit.create(Webservice.class);
        }

        return webservice;
    }
}
