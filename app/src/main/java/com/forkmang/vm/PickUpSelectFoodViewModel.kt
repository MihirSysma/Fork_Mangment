package com.forkmang.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.forkmang.data.Category_ItemList
import com.forkmang.data.Extra_Topping
import com.forkmang.helper.Constant
import com.forkmang.helper.showToastMessage
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
    fun callApiSearchFoodItem(categoryId: String?, searchItem: String?) {
        Api.info.getResCatItemListSearch(categoryId, searchItem)
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            if (jsonObject.getString("status")
                                    .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                            ) {
                                val categoryItemLists = ArrayList<Category_ItemList>()
                                val mjsonArr = jsonObject.getJSONArray("data")
                                for (i in 0 until mjsonArr.length()) {
                                    val categoryItemList = Category_ItemList()
                                    val mjsonObj = mjsonArr.getJSONObject(i)
                                    categoryItemList.id = mjsonObj.getString("id")
                                    categoryItemList.category_id =
                                        mjsonObj.getString("category_id")
                                    categoryItemList.name = mjsonObj.getString("name")
                                    categoryItemList.price = mjsonObj.getString("price")
                                    categoryItemList.image = mjsonObj.getString("image")
                                    val mjsonArrExtra = mjsonObj.getJSONArray("extra")
                                    val extraToppingArrayList = ArrayList<Extra_Topping>()
                                    for (j in 0 until mjsonArrExtra.length()) {
                                        val mjsonObjExtra = mjsonArrExtra.getJSONObject(j)
                                        val extraTopping = Extra_Topping()
                                        extraTopping.id = mjsonObjExtra.getString("id")
                                        extraTopping.item_id = mjsonObjExtra.getString("item_id")
                                        extraTopping.name = mjsonObjExtra.getString("name")
                                        extraTopping.price = mjsonObjExtra.getString("price")
                                        extraToppingArrayList.add(extraTopping)
                                    }
                                    categoryItemList.extra_toppingArrayList =
                                        extraToppingArrayList
                                    categoryItemLists.add(categoryItemList)
                                }
                                searchFoodItemData.postValue(categoryItemLists)
                            }
                        } else if (response.code() == Constant.ERROR_CODE) {
                            val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        app.applicationContext.showToastMessage("Error occur please try again")
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    app.applicationContext.showToastMessage("Error occur please try again")
                }
            })
    }

    fun callApiFoodItem(category_id: String?) {
        Api.info.getResCatItemList(category_id)?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    if (response.code() == Constant.SUCCESS_CODE_n) {
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        if (jsonObject.getString("status")
                                .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                        ) {
                            val categoryItemLists = ArrayList<Category_ItemList>()
                            val mjsonArr = jsonObject.getJSONArray("data")
                            for (i in 0 until mjsonArr.length()) {
                                val categoryItemList = Category_ItemList()
                                val mjsonObj = mjsonArr.getJSONObject(i)
                                categoryItemList.id = mjsonObj.getString("id")
                                categoryItemList.category_id = mjsonObj.getString("category_id")
                                categoryItemList.name = mjsonObj.getString("name")
                                categoryItemList.price = mjsonObj.getString("price")
                                categoryItemList.image = mjsonObj.getString("image")
                                val mjsonArrExtra = mjsonObj.getJSONArray("extra")
                                val extraToppingArrayList = ArrayList<Extra_Topping>()
                                for (j in 0 until mjsonArrExtra.length()) {
                                    val mjsonObjExtra = mjsonArrExtra.getJSONObject(j)
                                    val extraTopping = Extra_Topping()
                                    extraTopping.id = mjsonObjExtra.getString("id")
                                    extraTopping.item_id = mjsonObjExtra.getString("item_id")
                                    extraTopping.name = mjsonObjExtra.getString("name")
                                    extraTopping.price = mjsonObjExtra.getString("price")
                                    extraToppingArrayList.add(extraTopping)
                                }
                                categoryItemList.extra_toppingArrayList = extraToppingArrayList
                                categoryItemLists.add(categoryItemList)
                            }
                            categoryItemData.postValue(categoryItemLists)

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
                    app.applicationContext.showToastMessage(Constant.ERRORMSG)
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                app.applicationContext.showToastMessage(Constant.ERRORMSG)
            }
        })
    }
}