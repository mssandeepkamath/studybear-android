package com.sandeep.studybear.activity.fragment

import android.content.Context
import android.content.Intent
import com.sandeep.studybear.R
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.PixelCopy.request
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sandeep.studybear.activity.activity.MainActivity
import com.sandeep.studybear.activity.activity.PdfActivity.Companion.auth
import com.sandeep.studybear.activity.util.ConnectionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment(),View.OnClickListener {

    lateinit var myTextView: TextView
    lateinit var auth:FirebaseAuth
    lateinit var shimmer: ShimmerFrameLayout
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var cardOne:CardView
    lateinit var cardTwo:CardView
    lateinit var cardThree:CardView
    lateinit var cardFour:CardView
    lateinit var cardFive:CardView
    lateinit var cardSix:CardView
    lateinit var rewardBanner:ImageView
    lateinit var cardNotes:CardView
    lateinit var cardDiscuss:CardView
    lateinit var cardEvents:CardView
    lateinit var cardNews:CardView
    lateinit var cardTeachers:CardView
    lateinit var refresh: SwipeRefreshLayout



    var count:Int=0
    val urlHashMap= hashMapOf<Int,String>(R.id.imgOne to "https://i.postimg.cc/P5SCx0cK/dfb492a7-6978-48a0-a5bf-99801b273d13.png",
        R.id.imgTwo to "https://i.postimg.cc/mZ91wXz2/discord.webp",
        R.id.imgThree to "https://i.postimg.cc/FHnNLvxW/nft-coding-6117176-4998601.webp",
        R.id.imgFour to "https://i.postimg.cc/c4Ng5Rw7/news.webp",
        R.id.imgFive to "https://i.ibb.co/pfZwBrN/funny-activity-3027486-2526705.webp",
        R.id.imgSix to "https://i.postimg.cc/MHHXbTtb/business-3d-341.png",
        R.id.gifRewardBanner to "https://i.ibb.co/W3wx3tC/Copy-of-banner-studybear-Portrait-2.gif",
        R.id.imgNotes to "https://i.postimg.cc/d0VjbQ9V/ezgif-com-gif-maker-10.gif",
        R.id.imgDiscuss to "https://i.postimg.cc/gkwnxxxG/Group-discussion.gif",
        R.id.imgEvents to "https://i.postimg.cc/xdp3nrsy/ezgif-com-gif-maker-8.gif",
        R.id.imgNews to "https://i.postimg.cc/dtG6dx1w/ezgif-com-gif-maker-11.gif",
        R.id.imgTeachers to "https://i.postimg.cc/K8VCpLmN/memee.png",
        )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val mAdView1 = view.findViewById<AdView>(R.id.adViewHomeFragment)
        val adRequest = AdRequest.Builder().build()
        mAdView1.loadAd(adRequest)
        myTextView = view.findViewById(R.id.txt_type_writter)
        auth=FirebaseAuth.getInstance()
        shimmer = view.findViewById(R.id.lytShimmer)
        refresh=view.findViewById(R.id.parentViewHome)
        refresh.setColorScheme(R.color.black);
        bottomNavigationView=(activity as MainActivity).findViewById(R.id.vwBottomNavigation)
        navigationView=(activity as MainActivity).findViewById(R.id.vwNavigation)
        drawerLayout=(activity as MainActivity).findViewById(R.id.lytDrawer)
        cardOne=view.findViewById(R.id.cardOne)
        cardTwo=view.findViewById(R.id.cardTwo)
        cardThree=view.findViewById(R.id.cardThree)
        cardFour=view.findViewById(R.id.cardFour)
        cardFive=view.findViewById(R.id.cardFive)
        cardSix=view.findViewById(R.id.cardSix)
        cardNotes=view.findViewById(R.id.cardNotes)
        cardDiscuss=view.findViewById(R.id.cardDiscuss)
        cardEvents=view.findViewById(R.id.cardEvents)
        cardNews=view.findViewById(R.id.cardNews)
        cardTeachers=view.findViewById(R.id.cardTeachers)
        rewardBanner=view.findViewById(R.id.gifRewardBanner)

        var handler: Handler = Handler()
        var runnable: Runnable? = null
        shimmer.startShimmer()
        myTextView.typeWrite(this,"Welcome, ${auth.currentUser?.displayName}",50L)
        glideImageLoader(view)

        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, 10)
            if(count==12)
            {
                shimmer.hideShimmer()
                count=0
                refresh.isRefreshing=false
                handler.removeCallbacks(runnable!!)

            }
        }.also { runnable = it }, 10)



        refresh.setOnRefreshListener(object:SwipeRefreshLayout.OnRefreshListener
            {
                override fun onRefresh() {
                    glideImageLoader(view)
                    handler.postDelayed(Runnable {
                        handler.postDelayed(runnable!!, 10)
                        if(count==12)
                        {
                            shimmer.hideShimmer()
                            count=0
                            refresh.isRefreshing=false
                            handler.removeCallbacks(runnable!!)
                        }
                    }.also { runnable = it }, 10)
                }
            })



        cardOne.setOnClickListener(this)
        cardTwo.setOnClickListener(this)
        cardThree.setOnClickListener(this)
        cardFour.setOnClickListener(this)
        cardFive.setOnClickListener(this)
        cardSix.setOnClickListener(this)
        cardNotes.setOnClickListener(this)
        cardDiscuss.setOnClickListener(this)
        cardEvents.setOnClickListener(this)
        cardNews.setOnClickListener(this)
        cardTeachers  .setOnClickListener(this)
//        myTextView.setOnClickListener(this)






        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun TextView.typeWrite(lifecycleOwner: LifecycleOwner, text: String, intervalMs: Long) {

        this@typeWrite.text = "Wassup buddy?"
        lifecycleOwner.lifecycleScope.launch {
            repeat(text.length) {
                delay(intervalMs)
                this@typeWrite.text = text.take(it + 1)
            }
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.cardOne ->
            {
                replaceFragment(NotesFragment(),"2","Notes",R.id.notes,R.id.bottom_notes,true)
            }
            R.id.cardTwo  ->
            {

                (activity as MainActivity).startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/mhrprvmFEt")))

            }
            R.id.cardThree ->
            {
                bottomNavigationView.menu.clear()
                bottomNavigationView.inflateMenu(R.menu.new_bottom_navigation_menu)
                replaceFragment(EventsFragment(),"6","Coding Events",R.id.coding_events,null,true)
                (activity as MainActivity).flagBottom=true
            }
            R.id.cardFour ->
            {

                bottomNavigationView.menu.clear()
                bottomNavigationView.inflateMenu(R.menu.new_bottom_navigation_menu)
                replaceFragment(NewsFragment(),"5","Technology news",R.id.news,null,true)
                (activity as MainActivity).flagBottom=true


            }
            R.id.cardFive  ->
            {
                bottomNavigationView.menu.clear()
                bottomNavigationView.inflateMenu(R.menu.new_bottom_navigation_menu)
                replaceFragment(MemeFragment(), "8", "Memes", R.id.memes, null, true)
                (activity as MainActivity).flagBottom=true
            }
            R.id.cardSix ->
            {

                drawerLayout.openDrawer(GravityCompat.START)
                navigationView.checkedItem?.setChecked(false)
            }

            R.id.cardNotes ->
            {
                replaceFragment(NotesFragment(),"2","Notes",R.id.notes,R.id.bottom_notes,true)
            }
            R.id.cardDiscuss  ->
            {
                (activity as MainActivity).startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/mhrprvmFEt")))
            }
            R.id.cardEvents ->
            {
                bottomNavigationView.menu.clear()
                bottomNavigationView.inflateMenu(R.menu.new_bottom_navigation_menu)
                replaceFragment(EventsFragment(),"6","Coding Events",R.id.coding_events,null,true)
                (activity as MainActivity).flagBottom=true

            }
            R.id.cardNews ->
            {
                bottomNavigationView.menu.clear()
                bottomNavigationView.inflateMenu(R.menu.new_bottom_navigation_menu)
                replaceFragment(NewsFragment(),"5","News",R.id.news,null,true)
                (activity as MainActivity).flagBottom=true

            }
            R.id.cardTeachers  ->
            {  bottomNavigationView.menu.clear()
                bottomNavigationView.inflateMenu(R.menu.new_bottom_navigation_menu)
                replaceFragment(MemeFragment(), "8", "Memes", R.id.memes, null, true)
                (activity as MainActivity).flagBottom=true
            }
        }
    }


    fun replaceFragment(fragment: Fragment, tag: String, title: String, id1: Int,id2:Int?,flag:Boolean) {
        if ((activity as MainActivity).flagBottom) {
            bottomNavigationView.menu.clear()
            bottomNavigationView.inflateMenu(R.menu.bottom_navigation_menu)
            (activity as MainActivity).flagBottom=false
        }
        (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.lytFrame, fragment, tag).commit()
        navigationView.setCheckedItem(id1)
        (activity as MainActivity).supportActionBar?.title=title
        navigationView.checkedItem?.isChecked = true
        if(id2!=null)
            bottomNavigationView.menu.findItem(id2).setChecked(flag)

    }


    fun glideImageLoader(view:View)
    {

        if(ConnectionManager().checkConnectivity(activity as Context)==true)
        {
            for(data in urlHashMap)
            {
                Glide.with(activity as Context)
                    .load(data.value)
                    .placeholder(R.drawable.placeholder)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }
                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            count++
                            return false
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.no_placeholder_new)
                    .into(view.findViewById(data.key))
            }
        }else{
            ConnectionManager().createDialog((activity as MainActivity).findViewById(R.id.lytCoordinator),activity as Context)
        }

    }



}


