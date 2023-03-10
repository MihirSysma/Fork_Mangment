package com.forkmang.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.forkmang.vm.ViewModel
import com.forkmang.adapter.WalkinListingAdapter
import com.forkmang.data.RestoData
import com.forkmang.databinding.FragmentWalkinlistingLayoutBinding
import com.forkmang.helper.*
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

class WalkinListingFragment : Fragment() {

    var walkInListingAdapter: WalkinListingAdapter? = null
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
        val serviceId = "2"
        /*saveLatitude = 21.209589;
        saveLongitude = 72.860824;*/saveLatitude = 23.933689
        saveLongitude = 72.367458
        callApiGetBookTable(serviceId, saveLatitude.toString(), saveLongitude.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    private fun observe() {
        viewModel.searchData.observe(viewLifecycleOwner) {
            if (isVisible) {
                logThis("WalkIn Frag $it")
                if (it.isNullOrEmpty()) {
                    callReloadBookTable()
                } else {
                    filterBookTable(it)
                }
            }
        }
    }

    //Api code for Book Table start
    private fun callApiGetBookTable(serviceId: String, latitude: String, logitutde: String) {
        binding.progressBar.visibility = View.VISIBLE
        info.getListResWalkIn(serviceId, latitude, logitutde)
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
                                        val myJsonObj =
                                            jsonObject.getJSONObject("data").getJSONArray("data")
                                                .getJSONObject(i)

                                        //JSONObject mjson_obj = jsonObject.getJSONObject("data").getJSONArray("data").getJSONObject(0);
                                        bookTable.rest_name = myJsonObj.getString("rest_name")
                                        /*if(i > 0)
                                        {
                                            bookTable.setRest_name("REST"+" "+i);
                                        }
                                        else{
                                            bookTable.setRest_name(mjson_obj.getString("rest_name"));
                                        }*/if (myJsonObj.has("endtime")) {
                                            bookTable.endtime = myJsonObj.getString("endtime")
                                        } else {
                                            bookTable.endtime = "00"
                                        }
                                        bookTable.id = myJsonObj.getString("id")
                                        val double_val =
                                            floor(myJsonObj.getDouble("distance") * 100) / 100
                                        bookTable.distance = double_val.toString()
                                        restoDataArrayList?.add(bookTable)
                                    }
                                    binding.progressBar.visibility = View.GONE
                                    walkInListingAdapter = WalkinListingAdapter(
                                        "listing"
                                    ) { restId, restroData ->
                                        val intent =
                                            Intent(activity, WalkinDetailFragment::class.java)
                                        intent.putExtra("resturant_id", restId)
                                        intent.putExtra("restromodel", restroData)
                                        startActivity(intent)
                                    }
                                    binding.walkinlistingRecycleview.adapter =
                                        walkInListingAdapter
                                    walkInListingAdapter?.resto_dataArrayList =
                                        restoDataArrayList as ArrayList<RestoData>
                                } else {
                                    //no data in array list
                                    binding.progressBar.visibility = View.GONE
                                    context?.showToastMessage(Constant.NODATA)
                                }
                            } else {
                                binding.progressBar.visibility = View.GONE
                                // getContext.showToastMessage(, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            binding.progressBar.visibility = View.GONE
                            // getContext.showToastMessage(, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                        }
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        //getContext.showToastMessage(, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    context?.showToastMessage(Constant.ERRORMSG)
                    binding.progressBar.visibility = View.GONE
                }
            })
    }

    private fun callApiSearchBookTable(search_strq: String, latitude: String, logitutde: String) {
        binding.progressBar.visibility = View.VISIBLE
        info.getListSearchRes(search_strq, latitude, logitutde)
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
                                    val myJsonObj =
                                        jsonObject.getJSONObject("data").getJSONArray("data")
                                            .getJSONObject(i)
                                    bookTable.rest_name = myJsonObj.getString("rest_name")
                                    if (myJsonObj.has("endtime")) {
                                        bookTable.endtime = myJsonObj.getString("endtime")
                                    } else {
                                        bookTable.endtime = "00"
                                    }
                                    bookTable.id = myJsonObj.getString("id")
                                    val double_val =
                                        floor(myJsonObj.getDouble("distance") * 100) / 100
                                    bookTable.distance = double_val.toString()
                                    restoDataArrayList?.add(bookTable)
                                }
                                binding.progressBar.visibility = View.GONE
                                walkInListingAdapter = WalkinListingAdapter(
                                    "listing"
                                ) { restId, restroData ->
                                    val intent = Intent(activity, WalkinDetailFragment::class.java)
                                    intent.putExtra("resturant_id", restId)
                                    intent.putExtra("restromodel", restroData)
                                    startActivity(intent)
                                }
                                binding.walkinlistingRecycleview.adapter = walkInListingAdapter
                                walkInListingAdapter?.resto_dataArrayList =
                                    restoDataArrayList as ArrayList<RestoData>
                            } else {
                                //no data in array list
                                binding.progressBar.visibility = View.GONE
                                context?.showToastMessage(Constant.NODATA)
                            }
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

    private fun filterBookTable(search_str: String) {
        if (isNetworkAvailable(requireContext())) {
            callApiSearchBookTable(search_str, saveLatitude.toString(), saveLongitude.toString())
        } else {
            context?.showToastMessage(Constant.NETWORKEROORMSG)
        }
    }

    private fun callReloadBookTable() {
        if (activity?.applicationContext?.let { isNetworkAvailable(it) } == true) {
            saveLatitude = 21.209589
            saveLongitude = 72.860824
            val serviceId = "2"
            callApiGetBookTable(serviceId, saveLatitude.toString(), saveLongitude.toString())
        } else {
            context?.showToastMessage(Constant.NETWORKEROORMSG)
        }
    } //Api code for Book Table end

    companion object {
        lateinit var viewModel: ViewModel
        fun newInstance(viewModel: ViewModel): WalkinListingFragment {
            this.viewModel = viewModel
            return WalkinListingFragment()
        }
    }
}