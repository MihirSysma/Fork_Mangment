package com.forkmang.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.forkmang.R
import com.forkmang.databinding.ActivityDashboardDrawerBinding
import com.forkmang.fragment.*
import com.forkmang.helper.StorePrefrence
import com.forkmang.helper.showToastMessage
import com.google.android.material.navigation.NavigationView

class DashBoardActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    private val storePrefrence by lazy { StorePrefrence(this) }
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private val binding by lazy { ActivityDashboardDrawerBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(binding.root)
        initializeViews()
        toggleDrawer()
        initializeDefaultFragment(savedInstanceState, 0)
    }

    /**
     * Initialize all widgets
     */
    private fun initializeViews() {

        val navigationView: NavigationView = findViewById(R.id.navigationview_id)
        navigationView.setNavigationItemSelectedListener(this)
        /*val radius: Float = getResources().getDimension(R.dimen.roundcorner)
        val navViewBackground: MaterialShapeDrawable =
            navigationView.background as MaterialShapeDrawable
        navViewBackground.shapeAppearanceModel = navViewBackground.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, radius)
            .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
            .build()*/
    }

    /**
     * Checks if the savedInstanceState is null - onCreate() is ran
     * If so, display fragment of navigation drawer menu at position itemIndex and
     * set checked status as true
     * @param savedInstanceState
     * @param itemIndex
     */
    private fun initializeDefaultFragment(savedInstanceState: Bundle?, itemIndex: Int) {
        if (savedInstanceState == null) {
            val navigationView: NavigationView = findViewById(R.id.navigationview_id)
            val menuItem: MenuItem = navigationView.menu.getItem(itemIndex).setChecked(true)
            onNavigationItemSelected(menuItem)
        }
    }

    /**
     * Creates an instance of the ActionBarDrawerToggle class:
     * 1) Handles opening and closing the navigation drawer
     * 2) Creates a hamburger icon in the toolbar
     * 3) Attaches listener to open/close drawer on icon clicked and rotates the icon
     */
    private fun toggleDrawer() {
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, binding.drawerLayoutId, binding.toolbarId,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayoutId.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    override fun onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (binding.drawerLayoutId.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayoutId.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_scanorder -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.framelayout_id, ScanOrderFragment())
                    .commit()
                closeDrawer()
            }
            R.id.menu_walkin -> {
                /*getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new WalkinFragment())
                        .commit();*/
                val intent_walkin =
                    Intent(this@DashBoardActivity, BookingTabViewActivity::class.java)
                intent_walkin.putExtra("tab_no", "1")
                startActivity(intent_walkin)
                closeDrawer()
            }
            R.id.menu_logout ->                 //Toast.makeText(this, "Register Pressed", Toast.LENGTH_SHORT).show();
                showAlertView_logout()
            R.id.menu_home -> {
                //Toast.makeText(this, "Home Pressed", Toast.LENGTH_SHORT).show();
                val intent_home =
                    Intent(this@DashBoardActivity, BookingTabViewActivity::class.java)
                intent_home.putExtra("tab_no", "0")
                startActivity(intent_home)
                closeDrawer()
            }
            R.id.menu_order -> {
                //Toast.makeText(this, "Menu Order Pressed", Toast.LENGTH_SHORT).show();
                val i = Intent(this@DashBoardActivity, OrdersListingFragment::class.java)
                startActivity(i)
                closeDrawer()
            }
            R.id.menu_wallet -> {
                //Toast.makeText(this, "Menu Wallet Pressed", Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(DashBoardActivity_2.this,WalletView.class);
                startActivity(intent);*/
                supportFragmentManager.beginTransaction()
                    .replace(R.id.framelayout_id, WalletViewFragment())
                    .commit()
                closeDrawer()
            }
            R.id.menu_location -> {
                //Toast.makeText(this, "Menu Location Pressed", Toast.LENGTH_SHORT).show();
                /*Intent location_intent = new Intent(DashBoardActivity_2.this, LocationScreen_Fragment.class);
                startActivity(location_intent);*/
                supportFragmentManager.beginTransaction()
                    .replace(R.id.framelayout_id, LocationScreenFragment())
                    .commit()
                closeDrawer()
            }
            R.id.menu_share -> showToastMessage("Menu Share Pressed")
            R.id.menu_contact -> {
                //Toast.makeText(this, "Menu Contact Pressed", Toast.LENGTH_SHORT).show();
                val intent_conatct_term =
                    Intent(this@DashBoardActivity, ContactTermsFragment::class.java)
                startActivity(intent_conatct_term)
                closeDrawer()
            }
            R.id.menu_support -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.framelayout_id, ChatFragment())
                    .commit()
                closeDrawer()
            }
        }
        return true
    }

    /**
     * Checks if the navigation drawer is open - if so, close it
     */
    fun closeDrawer() {
        if (binding.drawerLayoutId.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayoutId.closeDrawer(GravityCompat.START)
        }
    }

    fun openDrawer() {
        binding.drawerLayoutId.openDrawer((binding.drawerLayoutId))
    }

    /**
     * Iterates through all the items in the navigation menu and deselects them:
     * removes the selection color
     */
    private fun deSelectCheckedState() {
        val navigationView: NavigationView = findViewById(R.id.navigationview_id)
        val noOfItems: Int = navigationView.menu.size()
        for (i in 0 until noOfItems) {
            if (navigationView.menu.getItem(i).isChecked) {
                navigationView.menu.getItem(i).isChecked = false
            }
        }
    }

    private fun showAlertView_logout() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@DashBoardActivity)
        val inflater: LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView: View = inflater.inflate(R.layout.conform_logout_view, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog: AlertDialog = alertDialog.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btn_cancel: Button = dialogView.findViewById(R.id.btn_cancel)
        val btn_yes_logout: Button = dialogView.findViewById(R.id.btn_yes)
        btn_yes_logout.setOnClickListener {
            closeDrawer()
            dialog.dismiss()
            storePrefrence.clear()
            val intent = Intent(this@DashBoardActivity, LoginActivity::class.java)
            startActivity(intent)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            finish()
        }
        btn_cancel.setOnClickListener {
            dialog.dismiss()
            closeDrawer()
            //onBackPressed();
        }
        dialog.show()
    }
}