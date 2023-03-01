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
import com.forkmang.data.AreaDropdown
import com.forkmang.data.BranchDropdown
import com.forkmang.data.FlooDropdown
import com.forkmang.data.RestoData
import com.forkmang.databinding.ActivityBookingtableDetailviewBinding
import com.forkmang.helper.Constant
import com.forkmang.helper.StorePrefrence
import com.forkmang.helper.Utils
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

class BookingTable_DetailView : Activity() {
    private val binding by lazy { ActivityBookingtableDetailviewBinding.inflate(layoutInflater) }
    var ctx: Context = this@BookingTable_DetailView
    var mYear = 0
    var mMonth = 0
    var mDay = 0
    var mHour = 0
    var mMinute = 0
    var mSecond = 0
    var booking_date: String? = null
    var booking_time: String? = null
    var Date_get = ""
    var resturant_id: String? = null
    var noof_person = "0"
    var str_area: String? = "Indoor"
    var datetime: String? = null
    var tableListArrayList: ArrayList<TableList>? = null
    var flooDropdownArrayList: ArrayList<FlooDropdown>? = null
    var areaDropdownArrayList: ArrayList<AreaDropdown>? = null
    var branchDropdownArrayList: ArrayList<BranchDropdown>? = null
    var restoData: RestoData? = null
    var storePrefrence: StorePrefrence? = null
    var is_tableconform = false
    var is_pesonselect = false
    var is_areatype = false
    var person = arrayOf("Select Person", "1", "2 ", "3", "4", "5", "6", "7", "8", "9", "10")
    var type_value = arrayOf("Value", "0.7", "0.8", "0.9", "1.0")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        storePrefrence = StorePrefrence(ctx)
        val intent = intent
        resturant_id = intent.getStringExtra("resturant_id")
        datetime = intent.getStringExtra("datetime")
        restoData = getIntent().getSerializableExtra("restromodel") as RestoData?
        binding.tableRecycleview.layoutManager = LinearLayoutManager(
            this@BookingTable_DetailView,
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
                this@BookingTable_DetailView,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            if (Utils.isNetworkAvailable(ctx)) {
                callapi_booktablelist(resturant_id)
            } else {
                Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
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
        current_dateshow()


        //call api for fill dropdown
        callapi_filldropdown(resturant_id)


        //spinner_person array adapter start
        val personAdapter = SpinnnerAdapter(applicationContext, person)
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
                    Toast.makeText(ctx, person[position], Toast.LENGTH_SHORT).show()
                    noof_person = person[position]
                    is_pesonselect = true
                } else {
                    is_pesonselect = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(ctx, "not selected", Toast.LENGTH_SHORT).show()
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
                    val branchDropdown: BranchDropdown = branchDropdownArrayList?.get(position)!!
                    Toast.makeText(ctx, branchDropdown.branch_name, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(ctx, "not selected", Toast.LENGTH_SHORT).show()
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
                    val flooDropdown: FlooDropdown = flooDropdownArrayList!![position]
                    Toast.makeText(ctx, flooDropdown.floor_name, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(ctx, "not selected", Toast.LENGTH_SHORT).show()
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
                    val areaDropdown: AreaDropdown = areaDropdownArrayList!!.get(position)
                    Toast.makeText(ctx, areaDropdown.area_name, Toast.LENGTH_SHORT).show()
                    str_area = areaDropdown.area_name
                    is_areatype = true
                    if (is_pesonselect) {
                        binding.relLablview.visibility = View.VISIBLE
                        binding.relTxtview.visibility = View.VISIBLE
                        binding.txtNoseat.text = "$str_area $noof_person Seats"
                        binding.txtTime.text = day
                    }
                } else {
                    is_areatype = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(ctx, "not selected", Toast.LENGTH_SHORT).show()
            }
        }
        //spinner_floor array adapter

        //spinner_type array adapter
        val type_valueAdapter = SpinnnerAdapter_Type_Value(applicationContext, type_value)
        binding.spinnerTypeValue.adapter = type_valueAdapter
        binding.spinnerTypeValue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    Toast.makeText(ctx, type_value[position], Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(ctx, "not selected", Toast.LENGTH_SHORT).show()
            }
        }
        //spinner_floor array adapter
    }

    fun showAlertView_tableselctionrule(tableList: TableList) {
        val alertDialog = AlertDialog.Builder(this@BookingTable_DetailView)
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.tableselection_alertview, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog = alertDialog.create()
        val btn_select_nxt: Button = dialogView.findViewById(R.id.btn_select_nxt)
        val tv_descr: TextView = dialogView.findViewById(R.id.tv_descr)
        val txt_reserveseat: TextView = dialogView.findViewById(R.id.txt_reserveseat)
        val tv_rule: TextView = dialogView.findViewById(R.id.tv_rule)
        val tv_dresscode: TextView = dialogView.findViewById(R.id.tv_dresscode)
        val tv_ocassion: TextView = dialogView.findViewById(R.id.tv_ocassion)
        tv_descr.text = tableList.table_descr
        tv_rule.text = tableList.table_rule
        tv_dresscode.text = tableList.table_drescode
        tv_ocassion.text = tableList.table_ocassion
        txt_reserveseat.text =
            "To Reserve:" + " " + ctx.resources.getString(R.string.rupee) + tableList.price
        btn_select_nxt.setOnClickListener {
            dialog.dismiss()
            showAlertView_paymentconform(tableList)
        }
        dialog.show()
    }

    private fun showAlertView_paymentconform(tableList: TableList) {
        val alertDialog = AlertDialog.Builder(this@BookingTable_DetailView)
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.payment_conform_alertview, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog = alertDialog.create()
        val txt_restroname: TextView = dialogView.findViewById(R.id.txt_restroname)
        val txt_custname: TextView = dialogView.findViewById(R.id.txt_custname)
        val txt_datetime: TextView = dialogView.findViewById(R.id.txt_datetime)
        val txt_phoneno: TextView = dialogView.findViewById(R.id.txt_phoneno)
        val etv_noperson: EditText = dialogView.findViewById(R.id.etv_noperson)
        val imgicon_edit: ImageView = dialogView.findViewById(R.id.imgicon_edit)
        val imgicon_save: ImageView = dialogView.findViewById(R.id.imgicon_save)
        txt_restroname.text = tableList.str_hotel_name
        txt_custname.text = tableList.str_customer_name
        txt_datetime.text = binding.txtDatetime.text.toString()
        etv_noperson.setText(noof_person)
        txt_phoneno.text = tableList.str_phone
        val btn_select_food: Button = dialogView.findViewById(R.id.btn_select_food)
        val btn_cancel: Button = dialogView.findViewById(R.id.btn_cancel)
        imgicon_edit.setOnClickListener {
            /*imgicon_save.setVisibility(View.VISIBLE);
            imgicon_edit.setVisibility(View.GONE);
            etv_noperson.setEnabled(true);
            etv_noperson.setBackgroundColor(Color.DKGRAY);
            etv_noperson.setTextColor(Color.WHITE);*/dialog.dismiss()
        }
        imgicon_save.setOnClickListener { }
        val btn_cnf_tablebook: Button = dialogView.findViewById(R.id.btn_cnf_payment)
        btn_cancel.setOnClickListener { dialog.dismiss() }
        btn_cnf_tablebook.setOnClickListener {
            //dialog.dismiss();
            if (Utils.isNetworkAvailable(ctx)) {
                callapi_conform_tablebooking(
                    tableList.restaurant_id,
                    tableList.id,
                    tableList.table_rule,
                    tableList.table_drescode,
                    tableList.table_ocassion,
                    booking_date,
                    tableList,
                    restoData
                )
            } else {
                Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
            }
        }
        btn_select_food.setOnClickListener {
            dialog.dismiss()
            val mainIntent: Intent =
                Intent(this@BookingTable_DetailView, SelectFood_Activity::class.java)
            mainIntent.putExtra("restromodel", restoData)
            mainIntent.putExtra("table_model", tableList)
            mainIntent.putExtra("timedate", binding.txtDatetime.text.toString())
            mainIntent.putExtra("day", day)
            mainIntent.putExtra("noseat", "$str_area $noof_person")
            startActivity(mainIntent)
        }
        dialog.show()
    }

    private fun callapi_booktablelist(restaurant_id: String?) {
        binding.progressBar.visibility = View.VISIBLE
        info.getres_detail(restaurant_id)!!.enqueue(object : Callback<JsonObject?> {
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
                                tableListArrayList = ArrayList()
                                for (i in 0 until jsonObject.getJSONObject("data")
                                    .getJSONArray("data").length()) {
                                    val mjson_array =
                                        jsonObject.getJSONObject("data").getJSONArray("data")
                                            .getJSONObject(i).getJSONArray("table")
                                    for (j in 0 until mjson_array.length()) {
                                        val mjson_object = mjson_array.getJSONObject(j)
                                        val tableList = TableList()
                                        tableList.id = mjson_object.getString("id")
                                        tableList.restaurant_id =
                                            mjson_object.getString("restaurant_id")
                                        tableList.table_no = mjson_object.getString("table_no")
                                        tableList.number_of_person =
                                            mjson_object.getString("number_of_person")
                                        tableList.type = mjson_object.getString("type")
                                        tableList.status_id = mjson_object.getString("status_id")

                                        //tableList.setFloor_id(mjson_object.getString("floor_id"));
                                        //tableList.setArea_id(mjson_object.getString("area_id"));
                                        tableList.price = mjson_object.getString("price")

                                        /* parameter for table reservation not available now  */if (mjson_object.has(
                                                "table_descr"
                                            )
                                        ) {
                                            tableList.table_descr =
                                                mjson_object.getString("table_descr")
                                        } else {
                                            tableList.table_descr = "Test description for table..."
                                        }
                                        if (mjson_object.has("table_rule")) {
                                            tableList.table_rule =
                                                mjson_object.getString("table_rule")
                                        } else {
                                            tableList.table_rule = "Test rule for table"
                                        }
                                        if (mjson_object.has("table_drescode")) {
                                            tableList.table_drescode =
                                                mjson_object.getString("table_drescode")
                                        } else {
                                            tableList.table_drescode =
                                                "Test White Shirt and Blue Jeans"
                                        }
                                        if (mjson_object.has("table_ocassion")) {
                                            tableList.table_ocassion =
                                                mjson_object.getString("table_ocassion")
                                        } else {
                                            tableList.table_ocassion = "Test Birthday Bash"
                                        }
                                        /* code end for table reservation */


                                        /* code to get customer and other data into table object */
                                        tableList.str_hotel_name = restoData?.rest_name
                                        if (storePrefrence?.getString(Constant.NAME).isNullOrEmpty()) {
                                            tableList.str_customer_name = "Test Customer Name"
                                        } else {
                                            tableList.str_customer_name =
                                                storePrefrence?.getString(
                                                    Constant.NAME
                                                )
                                        }

                                        //tableList.setStr_noseat(noof_person+" "+"Seats");
                                        tableList.str_time = binding.txtDatetime.text.toString()
                                        if (storePrefrence?.getString(Constant.MOBILE).isNullOrEmpty()) {
                                            tableList.str_phone = "9000012345"
                                        } else {
                                            tableList.str_phone = storePrefrence?.getString(
                                                Constant.MOBILE
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
                                    this@BookingTable_DetailView,
                                    tableListArrayList
                                )
                                binding.tableRecycleview.adapter = listTableBookingAdapter
                            } else {
                                //no data in array list
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(ctx, Constant.NODATA, Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show()
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show()
            }
        })
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
                                .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                        ) {
                            if (jsonObject.getJSONObject("data").getJSONArray("data")
                                    .length() > 0
                            ) {
                                flooDropdownArrayList = ArrayList()
                                branchDropdownArrayList = ArrayList()
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
                                    val spinnnerAdapter_type = SpinnnerAdapter_Type(
                                        applicationContext, areaDropdownArrayList!!
                                    )
                                    binding.spinnerType.adapter = spinnnerAdapter_type
                                    //code end

                                    //floor type spinner code start
                                    val mjson_array_floor =
                                        jsonObject.getJSONObject("data").getJSONArray("data")
                                            .getJSONObject(i).getJSONArray("floor")
                                    val flooDropdown_first = FlooDropdown()
                                    flooDropdown_first.id = "0"
                                    flooDropdown_first.floor_name = "Select Floor"
                                    flooDropdownArrayList!!.add(flooDropdown_first)
                                    for (j in 0 until mjson_array_floor.length()) {
                                        val flooDropdown = FlooDropdown()
                                        val mjson_object_floor = mjson_array_floor.getJSONObject(j)
                                        flooDropdown.id = mjson_object_floor.getString("id")
                                        flooDropdown.floor_name =
                                            mjson_object_floor.getString("name")
                                        flooDropdownArrayList?.add(flooDropdown)
                                    }
                                    val spinnnerAdapter_floor = SpinnnerAdapter_Floor(
                                        applicationContext, flooDropdownArrayList!!
                                    )
                                    binding.spinnerFloor.adapter = spinnnerAdapter_floor
                                    //floor type spinner code end

                                    //branch type spinner code start
                                    val mjson_array_branch =
                                        jsonObject.getJSONObject("data").getJSONArray("data")
                                            .getJSONObject(i).getJSONArray("child_restaurant")
                                    val branchDropdown_first = BranchDropdown()
                                    branchDropdown_first.id = "0"
                                    branchDropdown_first.branch_name = "Branch"
                                    branchDropdownArrayList?.add(branchDropdown_first)
                                    for (j in 0 until mjson_array_branch.length()) {
                                        val mjson_object_branch =
                                            mjson_array_branch.getJSONObject(j)
                                        val branchDropdown = BranchDropdown()
                                        branchDropdown.id = mjson_object_branch.getString("id")
                                        branchDropdown.branch_name =
                                            mjson_object_branch.getString("rest_branch")
                                        branchDropdownArrayList?.add(branchDropdown)
                                    }
                                    val spinnnerAdapter_branch = SpinnnerAdapter_Branch(
                                        applicationContext, branchDropdownArrayList!!
                                    )
                                    binding.spinnerBranch.adapter = spinnnerAdapter_branch
                                    //branch type spinner code end
                                }
                                binding.progressBar.visibility = View.GONE

                                //load remaining value in view
                                if (binding.buttonList.isChecked) {
                                    binding.linearListview.visibility = View.VISIBLE
                                    binding.frameLayout.visibility = View.GONE
                                    binding.linearLayout.visibility = View.GONE
                                    callapi_booktablelist(resturant_id)
                                }
                            } else {
                                //no data in array list
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(ctx, Constant.NODATA, Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show()
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun callapi_conform_tablebooking(
        restaurant_id: String?, table_id: String?, rules: String?, dresscode: String?,
        occasion: String?, date: String?, tableList: TableList, bookTable: RestoData?
    ) {
        Log.d("restaurant_id", (restaurant_id)?:"null")
        Log.d("table_id", (table_id)?:"null")
        Log.d("rules", (rules)?:"null")
        Log.d("dresscode", (dresscode)?:"null")
        Log.d("occasion", (occasion)?:"null")
        Log.d("date", (date)?:"null")
        //"2022-12-13 09:12:12"
        //table_id="8";
        binding.progressBar.visibility = View.VISIBLE
        info.book_table(
            "Bearer " + storePrefrence?.getString(Constant.TOKEN_LOGIN),  /*,"application/json",*/ /*"application/json",*/
            restaurant_id, table_id, rules, dresscode, occasion, date, storePrefrence?.getString(
                Constant.IDENTFIER
            )
        )
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            //Log.d("Result", jsonObject.toString());
                            if (jsonObject.getString("status")
                                    .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                            ) {
                                binding.progressBar.visibility = View.GONE
                                val mjson_obj = jsonObject.getJSONObject("data")
                                storePrefrence?.setString(
                                    Constant.CUSTOMERID,
                                    mjson_obj.getString("customer_id")
                                )
                                storePrefrence?.setString(
                                    "paymentstatus",
                                    mjson_obj.getString("payment_status")
                                )
                                storePrefrence?.setString(
                                    Constant.BOOKINGID,
                                    mjson_obj.getString("id")
                                )
                                Log.d("table_id", mjson_obj.getString("table_id"))
                                Log.d("restaurant_id", mjson_obj.getString("restaurant_id"))
                                Log.d("rules", mjson_obj.getString("rules"))
                                Toast.makeText(
                                    ctx,
                                    jsonObject.getString("message"),
                                    Toast.LENGTH_LONG
                                ).show()
                                is_tableconform = true
                                showAlertView_conformation(
                                    tableList,
                                    mjson_obj.getString("id"),
                                    bookTable
                                )
                            } else {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(ctx, Constant.NODATA, Toast.LENGTH_LONG).show()
                                is_tableconform = false
                            }
                        } else if (response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE) {
                            binding.progressBar.visibility = View.GONE
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            Toast.makeText(ctx, jsonObject.getString("message"), Toast.LENGTH_LONG)
                                .show()
                            is_tableconform = false
                        } else {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show()
                            is_tableconform = false
                        }
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show()
                        is_tableconform = false
                    } catch (ex: IOException) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show()
                        is_tableconform = false
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show()
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
                var str_month: String = ""
                var str_date: String = ""
                str_month = if (month < 10) {
                    "0$month"
                } else {
                    month.toString()
                }
                str_date = if (date < 10) {
                    "0$date"
                } else {
                    date.toString()
                }
                booking_date = "$selectedYear-$str_month-$str_date"
                Log.d("sendate==>", booking_date?:"null")
                binding.txtDatetime.text = ""
                binding.txtDatetime.text = selectedDay.toString() + "-" + getMonth(selectedMonth + 1)
                mYear = selectedYear
                mMonth = selectedMonth
                mDay = selectedDay
            }
        val datePickerDialog = DatePickerDialog(
            ctx, R.style.DialogTheme_picker, datePickerListener,
            mYear, mMonth, mDay
        )
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
            getString(R.string.cancel)
        ) { dialog: DialogInterface, which: Int ->
            if (which == DialogInterface.BUTTON_NEGATIVE) {
                dialog.cancel()
            }
        }
        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,
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
                var str_month = ""
                var str_date = ""
                month += 1
                str_month = if (month < 10) {
                    "0$month"
                } else {
                    month.toString()
                }
                str_date = if (date < 10) {
                    "0$date"
                } else {
                    date.toString()
                }
                Date_get = datePicker.year.toString() + "-" + str_month + "-" + str_date
                Log.d("sendate==>", Date_get)
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
        val timePickerDialog = TimePickerDialog(ctx, R.style.DialogTheme_picker,
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
                booking_date = "$booking_date $time $AM_PM"
                Log.d("senddate", booking_date?:"null")
                binding.txtDatetime.text = "$date, $time $AM_PM"
            }, mHour, mMinute, false
        )
        timePickerDialog.show()
        timePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).visibility = View.GONE
        timePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
    }

    private fun current_dateshow() {
        val c = Calendar.getInstance()
        var str_month = ""
        var str_date = ""
        val str_day: String
        var booking_date_n: String
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        mHour = c[Calendar.HOUR_OF_DAY]
        mMinute = c[Calendar.MINUTE]
        mSecond = c[Calendar.SECOND]
        val AM_PM: String
        if (mHour < 12) {
            AM_PM = "am"
        } else {
            AM_PM = "pm"
        }
        val time = "$mHour:$mMinute $AM_PM"
        val time_send = "$mHour:$mMinute:$mSecond"
        val month_n = mMonth + 1
        val date_n = mDay
        str_day = date_n.toString()
        if (month_n < 10) {
            str_month = "0$month_n"
        } else {
            str_month = month_n.toString()
        }
        if (date_n < 10) {
            str_date = "0$date_n"
        } else {
            str_date = date_n.toString()
        }
        val selectedYear: String = mYear.toString()
        booking_date = "$selectedYear-$str_month-$str_date $time_send"
        /*"2022-12-13 09:12:12"*/binding.txtDatetime.text = ""
        binding.txtDatetime.text = str_day + "-" + getMonth(month_n) + " " + time
        day = str_day + "-" + getMonth(month_n)
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
        var json: String?
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

    private fun showAlertView_conformation(
        tableList: TableList,
        booking_id: String,
        restoData: RestoData?
    ) {
        val alertDialog = AlertDialog.Builder(this@BookingTable_DetailView)
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.conform_booking_view, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog = alertDialog.create()
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btn_cancel: Button = dialogView.findViewById(R.id.btn_cancel)
        val btn_yes: Button = dialogView.findViewById(R.id.btn_yes)
        btn_yes.setOnClickListener {
            dialog.dismiss()
            val mainIntent: Intent =
                Intent(this@BookingTable_DetailView, PaymentScreenActivity::class.java)
            mainIntent.putExtra("model", tableList)
            mainIntent.putExtra("restromodel", restoData)
            mainIntent.putExtra(
                "totalpay",
                ctx.resources.getString(R.string.rupee) + tableList.price
            )
            mainIntent.putExtra("bookingid", booking_id)
            mainIntent.putExtra("isbooktable", "yes")
            startActivity(mainIntent)
        }
        btn_cancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    companion object {
        var day: String? = null
    }
}