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

    @POST(WebApi.REGISTER_SOCIALLOGIN)
    @FormUrlEncoded
    Call<JsonObject> register_sociallogin(
            //@Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("type") String type,
            @Field("id") String id
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


    @POST(WebApi.RESET_PASSWORD)
    @FormUrlEncoded
    Call<JsonObject> reset_pass(
            //@Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("contact") String contact,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation,
            @Field("token") String token
    );

    @POST(WebApi.LIST_RES_FILTER)
    @FormUrlEncoded
    Call<JsonObject> getlist_res_filter(
            //@Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("date") String date,
            @Field("person") String person,
            @Field("latitude") String latitude,
            @Field("logitutde") String logitutde,
            @Field("search") String search
    );

    @POST(WebApi.LIST_RES)
    @FormUrlEncoded
    Call<JsonObject> getlist_res(
            //@Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("service_id") String service_id,
            @Field("latitude") String latitude,
            @Field("longitude") String logitutde
       );

    @POST(WebApi.LIST_RES)
    @FormUrlEncoded
    Call<JsonObject> getlist_searchres(
            //@Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("search") String search,
            @Field("latitude") String latitude,
            @Field("longitude") String logitutde
    );

    @POST(WebApi.RES_DETAILPAGE)
    @FormUrlEncoded
    Call<JsonObject> getres_detail(
            //@Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("restaurant_id") String restaurant_id

    );









}