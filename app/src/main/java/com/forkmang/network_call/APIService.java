package com.forkmang.network_call;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {
    String API_BASE_URL = "https://staging.greatly-done.com/fork-mgmt/fork-management/api/v1/";


    @POST(WebApi.REGISTER_USER)
    @FormUrlEncoded
    Call<JsonObject> register_user(
            //@Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("name") String name,
            @Field("email") String email,
            @Field("contact") String contact,
            @Field("password") String password,
            @Field("c_password") String c_password,
            @Field("token") String token

        );

    @POST(WebApi.LOGIN_USER)
    @FormUrlEncoded
    Call<JsonObject> login_user(
            //@Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("contact") String contact,
            @Field("password") String password
    );

    @POST(WebApi.FORGOT_PASSWORD)
    @FormUrlEncoded
    Call<JsonObject> forgot_pass(
            //@Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("contact") String contact,
            @Field("token") String token


    );










}