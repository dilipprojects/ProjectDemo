package com.imazitech.peartechnologyassignment.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET("order/607a7b89142d753f1c9e56dc/details")
    Call<ResponseBody> getCartOrderData();
}
