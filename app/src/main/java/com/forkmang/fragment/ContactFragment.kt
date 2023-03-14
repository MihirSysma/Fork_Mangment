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
import com.forkmang.helper.Constant.ENTER_MOBILE
import com.forkmang.helper.Constant.ENTER_NAME
import com.forkmang.helper.Constant.ERRORMSG
import com.forkmang.helper.Constant.EmptyEmail
import com.forkmang.helper.Constant.MOBILE
import com.forkmang.helper.Constant.NAME
import com.forkmang.helper.Constant.SUCCESS_CODE_n
import com.forkmang.helper.Constant.TOKEN_LOGIN
import com.forkmang.helper.Constant.VALIDEmail
import com.forkmang.helper.Constant.VALID_NO
import com.forkmang.helper.StorePreference
import com.forkmang.helper.showToastMessage
import com.forkmang.network_call.Api.info
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactFragment : Fragment() {

    private val storePreference by lazy { StorePreference(requireContext()) }
    var progressBar: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_layout, container, false)
        progressBar = view.findViewById(R.id.progressBar)
        val etvUserName: EditText = view.findViewById(R.id.etv_username)
        val etvEmail: EditText = view.findViewById(R.id.etv_email)
        val etvMobile: EditText = view.findViewById(R.id.etv_mobile)
        val etvMsg: EditText = view.findViewById(R.id.etv_msg)
        val btnSubmit: Button = view.findViewById(R.id.btn_submit)
        etvUserName.setText(storePreference.getString(NAME))
        //etv_email.setText("test@gmail.com");
        etvMobile.setText(storePreference.getString(MOBILE))
        //etv_msg.setText("Hi this is test msg");
        btnSubmit.setOnClickListener {
            if (etvUserName.text.isNotEmpty()) {
                if (etvMobile.text.toString().isNotEmpty()) {
                    if (etvMobile.text.toString().length == 10) {
                        //Email is empty or not
                        if (etvEmail.text.isNullOrEmpty().not()) {
                            //Email is valid or not
                            if (isValidEmail(etvEmail.text.toString())) {
                                callApiContact(
                                    etvUserName.text.toString(),
                                    etvEmail.text.toString(),
                                    etvMobile.text.toString(),
                                    etvMsg.text.toString()
                                )
                            } else {
                                context?.showToastMessage(VALIDEmail)
                            }
                        } else {
                            context?.showToastMessage(EmptyEmail)
                        }
                    } else {
                        context?.showToastMessage(VALID_NO)
                    }
                } else {
                    context?.showToastMessage(ENTER_MOBILE)
                }
            } else {
                context?.showToastMessage(ENTER_NAME)
            }
        }
        return view
    }

    //Api code for Book Table start
    private fun callApiContact(name: String, email: String, phone: String, msg: String) {
        progressBar?.visibility = View.VISIBLE
        info.contact(
            "Bearer " + storePreference.getString(TOKEN_LOGIN),
            name, email, phone, msg
        )?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    if (response.code() == SUCCESS_CODE_n) {
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        context?.showToastMessage(jsonObject.getString("message"))
                        progressBar?.visibility = View.GONE
                        activity?.finish()
                    } else {
                        progressBar?.visibility = View.GONE
                        context?.showToastMessage(ERRORMSG)
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    progressBar?.visibility = View.GONE
                    context?.showToastMessage(ERRORMSG)
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                context?.showToastMessage(ERRORMSG)
                progressBar?.visibility = View.GONE
            }
        })
    }

    companion object {
        fun newInstance(): ContactFragment {
            return ContactFragment()
        }

        fun isValidEmail(target: CharSequence?): Boolean {
            return !TextUtils.isEmpty(target) && target?.let {
                Patterns.EMAIL_ADDRESS.matcher(it).matches()
            } == true
        }
    }
}