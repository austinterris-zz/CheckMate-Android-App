package com.checkmate.checkmate;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Brady on 12/22/2016.
 */

public interface CheckmateAPIInterface {
    @GET("info/{hf}")
    Observable<Item> getItem(@Path("hf") String hf);

    @GET("buy/{hf}")
    Observable<Item> buyItem(@Path("hf") String hf);
}
