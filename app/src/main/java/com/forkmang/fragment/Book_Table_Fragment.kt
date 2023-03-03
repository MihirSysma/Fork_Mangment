package com.forkmang.fragment

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.FacebookSdk.getApplicationContext
import com.forkmang.R
import com.forkmang.ViewModel
import com.forkmang.adapter.BookTableAdapter
import com.forkmang.adapter.SpinnnerAdapter
import com.forkmang.data.RestoData
import com.forkmang.databinding.FragmentBooktableLayoutBinding
import com.forkmang.helper.ApiConfig.getLocation
import com.forkmang.helper.Constant
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
import java.util.*
import kotlin.math.floor

class Book_Table_Fragment : Fragment() {
    var bookTableArrayList: ArrayList<RestoData>? = null
    var bookTableAdapter: BookTableAdapter? = null
    var gps: GPSTracker? = null
    var saveLatitude: Double? = null
    var saveLongitude: Double? = null
    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var mHour = 0
    private var mMinute = 0
    var booking_date: String? = null
    var Date_get = ""
    var progressBar: ProgressBar? = null
    var person = arrayOf("Select Person", "1", "2 ", "3", "4", "5", "6", "7", "8", "9", "10")

    private var _binding: FragmentBooktableLayoutBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBooktableLayoutBinding.inflate(inflater, container, false)
        val verticalLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.booktableRecycleview.layoutManager = verticalLayoutManager
        binding.booktableRecycleview.setHasFixedSize(true)

        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                verticalLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);*/


        //GET GPS Current start
        getLocation(requireActivity())
        gps = GPSTracker(activity)
        saveLatitude = gps?.latitude
        saveLongitude = gps?.longitude
        if (gps?.isGPSTrackingEnabled == true) {
            saveLatitude = gps?.latitude
            saveLongitude = gps?.longitude
        }

        //GET GPS Current end

        //spinner array adapter
        val customAdapter = SpinnnerAdapter(getApplicationContext(), person)
        binding.spinner.adapter = customAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    context?.showToastMessage(person[position])
                }

                /* String str = person[position];
                if(str.equalsIgnoreCase("Select Person"))
                {
                    //nothing do
                    noof_person="0";
                }
                else{
                    String[] strdate_arr_2 = str.split(" ");
                    noof_person = strdate_arr_2[1];

                    //call api for filter
                }
               */
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                context?.showToastMessage("not selected")
            }
        }

        //spinner array adapter
        binding.lytDatetime.setOnClickListener { datePicker() }
        current_dateshow()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    private fun observe() {
        viewModel.searchData.observe(viewLifecycleOwner) {
            if (isVisible) {
                logThis("BookTable Frag $it")
                if (it.isNullOrEmpty()) {
                    call_reloadbooktable()
                } else {
                    filter_booktable(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val service_id = "1"
        saveLatitude = 23.933689
        saveLongitude = 72.367458
        callapi_getbooktable(service_id, saveLatitude.toString(), saveLongitude.toString())
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
                val month = selectedMonth + 1
                var str_month = ""
                var str_date = ""
                str_month = if (month < 10) {
                    "0$month"
                } else {
                    month.toString()
                }
                str_date = if (selectedDay < 10) {
                    "0$selectedDay"
                } else {
                    selectedDay.toString()
                }
                booking_date = "$selectedYear-$str_month-$str_date"
                Log.d("sendate==>", booking_date?:"")
                binding.txtDatetime.text = ""
                binding.txtDatetime.text =
                    selectedDay.toString() + "-" + getMonth(selectedMonth + 1)
                mYear = selectedYear
                mMonth = selectedMonth
                mDay = selectedDay
            }
        val datePickerDialog = DatePickerDialog(
            requireContext(), R.style.DialogTheme_picker, datePickerListener,
            mYear, mMonth, mDay
        )

        //datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.setButton(
            DialogInterface.BUTTON_NEGATIVE,
            getString(R.string.cancel)
        ) { dialog, which ->
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
                var str_month = ""
                var str_date = ""
                month = month + 1
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
        val timePickerDialog = TimePickerDialog(context, R.style.DialogTheme_picker,
            { view: TimePicker?, hourOfDay: Int, minute: Int ->
                val date = binding.txtDatetime.text.toString()
                binding.txtDatetime.text = ""
                val time = String.format("%02d:%02d", hourOfDay, minute)
                val AM_PM: String
                AM_PM = if (hourOfDay < 12) {
                    "am"
                } else {
                    "pm"
                }
                booking_date = "$booking_date $time $AM_PM"
                Log.d("senddate", booking_date?:"")
                binding.txtDatetime.text = "$date, $time $AM_PM"
            }, mHour, mMinute, false
        )
        timePickerDialog.show()
        timePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).visibility = View.GONE
        timePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
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

    private fun current_dateshow() {
        val c = Calendar.getInstance()
        var str_month = ""
        var str_date = ""
        val str_day: String
        val selectedYear: String
        var booking_date_n: String
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        mHour = c[Calendar.HOUR_OF_DAY]
        mMinute = c[Calendar.MINUTE]
        val AM_PM: String
        AM_PM = if (mHour < 12) {
            "am"
        } else {
            "pm"
        }
        val time = "$mHour:$mMinute $AM_PM"
        val month_n = mMonth + 1
        val date_n = mDay
        str_day = date_n.toString()
        str_month = if (month_n < 10) {
            "0$month_n"
        } else {
            month_n.toString()
        }
        str_date = if (date_n < 10) {
            "0$date_n"
        } else {
            date_n.toString()
        }
        selectedYear = mYear.toString()
        booking_date = "$selectedYear-$str_month-$str_date"
        binding.txtDatetime.text = ""
        binding.txtDatetime.text = str_day + "-" + getMonth(month_n) + " " + time
    }

    //Date and time picker example code end
    //Api code for Book Table start
    private fun callapi_getbooktable(service_id: String, latitude: String, logitutde: String) {
        binding.progressBar.visibility = View.VISIBLE
        info.getlist_res(latitude, logitutde)?.enqueue(object : Callback<JsonObject?> {
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
                                bookTableArrayList = ArrayList()
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
                                    bookTableArrayList?.add(bookTable)
                                }
                                binding.progressBar.visibility = View.GONE
                                bookTableAdapter = BookTableAdapter(
                                    requireActivity(),
                                    binding.txtDatetime.text.toString()
                                )
                                binding.booktableRecycleview.adapter = bookTableAdapter
                                bookTableAdapter?.bookTable_dataArrayList = bookTableArrayList as ArrayList<RestoData>
                                //Constant.IS_BookTableFragmentLoad=true;
                            } else {
                                //no data in array list
                                binding.progressBar.visibility = View.GONE
                                context?.showToastMessage(Constant.NODATA)
                            }
                        } else {
                            binding.progressBar.visibility = View.GONE
                            // getContext.showToastMessage(, Constant.ERRORMSG);
                        }
                    } else {
                        binding.progressBar.visibility = View.GONE
                        // getContext.showToastMessage(, Constant.ERRORMSG);
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    //getContext.showToastMessage(, Constant.ERRORMSG);
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                context?.showToastMessage(Constant.ERRORMSG)
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
                                bookTableArrayList = ArrayList()
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
                                    bookTableArrayList?.add(bookTable)
                                }
                                binding.progressBar.visibility = View.GONE
                                bookTableAdapter = BookTableAdapter(
                                    requireActivity(),
                                    binding.txtDatetime.text.toString()
                                )
                                binding.booktableRecycleview.adapter = bookTableAdapter
                                bookTableAdapter?.bookTable_dataArrayList = bookTableArrayList as ArrayList<RestoData>
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

    fun filter_booktable(search_str: String) {
        if (isNetworkAvailable(requireContext())) {
            callapi_searchbooktable(search_str, saveLatitude.toString(), saveLongitude.toString())
        } else {
            context?.showToastMessage(Constant.NETWORKEROORMSG)
        }
    }

    fun call_reloadbooktable() {
        if (isNetworkAvailable(requireContext())) {
            saveLatitude = 23.937416
            saveLongitude = 72.375741
            callapi_getbooktable("2", saveLatitude.toString(), saveLongitude.toString())
        } else {
            context?.showToastMessage(Constant.NETWORKEROORMSG)
        }
    }

    companion object {
        lateinit var viewModel : ViewModel
        fun newInstance(viewModel: ViewModel): Book_Table_Fragment {
            this.viewModel = viewModel
            return Book_Table_Fragment()
        }

    }
}