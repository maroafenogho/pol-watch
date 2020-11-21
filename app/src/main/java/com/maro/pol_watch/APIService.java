package com.maro.pol_watch;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

    @POST("/register")
    @FormUrlEncoded
    Call<User> createUser(@Field("username") String username,
                            @Field("email") String email,
                            @Field("first_name") String first_name,
                            @Field("last_name") String last_name,
                            @Field("phone") String phone,
                            @Field("password") String password,
                            @Field("role") String role);

    @POST("login")
    @FormUrlEncoded
    Call<User> loginUser(@Field("username") String username,
                            @Field("password") String password);
}
