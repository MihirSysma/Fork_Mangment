package com.forkmang.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.forkmang.R;
import com.forkmang.fragment.ScanOrderFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;

public class DrawerActivity extends AppCompatActivity {

    Context mContext = DrawerActivity.this;
    public NavigationView navigationView;
    public DrawerLayout drawer;
    public ActionBarDrawerToggle drawerToggle;
    protected FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        frameLayout = findViewById(R.id.content_frame);
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);

        //View header = navigationView.getHeaderView(0);

        float radius = getResources().getDimension(R.dimen.roundcorner);
        MaterialShapeDrawable navViewBackground = (MaterialShapeDrawable) navigationView.getBackground();
        navViewBackground.setShapeAppearanceModel(
                navViewBackground.getShapeAppearanceModel()
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED,radius)
                        .setBottomLeftCorner(CornerFamily.ROUNDED,radius)
                        .build());

        setupNavigationDrawer();
    }

    private void setupNavigationDrawer() {
        Menu nav_Menu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            drawer.closeDrawers();
            switch (menuItem.getItemId()) {
                case R.id.menu_scanorder:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ScanOrderFragment())
                            .commit();
                    break;
                case R.id.menu_walkin:
                    /*getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new WalkinFragment())
                            .commit();*/

                    break;

                case R.id.menu_logout:
                    Toast.makeText(this, "Register Pressed", Toast.LENGTH_SHORT).show();

                    break;

                case R.id.menu_order:
                    //Toast.makeText(this, "Menu Order Pressed", Toast.LENGTH_SHORT).show();
                   /* getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new OrdersFragment())
                            .commit();*/



                    break;

                case R.id.menu_wallet:
                    Toast.makeText(this, "Menu Wallet Pressed", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.menu_location:
                    Toast.makeText(this, "Menu Location Pressed", Toast.LENGTH_SHORT).show();

                    break;
            }
            return false;
        });
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }



}