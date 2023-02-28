package com.forkmang.activity

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.forkmang.R


class DashBoardView : DrawerActivity() {
    var toolbar: Toolbar? = null
    var menu: Menu? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_dash_board_view, frameLayout)
        toolbar = findViewById(R.id.toolbar_id)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(resources.getString(R.string.app_name))
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        drawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer, toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ) {}
    }

    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu()
    }

    override fun onBackPressed() {
        if (navigationView?.let { drawer?.isDrawerOpen(it) } == true) drawer?.closeDrawers()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        //getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return false
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

}