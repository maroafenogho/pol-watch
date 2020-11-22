package com.maro.pol_watch;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("/auth/register")
    Call<JsonObject> register(@Body
           User user
    );

    @FormUrlEncoded
    @POST("/auth/register")
    Call<ResponseBody> signUp(
            @Field("username") String name,
            @Field("email") String email,
            @Field("first_name")String firstname,
            @Field("last_name")String lastname,
            @Field("phone")String phone,
            @Field("password") String password,
            @Field("role")String role
    );
}
