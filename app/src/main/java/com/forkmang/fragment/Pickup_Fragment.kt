package com.forkmang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.forkmang.adapter.Pickup_Fragment_BookTableAdapter
import com.forkmang.data.RestoData
import com.forkmang.databinding.FragmentPickupLayoutBinding
import com.forkmang.helper.ApiConfig.getLocation
import com.forkmang.helper.Constant
import com.forkmang.helper.GPSTracker
import com.forkmang.helper.Utils.isNetworkAvailable
import com.forkmang.network_call.Api.info
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Pickup_Fragment : Fragment() {
    var restoDataArrayList: ArrayList<RestoData>? = null
    var gps: GPSTracker? = null
    var saveLatitude: Double? = null
    var saveLongitude: Double? = null

    private var _binding: FragmentPickupLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickupLayoutBinding.inflate(inflater, container, false)
        val verticalLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.pickRecycleview.layoutManager = verticalLayoutManager
        binding.pickRecycleview.setHasFixedSize(true)
        //GET GPS Current start
        getLocation(requireActivity())
        gps = GPSTracker(activity)
        saveLatitude = gps!!.latitude
        saveLongitude = gps!!.longitude
        if (gps?.isGPSTrackingEnabled == true) {
            saveLatitude = gps!!.latitude
            saveLongitude = gps!!.longitude
        }
        //GET GPS Current end
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val service_id = "3"
        saveLatitude = 23.933689
        saveLongitude = 72.367458
        callapi_getbooktable(service_id, saveLatitude.toString(), saveLongitude.toString())
        //((Booking_TabView_Activity)getActivity()).hide_search();
    }

    //Api code for Book Table start
    private fun callapi_getbooktable(service_id: String, latitude: String, logitutde: String) {
        binding.progressbar.visibility = View.VISIBLE
        info.getlist_res_walkin(service_id, latitude, logitutde)!!
            .enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            //Log.d("Result", jsonObject.toString());
                            if (jsonObject.getString("status")
                                    .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                            ) {
                                if (jsonObject.getJSONObject("data").getJSONArray("data")
                                        .length() > 0
                                ) {
                                    restoDataArrayList = ArrayList()
                                    for (i in 0 until jsonObject.getJSONObject("data")
                                        .getJSONArray("data").length()) {
                                        val bookTable = RestoData()
                                        val mjson_obj =
                                            jsonObject.getJSONObject("data").getJSONArray("data")
                                                .getJSONObject(i)

                                        //JSONObject mjson_obj = jsonObject.getJSONObject("data").getJSONArray("data").getJSONObject(0);
                                        bookTable.rest_name = mjson_obj.getString("rest_name")
                                        /*if(i > 0)
                                        {
                                            bookTable.setRest_name("REST"+" "+i);
                                        }
                                        else{
                                            bookTable.setRest_name(mjson_obj.getString("rest_name"));
                                        }*/if (mjson_obj.has("endtime")) {
                                            bookTable.endtime = mjson_obj.getString("endtime")
                                        } else {
                                            bookTable.endtime = "00"
                                        }
                                        bookTable.id = mjson_obj.getString("id")
                                        val double_val =
                                            Math.floor(mjson_obj.getDouble("distance") * 100) / 100
                                        bookTable.distance = java.lang.Double.toString(double_val)
                                        restoDataArrayList!!.add(bookTable)
                                    }
                                    binding.progressbar.visibility = View.GONE
                                    val pickup_fragment_bookTableAdapter =
                                        Pickup_Fragment_BookTableAdapter(
                                            activity!!, restoDataArrayList, "listing", context
                                        )
                                    binding.pickRecycleview.adapter = pickup_fragment_bookTableAdapter
                                } else {
                                    //no data in array list
                                    binding.progressbar.visibility = View.GONE
                                    Toast.makeText(context, Constant.NODATA, Toast.LENGTH_LONG)
                                        .show()
                                }
                            } else {
                                binding.progressbar.visibility = View.GONE
                                // Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            binding.progressbar.visibility = View.GONE
                            // Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                        }
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        binding.progressbar.visibility = View.GONE
                        //Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    Toast.makeText(context, Constant.ERRORMSG, Toast.LENGTH_LONG).show()
                    binding.progressbar.visibility = View.GONE
                }
            })
    }

    private fun callapi_searchbooktable(search_strq: String, latitude: String, logitutde: String) {
        binding.progressbar.visibility = View.VISIBLE
        info.getlist_searchres(search_strq, latitude, logitutde)!!
            .enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        //Log.d("Result", jsonObject.toString());
                        if (jsonObject.getString("status")
                                .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                        ) {
                            if (jsonObject.getJSONObject("data").getJSONArray("data")
                                    .length() > 0
                            ) {
                                restoDataArrayList = ArrayList()
                                for (i in 0 until jsonObject.getJSONObject("data")
                                    .getJSONArray("data").length()) {
                                    val bookTable = RestoData()
                                    val mjson_obj =
                                        jsonObject.getJSONObject("data").getJSONArray("data")
                                            .getJSONObject(i)
                                    bookTable.rest_name = mjson_obj.getString("rest_name")
                                    if (mjson_obj.has("endtime")) {
                                        bookTable.endtime = mjson_obj.getString("endtime")
                                    } else {
                                        bookTable.endtime = "00"
                                    }
                                    bookTable.id = mjson_obj.getString("id")
                                    val double_val =
                                        Math.floor(mjson_obj.getDouble("distance") * 100) / 100
                                    bookTable.distance = java.lang.Double.toString(double_val)
                                    restoDataArrayList!!.add(bookTable)
                                }
                                binding.progressbar.visibility = View.GONE
                                val pickup_fragment_bookTableAdapter =
                                    Pickup_Fragment_BookTableAdapter(
                                        requireActivity(), restoDataArrayList, "listing", context
                                    )
                                binding.pickRecycleview.adapter = pickup_fragment_bookTableAdapter
                            } else {
                                //no data in array list
                                binding.progressbar.visibility = View.GONE
                                Toast.makeText(context, Constant.NODATA, Toast.LENGTH_LONG).show()
                            }
                        }
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(context, Constant.ERRORMSG, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    Toast.makeText(context, Constant.ERRORMSG, Toast.LENGTH_LONG).show()
                    binding.progressbar.visibility = View.GONE
                }
            })
    }

    fun filter_booktable(search_str: String) {
        if (isNetworkAvailable(requireContext())) {
            callapi_searchbooktable(search_str, saveLatitude.toString(), saveLongitude.toString())
        } else {
            Toast.makeText(context, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
        }
    }

    fun call_reloadbooktable() {
        if (isNetworkAvailable(requireContext())) {
            saveLatitude = 21.209589
            saveLongitude = 72.860824
            val service_id = "3"
            callapi_getbooktable(service_id, saveLatitude.toString(), saveLongitude.toString())
        } else {
            Toast.makeText(context, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance(): Pickup_Fragment {
            return Pickup_Fragment()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}