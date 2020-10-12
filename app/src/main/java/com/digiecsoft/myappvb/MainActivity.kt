package com.digiecsoft.myappvb

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.alejandrolora.mylibrary.ToolbarActivity
import com.digiecsoft.myappvb.adapters.PagerAdapter
import com.digiecsoft.myappvb.fragments.ResidentFragment
import com.digiecsoft.myappvb.fragments.VehicleFragment
import com.digiecsoft.myappvb.fragments.VisitorFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ToolbarActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var currentUser: FirebaseUser

    private var prevBottomSelectedNavigationButtom: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbarToLoad(toolbarView as Toolbar)
        /* Firebase instances */
        setUpCurrentUser()
        /* Drawer methods */
        setNavDrawer()
        setUserHeaderInformation()
        if (savedInstanceState == null) {
            fragmentTransaction(ResidentFragment())
            navView.menu.getItem(0).isChecked = true
            buttomNavigation.menu.getItem(0).isChecked = true
        }
        /* NavigationButtom and ViewPager methods */
        setUpViewPager(getPagerAdapter())
        setUpBottomNavigationBar()
    }

    /* Firebase instances */

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }

    /* NavigationButtom and ViewPager logic */

    private fun getPagerAdapter(): PagerAdapter {
        val adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(ResidentFragment())
        adapter.addFragment(VehicleFragment())
        adapter.addFragment(VisitorFragment())
        return adapter
    }

    private fun setUpViewPager(adapter: PagerAdapter) {
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = adapter.count
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (prevBottomSelectedNavigationButtom == null) {
                    buttomNavigation.menu.getItem(0).isChecked = false
                    navView.menu.getItem(0).isChecked = false
                } else {
                    prevBottomSelectedNavigationButtom!!.isChecked = false
                }
                buttomNavigation.menu.getItem(position).isChecked = true
                navView.menu.getItem(position).isChecked = true
                prevBottomSelectedNavigationButtom = buttomNavigation.menu.getItem(position)
            }


        })
    }

    private fun setUpBottomNavigationBar() {
        buttomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_resident -> {
                    viewPager.currentItem = 0; navView.menu.getItem(0).isChecked = true; true
                }
                R.id.bottom_nav_vehicle -> {
                    viewPager.currentItem = 1; navView.menu.getItem(1).isChecked = true; true
                }
                R.id.bottom_nav_visitor -> {
                    viewPager.currentItem = 2; navView.menu.getItem(2).isChecked = true; true
                }
                else -> false
            }
        }
    }

    /* Drawer Logic */

    private fun setNavDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            _toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    private fun fragmentTransaction(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.viewPager, fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        showMessageNavItemSelectedById(item.itemId)
        loadFragmentById(item.itemId)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadFragmentById(id: Int) {
        when (id) {
            R.id.nav_resident -> {
                viewPager.currentItem = 0;
            }
            R.id.nav_vehicle -> {
                viewPager.currentItem = 1;
            }
            R.id.nav_visitor -> {
                viewPager.currentItem = 2;
            }
        }
    }

    private fun showMessageNavItemSelectedById(id: Int) {
        when (id) {
            R.id.nav_profile -> toast("Aqui se veran el perfil del usuario que inicio sesion en la aplicacion")
            R.id.nav_settings -> toast("Aqui se veran las opciones que el usuario podra configurar")
        }
    }

    private fun setUserHeaderInformation() {
        val name = navView.getHeaderView(0).findViewById<TextView>(R.id.textViewHeaderFullName)
        val email = navView.getHeaderView(0).findViewById<TextView>(R.id.textViewHeaderEmail)
        name?.let { name.text = currentUser.displayName?.let { currentUser.displayName }
            ?: run { getString(R.string.info_no_name) }
        }
        email?.let { email.text = currentUser.email }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}