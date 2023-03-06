package com.forkmang.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.forkmang.data.Category_ItemList
import com.forkmang.data.Extra_Topping
import com.forkmang.helper.Constant
import com.forkmang.network_call.Api
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PickUpSelectFoodViewModel(var app: Application) : AndroidViewModel(app) {

    val command = MutableLiveData<String>()
    // TODO: can make them both in one?
    val searchFoodItemData = MutableLiveData<ArrayList<Category_ItemList>>()
    val categoryItemData = MutableLiveData<ArrayList<Category_ItemList>>()

    // TODO: handle the toasts and the progressbar
    fun callApiSearchFoodItem(category_id: String?, search_item: String?) {
        //context?.showToastMessage(,"CategoryID->"+category_id,Toast.LENGTH_SHORT).show();
        Api.info.getres_catitemlist_search(category_id, search_item)
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        //Log.d("Result", jsonObject.toString());
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            if (jsonObject.getString("status")
                                    .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                            ) {
                                val category_itemLists = ArrayList<Category_ItemList>()
                                val mjson_arr = jsonObject.getJSONArray("data")
                                for (i in 0 until mjson_arr.length()) {
                                    val category_itemList = Category_ItemList()
                                    val mjson_obj = mjson_arr.getJSONObject(i)
                                    category_itemList.id = mjson_obj.getString("id")
                                    category_itemList.category_id =
                                        mjson_obj.getString("category_id")
                                    category_itemList.name = mjson_obj.getString("name")
                                    category_itemList.price = mjson_obj.getString("price")
                                    category_itemList.image = mjson_obj.getString("image")
                                    val mjson_arr_extra = mjson_obj.getJSONArray("extra")
                                    val extra_toppingArrayList = ArrayList<Extra_Topping>()
                                    for (j in 0 until mjson_arr_extra.length()) {
                                        val mjson_obj_extra = mjson_arr_extra.getJSONObject(j)
                                        val extra_topping = Extra_Topping()
                                        extra_topping.id = mjson_obj_extra.getString("id")
                                        extra_topping.item_id = mjson_obj_extra.getString("item_id")
                                        extra_topping.name = mjson_obj_extra.getString("name")
                                        extra_topping.price = mjson_obj_extra.getString("price")
                                        extra_toppingArrayList.add(extra_topping)
                                    }
                                    category_itemList.extra_toppingArrayList =
                                        extra_toppingArrayList
                                    category_itemLists.add(category_itemList)
                                }
                                searchFoodItemData.postValue(category_itemLists)
                            }
                        } else if (response.code() == Constant.ERROR_CODE) {
                            val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        //showToastMessage("Error occur please try again")
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    //showToastMessage("Error occur please try again")
                }
            })
    }

    fun callApiFoodItem(category_id: String?) {
        //context?.showToastMessage(,"CategoryID->"+category_id,Toast.LENGTH_SHORT).show();
        Api.info.getres_catitemlist(category_id)?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    //Log.d("Result", jsonObject.toString());
                    if (response.code() == Constant.SUCCESS_CODE_n) {
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        if (jsonObject.getString("status")
                                .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                        ) {
                            val category_itemLists = ArrayList<Category_ItemList>()
                            val mjson_arr = jsonObject.getJSONArray("data")
                            for (i in 0 until mjson_arr.length()) {
                                val category_itemList = Category_ItemList()
                                val mjson_obj = mjson_arr.getJSONObject(i)
                                category_itemList.id = mjson_obj.getString("id")
                                category_itemList.category_id = mjson_obj.getString("category_id")
                                category_itemList.name = mjson_obj.getString("name")
                                category_itemList.price = mjson_obj.getString("price")
                                category_itemList.image = mjson_obj.getString("image")
                                val mjson_arr_extra = mjson_obj.getJSONArray("extra")
                                val extra_toppingArrayList = ArrayList<Extra_Topping>()
                                for (j in 0 until mjson_arr_extra.length()) {
                                    val mjson_obj_extra = mjson_arr_extra.getJSONObject(j)
                                    val extra_topping = Extra_Topping()
                                    extra_topping.id = mjson_obj_extra.getString("id")
                                    extra_topping.item_id = mjson_obj_extra.getString("item_id")
                                    extra_topping.name = mjson_obj_extra.getString("name")
                                    extra_topping.price = mjson_obj_extra.getString("price")
                                    extra_toppingArrayList.add(extra_topping)
                                }
                                category_itemList.extra_toppingArrayList = extra_toppingArrayList
                                category_itemLists.add(category_itemList)
                            }
                            categoryItemData.postValue(category_itemLists)

                            /*if(all_orderFood_adapter == null)
                                    {
                                        all_orderFood_adapter = new All_Food_Adapter(getContext(),getActivity(), category_itemLists, Select_Food_Fragment.this);
                                        recyclerView.setAdapter(all_orderFood_adapter);
                                    }
                                    else{
                                        all_orderFood_adapter.notifyDataSetChanged();
                                    }*/
                        }
                    } else if (response.code() == Constant.ERROR_CODE) {
                        val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    //showToastMessage("Error occur please try again")
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                //showToastMessage("Error occur please try again")
            }
        })
    }
}