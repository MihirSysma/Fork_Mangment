package com.forkmang.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.forkmang.R
import com.forkmang.helper.Constant
import com.forkmang.helper.Constant.TOKEN_LOGIN
import com.forkmang.helper.StorePrefrence
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

class Contact_Fragment : Fragment() {

    private val storePrefrence by lazy { StorePrefrence(requireContext()) }
    var progressBar: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_layout, container, false)
        progressBar = view.findViewById(R.id.progressBar)
        val etv_username: EditText = view.findViewById(R.id.etv_username)
        val etv_email: EditText = view.findViewById(R.id.etv_email)
        val etv_mobile: EditText = view.findViewById(R.id.etv_mobile)
        val etv_msg: EditText = view.findViewById(R.id.etv_msg)
        val btn_submit: Button = view.findViewById(R.id.btn_submit)
        etv_username.setText(storePrefrence.getString(Constant.NAME))
        //etv_email.setText("test@gmail.com");
        etv_mobile.setText(storePrefrence.getString(Constant.MOBILE))
        //etv_msg.setText("Hi this is test msg");
        btn_submit.setOnClickListener {
            if (etv_username.text.isNotEmpty()) {
                if (etv_mobile.text.toString().isNotEmpty()) {
                    if (etv_mobile.text.toString().length == 10) {
                        //Email is empty or not
                        if (Objects.requireNonNull(etv_email.text).isNotEmpty()) {
                            //Email is valid or not
                            if (isValidEmail(etv_email.text.toString())) {
                                callapi_contact(
                                    etv_username.text.toString(),
                                    etv_email.text.toString(),
                                    etv_mobile.text.toString(),
                                    etv_msg.text.toString()
                                )
                            } else {
                                context?.showToastMessage(Constant.VALIDEmail)
                            }
                        } else {
                            context?.showToastMessage(Constant.EmptyEmail)
                        }
                    } else {
                        context?.showToastMessage(Constant.VALID_NO)
                    }
                } else {
                    context?.showToastMessage(Constant.ENTER_MOBILE)
                }
            } else {
                context?.showToastMessage(Constant.ENTER_NAME)
            }
        }
        return view
    }

    //Api code for Book Table start
    private fun callapi_contact(name: String, email: String, phone: String, msg: String) {
        progressBar?.visibility = View.VISIBLE
        info.contact(
            "Bearer " + storePrefrence.getString(TOKEN_LOGIN),
            name, email, phone, msg
        )?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    if (response.code() == Constant.SUCCESS_CODE_n) {
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        context?.showToastMessage(jsonObject.getString("message"))
                        progressBar?.visibility = View.GONE
                        activity?.finish()
                        //Log.d("Result", jsonObject.toString());
                    } else {
                        progressBar?.visibility = View.GONE
                        context?.showToastMessage(Constant.ERRORMSG)
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    progressBar?.visibility = View.GONE
                    context?.showToastMessage(Constant.ERRORMSG)
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                context?.showToastMessage(Constant.ERRORMSG)
                progressBar?.visibility = View.GONE
            }
        })
    }

    companion object {
        fun newInstance(): Contact_Fragment {
            return Contact_Fragment()
        }

        fun isValidEmail(target: CharSequence?): Boolean {
            return !TextUtils.isEmpty(target) && target?.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() } == true
        }
    }
}