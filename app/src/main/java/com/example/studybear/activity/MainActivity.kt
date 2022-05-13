package com.example.studybear.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.example.studybear.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var navigationView: NavigationView
    lateinit var appBar:AppBarLayout
    var prev: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.lytDrawer)
        coordinatorLayout = findViewById(R.id.lytCoordinator)
        toolbar = findViewById(R.id.wdgToolbar)
        frameLayout = findViewById(R.id.lytFrame)
        bottomNavigationView = findViewById(R.id.vwBottomNavigation)
        navigationView = findViewById(R.id.vwNavigation)
        appBar=findViewById(R.id.lytAppBar)

        setUpToolbar()
        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        homeFragment()
        navigationView.setNavigationItemSelectedListener {
            appBar.setExpanded(true)

            prev?.isChecked = false
            it.isCheckable = true
            it.isChecked = true
            prev = it

            when (it.itemId) {
                R.id.notes -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.lytFrame, NotesFragment())
                        .commit()
                    supportActionBar?.title = "Notes"
                    bottomNavigationView.selectedItemId = R.id.bottom_notes
                    drawerLayout.closeDrawers()
                }
                R.id.discuss -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.lytFrame, DiscussFragment())
                        .commit()

                    supportActionBar?.title = "Discuss"
                    bottomNavigationView.selectedItemId = R.id.bottom_discuss
                    drawerLayout.closeDrawers()
                }
                R.id.coding_events -> {
                    drawerLayout.closeDrawers()
                }
                R.id.news -> {
                    drawerLayout.closeDrawers()
                }
                R.id.circular -> {
                    drawerLayout.closeDrawers()
                }
                R.id.teachers -> {
                    drawerLayout.closeDrawers()
                }
                R.id.leaderboard -> {
                    drawerLayout.closeDrawers()
                }
                R.id.report_bug -> {
                    drawerLayout.closeDrawers()
                }
                R.id.about_us -> {
                    val intent= Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                }
                R.id.account -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.lytFrame, AccountFragment())
                        .commit()
                    supportActionBar?.title = "Account"
                    bottomNavigationView.selectedItemId = R.id.bottom_account
                    drawerLayout.closeDrawers()
                }
                R.id.rate_us -> {
                    drawerLayout.closeDrawers()
                }

            }
            return@setNavigationItemSelectedListener true
        }

        bottomNavigationView.setOnNavigationItemSelectedListener {
            appBar.setExpanded(true)
            when (it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.lytFrame, HomeFragment())
                        .commit()
                    supportActionBar?.title = "Home"
                    navigationView.checkedItem?.isChecked = false

                }
                R.id.bottom_notes -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.lytFrame, NotesFragment())
                        .commit()
                    supportActionBar?.title = "Notes"
                    navigationView.setCheckedItem(R.id.notes)
                    navigationView.checkedItem?.isChecked = true
                }
                R.id.bottom_discuss -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.lytFrame, DiscussFragment())
                        .commit()

                    supportActionBar?.title = "Discuss"
                    navigationView.setCheckedItem(R.id.discuss)
                    navigationView.checkedItem?.isChecked = true
                }
                R.id.bottom_account -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.lytFrame, AccountFragment())
                        .commit()
                    supportActionBar?.title = "Account"
                    navigationView.setCheckedItem(R.id.account)
                    navigationView.checkedItem?.isChecked = true
                }

            }
            return@setOnNavigationItemSelectedListener true
        }
        val headerLayout = navigationView.getHeaderView(0)
        val imageHome = headerLayout.findViewById<ImageView>(R.id.img_home)
        imageHome.setOnClickListener {

            supportFragmentManager.beginTransaction().replace(R.id.lytFrame, HomeFragment())
                .commit()
            supportActionBar?.title = "Home"
            bottomNavigationView.selectedItemId = R.id.home
            drawerLayout.closeDrawers()

        }



    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Study Bear"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun homeFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.lytFrame, HomeFragment()).commit()
        supportActionBar?.title = "Home"
        navigationView.checkedItem?.isChecked = false
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.lytFrame)
        bottomNavigationView.selectedItemId = R.id.home
        when (frag) {
            !is HomeFragment -> homeFragment()
            else -> super.onBackPressed()
        }

    }



}