package com.forkmang.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.forkmang.R
import com.forkmang.fragment.ScanOrderFragment
import com.google.android.material.navigation.NavigationView

open class DrawerActivity : AppCompatActivity() {

    var navigationView: NavigationView? = null
    var drawer: DrawerLayout? = null
    var drawerToggle: ActionBarDrawerToggle? = null
    protected var frameLayout: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        frameLayout = findViewById(R.id.content_frame)
        navigationView = findViewById(R.id.nav_view)
        drawer = findViewById(R.id.drawer_layout)

        //View header = navigationView.getHeaderView(0);
        val radius: Float = getResources().getDimension(R.dimen.roundcorner)
        /*   val navViewBackground: MaterialShapeDrawable =
               navigationView.getBackground() as MaterialShapeDrawable
           navViewBackground.shapeAppearanceModel = navViewBackground.shapeAppearanceModel
               .toBuilder()
               .setTopLeftCorner(CornerFamily.ROUNDED, radius)
               .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
               .build()*/
        setupNavigationDrawer()
    }

    private fun setupNavigationDrawer() {
        val nav_Menu: Menu = navigationView!!.menu
        navigationView!!.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem: MenuItem ->
            drawer!!.closeDrawers()
            when (menuItem.itemId) {
                R.id.menu_scanorder -> supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, ScanOrderFragment())
                    .commit()
                R.id.menu_walkin -> {}
                R.id.menu_logout -> Toast.makeText(this, "Register Pressed", Toast.LENGTH_SHORT)
                    .show()
                R.id.menu_order -> {}
                R.id.menu_wallet -> Toast.makeText(
                    this,
                    "Menu Wallet Pressed",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.menu_location -> Toast.makeText(
                    this,
                    "Menu Location Pressed",
                    Toast.LENGTH_SHORT
                ).show()
            }
            false
        })
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        //Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle!!.syncState()
    }
}