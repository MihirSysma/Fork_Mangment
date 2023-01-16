package com.forkmang.network_call;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
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

    @POST(WebApi.LOGIN_GUEST)
    @FormUrlEncoded
    Call<JsonObject> login_guest(
            //@Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("identifier") String identifier

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
            //@Field("service_id") String service_id,
            @Field("latitude") String latitude,
            @Field("longitude") String logitutde
       );

    @POST(WebApi.RES_QUEELIST)
    @FormUrlEncoded
    Call<JsonObject> getqueelist(
            @Header("Authorization") String token,
            //@Header("Accept") String key,
            //@Field("service_id") String service_id,
            @Field("restaurant_id") String restaurant_id

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

    @POST(WebApi.RES_BOOKTABLE)
    @FormUrlEncoded
    Call<JsonObject> book_table(
            @Header("Authorization") String token,
            //@Header("content-type") String key,
            //@Header("Accept") String key_1,
            @Field("restaurant_id") String restaurant_id,
            @Field("table_id") String table_id,
            @Field("rules") String rules,
            @Field("dresscode") String dresscode,
            @Field("occasion") String occasion,
            @Field("date") String date,
            @Field("identifier") String identifier

    );

    @POST(WebApi.RES_FOODLIST)
    @FormUrlEncoded
    Call<JsonObject> getres_foodlist(
            //@Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("branch_id") String branch_id
    );

    @POST(WebApi.RES_CATLIST)
    @FormUrlEncoded
    Call<JsonObject> getres_catitemlist(
            //@Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("category_id") String category_id
    );


    @POST(WebApi.RES_CATLIST_SEARCH)
    @FormUrlEncoded
    Call<JsonObject> getres_catitemlist_search(
            //@Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("category_id") String category_id,
            @Field("search_item") String search_item

    );


    @POST(WebApi.RES_ADDITEMCART)
    @FormUrlEncoded
    Call<JsonObject> additem_cart(
            @Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("item_id") String item_id,
            @Field("qty") String qty,
            @Field("booking_table_id") String booking_table_id,
            @Field("item_extra") String item_extra,
            @Field("identifier") String identifier

    );

    @POST(WebApi.RES_UPDATEQTY)
    @FormUrlEncoded
    Call<JsonObject> cart_updateqty(
            @Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("cart_item_id") String cart_item_id,
            @Field("qty") String qty,
            @Field("identifier") String identifier

    );

    @POST(WebApi.RES_REMOVEITEMCART)
    @FormUrlEncoded
    Call<JsonObject> cart_removeqty(
            @Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("cart_item_id") String cart_item_id,
            @Field("identifier") String identifier

    );

    @POST(WebApi.RES_CREATEORDER)
    @FormUrlEncoded
    Call<JsonObject> create_order(
            @Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("restaurant_id") String restaurant_id,
            @Field("identifier") String identifier

    );

    /*@POST(WebApi.RES_MAKEPAYMENT)
    @FormUrlEncoded
    Call<JsonObject> make_payment(
            @Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("order_id") String order_id,
            @Field("payment_type") String payment_type
    );*/

    @POST(WebApi.RES_MAKEPAYMENT)
    @FormUrlEncoded
    Call<JsonObject> make_payment(
            @Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("order_id") String order_id,
            @Field("book_table_id") String book_table_id,
            @Field("payment_type") String payment_type,
            @Field("type") String type

    );

    @POST(WebApi.RES_ORDERDETAIL)
    @FormUrlEncoded
    Call<JsonObject> get_orderdetail(
            @Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("order_id") String order_id

    );



    @POST(WebApi.RES_GETCARTDETAIL)
    @FormUrlEncoded
    Call<JsonObject> getcart_detail(
            @Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("identifier") String identifier
     );


    @POST(WebApi.RES_GETTERMS)
    Call<JsonObject> getterms(
            @Header("Authorization") String token
            //@Header("Accept") String key,
            //@Field("test") String test
    );

    @POST(WebApi.RES_CONTACT)
    @FormUrlEncoded
    Call<JsonObject> contact(
            @Header("Authorization") String token,
            //@Header("Accept") String key,
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("message") String message
    );


    @POST(WebApi.RES_GETTABLEORDERSLIST)
    Call<JsonObject> getbooktable_listing(
            @Header("Authorization") String token
            //@Header("Accept") String key,
            //@Field("test") String test
    );

}