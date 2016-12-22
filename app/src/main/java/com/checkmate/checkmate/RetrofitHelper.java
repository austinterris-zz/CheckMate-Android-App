package com.checkmate.checkmate;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Brady on 12/22/2016.
 */

public class RetrofitHelper {
    private static final String BASE_URL = "http://caltec.dyndns.org:3000/items/";

    private static CheckmateAPIInterface sService;

    public static CheckmateAPIInterface get() {
        if (sService == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
            httpClient.addInterceptor(logging);  // <-- this is the important line!
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(httpClient.build())
                    .build();

            sService = retrofit.create(CheckmateAPIInterface.class);
        }

        return sService;
    }
}
