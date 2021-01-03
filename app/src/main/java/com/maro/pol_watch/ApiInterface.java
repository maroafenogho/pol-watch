package com.maro.pol_watch;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @POST("/auth/register")
    Call<JsonObject> register(@Body
           User user
    );

    @POST("/auth/login")
    Call<JsonObject> login(@Body
            User user
    );

    @Multipart
    @GET("/case/upload")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file,
                                   @Part("media_type") RequestBody media_type,
                                   @Part("location")RequestBody location,
                                   @Part("date")RequestBody date,
                                   @Part("time")RequestBody time
    );
}
