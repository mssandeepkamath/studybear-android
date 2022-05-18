package com.example.studybear.activity.fragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.studybear.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.bumptech.glide.request.target.Target
import com.example.studybear.activity.activity.MainActivity
import com.example.studybear.activity.util.ConnectionManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class HomeFragment : Fragment(),View.OnClickListener {

    lateinit var myTextView: TextView
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



    var count:Int=0
    val urlHashMap= hashMapOf<Int,String>(R.id.imgOne to "https://i.ibb.co/7XY8C5r/business-3d-girl-with-a-book-1-min.png",
        R.id.imgTwo to "https://i.ibb.co/88RpXrF/casual-life-3d-girl-chatting-remotely-with-group-of-three-people-min.png",
        R.id.imgThree to "https://i.ibb.co/DKSbNk8/business-3d-min.png",
        R.id.imgFour to "https://i.ibb.co/vJmFZpJ/business-3d-well-done-min.png",
        R.id.imgFive to "https://i.ibb.co/ssFrJX8/casual-life-3d-young-man-sitting-at-green-desk-and-raising-his-hand-min.png",
        R.id.imgSix to "https://i.ibb.co/CVXb3fh/business-3d-341-min.png",
        R.id.gifRewardBanner to "https://i.ibb.co/z8zBv99/banner-studybear-Portrait-5.gif",
        R.id.imgNotes to "https://i.ibb.co/8xRLRcr/notes.gif",
        R.id.imgDiscuss to "https://i.ibb.co/VYb00n7/discuss.gif",
        R.id.imgEvents to "https://i.ibb.co/C2Qgt8Z/coding-events.gif",
        R.id.imgNews to "https://i.ibb.co/vsrt6ZH/news.gif",
        R.id.imgTeachers to "https://i.ibb.co/wcRMxWs/teachers.gif",
        R.id.imgHeart to "https://i.ibb.co/bdfX8kH/3d-fluency-red-heart-min.png")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        if(ConnectionManager().checkConnectivity(activity as Context)==false)
        {
            ConnectionManager().createDialogRecycler(activity as Context)
        }
        myTextView = view.findViewById(R.id.txt_type_writter)
        shimmer = view.findViewById(R.id.lytShimmer)
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
        myTextView.typeWrite(this,"QUESTION OF THE DAY",50L)




        for(data in urlHashMap)
        {
            Glide.with(activity as Context)
                .load(data.value)
                .placeholder(R.drawable.placeholder)
                .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    Toast.makeText(activity,"Sorry,Failed to load contents!",Toast.LENGTH_SHORT).show()
                    return false
                }
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    count++
                    return false
                }
            })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.no_placeholder)
                .into(view.findViewById(data.key))
        }


        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, 10)
            if(count==13)
            {
                shimmer.hideShimmer()
                count=0
                handler.removeCallbacks(runnable!!)

            }
        }.also { runnable = it }, 10)

        cardOne.setOnClickListener(this)
        cardTwo.setOnClickListener(this)
        cardThree.setOnClickListener(this)
        cardFour.setOnClickListener(this)
        cardFive.setOnClickListener(this)
        cardSix.setOnClickListener(this)
        rewardBanner.setOnClickListener(this)
        cardNotes.setOnClickListener(this)
        cardDiscuss.setOnClickListener(this)
        cardEvents.setOnClickListener(this)
        cardNews.setOnClickListener(this)
        cardTeachers  .setOnClickListener(this)
        myTextView.setOnClickListener(this)


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

                replaceFragment(DiscussFragment(),"3","Discuss",R.id.discuss,R.id.bottom_discuss,true)

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
//                                replaceFragment(TeachersFragment(),"8","Teachers",R.id.teachers,null,true)

            }
            R.id.cardSix ->
            {

                drawerLayout.openDrawer(GravityCompat.START)
                navigationView.checkedItem?.setChecked(false)
            }
            R.id.gifRewardBanner ->
            {
                //                                replaceFragment(LeaderBoardFragment(),"8","Leaderboard",R.id.leaderboard,null,true)
            }
            R.id.cardNotes ->
            {
                replaceFragment(NotesFragment(),"2","Notes",R.id.notes,R.id.bottom_notes,true)
            }
            R.id.cardDiscuss  ->
            {
                replaceFragment(DiscussFragment(),"3","Discuss",R.id.discuss,R.id.bottom_discuss,true)
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
            {
                //                                replaceFragment(TeachersFragment(),"8","Teachers",R.id.teachers,null,true)

            }
            R.id.txt_type_writter->
            {
                Toast.makeText(activity,"Coming soon!",Toast.LENGTH_SHORT).show()
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



}

