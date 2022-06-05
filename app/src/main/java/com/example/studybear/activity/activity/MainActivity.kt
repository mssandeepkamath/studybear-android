package com.example.studybear.activity.activity

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.*
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
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
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
    var timerFlag = false


    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
//        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);
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

            Handler().postDelayed(
                {
                    timerFlag = true
                }, 2000
            )


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
                        replaceFragment(NotesFragment(),
                            "2",
                            "Notes",
                            R.id.bottom_notes,
                            it,
                            R.id.notes)

                    }
                    R.id.discuss -> {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/s2JT2Bnp")))
                    }
                    R.id.coding_events -> {
                        if (flagBottom == false) {
                            bottomNavigationView.menu.clear()
                            bottomNavigationView.inflateMenu(R.menu.new_bottom_navigation_menu)
                        }
                        replaceFragment(EventsFragment(),
                            "6",
                            "Coding Events",
                            null,
                            it,
                            R.id.coding_events)
                        flagBottom = true
                    }
                    R.id.news -> {
                        if (flagBottom == false) {
                            bottomNavigationView.menu.clear()
                            bottomNavigationView.inflateMenu(R.menu.new_bottom_navigation_menu)
                        }
                        replaceFragment(NewsFragment(), "5", "Technology news", null, it, R.id.news)
                        flagBottom = true
                    }

                    R.id.circular -> {
                        if (flagBottom == false) {
                            bottomNavigationView.menu.clear()
                            bottomNavigationView.inflateMenu(R.menu.new_bottom_navigation_menu)
                        }
                        replaceFragment(CircularFragment(),
                            "7",
                            "Circulars",
                            null,
                            it,
                            R.id.circular)
                        flagBottom = true
                    }
                    R.id.teachers -> {
                        if (flagBottom == false) {
                            bottomNavigationView.menu.clear()
                            bottomNavigationView.inflateMenu(R.menu.new_bottom_navigation_menu)
                        }
                        replaceFragment(TeachersFragment(),
                            "8",
                            "Teachers",
                            null,
                            it,
                            R.id.teachers)
                        flagBottom = true
                    }
                    R.id.leaderboard -> {
                        bottomNavigationView.menu.clear()
                        bottomNavigationView.inflateMenu(R.menu.new_bottom_navigation_menu)
                        replaceFragment(LeaderBoardFragment(),
                            "9",
                            "Leaderboard",
                            null,
                            it,
                            R.id.leaderboard)
                        flagBottom = true
                    }
                    R.id.report_bug -> {
                        val to = "teamstudybear@gmail.com"
                        val subject = "I FOUND A BUG IN STUDYBEAR ANDROID APP!"
                        val body =
                            "Please clearly mention page name, bug description, and other useful details here."
                        val mailTo = "mailto:" + to +
                                "?&subject=" + Uri.encode(subject) +
                                "&body=" + Uri.encode(body)
                        val emailIntent = Intent(Intent.ACTION_VIEW)
                        emailIntent.data = Uri.parse(mailTo)
                        startActivity(emailIntent)
                    }
                    R.id.about_us -> {
                        if (flagBottom == false) {
                            bottomNavigationView.menu.clear()
                            bottomNavigationView.inflateMenu(R.menu.new_bottom_navigation_menu)
                        }
                        replaceFragment(AboutUsFragment(),
                            "10",
                            "About us",
                            null,
                            it,
                            R.id.about_us)
                        flagBottom = true
                    }
                    R.id.account -> {
                        replaceFragment(AccountFragment(),
                            "4",
                            "Accounts",
                            R.id.bottom_account,
                            it,
                            R.id.account)
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
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/s2JT2Bnp")))
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
                replaceFragment(HomeFragment(), "1", "Home", R.id.home, null, R.id.img_home)
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

    fun replaceFragment(
        fragment: Fragment,
        tag: String,
        title: String,
        id: Int?,
        it: MenuItem?,
        idClicked: Int,
    ) {
        if (flagBottom and (it != navigationView.menu.findItem(idClicked))) {
            bottomNavigationView.menu.clear()
            bottomNavigationView.inflateMenu(R.menu.bottom_navigation_menu)
            flagBottom = false
        }
        supportFragmentManager.beginTransaction().replace(R.id.lytFrame, fragment, tag)
            .addToBackStack(null).commit()
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
        supportFragmentManager.beginTransaction().replace(R.id.lytFrame, fragment, tag)
            .addToBackStack(null).commit()
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
        val fragment3 = supportFragmentManager.findFragmentByTag("NotesTwo")
        val fragment4 = supportFragmentManager.findFragmentByTag("NotesThree")
        val progressLayout1 =
            fragment3?.activity?.findViewById<ProgressBar>(R.id.barProgressNotesOne)
        val progressLayout2 =
            fragment4?.activity?.findViewById<ProgressBar>(R.id.barProgressNotesOne)
        val frag = supportFragmentManager.findFragmentById(R.id.lytFrame)

        when (frag) {
            is NotesFragmentTwo -> {

                if (progressLayout1?.visibility == View.VISIBLE) {
                    Toast.makeText(this, "Please wait..", Toast.LENGTH_SHORT).show()
                } else
                    supportFragmentManager.popBackStackImmediate()
            }

            is NotesFragmentThree -> {
                if (progressLayout2?.visibility == View.VISIBLE) {
                    Toast.makeText(this, "Please wait..", Toast.LENGTH_SHORT).show()
                } else
                    supportFragmentManager.popBackStackImmediate()
            }
            else -> {
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
                        !is HomeFragment -> {
                            homeFragment()
                        }
                        else -> {
                            Glide.get(this).clearMemory()
                            ClearGlideCacheAsyncTask(this).execute()
                            if (timerFlag) {
                                ActivityCompat.finishAffinity(this)
                            } else {
                                Toast.makeText(this@MainActivity,
                                    "Please wait..",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }


    companion object {
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
//                    Glide.get(activityReference.get()!!).clearDiskCache()
                    result = true
                } catch (e: Exception) {
                    println("Called Error: $e")
                }
                return result
            }
        }

    }


}




