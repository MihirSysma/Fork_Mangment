package com.forkmang.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.forkmang.R
import com.forkmang.activity.BookingTabViewActivity

class ScanOrderFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scanorder_layout, container, false)
        val txt: TextView = view.findViewById(R.id.txt)
        val txt_need_support = view.findViewById<TextView>(R.id.need_support)
        txt_need_support.setOnClickListener { }
        txt.setOnClickListener {
            Log.d("HI", "Hello")
            val intent = Intent(activity, BookingTabViewActivity::class.java)
            intent.putExtra("tab_no", "0")
            startActivity(intent)
        }
        return view
    }
}