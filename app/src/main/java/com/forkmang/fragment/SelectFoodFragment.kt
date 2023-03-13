package com.forkmang.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.forkmang.R
import com.forkmang.vm.SelectFoodViewModel
import com.forkmang.activity.ActivityPaymentSummary
import com.forkmang.adapter.ADD_QTY
import com.forkmang.adapter.CartListingAdapter
import com.forkmang.adapter.FoodListAdapter
import com.forkmang.adapter.REMOVE_CART_ITEM
import com.forkmang.data.CartBooking
import com.forkmang.data.Category_ItemList
import com.forkmang.data.Extra_Topping
import com.forkmang.data.RestoData
import com.forkmang.databinding.FragmentOrderfoodLayoutBinding
import com.forkmang.helper.Constant
import com.forkmang.helper.Constant.ERRORMSG
import com.forkmang.helper.Constant.MOBILE
import com.forkmang.helper.Constant.TOKEN_LOGIN
import com.forkmang.helper.StorePrefrence
import com.forkmang.helper.Utils.isNetworkAvailable
import com.forkmang.helper.showToastMessage
import com.forkmang.models.TableList
import com.forkmang.network_call.Api.info
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class SelectFoodFragment : Fragment() {

    var cartBookingArrayList: ArrayList<CartBooking>? = null
    var selectedidRadiobtnTopping = 0
    var progressbarAlertview: ProgressBar? = null
    var allOrderFoodAdapter: FoodListAdapter? = null
    private val storePrefrence by lazy { StorePrefrence(requireContext()) }

    private var _binding: FragmentOrderfoodLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderfoodLayoutBinding.inflate(inflater, container, false)
        binding.orderFoodRecycleview.layoutManager = LinearLayoutManager(context)
        binding.orderFoodRecycleview.adapter = allOrderFoodAdapter
        observe()
        return binding.root
    }

    private fun observe() {

        viewModel.command.observe(viewLifecycleOwner) { command ->
            when (command) {
                Constant.COMMAND_CART_LIST_VIEW -> {
                    cartListingView()
                }
            }
        }

        viewModel.searchFoodItemData.observe(viewLifecycleOwner) { data ->
            allOrderFoodAdapter = FoodListAdapter(
                requireContext(),
                requireActivity(),
                data
            ) {
                showAlertView(it)
            }
        }

        viewModel.categoryItemData.observe(viewLifecycleOwner) { data ->
            allOrderFoodAdapter = FoodListAdapter(
                requireContext(),
                requireActivity(),
                data
            ) {
                showAlertView(it)
            }
        }

    }

    private fun callApiAddToCart(
        item_id: String?,
        qty: String?,
        booking_table_id: String?,
        item_extra: String?,
        type: String?
    ) {
        binding.progressBar.visibility = View.VISIBLE
        info.addItemCart(
            "Bearer " + storePrefrence.getString(TOKEN_LOGIN),
            item_id,
            qty,
            booking_table_id,
            item_extra,
            storePrefrence.getString(
                Constant.IDENTFIER
            ),
            type,
            restoData?.id,
        )
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        //Log.d("Result", jsonObject.toString());
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            if (jsonObject.getString("status").equals("200", ignoreCase = true)) {
                                context?.showToastMessage(jsonObject.getString("message"))
                                storePrefrence.setString(
                                    Constant.CARTID,
                                    jsonObject.getJSONObject("data").getString("cart_id")
                                )
                                storePrefrence.setString(
                                    Constant.CART_ITEMID,
                                    jsonObject.getJSONObject("data").getString("item_id")
                                )
                                binding.progressBar.visibility = View.GONE
                                cartListingView()
                            }
                        } else if (response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE) {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            binding.progressBar.visibility = View.GONE
                            context?.showToastMessage(jsonObject.getString("message"))

                            /*JSONObject obj = new JSONObject(loadJSONFromAsset());
                                if(obj.getString("status").equalsIgnoreCase("200"))
                                {
                                    context?.showToastMessage(, obj.getString("message")+" offline "                                   storePrefrence.setString("cartid", obj.getJSONObject("data").getString("cart_id"));
                                    storePrefrence.setString("item_id", obj.getJSONObject("data").getString("item_id"));
                                    cartListingView();

                                }*/
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        context?.showToastMessage(ERRORMSG)
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    context?.showToastMessage(ERRORMSG)
                    binding.progressBar.visibility = View.GONE
                }
            })
    }

    fun showAlertView(category_itemList: Category_ItemList) {
        val alertDialog = AlertDialog.Builder(
            requireActivity()
        )
        val inflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.addqty_alertview, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog = alertDialog.create()
        val radioBtnIdArr = ArrayList<String>()
        /*var radioButton4_extra: RadioButton
        var radioButton5_extra: RadioButton
        var radioButton6_extra: RadioButton*/
        /*radioButton4_extra=dialogView.findViewById(R.id.radioButton4);
        radioButton5_extra=dialogView.findViewById(R.id.radioButton5);
        radioButton6_extra=dialogView.findViewById(R.id.radioButton6);

        ArrayList<Extra_Topping> extra_toppingArrayList_get;
        extra_toppingArrayList_get = category_itemList.getExtra_toppingArrayList();
        for(int i=0;i<extra_toppingArrayList_get.size(); i++)
        {
            Extra_Topping extra_topping = extra_toppingArrayList_get.get(i);
            radioButton4_extra

        }*/
        val extraToppingArrayListGet: ArrayList<Extra_Topping>? =
            category_itemList.extra_toppingArrayList
        val rb = extraToppingArrayListGet?.size?.let { arrayOfNulls<RadioButton>(it) }
        val rg = RadioGroup(context) //create the RadioGroup
        rg.orientation = RadioGroup.VERTICAL //or RadioGroup.VERTICAL
        val layout2 = LinearLayout(context)
        layout2.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layout2.orientation = LinearLayout.VERTICAL
        //rg.setBackgroundColor(Color.parseColor("#3F51B5"));
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_enabled)
            ), intArrayOf(
                Color.parseColor("#000000"),  // disabled
                Color.parseColor("#C91107") // enabled
            )
        )
        for (i in extraToppingArrayListGet?.indices!!) {
            val extraTopping = extraToppingArrayListGet[i]
            rb?.set(i, RadioButton(context))
            rb?.get(i)?.text = extraTopping.name
            rb?.get(i)?.setTextColor(resources.getColor(R.color.black))
            rb?.get(i)?.buttonTintList = colorStateList
            rb?.get(i)?.id = extraTopping.id?.toInt()!!

            //rg.addView(rb[i]);
            layout2.addView(rb?.get(i)!!)
            rb[i]
                ?.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
                    if (buttonView.isChecked) {
                        radioBtnIdArr.add(buttonView.id.toString())
                    }
                }
        }
        val lyt: LinearLayout = dialogView.findViewById(R.id.lyt)
        lyt.addView(layout2) //you add the whole RadioGroup to the layout
        //rg.setOnCheckedChangeListener((group, checkedId) -> selectedId_radiobtn_topping = rg.getCheckedRadioButtonId());
        val btnAdd: Button = dialogView.findViewById(R.id.btn_add)
        val btnReserve: Button = dialogView.findViewById(R.id.btn_reserve)
        val plusBtn: TextView = dialogView.findViewById(R.id.plus_btn)
        val minus1: TextView = dialogView.findViewById(R.id.minus_btn)
        val txtQty: TextView = dialogView.findViewById(R.id.txt_qty)
        plusBtn.setOnClickListener {
            var value = txtQty.text.toString().toInt()
            ++value
            txtQty.text = value.toString()
        }
        minus1.setOnClickListener {
            var value = txtQty.text.toString().toInt()
            --value
            txtQty.text = value.toString()
        }
        btnAdd.setOnClickListener {
            // dialog.dismiss();
            val itemId = category_itemList.id
            val qty = txtQty.text.toString()
            //String extra ="1,2";
            var extra = ""
            for (i in radioBtnIdArr.indices) {
                extra = if (i == 0) {
                    radioBtnIdArr[i]
                } else {
                    extra + "," + radioBtnIdArr[i]
                }
            }

            //Log.d("booking_id", booking_id);
            radioBtnIdArr.clear()
            if (extra.isEmpty()) {
                extra = "1,2" //hardcoded please coreect it
            }
            Log.d("extra", extra)
            Log.d("qty", qty)
            Log.d("item_id", itemId!!)
            Log.d("selectedId", "" + selectedidRadiobtnTopping)

            //api call
            if (isNetworkAvailable(requireContext())) {
                dialog.dismiss()
                callApiAddToCart(
                    itemId,
                    qty,
                    storePrefrence.getString(Constant.BOOKINGID),
                    extra,
                    "book_table"
                )
            } else {
                context?.showToastMessage(Constant.NETWORKEROORMSG)
            }
        }
        btnReserve.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun cartListingView() {
        val dialog = Dialog(requireActivity(), R.style.FullHeightDialog)
        dialog.setContentView(R.layout.cartview_alertview_2)
        if (dialog.window != null) {
            dialog.window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        val txtRestroname: TextView = dialog.findViewById(R.id.txt_restroname)
        val txtCustname: TextView = dialog.findViewById(R.id.txt_custname)
        val txtDatetime: TextView = dialog.findViewById(R.id.txt_datetime)
        val txtPhoneno: TextView = dialog.findViewById(R.id.txt_phoneno)
        val etvNoperson: EditText = dialog.findViewById(R.id.etv_noperson)
        val btnPayTableFood: Button = dialog.findViewById(R.id.btn_pay_table_food)
        val btnPayTable: Button = dialog.findViewById(R.id.btn_pay_table)
        val imgClose: ImageView = dialog.findViewById(R.id.img_close)
        progressbarAlertview = dialog.findViewById(R.id.progressBar_alertview)
        txtRestroname.text = tablelistGet?.str_hotel_name
        txtCustname.text = tablelistGet?.str_customer_name
        etvNoperson.setText(tablelistGet?.number_of_person)
        txtPhoneno.text = storePrefrence.getString(MOBILE)
        txtDatetime.text = tablelistGet?.str_time
        imgClose.setOnClickListener { dialog.dismiss() }
        binding.orderFoodRecycleview.layoutManager = LinearLayoutManager(activity)
        if (isNetworkAvailable(requireContext())) {
            callApiCartListView()
        } else {
            context?.showToastMessage(Constant.NETWORKEROORMSG)
        }
        btnPayTableFood.setOnClickListener {
            dialog.dismiss()
            val mainIntent = Intent(context, ActivityPaymentSummary::class.java)
            //Bundle bundle = new Bundle();
            //bundle.putParcelableArrayList("cartbookingarraylist", cartBookingArrayList);
            mainIntent.putExtra("model", tablelistGet)
            mainIntent.putExtra("restromodel", restoData)
            mainIntent.putExtra("comingfrom", "SelectFood")
            startActivity(mainIntent)
        }
        btnPayTable.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun loadJSONFromAsset(): String? {
        var json: String? = null
        json = try {
            val `is` = requireActivity().assets.open("local2.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer)// "UTF-8"
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun loadJSONFromAsset_t(): String? {
        val json: String? = try {
            val `is` = requireActivity().assets.open("local4.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer)//"UTF-8"
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun callApiCartListView() {
        progressbarAlertview?.visibility = View.VISIBLE
        info.getCartDetail(
            "Bearer " + storePrefrence.getString(TOKEN_LOGIN), storePrefrence.getString(
                Constant.IDENTFIER
            )
        )
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val obj = JSONObject(Gson().toJson(response.body()))
                            if (obj.getString("status").equals("200", ignoreCase = true)) {
                                val dataObj = obj.getJSONObject("data")
                                val cartArrayItem = dataObj.getJSONArray("cart_item")
                                cartBookingArrayList = ArrayList()
                                for (i in 0 until cartArrayItem.length()) {
                                    val cartBooking = CartBooking()
                                    val cartDetailObj = cartArrayItem.getJSONObject(i)

                                    //data obj
                                    cartBooking.data_userid = dataObj.getString("user_id")
                                    if (dataObj.has("booking_table_id")) {
                                        cartBooking.data_booking_table_id =
                                            dataObj.getString("booking_table_id")
                                    } else {
                                        cartBooking.data_booking_table_id = ""
                                    }
                                    cartBooking.data_total = dataObj.getString("total")

                                    //cart_item obj
                                    cartBooking.cart_item_qty = cartDetailObj.getString("qty")
                                    cartBooking.cart_item_cartid =
                                        cartDetailObj.getString("cart_id")
                                    cartBooking.cart_item_id = cartDetailObj.getString("id")
                                    if (cartDetailObj.has("item_extra_id")) {
                                        cartBooking.cart_item_extra_id =
                                            cartDetailObj.getString("item_extra_id")
                                    } else {
                                        cartBooking.cart_item_extra_id = "0"
                                    }
                                    //cart_item_details obj
                                    cartBooking.cart_item_details_category_id =
                                        cartDetailObj.getJSONObject("cart_item_details")
                                            .getString("category_id")
                                    cartBooking.cart_item_details_name =
                                        cartDetailObj.getJSONObject("cart_item_details")
                                            .getString("name")
                                    cartBooking.cart_item_details_price =
                                        cartDetailObj.getJSONObject("cart_item_details")
                                            .getString("price")


                                    //extra_item_details obj
                                    val extraNameList = ArrayList<String>()
                                    val extraPriceList = ArrayList<String>()
                                    if (cartDetailObj.getJSONArray("extra_item_details")
                                            .length() > 0
                                    ) {
                                        for (j in 0 until cartDetailObj.getJSONArray("extra_item_details")
                                            .length()) {
                                            val extraItemObj =
                                                cartDetailObj.getJSONArray("extra_item_details")
                                                    .getJSONObject(j)
                                            if (extraItemObj.has("name")) {
                                                extraNameList.add(extraItemObj.getString("name"))
                                            } else {
                                                extraNameList.add("")
                                            }
                                            if (extraItemObj.has("price")) {
                                                extraPriceList.add(extraItemObj.getString("price"))
                                            } else {
                                                extraPriceList.add("")
                                            }
                                            //cartBooking.setExtra_item_details_item_id(extra_item_obj.getString("item_id"));
                                        }
                                    } else {
                                        extraNameList.add("")
                                        extraPriceList.add("")
                                    }

                                    //extra name
                                    var strExtraname = ""
                                    for (k in extraNameList.indices) {
                                        strExtraname = if (k == 0) {
                                            extraNameList[k]
                                        } else {
                                            strExtraname + "," + extraNameList[k]
                                        }
                                    }
                                    cartBooking.extra_item_details_name = strExtraname

                                    //extra price
                                    var strExtraprice = ""
                                    for (k in extraPriceList.indices) {
                                        strExtraprice = if (k == 0) {
                                            extraPriceList[k]
                                        } else {
                                            strExtraprice + "," + extraPriceList[k]
                                        }
                                    }
                                    cartBooking.extra_item_details_price = strExtraprice
                                    cartBookingArrayList?.add(cartBooking)
                                }
                                progressbarAlertview?.visibility = View.GONE
                                //call adapter
                                val cartBookingAdapter =
                                    CartListingAdapter(
                                        requireContext(),
                                        cartBookingArrayList
                                    ) { func_name, cart_item_id, qty_update ->
                                        when (func_name) {
                                            ADD_QTY -> {
                                                callApiAddQty(cart_item_id, qty_update)
                                            }
                                            REMOVE_CART_ITEM -> {
                                                callApiRemoveItemCart(cart_item_id)
                                            }
                                        }
                                    }
                                binding.orderFoodRecycleview.adapter = cartBookingAdapter
                            }
                        } else if (response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE) {
                            val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
                            context?.showToastMessage(jsonObject?.getString("message").toString())
                            progressbarAlertview?.visibility = View.GONE
                            /*JSONObject obj = new JSONObject(loadJSONFromAsset_t());

                                if(obj.getString("status").equalsIgnoreCase("200"))
                                {
                                    JSONObject data_obj = obj.getJSONObject("data");
                                    JSONArray cart_array_item = data_obj.getJSONArray("cart_item");
                                    cartBookingArrayList = new ArrayList<>();
                                    for(int i = 0; i<cart_array_item.length(); i++)
                                    {
                                        CartBooking cartBooking = new CartBooking();
                                        JSONObject cart_detail_obj = cart_array_item.getJSONObject(i);


                                        //data obj
                                        cartBooking.setData_userid(data_obj.getString("user_id"));
                                        cartBooking.setData_booking_table_id(data_obj.getString("booking_table_id"));
                                        cartBooking.setData_total(data_obj.getString("total"));

                                        //cart_item obj
                                        cartBooking.setCart_item_qty(cart_detail_obj.getString("qty"));
                                        cartBooking.setCart_item_cartid(cart_detail_obj.getString("cart_id"));
                                        cartBooking.setCart_item_id(cart_detail_obj.getString("item_id"));
                                        cartBooking.setCart_item_extra_id(cart_detail_obj.getString("item_extra_id"));

                                        //cart_item_details obj
                                        cartBooking.setCart_item_details_category_id(cart_detail_obj.getJSONObject("cart_item_details").getString("category_id"));
                                        cartBooking.setCart_item_details_name(cart_detail_obj.getJSONObject("cart_item_details").getString("name"));
                                        cartBooking.setCart_item_details_price(cart_detail_obj.getJSONObject("cart_item_details").getString("price"));


                                        //extra_item_details obj
                                        ArrayList<String>extra_namelist = new ArrayList<>();
                                        ArrayList<String>extra_pricelist = new ArrayList<>();
                                        for(int j = 0; j<cart_detail_obj.getJSONArray("extra_item_details").length(); j++)
                                        {
                                            JSONObject extra_item_obj = cart_detail_obj.getJSONArray("extra_item_details").getJSONObject(j);

                                            extra_namelist.add(extra_item_obj.getString("name"));
                                            extra_pricelist.add(extra_item_obj.getString("price"));


                                            //cartBooking.setExtra_item_details_item_id(extra_item_obj.getString("item_id"));
                                        }

                                        //extra name
                                        String str_extraname ="";
                                        for(int k =0; k<extra_namelist.size(); k++)
                                        {
                                            if (k == 0)
                                            { str_extraname = extra_namelist.get(k); }
                                            else{  str_extraname = str_extraname+","+extra_namelist.get(k);}
                                        }
                                        cartBooking.setExtra_item_details_name(str_extraname);

                                        //extra price
                                        String str_extraprice ="";
                                        for(int k =0; k<extra_pricelist.size(); k++)
                                        {
                                            if (k == 0)
                                            { str_extraprice = extra_pricelist.get(k); }
                                            else{  str_extraprice = str_extraprice+","+extra_pricelist.get(k);}
                                        }
                                        cartBooking.setExtra_item_details_price(str_extraprice);
                                        cartBookingArrayList.add(cartBooking);

                                   }
                                    progressBar.setVisibility(View.GONE);
                                    //call adapter
                                    CartBookingAdapter cartBookingAdapter = new CartBookingAdapter(getActivity(),cartBookingArrayList);
                                    recycleView.setAdapter(cartBookingAdapter);

                                }*/
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        progressbarAlertview?.visibility = View.GONE
                        context?.showToastMessage(ERRORMSG)
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    progressbarAlertview?.visibility = View.GONE
                    context?.showToastMessage(ERRORMSG)
                }
            })
    }

    fun callApiAddQty(cart_itemid: String?, qty: String?) {
        binding.progressBar.visibility = View.VISIBLE
        info.cartUpdateQty(
            "Bearer " + storePrefrence.getString(TOKEN_LOGIN),
            cart_itemid,
            qty,
            storePrefrence.getString(
                Constant.IDENTFIER
            )
        )
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        //Log.d("Result", jsonObject.toString());
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val obj = JSONObject(Gson().toJson(response.body()))
                            if (obj.getString("status").equals("200", ignoreCase = true)) {
                                context?.showToastMessage(obj.getString("message"))
                                binding.progressBar.visibility = View.GONE
                                if (isNetworkAvailable(requireContext())) {
                                    callApiCartListView()
                                } else {
                                    context?.showToastMessage(Constant.NETWORKEROORMSG)
                                }
                            }
                        } else if (response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE) {
                            val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
                            context?.showToastMessage(jsonObject?.getString("message").toString())
                            binding.progressBar.visibility = View.GONE
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        context?.showToastMessage(ERRORMSG)
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    context?.showToastMessage(ERRORMSG)
                }
            })
    }

    fun callApiRemoveItemCart(cartItemid: String?) {
        binding.progressBar.visibility = View.VISIBLE
        info.cartRemoveQty(
            "Bearer " + storePrefrence.getString(TOKEN_LOGIN),
            cartItemid,
            storePrefrence.getString(
                Constant.IDENTFIER
            )
        )
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val obj = JSONObject(Gson().toJson(response.body()))
                            if (obj.getString("status").equals("200", ignoreCase = true)) {
                                context?.showToastMessage(obj.getString("message"))
                                binding.progressBar.visibility = View.GONE
                                if (isNetworkAvailable(requireContext())) {
                                    callApiCartListView()
                                } else {
                                    context?.showToastMessage(Constant.NETWORKEROORMSG)
                                }
                            }
                        } else if (response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE) {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            context?.showToastMessage(jsonObject.getString("message"))
                            binding.progressBar.visibility = View.GONE
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        context?.showToastMessage(ERRORMSG)
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    context?.showToastMessage(ERRORMSG)
                }
            })
    }

    companion object {
        //var category_id: String? = null
        var tablelistGet: TableList? = null
        var restoData: RestoData? = null
        lateinit var viewModel: SelectFoodViewModel
        fun newInstance(tableList: TableList?, bookTable: RestoData?, vm: SelectFoodViewModel): SelectFoodFragment {
            //category_id = category_id_val;
            //Log.d("idval",category_id);
            tablelistGet = tableList
            restoData = bookTable
            viewModel = vm
            return SelectFoodFragment()
        }
    }
}