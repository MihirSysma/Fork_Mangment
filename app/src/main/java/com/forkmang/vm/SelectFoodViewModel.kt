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

class SelectFoodViewModel(var app: Application) : AndroidViewModel(app) {

    val command = MutableLiveData<String>()
    val searchFoodItemData = MutableLiveData<ArrayList<Category_ItemList>>()
    val categoryItemData = MutableLiveData<ArrayList<Category_ItemList>>()

    fun callApiSearchFoodItem(category_id: String?, search_item: String?) {
        //context?.showToastMessage(,"CategoryID->"+category_id,Toast.LENGTH_SHORT).show();
        //binding.progressBar.visibility = View.VISIBLE
        Api.info.getResCatItemListSearch(category_id, search_item)
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        //Log.d("Result", jsonObject.toString());
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
                                //binding.progressBar.visibility = View.GONE
                                //
                            }
                        } else if (response.code() == Constant.ERROR_CODE) {
                            //val jsonObject = JSONObject(response.errorBody()!!.string())
                            //binding.progressBar.visibility = View.GONE
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        //binding.progressBar.visibility = View.GONE
                        //context?.showToastMessage("Error occur please try again")
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    //context?.showToastMessage("Error occur please try again")
                    //binding.progressBar.visibility = View.GONE
                }
            })
    }

    fun callApiFoodItem(category_id: String?) {
        //context?.showToastMessage(,"CategoryID->"+category_id,Toast.LENGTH_SHORT).show();
        //binding.progressBar.visibility = View.VISIBLE
        Api.info.getResCatItemList(category_id)?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    //Log.d("Result", jsonObject.toString());
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
                            //binding.progressBar.visibility = View.GONE

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
                        //val jsonObject = JSONObject(response.errorBody()!!.string())
                        //binding.progressBar.visibility = View.GONE
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    //binding.progressBar.visibility = View.GONE
                    //context?.showToastMessage("Error occur please try again")
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                //context?.showToastMessage("Error occur please try again")
                //binding.progressBar.visibility = View.GONE
            }
        })
    }


}