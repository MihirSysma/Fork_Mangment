package com.forkmang.network_call

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface APIService {
    @POST(WebApi.REGISTER_USER)
    @FormUrlEncoded
    fun register_user( //@Header("Authorization") String token,
        //@Header("Accept") String key,
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("contact") contact: String?,
        @Field("password") password: String?,
        @Field("c_password") c_password: String?,
        @Field("token") token: String?
    ): Call<JsonObject?>?

    @POST(WebApi.REGISTER_SOCIALLOGIN)
    @FormUrlEncoded
    fun register_sociallogin( //@Header("Authorization") String token,
        //@Header("Accept") String key,
        @Field("type") type: String?,
        @Field("id") id: String?
    ): Call<JsonObject?>?

    @POST(WebApi.LOGIN_USER)
    @FormUrlEncoded
    fun login_user( //@Header("Authorization") String token,
        //@Header("Accept") String key,
        @Field("contact") contact: String?,
        @Field("password") password: String?
    ): Call<JsonObject?>?

    @POST(WebApi.LOGIN_GUEST)
    @FormUrlEncoded
    fun login_guest( //@Header("Authorization") String token,
        //@Header("Accept") String key,
        @Field("identifier") identifier: String?
    ): Call<JsonObject?>?

    @POST(WebApi.FORGOT_PASSWORD)
    @FormUrlEncoded
    fun forgot_pass( //@Header("Authorization") String token,
        //@Header("Accept") String key,
        @Field("contact") contact: String?,
        @Field("token") token: String?
    ): Call<JsonObject?>?

    @POST(WebApi.RESET_PASSWORD)
    @FormUrlEncoded
    fun reset_pass( //@Header("Authorization") String token,
        //@Header("Accept") String key,
        @Field("contact") contact: String?,
        @Field("password") password: String?,
        @Field("password_confirmation") password_confirmation: String?,
        @Field("token") token: String?
    ): Call<JsonObject?>?

    @POST(WebApi.LIST_RES_FILTER)
    @FormUrlEncoded
    fun getlist_res_filter( //@Header("Authorization") String token,
        //@Header("Accept") String key,
        @Field("date") date: String?,
        @Field("person") person: String?,
        @Field("latitude") latitude: String?,
        @Field("logitutde") logitutde: String?,
        @Field("search") search: String?
    ): Call<JsonObject?>?

    @POST(WebApi.LIST_RES)
    @FormUrlEncoded
    fun getlist_res( //@Header("Authorization") String token,
        //@Header("Accept") String key,
        //@Field("service_id") String service_id,
        @Field("latitude") latitude: String?,
        @Field("longitude") logitutde: String?
    ): Call<JsonObject?>?

    @POST(WebApi.LIST_RES)
    @FormUrlEncoded
    fun getlist_res_walkin( //@Header("Authorization") String token,
        //@Header("Accept") String key,
        @Field("service_id") service_id: String?,
        @Field("latitude") latitude: String?,
        @Field("longitude") logitutde: String?
    ): Call<JsonObject?>?

    @POST(WebApi.RES_QUEECONFORMATION)
    @FormUrlEncoded
    fun queue_confirmation(
        @Header("Authorization") token: String?,  //@Header("Accept") String key,
        @Field("action") action: String?,
        @Field("restaurant_id") restaurant_id: String?,
        @Field("person") person: String?,
        @Field("occasion") occasion: String?,
        @Field("area") area: String?,
        @Field("identifier") identifier: String?
    ): Call<JsonObject?>?

    @POST(WebApi.RES_GETQUEE)
    @FormUrlEncoded
    fun queue_get(
        @Header("Authorization") token: String?,  //@Header("Accept") String key,
        @Field("restaurant_id") restaurant_id: String?,
        @Field("person") person: String?,
        @Field("occasion") occasion: String?,
        @Field("area") area: String?
    ): Call<JsonObject?>?

    @POST(WebApi.RES_QUEELIST)
    @FormUrlEncoded
    fun getqueelist(
        @Header("Authorization") token: String?,  //@Header("Accept") String key,
        //@Field("service_id") String service_id,
        @Field("restaurant_id") restaurant_id: String?
    ): Call<JsonObject?>?

    @POST(WebApi.RES_GETPERSONQUEE)
    @FormUrlEncoded
    fun getpersonqueeno(
        @Header("Authorization") token: String?,
        @Field("restaurant_id") restaurant_id: String?
    ): Call<JsonObject?>?

    @POST(WebApi.LIST_RES)
    @FormUrlEncoded
    fun getlist_searchres( //@Header("Authorization") String token,
        //@Header("Accept") String key,
        @Field("search") search: String?,
        @Field("latitude") latitude: String?,
        @Field("longitude") logitutde: String?
    ): Call<JsonObject?>?

    @POST(WebApi.RES_DETAILPAGE)
    @FormUrlEncoded
    fun getres_detail( //@Header("Authorization") String token,
        //@Header("Accept") String key,
        @Field("restaurant_id") restaurant_id: String?
    ): Call<JsonObject?>?

    @POST(WebApi.RES_BOOKTABLE)
    @FormUrlEncoded
    fun book_table(
        @Header("Authorization") token: String?,  //@Header("content-type") String key,
        //@Header("Accept") String key_1,
        @Field("restaurant_id") restaurant_id: String?,
        @Field("table_id") table_id: String?,
        @Field("rules") rules: String?,
        @Field("dresscode") dresscode: String?,
        @Field("occasion") occasion: String?,
        @Field("date") date: String?,
        @Field("identifier") identifier: String?
    ): Call<JsonObject?>?

    @POST(WebApi.RES_FOODLIST)
    @FormUrlEncoded
    fun getres_foodlist( //@Header("Authorization") String token,
        //@Header("Accept") String key,
        @Field("branch_id") branch_id: String?
    ): Call<JsonObject?>?

    @POST(WebApi.RES_CATLIST)
    @FormUrlEncoded
    fun getres_catitemlist( //@Header("Authorization") String token,
        //@Header("Accept") String key,
        @Field("category_id") category_id: String?
    ): Call<JsonObject?>?

    @POST(WebApi.RES_CATLIST_SEARCH)
    @FormUrlEncoded
    fun getres_catitemlist_search( //@Header("Authorization") String token,
        //@Header("Accept") String key,
        @Field("category_id") category_id: String?,
        @Field("search_item") search_item: String?
    ): Call<JsonObject?>?

    @POST(WebApi.RES_ADDITEMCART)
    @FormUrlEncoded
    fun additem_cart(
        @Header("Authorization") token: String?,  //@Header("Accept") String key,
        @Field("item_id") item_id: String?,
        @Field("qty") qty: String?,
        @Field("booking_table_id") booking_table_id: String?,
        @Field("item_extra") item_extra: String?,
        @Field("identifier") identifier: String?,
        @Field("type") type: String?,
        @Field("restaurant_id") restaurant_id: String?,
    ): Call<JsonObject?>?

    @POST(WebApi.RES_UPDATEQTY)
    @FormUrlEncoded
    fun cart_updateqty(
        @Header("Authorization") token: String?,  //@Header("Accept") String key,
        @Field("cart_item_id") cart_item_id: String?,
        @Field("qty") qty: String?,
        @Field("identifier") identifier: String?
    ): Call<JsonObject?>?

    @POST(WebApi.RES_REMOVEITEMCART)
    @FormUrlEncoded
    fun cart_removeqty(
        @Header("Authorization") token: String?,  //@Header("Accept") String key,
        @Field("cart_item_id") cart_item_id: String?,
        @Field("identifier") identifier: String?
    ): Call<JsonObject?>?

    @POST(WebApi.RES_CREATEORDER)
    @FormUrlEncoded
    fun create_order(
        @Header("Authorization") token: String?,  //@Header("Accept") String key,
        @Field("restaurant_id") restaurant_id: String?,
        @Field("type") type: String?,
        @Field("queue_id") queue_id: String?,
        @Field("booking_table_id") booking_table_id: String?,
        @Field("identifier") identifier: String?
    ): Call<JsonObject?>?

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
    fun make_payment(
        @Header("Authorization") token: String?,  //@Header("Accept") String key,
        @Field("order_id") order_id: String?,
        @Field("book_table_id") book_table_id: String?,
        @Field("payment_type") payment_type: String?,
        @Field("type") type: String?
    ): Call<JsonObject?>?

    @POST(WebApi.RES_ORDERDETAIL)
    @FormUrlEncoded
    fun get_orderdetail(
        @Header("Authorization") token: String?,  //@Header("Accept") String key,
        @Field("order_id") order_id: String?
    ): Call<JsonObject?>?

    @POST(WebApi.RES_GETCARTDETAIL)
    @FormUrlEncoded
    fun getcart_detail(
        @Header("Authorization") token: String?,  //@Header("Accept") String key,
        @Field("identifier") identifier: String?
    ): Call<JsonObject?>?

    @POST(WebApi.RES_GETTERMS)
    fun getterms(
        @Header("Authorization") token: String? //@Header("Accept") String key,
        //@Field("test") String test
    ): Call<JsonObject?>?

    @POST(WebApi.RES_CONTACT)
    @FormUrlEncoded
    fun contact(
        @Header("Authorization") token: String?,  //@Header("Accept") String key,
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("phone") phone: String?,
        @Field("message") message: String?
    ): Call<JsonObject?>?

    @POST(WebApi.RES_GETTABLEORDERSLIST)
    fun getbooktable_listing(
        @Header("Authorization") token: String? //@Header("Accept") String key,
        //@Field("test") String test
    ): Call<JsonObject?>?

    @POST(WebApi.RES_GETQUEE)
    @FormUrlEncoded
    fun queue_action(
        @Header("Authorization") token: String?,  //@Header("Accept") String key,
        @Field("action") action: String?,
        @Field("restaurant_id") restaurant_id: String?,
        @Field("person") person: String?,
        @Field("occasion") occasion: String?,
        @Field("area") area: String?,
        @Field("identifier") identifier: String?
    ): Call<JsonObject?>?

    companion object {
        const val API_BASE_URL =
            "https://staging.greatly-done.com/fork-mgmt/fork-management/api/v1/"
    }
}