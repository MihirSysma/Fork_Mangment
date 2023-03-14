package com.forkmang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
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

class TermFragment : Fragment() {

    private val storePreference by lazy { StorePrefrence(requireContext()) }
    var progressBar: ProgressBar? = null
    var txtTerm: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_term_layout, container, false)
        progressBar = view.findViewById(R.id.progressBar)
        txtTerm = view.findViewById(R.id.txt_term)
        callApiTerms()
        return view
    }

    //Api code for Book Table start
    private fun callApiTerms() {
        progressBar?.visibility = View.VISIBLE
        info.getTerms("Bearer " + storePreference.getString(TOKEN_LOGIN))
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            val dataArray = jsonObject.getJSONObject("data").getJSONArray("data")
                            val term = dataArray.getJSONObject(0).getString("content")
                            txtTerm?.text = term
                            progressBar?.visibility = View.GONE

                        } else {
                            progressBar?.visibility = View.GONE
                            txtTerm?.text = "Error Occur During  Fetching Terms & Condition"
                        }
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        progressBar?.visibility = View.GONE
                        txtTerm?.text = "Error Occur During  Fetching Terms & Condition"
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    context?.showToastMessage(Constant.ERRORMSG)
                    txtTerm?.text = "Error Occur During  Fetching Terms & Condition"
                    progressBar?.visibility = View.GONE
                }
            })
    }

    companion object {
        fun newInstance(): TermFragment {
            return TermFragment()
        }
    }
}