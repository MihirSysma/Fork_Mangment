package com.forkmang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
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
import com.forkmang.fragment.Contact_TermsFragment;
import com.forkmang.fragment.LocationScreen_Fragment;
import com.forkmang.fragment.OrdersFragment;
import com.forkmang.fragment.ScanOrderFragment;
import com.forkmang.fragment.WalletView_Fragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;

public class DashBoardActivity_2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FrameLayout frameLayout;
    private NavigationView navigationView;
    RadioButton button_arabic,button_eng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_3);
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
                Toast.makeText(this, "Walkin Pressed", Toast.LENGTH_SHORT).show();

                closeDrawer();
                break;
            case R.id.menu_register:
                Toast.makeText(this, "Register Pressed", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_home:
                Toast.makeText(this, "Home Pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_order:
                //Toast.makeText(this, "Menu Order Pressed", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DashBoardActivity_2.this,OrdersFragment.class);
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
                /*getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new SettingsFragment())
                        .commit();*/

                Toast.makeText(this, "Menu Support Pressed", Toast.LENGTH_SHORT).show();
                //deSelectCheckedState();
                closeDrawer();
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




}
