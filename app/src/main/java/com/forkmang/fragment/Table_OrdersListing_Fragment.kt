package com.forkmang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.forkmang.adapter.TableOrdersListing_Adapter
import com.forkmang.data.TableOrderListing
import com.forkmang.databinding.FragmentBooktableLayoutOrdersBinding
import com.forkmang.helper.Constant
import com.forkmang.helper.Constant.TOKEN_LOGIN
import com.forkmang.helper.StorePrefrence
import com.forkmang.helper.Utils.isNetworkAvailable
import com.forkmang.helper.showToastMessage
import com.forkmang.network_call.Api.info
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Table_OrdersListing_Fragment : Fragment() {
    var storePrefrence: StorePrefrence? = null
    var tableOrderListingArrayList: ArrayList<TableOrderListing>? = null

    private var _binding: FragmentBooktableLayoutOrdersBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBooktableLayoutOrdersBinding.inflate(inflater, container, false)
        storePrefrence = StorePrefrence(requireContext())
        binding.booktableRecycleviewOrders.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (isNetworkAvailable(requireContext())) {
            callapi_gettableorderslist()
        } else {
            context?.showToastMessage(Constant.NETWORKEROORMSG)
        }
    }

    //Api code for Book Table start
    private fun callapi_gettableorderslist() {
        binding.progressBar.visibility = View.VISIBLE
        info.getbooktable_listing("Bearer " + storePrefrence?.getString(TOKEN_LOGIN))
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            //Log.d("Result", jsonObject.toString());
                            if (jsonObject.getString("status")
                                    .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                            ) {
                                if (jsonObject.getJSONArray("data").length() > 0) {
                                    val tableOrderListing = TableOrderListing()
                                    tableOrderListingArrayList = ArrayList()
                                    for (i in 0 until jsonObject.getJSONArray("data").length()) {
                                        val mjsonobj =
                                            jsonObject.getJSONArray("data").getJSONObject(i)
                                        tableOrderListing.order_id = mjsonobj.getString("id")
                                        tableOrderListing.user_id = mjsonobj.getString("user_id")
                                        tableOrderListing.order_total = mjsonobj.getString("total")
                                        tableOrderListing.payment_status =
                                            mjsonobj.getString("payment_status")
                                        tableOrderListing.order_status =
                                            mjsonobj.getString("order_status")
                                        tableOrderListing.resturant_id =
                                            mjsonobj.getString("restaurant_id")
                                        tableOrderListing.str_restroname =
                                            mjsonobj.getJSONObject("restaurant")
                                                .getString("rest_name")
                                        tableOrderListing.resturant_branch =
                                            mjsonobj.getJSONObject("restaurant")
                                                .getString("rest_branch")
                                        tableOrderListing.resturant_contact =
                                            mjsonobj.getJSONObject("restaurant")
                                                .getString("contact")
                                        if (mjsonobj.isNull(mjsonobj.getString("timing"))) {
                                            tableOrderListing.resturant_timing = "00"
                                        } else {
                                            tableOrderListing.resturant_timing =
                                                mjsonobj.getString("timing")
                                        }
                                        tableOrderListingArrayList?.add(tableOrderListing)
                                    }
                                    val bookTableAdapter_orders = TableOrdersListing_Adapter(
                                        requireActivity(), tableOrderListingArrayList
                                    )
                                    binding.booktableRecycleviewOrders.adapter =
                                        bookTableAdapter_orders
                                    binding.progressBar.visibility = View.GONE
                                } else {
                                    //no data in array list
                                    binding.progressBar.visibility = View.GONE
                                    context?.showToastMessage(Constant.NODATA)
                                }
                            } else {
                                binding.progressBar.visibility = View.GONE
                                context?.showToastMessage(Constant.ERRORMSG)
                            }
                        } else {
                            binding.progressBar.visibility = View.GONE
                            context?.showToastMessage(Constant.ERRORMSG)
                        }
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        context?.showToastMessage(Constant.ERRORMSG)
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    context?.showToastMessage(Constant.ERRORMSG)
                    binding.progressBar.visibility = View.GONE
                }
            })
    }

    companion object {
        fun newInstance(): Table_OrdersListing_Fragment {
            return Table_OrdersListing_Fragment()
        }
    }
}