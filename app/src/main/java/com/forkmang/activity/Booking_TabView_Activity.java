package com.forkmang.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.forkmang.R;
import com.forkmang.adapter.ViewPagerAdapter;
import com.forkmang.fragment.Book_Table_Fragment;
import com.forkmang.fragment.Pickup_Fragment;
import com.forkmang.fragment.Walkin_Fragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Booking_TabView_Activity extends AppCompatActivity {
    ViewPager2 viewPager;
    TabLayout tabLayout;
    Activity activity = Booking_TabView_Activity.this;
    EditText etv_serach;
    Context ctx ;
    int current_tabactive;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_tab_view);
        ctx=Booking_TabView_Activity.this;
        viewPager=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tabLayout);
        etv_serach=findViewById(R.id.etv_serach);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),getLifecycle(),ctx);
        viewPager.setAdapter(viewPagerAdapter);


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position,
                                       float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d("pageno", ""+position);
                current_tabactive=position;
            }

        });


        String str_tab_no  = getIntent().getStringExtra("tab_no");


        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if(position==0){
                tab.setText(R.string.book_table);
            }
            else if(position==1){
                tab.setText(R.string.walkin);
            }
            else if(position==2){
                tab.setText(R.string.pickup);
            }
        }).attach();

        selectPage(Integer.parseInt(str_tab_no));

        /*if(Constant.IS_BookTableFragmentLoad)
        {
            etv_serach.setEnabled(true);
        }
        else{
            etv_serach.setEnabled(false);
        }*/





        etv_serach.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                if(current_tabactive==0)
                {
                    Book_Table_Fragment book_table_fragment = Book_Table_Fragment.GetInstance();
                    //Book Table Fragment
                    if(s.toString().length()==0)
                    {
                        Hidekeyboard();
                        book_table_fragment.call_reloadbooktable();
                    }
                    else if(s.toString().length() > 3)
                    {
                        Hidekeyboard();
                        book_table_fragment.filter_booktable(etv_serach.getText().toString());
                    }
                }
                else if(current_tabactive == 1)
                {
                    //walkin fragment
                    if(s.toString().length() > 3)
                    {
                        Walkin_Fragment walkin_fragment = Walkin_Fragment.GetInstance();
                        Log.d("current_tabactive","walkin");
                    }


                }
                else if(current_tabactive == 3)
                {
                    if(s.toString().length() > 3)
                    {
                        //pickup fragment
                        Pickup_Fragment pickup_Fragment = Pickup_Fragment.GetInstance();
                        Log.d("current_tabactive","pickup");
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }


        });


        etv_serach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    Hidekeyboard();

                    /*Book_Table_Fragment frag = Book_Table_Fragment.GetInstance();
                    if(etv_serach.getText().length() > 3)
                    {
                        frag.filter_booktable(etv_serach.getText().toString());
                    }*/

                    return true;
                }

                return false;
            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    void selectPage(int pageIndex){
        Log.d("tabno=>",""+pageIndex);
        tabLayout.setScrollPosition(pageIndex,0f,true);
        viewPager.setCurrentItem(pageIndex);
    }


    private void Hidekeyboard()
    {
        etv_serach.clearFocus();
        InputMethodManager in = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(etv_serach.getWindowToken(), 0);
    }

    public void hide_search()
    {
        etv_serach.setVisibility(View.GONE);
    }

    public void visble_search()
    {
        etv_serach.setVisibility(View.VISIBLE);
    }

}