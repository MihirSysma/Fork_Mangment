package com.forkmang.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.forkmang.vm.ViewModel
import com.forkmang.activity.PickupSelectFoodActivity
import com.forkmang.adapter.PickupFragmentBookTableAdapter
import com.forkmang.data.RestoData
import com.forkmang.databinding.FragmentPickupLayoutBinding
import com.forkmang.helper.ApiConfig.getLocation
import com.forkmang.helper.Constant.ERRORMSG
import com.forkmang.helper.Constant.NETWORKEROORMSG
import com.forkmang.helper.Constant.NODATA
import com.forkmang.helper.Constant.SUCCESS_CODE
import com.forkmang.helper.Constant.SUCCESS_CODE_n
import com.forkmang.helper.GPSTracker
import com.forkmang.helper.Utils.isNetworkAvailable
import com.forkmang.helper.logThis
import com.forkmang.helper.showToastMessage
import com.forkmang.network_call.Api.info
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.floor

class PickupFragment : Fragment() {

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
        val serviceId = "3"
        saveLatitude = 23.933689
        saveLongitude = 72.367458
        callApiGetBookTable(serviceId, saveLatitude.toString(), saveLongitude.toString())
        //((Booking_TabView_Activity)getActivity()).hide_search();
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    private fun observe() {
        viewModel.searchData.observe(viewLifecycleOwner) {
            if (isVisible) {
                logThis("PickUp Frag $it")
                if (it.isNullOrEmpty()) {
                    callReloadBookTable()
                } else {
                    filterBookTable(it)
                }
            }
        }
    }

    //Api code for Book Table start
    private fun callApiGetBookTable(service_id: String, latitude: String, logitutde: String) {
        binding.progressbar.visibility = View.VISIBLE
        info.getListResWalkIn(service_id, latitude, logitutde)
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        if (response.code() == SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            //Log.d("Result", jsonObject.toString());
                            if (jsonObject.getString("status")
                                    .equals(SUCCESS_CODE, ignoreCase = true)
                            ) {
                                if (jsonObject.getJSONObject("data").getJSONArray("data")
                                        .length() > 0
                                ) {
                                    restoDataArrayList = ArrayList()
                                    for (i in 0 until jsonObject.getJSONObject("data")
                                        .getJSONArray("data").length()) {
                                        val bookTable = RestoData()
                                        val mjsonObj =
                                            jsonObject.getJSONObject("data").getJSONArray("data")
                                                .getJSONObject(i)

                                        //JSONObject mjson_obj = jsonObject.getJSONObject("data").getJSONArray("data").getJSONObject(0);
                                        bookTable.rest_name = mjsonObj.getString("rest_name")
                                        /*if(i > 0)
                                        {
                                            bookTable.setRest_name("REST"+" "+i);
                                        }
                                        else{
                                            bookTable.setRest_name(mjson_obj.getString("rest_name"));
                                        }*/if (mjsonObj.has("endtime")) {
                                            bookTable.endtime = mjsonObj.getString("endtime")
                                        } else {
                                            bookTable.endtime = "00"
                                        }
                                        bookTable.id = mjsonObj.getString("id")
                                        val doubleVal =
                                            floor(mjsonObj.getDouble("distance") * 100) / 100
                                        bookTable.distance = doubleVal.toString()
                                        restoDataArrayList?.add(bookTable)
                                    }
                                    binding.progressbar.visibility = View.GONE
                                    val pickupFragmentBookTableAdapter =
                                        PickupFragmentBookTableAdapter {
                                            val mainIntent = Intent(
                                                activity,
                                                PickupSelectFoodActivity::class.java
                                            )
                                            mainIntent.putExtra("restromodel", it)
                                            activity?.startActivity(mainIntent)
                                        }
                                    binding.pickRecycleview.adapter = pickupFragmentBookTableAdapter
                                    pickupFragmentBookTableAdapter.resto_dataArrayList =
                                        restoDataArrayList as ArrayList<RestoData>
                                } else {
                                    //no data in array list
                                    binding.progressbar.visibility = View.GONE
                                    context?.showToastMessage(NODATA)
                                }
                            } else {
                                binding.progressbar.visibility = View.GONE
                                context?.showToastMessage(ERRORMSG)
                            }
                        } else {
                            binding.progressbar.visibility = View.GONE
                            context?.showToastMessage(ERRORMSG)
                        }
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        binding.progressbar.visibility = View.GONE
                        context?.showToastMessage(ERRORMSG)
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    context?.showToastMessage(ERRORMSG)
                    binding.progressbar.visibility = View.GONE
                }
            })
    }

    private fun callApiSearchBookTable(search_strq: String, latitude: String, logitutde: String) {
        binding.progressbar.visibility = View.VISIBLE
        info.getListSearchRes(search_strq, latitude, logitutde)
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        //Log.d("Result", jsonObject.toString());
                        if (jsonObject.getString("status")
                                .equals(SUCCESS_CODE, ignoreCase = true)
                        ) {
                            if (jsonObject.getJSONObject("data").getJSONArray("data")
                                    .length() > 0
                            ) {
                                restoDataArrayList = ArrayList()
                                for (i in 0 until jsonObject.getJSONObject("data")
                                    .getJSONArray("data").length()) {
                                    val bookTable = RestoData()
                                    val mjsonObj =
                                        jsonObject.getJSONObject("data").getJSONArray("data")
                                            .getJSONObject(i)
                                    bookTable.rest_name = mjsonObj.getString("rest_name")
                                    if (mjsonObj.has("endtime")) {
                                        bookTable.endtime = mjsonObj.getString("endtime")
                                    } else {
                                        bookTable.endtime = "00"
                                    }
                                    bookTable.id = mjsonObj.getString("id")
                                    val doubleVal =
                                        floor(mjsonObj.getDouble("distance") * 100) / 100
                                    bookTable.distance = doubleVal.toString()
                                    restoDataArrayList?.add(bookTable)
                                }
                                binding.progressbar.visibility = View.GONE
                                val pickupFragmentBookTableAdapter =
                                    PickupFragmentBookTableAdapter {
                                        val mainIntent =
                                            Intent(activity, PickupSelectFoodActivity::class.java)
                                        mainIntent.putExtra("restromodel", it)
                                        activity?.startActivity(mainIntent)
                                    }
                                binding.pickRecycleview.adapter = pickupFragmentBookTableAdapter
                                pickupFragmentBookTableAdapter.resto_dataArrayList =
                                    restoDataArrayList as ArrayList<RestoData>
                            } else {
                                //no data in array list
                                binding.progressbar.visibility = View.GONE
                                context?.showToastMessage(NODATA)
                            }
                        }
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        binding.progressbar.visibility = View.GONE
                        context?.showToastMessage(ERRORMSG)
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    context?.showToastMessage(ERRORMSG)
                    binding.progressbar.visibility = View.GONE
                }
            })
    }

    private fun filterBookTable(search_str: String) {
        if (isNetworkAvailable(requireContext())) {
            callApiSearchBookTable(search_str, saveLatitude.toString(), saveLongitude.toString())
        } else {
            context?.showToastMessage(NETWORKEROORMSG)
        }
    }

    private fun callReloadBookTable() {
        if (isNetworkAvailable(requireContext())) {
            saveLatitude = 21.209589
            saveLongitude = 72.860824
            val serviceId = "3"
            callApiGetBookTable(serviceId, saveLatitude.toString(), saveLongitude.toString())
        } else {
            context?.showToastMessage(NETWORKEROORMSG)
        }
    }

    companion object {
        lateinit var viewModel: ViewModel
        fun newInstance(viewModel: ViewModel): PickupFragment {
            this.viewModel = viewModel
            return PickupFragment()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}