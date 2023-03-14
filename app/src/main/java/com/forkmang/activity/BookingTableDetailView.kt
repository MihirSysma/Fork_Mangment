package com.forkmang.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.forkmang.R
import com.forkmang.adapter.*
import com.forkmang.adapter.SpinnerAdapter
import com.forkmang.data.AreaDropdown
import com.forkmang.data.BranchDropdown
import com.forkmang.data.FloorDropdown
import com.forkmang.data.RestoData
import com.forkmang.databinding.ActivityBookingtableDetailviewBinding
import com.forkmang.helper.Constant.BOOKINGID
import com.forkmang.helper.Constant.CUSTOMERID
import com.forkmang.helper.Constant.ERRORMSG
import com.forkmang.helper.Constant.ERROR_CODE
import com.forkmang.helper.Constant.ERROR_CODE_n
import com.forkmang.helper.Constant.IDENTFIER
import com.forkmang.helper.Constant.MOBILE
import com.forkmang.helper.Constant.NAME
import com.forkmang.helper.Constant.NETWORKEROORMSG
import com.forkmang.helper.Constant.NODATA
import com.forkmang.helper.Constant.SUCCESS_CODE
import com.forkmang.helper.Constant.SUCCESS_CODE_n
import com.forkmang.helper.Constant.TOKEN_LOGIN
import com.forkmang.helper.StorePreference
import com.forkmang.helper.Utils
import com.forkmang.helper.showToastMessage
import com.forkmang.models.TableList
import com.forkmang.network_call.Api.info
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class BookingTableDetailView : Activity() {

    private val binding by lazy { ActivityBookingtableDetailviewBinding.inflate(layoutInflater) }
    var ctx: Context = this@BookingTableDetailView
    var mYear = 0
    var mMonth = 0
    var mDay = 0
    var mHour = 0
    var mMinute = 0
    var mSecond = 0
    var bookingDate: String? = null
    var dateGet = ""
    var restaurantId: String? = null
    var noOfPerson = "0"
    var strArea: String? = "Indoor"
    var datetime: String? = null
    var tableListArrayList: ArrayList<TableList>? = null
    var floorDropdownArrayList: ArrayList<FloorDropdown>? = null
    var areaDropdownArrayList: ArrayList<AreaDropdown>? = null
    var branchDropdownArrayList: ArrayList<BranchDropdown>? = null
    var restoData: RestoData? = null
    private val storePreference by lazy { StorePreference(this) }
    var isTableConform = false
    var isPersonSelect = false
    var isAreaType = false
    var person = arrayOf("Select Person", "1", "2 ", "3", "4", "5", "6", "7", "8", "9", "10")
    var typeValue = arrayOf("Value", "0.7", "0.8", "0.9", "1.0")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val intent = intent
        restaurantId = intent.getStringExtra("resturant_id")
        datetime = intent.getStringExtra("datetime")
        restoData = getIntent().getSerializableExtra("restromodel") as RestoData?
        binding.tableRecycleview.layoutManager = LinearLayoutManager(
            this@BookingTableDetailView,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.txtrestroname.text = restoData?.rest_name
        binding.txtTime.text = restoData?.endtime
        binding.txtTotalkm.text = restoData?.distance + " km"
        binding.btnPaymentConform.setOnClickListener { }
        binding.buttonFloor.setOnClickListener {
            binding.frameLayout.visibility = View.VISIBLE
            binding.linearLayout.visibility = View.VISIBLE
            binding.linearListview.visibility = View.GONE
        }
        binding.buttonList.setOnClickListener {
            binding.linearListview.visibility = View.VISIBLE
            binding.frameLayout.visibility = View.GONE
            binding.linearLayout.visibility = View.GONE
            binding.tableRecycleview.layoutManager = LinearLayoutManager(
                this@BookingTableDetailView,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            if (Utils.isNetworkAvailable(ctx)) {
                callApiBookTableList(restaurantId)
            } else {
                showToastMessage(NETWORKEROORMSG)
            }
        }
        binding.lytDatetime.setOnClickListener { datePicker() }
    }

    override fun onResume() {
        super.onResume()
        binding.relLablview.visibility = View.GONE
        binding.relTxtview.visibility = View.GONE
        binding.buttonFloor.isChecked = false
        binding.buttonList.isChecked = true
        currentDateShow()

        //call api for fill dropdown
        callApiFillDropdown(restaurantId)

        //spinner_person array adapter start
        val personAdapter = SpinnerAdapter(applicationContext, person)
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
                    showToastMessage(person[position])
                    noOfPerson = person[position]
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


        //spinner_branch array adapter
        binding.spinnerBranch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    val branchDropdown: BranchDropdown? = branchDropdownArrayList?.get(position)
                    showToastMessage(branchDropdown?.branch_name.toString())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showToastMessage("not selected")
            }
        }
        //spinner_branch array adapter

        //spinner_floor array adapter
        binding.spinnerFloor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    val floorDropdown: FloorDropdown? = floorDropdownArrayList?.get(position)
                    showToastMessage(floorDropdown?.floor_name.toString())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showToastMessage("not selected")
            }
        }
        //spinner_floor array adapter

        //spinner_floor array adapter
        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    val areaDropdown: AreaDropdown? = areaDropdownArrayList?.get(position)
                    showToastMessage(areaDropdown?.area_name.toString())
                    strArea = areaDropdown?.area_name
                    isAreaType = true
                    if (isPersonSelect) {
                        binding.relLablview.visibility = View.VISIBLE
                        binding.relTxtview.visibility = View.VISIBLE
                        binding.txtNoseat.text = "$strArea $noOfPerson Seats"
                        binding.txtTime.text = day
                    }
                } else {
                    isAreaType = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showToastMessage("not selected")
            }
        }
        //spinner_floor array adapter

        //spinner_type array adapter
        val typeValueAdapter = SpinnerAdapterTypeValue(applicationContext, typeValue)
        binding.spinnerTypeValue.adapter = typeValueAdapter
        binding.spinnerTypeValue.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        showToastMessage(typeValue[position])
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    showToastMessage("not selected")
                }
            }
        //spinner_floor array adapter
    }

    fun showAlertViewTableSelectionRule(tableList: TableList) {
        val alertDialog = AlertDialog.Builder(this@BookingTableDetailView)
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.tableselection_alertview, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog = alertDialog.create()
        val btnSelectNxt: Button = dialogView.findViewById(R.id.btn_select_nxt)
        val tvDesc: TextView = dialogView.findViewById(R.id.tv_descr)
        val txtReserveSeat: TextView = dialogView.findViewById(R.id.txt_reserveseat)
        val tvRule: TextView = dialogView.findViewById(R.id.tv_rule)
        val tvDressCode: TextView = dialogView.findViewById(R.id.tv_dresscode)
        val tvOccasion: TextView = dialogView.findViewById(R.id.tv_ocassion)
        tvDesc.text = tableList.table_descr
        tvRule.text = tableList.table_rule
        tvDressCode.text = tableList.table_drescode
        tvOccasion.text = tableList.table_ocassion
        txtReserveSeat.text =
            "To Reserve:" + " " + ctx.resources.getString(R.string.rupee) + tableList.price
        btnSelectNxt.setOnClickListener {
            dialog.dismiss()
            showAlertViewPaymentConfirm(tableList)
        }
        dialog.show()
    }

    private fun showAlertViewPaymentConfirm(tableList: TableList) {
        val alertDialog = AlertDialog.Builder(this@BookingTableDetailView)
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.payment_conform_alertview, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog = alertDialog.create()
        val txtRestroName: TextView = dialogView.findViewById(R.id.txt_restroname)
        val txtCustName: TextView = dialogView.findViewById(R.id.txt_custname)
        val txtDateTime: TextView = dialogView.findViewById(R.id.txt_datetime)
        val txtPhoneNo: TextView = dialogView.findViewById(R.id.txt_phoneno)
        val etvNoPerson: EditText = dialogView.findViewById(R.id.etv_noperson)
        val imgIconEdit: ImageView = dialogView.findViewById(R.id.imgicon_edit)
        val imgIconSave: ImageView = dialogView.findViewById(R.id.imgicon_save)
        txtRestroName.text = tableList.str_hotel_name
        txtCustName.text = tableList.str_customer_name
        txtDateTime.text = binding.txtDatetime.text.toString()
        etvNoPerson.setText(noOfPerson)
        txtPhoneNo.text = tableList.str_phone
        val btnSelectFood: Button = dialogView.findViewById(R.id.btn_select_food)
        val btnCancel: Button = dialogView.findViewById(R.id.btn_cancel)
        imgIconEdit.setOnClickListener {
            /*imgicon_save.setVisibility(View.VISIBLE);
            imgicon_edit.setVisibility(View.GONE);
            etv_noperson.setEnabled(true);
            etv_noperson.setBackgroundColor(Color.DKGRAY);
            etv_noperson.setTextColor(Color.WHITE);*/dialog.dismiss()
        }
        imgIconSave.setOnClickListener { }
        val btnCnfTableBook: Button = dialogView.findViewById(R.id.btn_cnf_payment)
        btnCancel.setOnClickListener { dialog.dismiss() }
        btnCnfTableBook.setOnClickListener {
            //dialog.dismiss();
            if (Utils.isNetworkAvailable(ctx)) {
                callApiConfirmTableBooking(
                    tableList.restaurant_id,
                    tableList.id,
                    tableList.table_rule,
                    tableList.table_drescode,
                    tableList.table_ocassion,
                    bookingDate,
                    tableList,
                    restoData
                )
            } else {
                showToastMessage(NETWORKEROORMSG)
            }
        }
        btnSelectFood.setOnClickListener {
            dialog.dismiss()
            val mainIntent =
                Intent(this@BookingTableDetailView, SelectFoodActivity::class.java)
            mainIntent.putExtra("restromodel", restoData)
            mainIntent.putExtra("table_model", tableList)
            mainIntent.putExtra("timedate", binding.txtDatetime.text.toString())
            mainIntent.putExtra("day", day)
            mainIntent.putExtra("noseat", "$strArea $noOfPerson")
            startActivity(mainIntent)
        }
        dialog.show()
    }

    private fun callApiBookTableList(restaurant_id: String?) {
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
                                tableListArrayList = ArrayList()
                                for (i in 0 until jsonObject.getJSONObject("data")
                                    .getJSONArray("data").length()) {
                                    val mjsonArray =
                                        jsonObject.getJSONObject("data").getJSONArray("data")
                                            .getJSONObject(i).getJSONArray("table")
                                    for (j in 0 until mjsonArray.length()) {
                                        val mjsonObject = mjsonArray.getJSONObject(j)
                                        val tableList = TableList()
                                        tableList.id = mjsonObject.getString("id")
                                        tableList.restaurant_id =
                                            mjsonObject.getString("restaurant_id")
                                        tableList.table_no = mjsonObject.getString("table_no")
                                        tableList.number_of_person =
                                            mjsonObject.getString("number_of_person")
                                        tableList.type = mjsonObject.getString("type")
                                        tableList.status_id = mjsonObject.getString("status_id")

                                        //tableList.setFloor_id(mjson_object.getString("floor_id"));
                                        //tableList.setArea_id(mjson_object.getString("area_id"));
                                        tableList.price = mjsonObject.getString("price")

                                        /* parameter for table reservation not available now  */if (mjsonObject.has(
                                                "table_descr"
                                            )
                                        ) {
                                            tableList.table_descr =
                                                mjsonObject.getString("table_descr")
                                        } else {
                                            tableList.table_descr = "Test description for table..."
                                        }
                                        if (mjsonObject.has("table_rule")) {
                                            tableList.table_rule =
                                                mjsonObject.getString("table_rule")
                                        } else {
                                            tableList.table_rule = "Test rule for table"
                                        }
                                        if (mjsonObject.has("table_drescode")) {
                                            tableList.table_drescode =
                                                mjsonObject.getString("table_drescode")
                                        } else {
                                            tableList.table_drescode =
                                                "Test White Shirt and Blue Jeans"
                                        }
                                        if (mjsonObject.has("table_ocassion")) {
                                            tableList.table_ocassion =
                                                mjsonObject.getString("table_ocassion")
                                        } else {
                                            tableList.table_ocassion = "Test Birthday Bash"
                                        }
                                        /* code end for table reservation */


                                        /* code to get customer and other data into table object */
                                        tableList.str_hotel_name = restoData?.rest_name
                                        if (storePreference.getString(NAME)
                                                .isNullOrEmpty()
                                        ) {
                                            tableList.str_customer_name = "Test Customer Name"
                                        } else {
                                            tableList.str_customer_name =
                                                storePreference.getString(
                                                    NAME
                                                )
                                        }

                                        //tableList.setStr_noseat(noof_person+" "+"Seats");
                                        tableList.str_time = binding.txtDatetime.text.toString()
                                        if (storePreference.getString(MOBILE)
                                                .isNullOrEmpty()
                                        ) {
                                            tableList.str_phone = "9000012345"
                                        } else {
                                            tableList.str_phone = storePreference.getString(
                                                MOBILE
                                            )
                                        }

                                        /* code to get customer and other data into table object end */
                                        tableListArrayList?.add(
                                            tableList
                                        )
                                    }
                                }
                                binding.progressBar.visibility = View.GONE
                                //go to adapter
                                val listTableBookingAdapter = ListTableBookingAdapter(
                                    this@BookingTableDetailView,
                                    tableListArrayList
                                ) {
                                    showAlertViewTableSelectionRule(it)
                                }
                                binding.tableRecycleview.adapter = listTableBookingAdapter
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
                                floorDropdownArrayList = ArrayList()
                                branchDropdownArrayList = ArrayList()
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

                                    //floor type spinner code start
                                    val mjsonArrayFloor =
                                        jsonObject.getJSONObject("data").getJSONArray("data")
                                            .getJSONObject(i).getJSONArray("floor")
                                    val floorDropdownFirst = FloorDropdown()
                                    floorDropdownFirst.id = "0"
                                    floorDropdownFirst.floor_name = "Select Floor"
                                    floorDropdownArrayList?.add(floorDropdownFirst)
                                    for (j in 0 until mjsonArrayFloor.length()) {
                                        val floorDropdown = FloorDropdown()
                                        val mjsonObjectFloor = mjsonArrayFloor.getJSONObject(j)
                                        floorDropdown.id = mjsonObjectFloor.getString("id")
                                        floorDropdown.floor_name =
                                            mjsonObjectFloor.getString("name")
                                        floorDropdownArrayList?.add(floorDropdown)
                                    }
                                    val spinnerAdapterFloor = SpinnnerAdapterFloor(
                                        applicationContext, floorDropdownArrayList!!
                                    )
                                    binding.spinnerFloor.adapter = spinnerAdapterFloor
                                    //floor type spinner code end

                                    //branch type spinner code start
                                    val mjsonArrayBranch =
                                        jsonObject.getJSONObject("data").getJSONArray("data")
                                            .getJSONObject(i).getJSONArray("child_restaurant")
                                    val branchDropdownFirst = BranchDropdown()
                                    branchDropdownFirst.id = "0"
                                    branchDropdownFirst.branch_name = "Branch"
                                    branchDropdownArrayList?.add(branchDropdownFirst)
                                    for (j in 0 until mjsonArrayBranch.length()) {
                                        val mjsonObjectBranch =
                                            mjsonArrayBranch.getJSONObject(j)
                                        val branchDropdown = BranchDropdown()
                                        branchDropdown.id = mjsonObjectBranch.getString("id")
                                        branchDropdown.branch_name =
                                            mjsonObjectBranch.getString("rest_branch")
                                        branchDropdownArrayList?.add(branchDropdown)
                                    }
                                    val spinnnerAdapterBranch = SpinnnerAdapterBranch(
                                        applicationContext, branchDropdownArrayList!!
                                    )
                                    binding.spinnerBranch.adapter = spinnnerAdapterBranch
                                    //branch type spinner code end
                                }
                                binding.progressBar.visibility = View.GONE

                                //load remaining value in view
                                if (binding.buttonList.isChecked) {
                                    binding.linearListview.visibility = View.VISIBLE
                                    binding.frameLayout.visibility = View.GONE
                                    binding.linearLayout.visibility = View.GONE
                                    callApiBookTableList(restaurantId)
                                }
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

    private fun callApiConfirmTableBooking(
        restaurant_id: String?, table_id: String?, rules: String?, dresscode: String?,
        occasion: String?, date: String?, tableList: TableList, bookTable: RestoData?
    ) {
        Log.d("restaurant_id", (restaurant_id) ?: "null")
        Log.d("table_id", (table_id) ?: "null")
        Log.d("rules", (rules) ?: "null")
        Log.d("dresscode", (dresscode) ?: "null")
        Log.d("occasion", (occasion) ?: "null")
        Log.d("date", (date) ?: "null")
        //"2022-12-13 09:12:12"
        //table_id="8";
        binding.progressBar.visibility = View.VISIBLE
        info.bookTable(
            "Bearer " + storePreference.getString(TOKEN_LOGIN),  /*,"application/json",*/ /*"application/json",*/
            restaurant_id, table_id, rules, dresscode, occasion, date, storePreference.getString(
                IDENTFIER
            )
        )
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        if (response.code() == SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            if (jsonObject.getString("status")
                                    .equals(SUCCESS_CODE, ignoreCase = true)
                            ) {
                                binding.progressBar.visibility = View.GONE
                                val mjsonObj = jsonObject.getJSONObject("data")
                                storePreference.setString(
                                    CUSTOMERID,
                                    mjsonObj.getString("customer_id")
                                )
                                storePreference.setString(
                                    "paymentstatus",
                                    mjsonObj.getString("payment_status")
                                )
                                storePreference.setString(
                                    BOOKINGID,
                                    mjsonObj.getString("id")
                                )
                                Log.d("table_id", mjsonObj.getString("table_id"))
                                Log.d("restaurant_id", mjsonObj.getString("restaurant_id"))
                                Log.d("rules", mjsonObj.getString("rules"))
                                showToastMessage(jsonObject.getString("message"))
                                isTableConform = true
                                showAlertViewConfirmation(
                                    tableList,
                                    mjsonObj.getString("id"),
                                    bookTable
                                )
                            } else {
                                binding.progressBar.visibility = View.GONE
                                showToastMessage(NODATA)
                                isTableConform = false
                            }
                        } else if (response.code() == ERROR_CODE_n || response.code() == ERROR_CODE) {
                            binding.progressBar.visibility = View.GONE
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            showToastMessage(jsonObject.getString("message"))
                            isTableConform = false
                        } else {
                            binding.progressBar.visibility = View.GONE
                            showToastMessage(ERRORMSG)
                            isTableConform = false
                        }
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        showToastMessage(ERRORMSG)
                        isTableConform = false
                    } catch (ex: IOException) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        showToastMessage(ERRORMSG)
                        isTableConform = false
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    showToastMessage(ERRORMSG)
                }
            })
    }

    //Date and time picker example code start
    private fun datePicker() {
        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]

        // when dialog box is closed, below method will be called.
        val datePickerListener =
            OnDateSetListener { view: DatePicker?, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                val month: Int = selectedMonth + 1
                val date: Int = selectedDay
                val strMonth = if (month < 10) {
                    "0$month"
                } else {
                    month.toString()
                }
                val strDate = if (date < 10) {
                    "0$date"
                } else {
                    date.toString()
                }
                bookingDate = "$selectedYear-$strMonth-$strDate"
                Log.d("sendate==>", bookingDate ?: "null")
                binding.txtDatetime.text = ""
                binding.txtDatetime.text =
                    selectedDay.toString() + "-" + getMonth(selectedMonth + 1)
                mYear = selectedYear
                mMonth = selectedMonth
                mDay = selectedDay
            }
        val datePickerDialog = DatePickerDialog(
            ctx, R.style.DialogTheme_picker, datePickerListener,
            mYear, mMonth, mDay
        )
        datePickerDialog.setButton(
            DialogInterface.BUTTON_NEGATIVE,
            getString(R.string.cancel)
        ) { dialog: DialogInterface, which: Int ->
            if (which == DialogInterface.BUTTON_NEGATIVE) {
                dialog.cancel()
            }
        }
        datePickerDialog.setButton(
            DialogInterface.BUTTON_POSITIVE,
            "OK"
        ) { dialog, which ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                val datePicker = datePickerDialog
                    .datePicker
                datePickerListener.onDateSet(
                    datePicker,
                    datePicker.year,
                    datePicker.month,
                    datePicker.dayOfMonth
                )
                var month = datePicker.month
                val date = datePicker.dayOfMonth
                month += 1
                val strMonth = if (month < 10) {
                    "0$month"
                } else {
                    month.toString()
                }
                val strDate = if (date < 10) {
                    "0$date"
                } else {
                    date.toString()
                }
                dateGet = datePicker.year.toString() + "-" + strMonth + "-" + strDate
                Log.d("sendate==>", dateGet)
                timePicker()

                //callApi_senddate(deliveryDate_get);
            }
        }
        datePickerDialog.setCancelable(false)
        val now = System.currentTimeMillis()
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        /*if(Constant.DELIVERY_MAXDATE_AFTER_ORDER == 0)
        {
            //user can select any date
        }
        else if(Constant.DELIVERY_MAXDATE_AFTER_ORDER > 0){
            datePickerDialog.getDatePicker().setMaxDate(now+(1000*60*60*24*Constant.DELIVERY_MAXDATE_AFTER_ORDER));
        }
       */datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
    }

    private fun timePicker() {
        val c = Calendar.getInstance()
        mHour = c[Calendar.HOUR_OF_DAY]
        mMinute = c[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(
            ctx, R.style.DialogTheme_picker,
            { view: TimePicker?, hourOfDay: Int, minute: Int ->
                val date: String = binding.txtDatetime.text.toString()
                day = binding.txtDatetime.text.toString()
                binding.txtDatetime.text = ""
                val time: String = String.format("%02d:%02d", hourOfDay, minute)
                val AM_PM: String = if (hourOfDay < 12) {
                    "am"
                } else {
                    "pm"
                }
                bookingDate = "$bookingDate $time $AM_PM"
                Log.d("senddate", bookingDate ?: "null")
                binding.txtDatetime.text = "$date, $time $AM_PM"
            }, mHour, mMinute, false
        )
        timePickerDialog.show()
        timePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).visibility = View.GONE
        timePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
    }

    private fun currentDateShow() {
        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        mHour = c[Calendar.HOUR_OF_DAY]
        mMinute = c[Calendar.MINUTE]
        mSecond = c[Calendar.SECOND]
        val AM_PM: String = if (mHour < 12) {
            "am"
        } else {
            "pm"
        }
        val time = "$mHour:$mMinute $AM_PM"
        val timeSend = "$mHour:$mMinute:$mSecond"
        val monthN = mMonth + 1
        val dateN = mDay
        val strDay = dateN.toString()
        val strMonth = if (monthN < 10) {
            "0$monthN"
        } else {
            monthN.toString()
        }
        val strDate = if (dateN < 10) {
            "0$dateN"
        } else {
            dateN.toString()
        }
        val selectedYear: String = mYear.toString()
        bookingDate = "$selectedYear-$strMonth-$strDate $timeSend"
        /*"2022-12-13 09:12:12"*/binding.txtDatetime.text = ""
        binding.txtDatetime.text = strDay + "-" + getMonth(monthN) + " " + time
        day = strDay + "-" + getMonth(monthN)
    }

    private fun getMonth(month: Int): String {
        val monthNames = arrayOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        )
        return monthNames[month - 1]
    }

    //Date and time picker example code end
    fun loadJSONFromAsset(): String? {
        val json: String?
        try {
            val `is` = ctx.assets.open("local3.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer) // "UTF-8"
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    private fun showAlertViewConfirmation(
        tableList: TableList,
        booking_id: String,
        restoData: RestoData?
    ) {
        val alertDialog = AlertDialog.Builder(this@BookingTableDetailView)
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.conform_booking_view, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog = alertDialog.create()
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnCancel: Button = dialogView.findViewById(R.id.btn_cancel)
        val btnYes: Button = dialogView.findViewById(R.id.btn_yes)
        btnYes.setOnClickListener {
            dialog.dismiss()
            val mainIntent =
                Intent(this@BookingTableDetailView, PaymentScreenActivity::class.java)
            mainIntent.putExtra("model", tableList)
            mainIntent.putExtra("restromodel", restoData)
            mainIntent.putExtra(
                "totalpay",
                tableList.price?.toInt()
            )
            mainIntent.putExtra("bookingid", booking_id)
            mainIntent.putExtra("isbooktable", "yes")
            startActivity(mainIntent)
        }
        btnCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    companion object {
        var day: String? = null
    }
}