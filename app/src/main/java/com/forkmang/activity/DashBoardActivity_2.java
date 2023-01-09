package com.forkmang.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.forkmang.R;
import com.forkmang.fragment.ChatFragment;
import com.forkmang.fragment.Contact_TermsFragment;
import com.forkmang.fragment.LocationScreen_Fragment;
import com.forkmang.fragment.OrdersListing_Fragment;
import com.forkmang.fragment.ScanOrderFragment;
import com.forkmang.fragment.WalletView_Fragment;
import com.forkmang.helper.StorePrefrence;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.util.Objects;

public class DashBoardActivity_2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FrameLayout frameLayout;
    private NavigationView navigationView;
    RadioButton button_arabic,button_eng;
    StorePrefrence storePrefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_3);
        storePrefrence = new StorePrefrence(DashBoardActivity_2.this);
        initializeViews();
        toggleDrawer();
        initializeDefaultFragment(savedInstanceState,0);
    }

    /**
     * Initialize all widgets
     */
    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar_id);
        toolbar.setTitle(R.string.app_name);
        toolbar.getBackground().setAlpha(0);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = findViewById(R.id.drawer_layout_id);
        frameLayout = findViewById(R.id.framelayout_id);
        navigationView = findViewById(R.id.navigationview_id);
        navigationView.setNavigationItemSelectedListener(this);

        float radius = getResources().getDimension(R.dimen.roundcorner);
        MaterialShapeDrawable navViewBackground = (MaterialShapeDrawable) navigationView.getBackground();
        navViewBackground.setShapeAppearanceModel(
                navViewBackground.getShapeAppearanceModel()
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED,radius)
                        .setBottomLeftCorner(CornerFamily.ROUNDED,radius)
                        .build());


   }

    /**
     * Checks if the savedInstanceState is null - onCreate() is ran
     * If so, display fragment of navigation drawer menu at position itemIndex and
     * set checked status as true
     * @param savedInstanceState
     * @param itemIndex
     */
    private void initializeDefaultFragment(Bundle savedInstanceState, int itemIndex){
        if (savedInstanceState == null)
        {
            MenuItem menuItem = navigationView.getMenu().getItem(itemIndex).setChecked(true);
            onNavigationItemSelected(menuItem);
        }

    }

    /**
     * Creates an instance of the ActionBarDrawerToggle class:
     * 1) Handles opening and closing the navigation drawer
     * 2) Creates a hamburger icon in the toolbar
     * 3) Attaches listener to open/close drawer on icon clicked and rotates the icon
     */
    private void toggleDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // If drawer is already close -- Do not override original functionality
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.menu_scanorder:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new ScanOrderFragment())
                        .commit();
                closeDrawer();
                break;
            case R.id.menu_walkin:
                /*getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new WalkinFragment())
                        .commit();*/
                Intent intent_walkin = new Intent(DashBoardActivity_2.this,Booking_TabView_Activity.class);
                intent_walkin.putExtra("tab_no", "1");
                startActivity(intent_walkin);
                closeDrawer();
                //Toast.makeText(this, "Walkin Pressed", Toast.LENGTH_SHORT).show();
                //closeDrawer();
                break;
            case R.id.menu_register:
                //Toast.makeText(this, "Register Pressed", Toast.LENGTH_SHORT).show();
                showAlertView_logout();
                break;

            case R.id.menu_home:
                //Toast.makeText(this, "Home Pressed", Toast.LENGTH_SHORT).show();
                Intent intent_home = new Intent(DashBoardActivity_2.this,Booking_TabView_Activity.class);
                intent_home.putExtra("tab_no", "0");
                startActivity(intent_home);
                closeDrawer();
                break;
            case R.id.menu_order:
                //Toast.makeText(this, "Menu Order Pressed", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DashBoardActivity_2.this, OrdersListing_Fragment.class);
                startActivity(i);
                closeDrawer();
                break;

            case R.id.menu_wallet:
                //Toast.makeText(this, "Menu Wallet Pressed", Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(DashBoardActivity_2.this,WalletView.class);
                startActivity(intent);*/
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new WalletView_Fragment())
                        .commit();
                closeDrawer();
                break;

            case R.id.menu_location:
                //Toast.makeText(this, "Menu Location Pressed", Toast.LENGTH_SHORT).show();
                /*Intent location_intent = new Intent(DashBoardActivity_2.this, LocationScreen_Fragment.class);
                startActivity(location_intent);*/
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new LocationScreen_Fragment())
                        .commit();
                closeDrawer();
                break;
            case R.id.menu_share:
                Toast.makeText(this, "Menu Share Pressed", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_contact:
                //Toast.makeText(this, "Menu Contact Pressed", Toast.LENGTH_SHORT).show();
                Intent intent_conatct_term = new Intent(DashBoardActivity_2.this, Contact_TermsFragment.class);
                startActivity(intent_conatct_term);
                closeDrawer();
                break;

            case R.id.menu_support:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new ChatFragment())
                        .commit();
                closeDrawer();

                /*Intent intent_chat = new Intent(DashBoardActivity_2.this, ChatFragment.class);
                startActivity(intent_chat);
                closeDrawer();*/

                //Toast.makeText(this, "Menu Support Pressed", Toast.LENGTH_SHORT).show();
                //deSelectCheckedState();
                break;
        }

        return true;
    }


    /**
     * Checks if the navigation drawer is open - if so, close it
     */
    public void closeDrawer(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void openDrawer(){
        drawerLayout.openDrawer(drawerLayout);
    }
    
    /**
     * Iterates through all the items in the navigation menu and deselects them:
     * removes the selection color
     */
    private void deSelectCheckedState(){
        int noOfItems = navigationView.getMenu().size();
        for (int i=0; i<noOfItems;i++){
            if(navigationView.getMenu().getItem(i).isChecked())
            {
                navigationView.getMenu().getItem(i).setChecked(false);
            }
        }
    }


    private void showAlertView_logout() {
        final androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(DashBoardActivity_2.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.conform_logout_view, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final androidx.appcompat.app.AlertDialog dialog = alertDialog.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button btn_cancel, btn_yes_logout;

        btn_cancel = dialogView.findViewById(R.id.btn_cancel);
        btn_yes_logout = dialogView.findViewById(R.id.btn_yes);


        btn_yes_logout.setOnClickListener(view -> {
            closeDrawer();
            dialog.dismiss();
            storePrefrence.clear();

            Intent intent = new Intent(DashBoardActivity_2.this, LoginActivity.class);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            //System.exit(0);
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                closeDrawer();
                //onBackPressed();
            }
        });
        dialog.show();
    }


}
