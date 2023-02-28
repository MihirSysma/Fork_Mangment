package com.forkmang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.forkmang.R
import com.forkmang.adapter.Walkin_listing_Adapter
import com.forkmang.data.RestoData
import com.forkmang.databinding.FragmentWalkinlistingLayoutBinding
import com.forkmang.helper.ApiConfig
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
import kotlin.math.floor

class Walkin_listing_Fragment : Fragment() {
    var walkin_listing_adapter: Walkin_listing_Adapter? = null
    var restoDataArrayList: ArrayList<RestoData>? = null
    var gps: GPSTracker? = null
    var saveLatitude: Double? = null
    var saveLongitude: Double? = null

    private var _binding: FragmentWalkinlistingLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalkinlistingLayoutBinding.inflate(inflater, container, false)
        //get_inquee = view.findViewById(R.id.get_inquee);
        val verticalLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.walkinlistingRecycleview.layoutManager = verticalLayoutManager
        binding.walkinlistingRecycleview.setHasFixedSize(true)


        //GET GPS Current start
        ApiConfig.getLocation(requireActivity())
        gps = GPSTracker(activity)
        saveLatitude = gps?.latitude
        saveLongitude = gps?.longitude
        if (gps?.isGPSTrackingEnabled == true) {
            saveLatitude = gps?.latitude
            saveLongitude = gps?.longitude
        }
        //GET GPS Current end

        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                verticalLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);*/

        //BookTableAdapter bookTableAdapter = new BookTableAdapter(getActivity());
        //recyclerView.setAdapter(bookTableAdapter);
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // ((Booking_TabView_Activity)getActivity()).hide_search();
        val service_id = "2"
        /*saveLatitude = 21.209589;
        saveLongitude = 72.860824;*/saveLatitude = 23.933689
        saveLongitude = 72.367458
        callapi_getbooktable(service_id, saveLatitude.toString(), saveLongitude.toString())
    }

    //Api code for Book Table start
    private fun callapi_getbooktable(service_id: String, latitude: String, logitutde: String) {
        binding.progressBar.visibility = View.VISIBLE
        info.getlist_res_walkin(service_id, latitude, logitutde)
            ?.enqueue(object : Callback<JsonObject?> {
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
                                            floor(mjson_obj.getDouble("distance") * 100) / 100
                                        bookTable.distance = double_val.toString()
                                        restoDataArrayList?.add(bookTable)
                                    }
                                    binding.progressBar.visibility = View.GONE
                                    walkin_listing_adapter = Walkin_listing_Adapter(
                                        requireActivity(),
                                        restoDataArrayList,
                                        "listing",
                                        context
                                    )
                                    binding.walkinlistingRecycleview.adapter = walkin_listing_adapter
                                } else {
                                    //no data in array list
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(context, Constant.NODATA, Toast.LENGTH_LONG)
                                        .show()
                                }
                            } else {
                                binding.progressBar.visibility = View.GONE
                                // Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            binding.progressBar.visibility = View.GONE
                            // Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                        }
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        //Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    Toast.makeText(context, Constant.ERRORMSG, Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                }
            })
    }

    private fun callapi_searchbooktable(search_strq: String, latitude: String, logitutde: String) {
        binding.progressBar.visibility = View.VISIBLE
        info.getlist_searchres(search_strq, latitude, logitutde)
            ?.enqueue(object : Callback<JsonObject?> {
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
                                        floor(mjson_obj.getDouble("distance") * 100) / 100
                                    bookTable.distance = double_val.toString()
                                    restoDataArrayList?.add(bookTable)
                                }
                                binding.progressBar.visibility = View.GONE
                                walkin_listing_adapter = Walkin_listing_Adapter(
                                    requireActivity(),
                                    restoDataArrayList,
                                    "listing",
                                    context
                                )
                                binding.walkinlistingRecycleview.adapter = walkin_listing_adapter
                            } else {
                                //no data in array list
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(context, Constant.NODATA, Toast.LENGTH_LONG).show()
                            }
                        }
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, Constant.ERRORMSG, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    Toast.makeText(context, Constant.ERRORMSG, Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
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
            val service_id = "2"
            callapi_getbooktable(service_id, saveLatitude.toString(), saveLongitude.toString())
        } else {
            Toast.makeText(context, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
        }
    } //Api code for Book Table end

    companion object {
        fun newInstance(): Walkin_listing_Fragment {
            return Walkin_listing_Fragment()
        }
    }
}