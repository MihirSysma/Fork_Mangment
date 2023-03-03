package com.forkmang.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.forkmang.R
import com.forkmang.activity.Walkin_ActionPage
import com.forkmang.adapter.SpinnerAdapter
import com.forkmang.adapter.SpinnerAdapterType
import com.forkmang.adapter.SpinnerAdapterOccasion
import com.forkmang.adapter.WalkinListingAdapter
import com.forkmang.data.AreaDropdown
import com.forkmang.data.RestoData
import com.forkmang.databinding.FragmentWalkindetailLayoutBinding
import com.forkmang.helper.Constant
import com.forkmang.helper.Constant.SUCCESS_CODE
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
import java.io.IOException
import kotlin.math.floor

class WalkinDetailFragment : FragmentActivity() {
    //Walkin_detail_Fragment instance;
    var relative_bottom: RelativeLayout? = null
    var ctx: Context = this@WalkinDetailFragment
    var resturant_id: String? = null
    var str_area = "Indoor"
    var restoData: RestoData? = null
    var saveLatitude: Double? = null
    var saveLongitude: Double? = null
    var restoDataArrayList: ArrayList<RestoData>? = null
    var walkin_listing_adapter: WalkinListingAdapter? = null
    private val storePrefrence by lazy { StorePrefrence(this) }
    var quee_no: String? = null
    var noof_person: String? = null
    var occasion: String? = null
    var area: String? = null
    var identifier = ""
    var str_area_id: String? = null
    var txtqno_bottom: TextView? = null
    var txtnextview: TextView? = null
    var areaDropdownArrayList: ArrayList<AreaDropdown>? = null
    var is_areatype = false
    var is_pesonselect = false
    var is_occasionselect = false
    var person_arr = arrayOf("Select Person", "1", "2 ", "3", "4", "5", "6", "7", "8", "9", "10")
    var occasion_arr = arrayOf("Select Occasion", "Birthday", "Anniversary", "Marriage")

    private val binding by lazy { FragmentWalkindetailLayoutBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //instance= this;
        txtqno_bottom = findViewById(R.id.txtqno_bottom)
        relative_bottom = findViewById(R.id.relative_bottom)
        txtnextview = findViewById(R.id.txtnextview)
        val verticalLayoutManager = LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false)
        binding.walkinRecycleview.layoutManager = verticalLayoutManager
        binding.walkinRecycleview.setHasFixedSize(true)
        val intent = intent
        resturant_id = intent.getStringExtra("resturant_id")
        restoData = getIntent().getSerializableExtra("restromodel") as RestoData?


        //spinner_person array adapter start
        val personAdapter = SpinnerAdapter(applicationContext, person_arr)
        binding.spinner.adapter = personAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                is_pesonselect = false
                if (position > 0) {
                    showToastMessage(person_arr[position])
                    noof_person = person_arr[position]
                    val arrOfStr = noof_person?.split(" ".toRegex(), limit = 2)?.toTypedArray()
                    noof_person = arrOfStr?.get(0)
                    is_pesonselect = true
                } else {
                    is_pesonselect = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showToastMessage("not selected")
            }
        }
        //spinner_person array adapter end


        //spinner_occasion array adapter start
        val ocassionAdapter = SpinnerAdapterOccasion(applicationContext, occasion_arr)
        binding.spinnerOcassion.adapter = ocassionAdapter
        binding.spinnerOcassion.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    is_occasionselect = false
                    if (position > 0) {
                        showToastMessage(occasion_arr[position])
                        occasion = occasion_arr[position]
                        is_occasionselect = true
                    } else {
                        is_occasionselect = false
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    showToastMessage("not selected")
                }
            }
        //spinner_occasion array adapter end


        //spinner area array adapter start
        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    val areaDropdown = areaDropdownArrayList?.get(position)
                    showToastMessage(areaDropdown?.area_name.toString())
                    area = areaDropdown?.area_name
                    str_area_id = areaDropdown?.id
                    is_areatype = true
                    /*if(is_pesonselect)
                        {
                            / *rel_lablview.setVisibility(View.VISIBLE);
                            rel_txtview.setVisibility(View.VISIBLE);
                            txt_noseat.setText(str_area+" "+noof_person+" "+"Seats");
                            txt_view_date.setText(day); } */
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //spinner area array adapter end
        if (isNetworkAvailable(ctx)) {
            callapi_getquess(resturant_id)
            callapi_filldropdown(resturant_id)
        } else {
            showToastMessage(Constant.NETWORKEROORMSG)
        }
        binding.txtnextview.setOnClickListener {
            //testing purpose
            val mainIntent = Intent(ctx, Walkin_ActionPage::class.java)
            mainIntent.putExtra("quee_no", quee_no)
            mainIntent.putExtra("person", noof_person)
            mainIntent.putExtra("occasion", occasion)
            mainIntent.putExtra("area", area)
            mainIntent.putExtra("restromodel", restoData)
            startActivity(mainIntent)
        }
        binding.getInquee.setOnClickListener {
            //testing purpose hard coded
            identifier = ""
            noof_person = "8"
            occasion = "Birthday"
            area = "Terace View"
            val id = str_area_id
            //callapi_getqueeconform(action,resturant_id, noof_person, occasion, str_area,identifier);
            if (isNetworkAvailable(ctx)) {
                callapi_getquee(resturant_id, noof_person!!, occasion!!, area!!)
            } else {
                showToastMessage(Constant.NETWORKEROORMSG)
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        // ((Booking_TabView_Activity)getActivity()).hide_search();
        val service_id = "2"
        /*saveLatitude = 21.209589;
        saveLongitude = 72.860824;*/saveLatitude = 23.933689
        saveLongitude = 72.367458
        if (isNetworkAvailable(ctx)) {
            callapi_getbooktable(service_id, saveLatitude.toString(), saveLongitude.toString())
        } else {
            showToastMessage(Constant.NETWORKEROORMSG)
        }
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
                                    .equals(SUCCESS_CODE, ignoreCase = true)
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
                                    walkin_listing_adapter = WalkinListingAdapter(
                                        "detail"
                                    ) { restId, _ ->
                                        callapi_getquess(restId)
                                    }
                                    binding.walkinRecycleview.adapter = walkin_listing_adapter
                                    walkin_listing_adapter?.resto_dataArrayList =
                                        restoDataArrayList as ArrayList<RestoData>
                                } else {
                                    //no data in array list
                                    binding.progressBar.visibility = View.GONE
                                    showToastMessage(Constant.NODATA)
                                }
                            } else {
                                binding.progressBar.visibility = View.GONE
                                // getContext?.showToastMessage(, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            binding.progressBar.visibility = View.GONE
                            // getContext?.showToastMessage(, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                        }
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        //getContext?.showToastMessage(, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    showToastMessage(Constant.ERRORMSG)
                    binding.progressBar.visibility = View.GONE
                }
            })
    }

    //Api code for get queue list no
    fun callapi_getquess(resturant_id: String?) {
        binding.progressBar.visibility = View.VISIBLE
        info.getqueelist("Bearer " + storePrefrence.getString(TOKEN_LOGIN), resturant_id)
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            //Log.d("Result", jsonObject.toString());
                            if (jsonObject.getString("status")
                                    .equals(SUCCESS_CODE, ignoreCase = true)
                            ) {
                                //ctx?.showToastMessage(jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                quee_no = jsonObject.getString("data")
                                val quee_no = jsonObject.getString("data")
                                if (binding.txtQueeno.text.toString() == "0") {
                                    //set quee_no
                                    binding.txtQueeno.text = quee_no
                                } else if (binding.txtQueeno.text.toString()
                                        .toInt() < quee_no.toInt()
                                ) {
                                    binding.progressBar.visibility = View.GONE
                                    showAlertView(quee_no)
                                } else if (binding.txtQueeno.text.toString()
                                        .toInt() > quee_no.toInt()
                                ) {
                                    binding.txtQueeno.text = quee_no
                                } else {
                                    binding.txtQueeno.text = quee_no
                                }
                            } else {
                                // getContext?.showToastMessage(, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // getContext?.showToastMessage(, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                        }
                        binding.progressBar.visibility = View.GONE
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        //getContext?.showToastMessage(, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    // ctx?.showToastMessage(Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                    binding.progressBar.visibility = View.GONE
                }
            })
    }

    //Api code for get person queue  no
    fun callapi_personqueue_no(resturant_id: String?) {
        binding.progressBar.visibility = View.VISIBLE
        info.getpersonqueeno("Bearer " + storePrefrence.getString(TOKEN_LOGIN), resturant_id)
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            //Log.d("Result", jsonObject.toString());
                            if (jsonObject.getString("status")
                                    .equals(SUCCESS_CODE, ignoreCase = true)
                            ) {
                                binding.relativeBottom.visibility = View.VISIBLE
                                showToastMessage(jsonObject.getString("message"))
                                binding.txtqnoBottom.text =
                                    "No in current queue: " + jsonObject.getJSONObject("data")
                                        .getString("queue_id")
                            } else {
                                binding.relativeBottom.visibility = View.GONE
                                // getContext?.showToastMessage(, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            binding.relativeBottom.visibility = View.GONE
                            // getContext?.showToastMessage(, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                        }
                        binding.progressBar.visibility = View.GONE
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        //getContext?.showToastMessage(, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    showToastMessage(Constant.ERRORMSG)
                    binding.progressBar.visibility = View.GONE
                }
            })
    }

    fun showAlertView(quee_no: String?) {
        val alertDialog = AlertDialog.Builder(ctx)
        val inflater = ctx.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.quee_alertview, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog = alertDialog.create()
        val txt_msg: TextView = dialogView.findViewById(R.id.txt_msg)
        val tvyes: TextView = dialogView.findViewById(R.id.tvyes)
        val tvno: TextView = dialogView.findViewById(R.id.tvno)
        txt_msg.text =
            "new queue no is grater than previous quee no still you want to set new queue no ?"
        tvyes.setOnClickListener { v: View? ->
            dialog.dismiss()
            binding.txtQueeno.text = quee_no
        }
        tvno.setOnClickListener { v: View? -> dialog.dismiss() }
        dialog.show()
    }

    fun showAlertView_yeswantbook(msg: String?) {
        val alertDialog = AlertDialog.Builder(ctx)
        val inflater = ctx.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.quee_alertview, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog = alertDialog.create()
        val txt_msg: TextView = dialogView.findViewById(R.id.txt_msg)
        val tvyes: TextView = dialogView.findViewById(R.id.tvyes)
        val tvno: TextView = dialogView.findViewById(R.id.tvno)
        txt_msg.text = msg
        tvyes.setOnClickListener { v: View? ->
            dialog.dismiss()
            if (isNetworkAvailable(ctx)) {
                callapi_getqueeconform(
                    "yes",
                    restoData?.id,
                    noof_person,
                    occasion,
                    area,
                    identifier
                )
            } else {
                showToastMessage(Constant.NETWORKEROORMSG)
            }
        }
        tvno.setOnClickListener { v: View? -> dialog.dismiss() }
        dialog.show()
    }

    private fun callapi_filldropdown(restaurant_id: String?) {
        binding.progressBar.visibility = View.VISIBLE
        info.getres_detail(restaurant_id)?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    if (response.code() == Constant.SUCCESS_CODE_n) {
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        //Log.d("Result", jsonObject.toString());
                        if (jsonObject.getString("status")
                                .equals(SUCCESS_CODE, ignoreCase = true)
                        ) {
                            if (jsonObject.getJSONObject("data").getJSONArray("data")
                                    .length() > 0
                            ) {
                                areaDropdownArrayList = ArrayList()
                                for (i in 0 until jsonObject.getJSONObject("data")
                                    .getJSONArray("data").length()) {
                                    //Area Type Spinner code
                                    val mjson_array_area =
                                        jsonObject.getJSONObject("data").getJSONArray("data")
                                            .getJSONObject(i).getJSONArray("area")
                                    val areaDropdown_first = AreaDropdown()
                                    areaDropdown_first.id = "0"
                                    areaDropdown_first.area_name = "Select Area"
                                    areaDropdownArrayList?.add(areaDropdown_first)
                                    for (j in 0 until mjson_array_area.length()) {
                                        val areaDropdown = AreaDropdown()
                                        val mjson_object_area = mjson_array_area.getJSONObject(j)
                                        areaDropdown.id = mjson_object_area.getString("id")
                                        areaDropdown.area_name = mjson_object_area.getString("name")
                                        areaDropdownArrayList?.add(areaDropdown)
                                    }
                                    val spinnnerAdapter_type = SpinnerAdapterType(
                                        applicationContext, areaDropdownArrayList!!
                                    )
                                    binding.spinnerType.adapter = spinnnerAdapter_type
                                    //code end
                                }
                                binding.progressBar.visibility = View.GONE
                            } else {
                                //no data in array list
                                binding.progressBar.visibility = View.GONE
                                showToastMessage(Constant.NODATA)
                            }
                        }
                    } else {
                        binding.progressBar.visibility = View.GONE
                        showToastMessage(Constant.ERRORMSG)
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    showToastMessage(Constant.ERRORMSG)
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                showToastMessage(Constant.ERRORMSG)
            }
        })
    }

    private fun callapi_getqueeconform(
        action: String,
        restaurant_id: String?,
        person: String?,
        occasion: String?,
        area: String?,
        identifier: String
    ) {
        binding.progressBar.visibility = View.VISIBLE
        info.queue_confirmation(
            "Bearer " + storePrefrence.getString(TOKEN_LOGIN),
            action, restaurant_id, person, occasion, area, identifier
        )?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    if (response.code() == Constant.SUCCESS_CODE_n) {
                        binding.progressBar.visibility = View.GONE
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        //Log.d("Result", jsonObject.toString());
                        showToastMessage(jsonObject.getString("message"))
                        if (isNetworkAvailable(ctx)) {
                            callapi_personqueue_no(restaurant_id)
                        } else {
                            showToastMessage(Constant.NETWORKEROORMSG)
                        }
                    } else {
                        binding.progressBar.visibility = View.GONE
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        showToastMessage(jsonObject.getString("message"))
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    showToastMessage(Constant.ERRORMSG)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    showToastMessage(Constant.ERRORMSG)
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                showToastMessage(Constant.ERRORMSG)
            }
        })
    }

    private fun callapi_getquee(
        restaurant_id: String?,
        person: String,
        occasion: String,
        area: String
    ) {
        binding.progressBar.visibility = View.VISIBLE
        info.queue_get(
            "Bearer " + storePrefrence.getString(TOKEN_LOGIN),
            restaurant_id, person, occasion, area
        )?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    if (response.code() == Constant.SUCCESS_CODE_n) {
                        binding.progressBar.visibility = View.GONE
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                            // Do something with object.
                            val is_showalert =
                                jsonObject.getJSONObject("data").getBoolean("showalert")
                            if (is_showalert) {
                                showAlertView_yeswantbook(jsonObject.getString("message"))
                            } else {
                                showToastMessage(jsonObject.getString("message"))
                            }
                        } else {
                            ctx.showToastMessage(jsonObject.getString("message"))
                            if (isNetworkAvailable(ctx)) {
                                callapi_personqueue_no(restaurant_id)
                            } else {
                                ctx.showToastMessage(Constant.NETWORKEROORMSG)
                            }
                        }
                    } else if (response.code() == Constant.ERROR_CODE_n) {
                        binding.progressBar.visibility = View.GONE
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        ctx.showToastMessage(jsonObject.getString("message"))
                        if (isNetworkAvailable(ctx)) {
                            callapi_personqueue_no(restaurant_id)
                        } else {
                            ctx.showToastMessage(Constant.NETWORKEROORMSG)
                        }
                        /* if (jsonObject.has("data") && !jsonObject.isNull("data"))
                                {
                                    // Do something with object.
                                    boolean is_showalert = jsonObject.getJSONObject("data").getBoolean("showalert");
                                    if(is_showalert)
                                    {
                                        showAlertView_yeswantbook(jsonObject.getString("message"));
                                    }
                                    else{
                                        ctx?.showToastMessage(jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                }*/
                    } else if (response.code() == Constant.ERROR_CODE) {
                        binding.progressBar.visibility = View.GONE
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        ctx.showToastMessage(jsonObject.getString("message"))
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    ctx.showToastMessage(Constant.ERRORMSG)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    ctx.showToastMessage(Constant.ERRORMSG)
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                ctx.showToastMessage(Constant.ERRORMSG)
            }
        })
    }

    companion object {
        fun newInstance(): WalkinDetailFragment {
            return WalkinDetailFragment()
        }
    }
}