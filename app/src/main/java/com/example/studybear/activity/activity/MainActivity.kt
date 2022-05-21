package com.example.studybear.activity.activity

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.studybear.R
import com.example.studybear.activity.fragment.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.wallet.callback.OnCompleteListener
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.skydoves.powerspinner.PowerSpinnerView
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var navigationView: NavigationView
    lateinit var appBar: AppBarLayout
    var prev: MenuItem? = null
    lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    var flagBottom: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val current_user = auth.currentUser
        val email_id = current_user?.email
        val flag = if (email_id == null) true
        else !email_id.contains("@rvce.edu.in", true)

        if (flag) {
            Toast.makeText(this, "Please use RVCE email id", Toast.LENGTH_LONG).show()
            googleSignInClient.signOut()
            current_user!!.delete()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {

            setContentView(R.layout.activity_main)
            drawerLayout = findViewById(R.id.lytDrawer)
            coordinatorLayout = findViewById(R.id.lytCoordinator)
            toolbar = findViewById(R.id.wdgToolbar)
            frameLayout = findViewById(R.id.lytFrame)
            bottomNavigationView = findViewById(R.id.vwBottomNavigation)
            navigationView = findViewById(R.id.vwNavigation)
            appBar = findViewById(R.id.lytAppBar)


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
                        replaceFragment(NotesFragment(), "2", "Notes", R.id.bottom_notes, it,R.id.notes)

                    }
                    R.id.discuss -> {
                        replaceFragment(DiscussFragment(), "3", "Discuss", R.id.bottom_discuss, it,R.id.discuss)
                    }
                    R.id.coding_events -> {
                        if (flagBottom == false) {
                            bottomNavigationView.menu.clear()
                            bottomNavigationView.inflateMenu(R.menu.new_bottom_navigation_menu)
                        }
                        replaceFragment(EventsFragment(),"6","Coding Events",null,it, R.id.coding_events)
                        flagBottom = true
                    }
                    R.id.news -> {
                        if (flagBottom == false) {
                            bottomNavigationView.menu.clear()
                            bottomNavigationView.inflateMenu(R.menu.new_bottom_navigation_menu)
                        }
                        replaceFragment(NewsFragment(), "5", "Technology news", null, it,R.id.news)
                        flagBottom = true
                    }

                    R.id.circular -> {

                        //          replaceFragment(CircularsFragment(),"7","Circulars",null,it, R.id.circular)
                    }
                    R.id.teachers -> {
                        //          replaceFragment(TeachersFragment(),"8","Teachers",null,it,  R.id.teachers )
                    }
                    R.id.leaderboard -> {
                        //          replaceFragment(LeaderBoardFragment(),"9","Leaderboard",null,it, R.id.leaderboard)
                    }
                    R.id.report_bug -> {
                        //implicit intent
                    }
                    R.id.about_us -> {
                        val intent=Intent(this,RazorPayDataActivity::class.java)
                        startActivity(intent)
                        finish()
                        //          replaceFragment(AboutUsFragment(),"10","About us",null,it,R.id.about_us )
                    }
                    R.id.account -> {
                        replaceFragment(AccountFragment(), "4", "Accounts", R.id.bottom_account, it,R.id.account)
                    }
                    R.id.rate_us -> {
                        //implicit intent
                    }

                }
                return@setNavigationItemSelectedListener true
            }

            bottomNavigationView.setOnNavigationItemSelectedListener {
                appBar.setExpanded(true)
                when (it.itemId) {
                    R.id.home -> {
                        replaceFragmentBottom(HomeFragment(), "1", "Home", null, false)
                        navigationView.checkedItem?.isChecked = false

                    }
                    R.id.bottom_notes -> {
                        replaceFragmentBottom(NotesFragment(), "2", "Notes", R.id.notes, true)

                    }
                    R.id.bottom_discuss -> {
                        replaceFragmentBottom(DiscussFragment(), "3", "Discuss", R.id.discuss, true)
                    }
                    R.id.bottom_account -> {
                        replaceFragmentBottom(AccountFragment(), "4", "Account", R.id.account, true)
                    }
                    R.id.newHome -> {
                        bottomNavigationView.menu.clear()
                        bottomNavigationView.inflateMenu(R.menu.bottom_navigation_menu)
                        flagBottom = false
                        replaceFragmentBottom(HomeFragment(), "1", "Home", null, false)

                    }

                }
                return@setOnNavigationItemSelectedListener true
            }
            val headerLayout = navigationView.getHeaderView(0)
            val imageHome = headerLayout.findViewById<ImageView>(R.id.img_home)
            imageHome.setOnClickListener {
                replaceFragment(HomeFragment(), "1", "Home", R.id.home, null,R.id.img_home)
            }


        }

    }


    fun BottomNavigationView.uncheckAllItems() {
        menu.setGroupCheckable(0, true, false)
        for (i in 0 until menu.size()) {
            menu.getItem(i).isChecked = false
        }
        menu.setGroupCheckable(0, true, true)
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
        replaceFragmentBottom(HomeFragment(), "1", "Home", null, false)
    }

    fun replaceFragment(fragment: Fragment, tag: String, title: String, id: Int?, it: MenuItem?,idClicked:Int) {
        if (flagBottom and (it != navigationView.menu.findItem(idClicked))) {
            bottomNavigationView.menu.clear()
            bottomNavigationView.inflateMenu(R.menu.bottom_navigation_menu)
            flagBottom = false
        }
        supportFragmentManager.beginTransaction().replace(R.id.lytFrame, fragment, tag).addToBackStack(null).commit()
        supportActionBar?.title = title
        if (id != null)
            bottomNavigationView.selectedItemId = id

        drawerLayout.closeDrawers()
    }

    fun replaceFragmentBottom(
        fragment: Fragment,
        tag: String,
        title: String,
        id: Int?,
        flag: Boolean,
    ) {
        supportFragmentManager.beginTransaction().replace(R.id.lytFrame, fragment, tag).addToBackStack(null).commit()
        supportActionBar?.title = title
        if (id != null)
            navigationView.setCheckedItem(id)
        navigationView.checkedItem?.isChecked = flag
    }



    override fun onBackPressed() {

        val fragment1 = supportFragmentManager.findFragmentByTag("4")
        val fragment2 = supportFragmentManager.findFragmentByTag("5")
        val spinner = fragment1?.activity?.findViewById<PowerSpinnerView>(R.id.spinnerSem)
        val refresh = fragment2?.activity?.findViewById<SwipeRefreshLayout>(R.id.lytRefresh)
        val frag = supportFragmentManager.findFragmentById(R.id.lytFrame)

        if (flagBottom) {
            bottomNavigationView.menu.clear()
            bottomNavigationView.inflateMenu(R.menu.bottom_navigation_menu)
            flagBottom = false
        }
        bottomNavigationView.selectedItemId = R.id.home


        if (spinner?.isShowing == true) {
            spinner.dismiss()
        } else {

            when (frag) {
                is NewsFragment -> {

                    if (refresh?.isRefreshing == true)
                        println("Cannot go back")
                    else
                    {
                        homeFragment()
                    }


                }
                !is HomeFragment -> {
                    homeFragment()
                }

                else -> {
                    Glide.get(this).clearMemory()
                    ClearGlideCacheAsyncTask(this).execute()
                  ActivityCompat.finishAffinity(this)
                }

            }

        }

    }

    companion object
    {
        class ClearGlideCacheAsyncTask internal constructor(context: MainActivity) :
            AsyncTask<Void?, Void?, Boolean>() {

            private val activityReference: WeakReference<MainActivity> = WeakReference(context)
            private var result = false

            override fun onPostExecute(result: Boolean) {
                super.onPostExecute(result)
                if (result) println("Called Succes")
            }

            override fun doInBackground(vararg params: Void?): Boolean {
                println("Called")
                try {
                    Glide.get(activityReference.get()!!).clearDiskCache()
                    result = true
                } catch (e: Exception) {
                    println("Called Error: $e")
                }
                return result
            }
        }

    }


}




