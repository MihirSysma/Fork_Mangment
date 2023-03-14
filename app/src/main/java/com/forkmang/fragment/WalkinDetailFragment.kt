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
import com.forkmang.activity.WalkinActionPage
import com.forkmang.adapter.SpinnerAdapter
import com.forkmang.adapter.SpinnerAdapterType
import com.forkmang.adapter.SpinnerAdapterOccasion
import com.forkmang.adapter.WalkinListingAdapter
import com.forkmang.data.AreaDropdown
import com.forkmang.data.RestoData
import com.forkmang.databinding.FragmentWalkindetailLayoutBinding
import com.forkmang.helper.Constant.ERRORMSG
import com.forkmang.helper.Constant.ERROR_CODE
import com.forkmang.helper.Constant.ERROR_CODE_n
import com.forkmang.helper.Constant.NETWORKEROORMSG
import com.forkmang.helper.Constant.NODATA
import com.forkmang.helper.Constant.SUCCESS_CODE
import com.forkmang.helper.Constant.SUCCESS_CODE_n
import com.forkmang.helper.Constant.TOKEN_LOGIN
import com.forkmang.helper.StorePreference
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
    var relativeBottom: RelativeLayout? = null
    var ctx: Context = this@WalkinDetailFragment
    var restaurantId: String? = null
    var restoData: RestoData? = null
    var saveLatitude: Double? = null
    var saveLongitude: Double? = null
    var restoDataArrayList: ArrayList<RestoData>? = null
    var walkinListingAdapter: WalkinListingAdapter? = null
    private val storePreference by lazy { StorePreference(this) }
    var queeNo1: String? = null
    var noOfPerson: String? = null
    var occasion: String? = null
    var area: String? = null
    var identifier = ""
    var strAreaId: String? = null
    var txtqnoBottom: TextView? = null
    var txtnextview: TextView? = null
    var areaDropdownArrayList: ArrayList<AreaDropdown>? = null
    var isAreaType = false
    var isPersonSelect = false
    var isOccasionSelect = false
    var personArr = arrayOf("Select Person", "1", "2 ", "3", "4", "5", "6", "7", "8", "9", "10")
    var occasionArr = arrayOf("Select Occasion", "Birthday", "Anniversary", "Marriage")

    private val binding by lazy { FragmentWalkindetailLayoutBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //instance= this;

        txtqnoBottom = findViewById(R.id.txtqno_bottom)
        relativeBottom = findViewById(R.id.relative_bottom)
        txtnextview = findViewById(R.id.txtnextview)
        val verticalLayoutManager = LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false)
        binding.walkinRecycleview.layoutManager = verticalLayoutManager
        binding.walkinRecycleview.setHasFixedSize(true)
        val intent = intent
        restaurantId = intent.getStringExtra("resturant_id")
        restoData = getIntent().getSerializableExtra("restromodel") as RestoData?


        //spinner_person array adapter start
        val personAdapter = SpinnerAdapter(applicationContext, personArr)
        binding.spinner.adapter = personAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                isPersonSelect = false
                if (position > 0) {
                    showToastMessage(personArr[position])
                    noOfPerson = personArr[position]
                    val arrOfStr = noOfPerson?.split(" ".toRegex(), limit = 2)?.toTypedArray()
                    noOfPerson = arrOfStr?.get(0)
                    isPersonSelect = true
                } else {
                    isPersonSelect = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showToastMessage("not selected")
            }
        }
        //spinner_person array adapter end


        //spinner_occasion array adapter start
        val ocassionAdapter = SpinnerAdapterOccasion(applicationContext, occasionArr)
        binding.spinnerOcassion.adapter = ocassionAdapter
        binding.spinnerOcassion.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    isOccasionSelect = false
                    if (position > 0) {
                        showToastMessage(occasionArr[position])
                        occasion = occasionArr[position]
                        isOccasionSelect = true
                    } else {
                        isOccasionSelect = false
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
                    strAreaId = areaDropdown?.id
                    isAreaType = true
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
            callApiGetQueeList(restaurantId)
            callApiFillDropdown(restaurantId)
        } else {
            showToastMessage(NETWORKEROORMSG)
        }
        binding.txtnextview.setOnClickListener {
            //testing purpose
            val mainIntent = Intent(ctx, WalkinActionPage::class.java)
            mainIntent.putExtra("quee_no", queeNo1)
            mainIntent.putExtra("person", noOfPerson)
            mainIntent.putExtra("occasion", occasion)
            mainIntent.putExtra("area", area)
            mainIntent.putExtra("restromodel", restoData)
            startActivity(mainIntent)
        }
        binding.getInquee.setOnClickListener {
            //testing purpose hard coded
            identifier = ""
            noOfPerson = "8"
            occasion = "Birthday"
            area = "Terace View"
            val id = strAreaId
            //callapi_getqueeconform(action,resturant_id, noof_person, occasion, str_area,identifier);
            if (isNetworkAvailable(ctx)) {
                callApiGetQuee(restaurantId, noOfPerson!!, occasion!!, area!!)
            } else {
                showToastMessage(NETWORKEROORMSG)
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        // ((Booking_TabView_Activity)getActivity()).hide_search();
        val serviceId = "2"
        /*saveLatitude = 21.209589;
        saveLongitude = 72.860824;*/saveLatitude = 23.933689
        saveLongitude = 72.367458
        if (isNetworkAvailable(ctx)) {
            callApiGetBookTable(serviceId, saveLatitude.toString(), saveLongitude.toString())
        } else {
            showToastMessage(NETWORKEROORMSG)
        }
    }

    //Api code for Book Table start
    private fun callApiGetBookTable(service_id: String, latitude: String, logitutde: String) {
        binding.progressBar.visibility = View.VISIBLE
        info.getListResWalkIn(service_id, latitude, logitutde)
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        if (response.code() == SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
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
                                    binding.progressBar.visibility = View.GONE
                                    walkinListingAdapter = WalkinListingAdapter(
                                        "detail"
                                    ) { restId, _ ->
                                        callApiGetQueeList(restId)
                                    }
                                    binding.walkinRecycleview.adapter = walkinListingAdapter
                                    walkinListingAdapter?.resto_dataArrayList =
                                        restoDataArrayList as ArrayList<RestoData>
                                } else {
                                    //no data in array list
                                    binding.progressBar.visibility = View.GONE
                                    showToastMessage(NODATA)
                                }
                            } else {
                                binding.progressBar.visibility = View.GONE
                                showToastMessage(ERRORMSG)
                            }
                        } else {
                            binding.progressBar.visibility = View.GONE
                            showToastMessage(ERRORMSG)
                        }
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        showToastMessage(ERRORMSG)
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    showToastMessage(ERRORMSG)
                    binding.progressBar.visibility = View.GONE
                }
            })
    }

    //Api code for get queue list no
    fun callApiGetQueeList(resturant_id: String?) {
        binding.progressBar.visibility = View.VISIBLE
        info.getQueueList("Bearer " + storePreference.getString(TOKEN_LOGIN), resturant_id)
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        if (response.code() == SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            //Log.d("Result", jsonObject.toString());
                            if (jsonObject.getString("status")
                                    .equals(SUCCESS_CODE, ignoreCase = true)
                            ) {
                                //ctx?.showToastMessage(jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                val data = jsonObject.getJSONObject("data")
                                val queeNo = data.getString("queue_number")
                                if (binding.txtQueeno.text.toString() == "0") {
                                    //set quee_no
                                    binding.txtQueeno.text = queeNo
                                } else if (binding.txtQueeno.text.toString()
                                        .toInt() < queeNo.toInt()
                                ) {
                                    binding.progressBar.visibility = View.GONE
                                    showAlertView(queeNo)
                                } else if (binding.txtQueeno.text.toString()
                                        .toInt() > queeNo.toInt()
                                ) {
                                    binding.txtQueeno.text = queeNo
                                } else {
                                    binding.txtQueeno.text = queeNo
                                }
                            } else {
                                showToastMessage(ERRORMSG)
                            }
                        } else {
                            showToastMessage(ERRORMSG)
                        }
                        binding.progressBar.visibility = View.GONE
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        showToastMessage(ERRORMSG)
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    showToastMessage(ERRORMSG)
                    binding.progressBar.visibility = View.GONE
                }
            })
    }

    //Api code for get person queue  no
    fun callApiPersonQueueNo(resturant_id: String?) {
        binding.progressBar.visibility = View.VISIBLE
        info.getPersonQueeNo("Bearer " + storePreference.getString(TOKEN_LOGIN), resturant_id)
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        if (response.code() == SUCCESS_CODE_n) {
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
                                showToastMessage(ERRORMSG)
                            }
                        } else {
                            binding.relativeBottom.visibility = View.GONE
                            showToastMessage(ERRORMSG)
                        }
                        binding.progressBar.visibility = View.GONE
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        showToastMessage(ERRORMSG)
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    showToastMessage(ERRORMSG)
                    binding.progressBar.visibility = View.GONE
                }
            })
    }

    fun showAlertView(queeNo: String?) {
        val alertDialog = AlertDialog.Builder(ctx)
        val inflater = ctx.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.quee_alertview, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog = alertDialog.create()
        val txtMsg: TextView = dialogView.findViewById(R.id.txt_msg)
        val tvYes: TextView = dialogView.findViewById(R.id.tvyes)
        val tvNo: TextView = dialogView.findViewById(R.id.tvno)
        txtMsg.text =
            "new queue no is grater than previous quee no still you want to set new queue no ?"
        tvYes.setOnClickListener {
            dialog.dismiss()
            binding.txtQueeno.text = queeNo
        }
        tvNo.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun showAlertViewYesWantBook(msg: String?) {
        val alertDialog = AlertDialog.Builder(ctx)
        val inflater = ctx.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.quee_alertview, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog = alertDialog.create()
        val txtMsg: TextView = dialogView.findViewById(R.id.txt_msg)
        val tvYes: TextView = dialogView.findViewById(R.id.tvyes)
        val tvNo: TextView = dialogView.findViewById(R.id.tvno)
        txtMsg.text = msg
        tvYes.setOnClickListener {
            dialog.dismiss()
            if (isNetworkAvailable(ctx)) {
                callApiGetQueeConform(
                    "yes",
                    restoData?.id,
                    noOfPerson,
                    occasion,
                    area,
                    identifier
                )
            } else {
                showToastMessage(NETWORKEROORMSG)
            }
        }
        tvNo.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun callApiFillDropdown(restaurant_id: String?) {
        binding.progressBar.visibility = View.VISIBLE
        info.getResDetail(restaurant_id)?.enqueue(object : Callback<JsonObject?> {
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
                                areaDropdownArrayList = ArrayList()
                                for (i in 0 until jsonObject.getJSONObject("data")
                                    .getJSONArray("data").length()) {
                                    //Area Type Spinner code
                                    val mjsonArrayArea =
                                        jsonObject.getJSONObject("data").getJSONArray("data")
                                            .getJSONObject(i).getJSONArray("area")
                                    val areadropdownFirst = AreaDropdown()
                                    areadropdownFirst.id = "0"
                                    areadropdownFirst.area_name = "Select Area"
                                    areaDropdownArrayList?.add(areadropdownFirst)
                                    for (j in 0 until mjsonArrayArea.length()) {
                                        val areaDropdown = AreaDropdown()
                                        val mjsonObjectArea = mjsonArrayArea.getJSONObject(j)
                                        areaDropdown.id = mjsonObjectArea.getString("id")
                                        areaDropdown.area_name = mjsonObjectArea.getString("name")
                                        areaDropdownArrayList?.add(areaDropdown)
                                    }
                                    val spinnnerAdapterType = SpinnerAdapterType(
                                        applicationContext, areaDropdownArrayList!!
                                    )
                                    binding.spinnerType.adapter = spinnnerAdapterType
                                    //code end
                                }
                                binding.progressBar.visibility = View.GONE
                            } else {
                                //no data in array list
                                binding.progressBar.visibility = View.GONE
                                showToastMessage(NODATA)
                            }
                        }
                    } else {
                        binding.progressBar.visibility = View.GONE
                        showToastMessage(ERRORMSG)
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    showToastMessage(ERRORMSG)
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                showToastMessage(ERRORMSG)
            }
        })
    }

    private fun callApiGetQueeConform(
        action: String,
        restaurant_id: String?,
        person: String?,
        occasion: String?,
        area: String?,
        identifier: String
    ) {
        binding.progressBar.visibility = View.VISIBLE
        info.queueConfirmation(
            "Bearer " + storePreference.getString(TOKEN_LOGIN),
            action, restaurant_id, person, occasion, area, identifier
        )?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    if (response.code() == SUCCESS_CODE_n) {
                        binding.progressBar.visibility = View.GONE
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        //Log.d("Result", jsonObject.toString());
                        showToastMessage(jsonObject.getString("message"))
                        if (isNetworkAvailable(ctx)) {
                            callApiPersonQueueNo(restaurant_id)
                        } else {
                            showToastMessage(NETWORKEROORMSG)
                        }
                    } else {
                        binding.progressBar.visibility = View.GONE
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        showToastMessage(jsonObject.getString("message"))
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    showToastMessage(ERRORMSG)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    showToastMessage(ERRORMSG)
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                showToastMessage(ERRORMSG)
            }
        })
    }

    private fun callApiGetQuee(
        restaurant_id: String?,
        person: String,
        occasion: String,
        area: String
    ) {
        binding.progressBar.visibility = View.VISIBLE
        info.queueGet(
            "Bearer " + storePreference.getString(TOKEN_LOGIN),
            restaurant_id, person, occasion, area
        )?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    if (response.code() == SUCCESS_CODE_n) {
                        binding.progressBar.visibility = View.GONE
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                            // Do something with object.
                            val isShowAlert =
                                jsonObject.getJSONObject("data").getBoolean("showalert")
                            if (isShowAlert) {
                                showAlertViewYesWantBook(jsonObject.getString("message"))
                            } else {
                                showToastMessage(jsonObject.getString("message"))
                            }
                        } else {
                            showToastMessage(jsonObject.getString("message"))
                            if (isNetworkAvailable(ctx)) {
                                callApiPersonQueueNo(restaurant_id)
                            } else {
                                showToastMessage(NETWORKEROORMSG)
                            }
                        }
                    } else if (response.code() == ERROR_CODE_n) {
                        binding.progressBar.visibility = View.GONE
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        showToastMessage(jsonObject.getString("message"))
                        if (isNetworkAvailable(ctx)) {
                            callApiPersonQueueNo(restaurant_id)
                        } else {
                            showToastMessage(NETWORKEROORMSG)
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
                    } else if (response.code() == ERROR_CODE) {
                        binding.progressBar.visibility = View.GONE
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        showToastMessage(jsonObject.getString("message"))
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    showToastMessage(ERRORMSG)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    showToastMessage(ERRORMSG)
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                showToastMessage(ERRORMSG)
            }
        })
    }

    companion object {
        fun newInstance(): WalkinDetailFragment {
            return WalkinDetailFragment()
        }
    }
}