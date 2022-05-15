package com.example.studybear.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.studybear.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.skydoves.powerspinner.PowerSpinnerView

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var navigationView: NavigationView
    lateinit var appBar:AppBarLayout
    var prev: MenuItem? = null
    lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        super.onCreate(savedInstanceState)
        auth= FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val current_user=auth.currentUser
        val email_id=current_user?.email
        val flag=if(email_id==null) true
        else !email_id.contains("@rvce.edu.in",true)

        if(flag)
        {
            Toast.makeText(this,"Please use RVCE email id",Toast.LENGTH_LONG).show()
            googleSignInClient.signOut()
            current_user!!.delete()
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else
        {
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
                        replaceFragement(NotesFragment(),"2")
                        supportActionBar?.title = "Notes"
                        bottomNavigationView.selectedItemId = R.id.bottom_notes
                        drawerLayout.closeDrawers()
                    }
                    R.id.discuss -> {
                        replaceFragement(DiscussFragment(),"3")
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
                        replaceFragement(AccountFragment(),"4")
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
                        replaceFragement(HomeFragment(),"1")
                        supportActionBar?.title = "Home"
                        navigationView.checkedItem?.isChecked = false

                    }
                    R.id.bottom_notes -> {
                        replaceFragement(NotesFragment(),"2")
                        supportActionBar?.title = "Notes"
                        navigationView.setCheckedItem(R.id.notes)
                        navigationView.checkedItem?.isChecked = true
                    }
                    R.id.bottom_discuss -> {
                        replaceFragement(DiscussFragment(),"3")
                        supportActionBar?.title = "Discuss"
                        navigationView.setCheckedItem(R.id.discuss)
                        navigationView.checkedItem?.isChecked = true
                    }
                    R.id.bottom_account -> {
                        replaceFragement(AccountFragment(),"4")
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

                replaceFragement(HomeFragment(),"1")
                supportActionBar?.title = "Home"
                bottomNavigationView.selectedItemId = R.id.home
                drawerLayout.closeDrawers()

            }


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
        replaceFragement(HomeFragment(),"1")
        supportActionBar?.title = "Home"
        navigationView.checkedItem?.isChecked = false
    }

    fun replaceFragement(fragment:Fragment,tag:String)
    {
        supportFragmentManager.beginTransaction().replace(R.id.lytFrame, fragment,tag).commit()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag("4")
        val spinner = fragment?.activity?.findViewById<PowerSpinnerView>(R.id.spinnerSem)
        val frag = supportFragmentManager.findFragmentById(R.id.lytFrame)
        bottomNavigationView.selectedItemId = R.id.home

        if (spinner?.isShowing == true) {
            spinner.dismiss()
        }else
        {
            when (frag) {
                !is HomeFragment -> homeFragment()
                else -> super.onBackPressed()
            }

        }

        }


    }
