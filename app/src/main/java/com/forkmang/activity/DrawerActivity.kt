package com.forkmang.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.forkmang.R
import com.forkmang.databinding.ActivityDrawerBinding
import com.forkmang.fragment.ScanOrderFragment
import com.forkmang.helper.showToastMessage
import com.google.android.material.navigation.NavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable

open class DrawerActivity : AppCompatActivity() {

    var navigationView: NavigationView? = null
    var drawer: DrawerLayout? = null
    var drawerToggle: ActionBarDrawerToggle? = null
    protected var frameLayout: FrameLayout? = null

    val binding by lazy { ActivityDrawerBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        frameLayout = binding.contentFrame
        navigationView = binding.navView
        drawer = binding.drawerLayout

  /*      val radius: Float = getResources().getDimension(R.dimen.roundcorner)
           val navViewBackground: MaterialShapeDrawable =
               binding.navView.background as MaterialShapeDrawable
           navViewBackground.shapeAppearanceModel = navViewBackground.shapeAppearanceModel
               .toBuilder()
               .setTopLeftCorner(CornerFamily.ROUNDED, radius)
               .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
               .build()*/
        setupNavigationDrawer()
    }

    private fun setupNavigationDrawer() {
        binding.navView.setNavigationItemSelectedListener { menuItem: MenuItem ->
            binding.drawerLayout.closeDrawers()
            when (menuItem.itemId) {
                R.id.menu_scanorder -> supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, ScanOrderFragment())
                    .commit()
                R.id.menu_walkin -> {}
                R.id.menu_logout -> showToastMessage( "Register Pressed")
                R.id.menu_order -> {}
                R.id.menu_wallet -> showToastMessage("Menu Wallet Pressed")
                R.id.menu_location -> showToastMessage("Menu Location Pressed")
            }
            false
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        //Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle?.syncState()
    }
}