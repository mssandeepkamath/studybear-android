package com.example.studybear.activity.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.studybear.R
import com.example.studybear.activity.activity.MainActivity
import com.example.studybear.activity.adapter.NotesAdapter
import com.example.studybear.activity.model.DatabaseReferenceClass
import com.example.studybear.activity.util.ConnectionManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class NotesFragmentTwo : Fragment() {
    private lateinit var database: DatabaseReference
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var errorText: TextView
    lateinit var refresh: SwipeRefreshLayout
    lateinit var appBar: AppBarLayout
    val itemArray= arrayListOf<String>()
    var myClass:DatabaseReferenceClass?=null
    lateinit var empty_box: ImageView
    lateinit var fab:FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_notes, container, false)
        appBar = (activity as MainActivity).findViewById(R.id.lytAppBar)
        appBar.setExpanded(true)
        itemArray.clear()
        recyclerView=view.findViewById(R.id.vwRecyclerNotesOne)
        layoutManager= LinearLayoutManager(activity as MainActivity)
        progressBar=view.findViewById(R.id.barProgressNotesOne)
        progressLayout=view.findViewById(R.id.lytProgressNotesOne)
        empty_box=view.findViewById(R.id.imgempty)
        refresh=view.findViewById(R.id.lytRefreshNotesOne)
        refresh.setColorSchemeColors(ContextCompat.getColor(activity as Context,R.color.blue),
            ContextCompat.getColor(activity as Context,R.color.red));
        errorText=view.findViewById(R.id.txtErrorNotesOne)
        progressLayout.visibility=View.VISIBLE
        errorText.visibility=View.GONE
        fab=view.findViewById(R.id.fab)
        recyclerView.layoutManager=layoutManager
        database = Firebase.database.reference
        val subject = arguments?.getString("subjects")
        val unit = arguments?.getString("units")
        val semester=arguments?.getString("semester")
        empty_box.visibility=View.GONE
        loadContents(subject.toString(),unit.toString(),semester)
        fab.visibility=View.GONE


        refresh.setOnRefreshListener(object :SwipeRefreshLayout.OnRefreshListener
        {

            override fun onRefresh() {
                itemArray.clear()
                recyclerView.adapter?.notifyDataSetChanged()
                progressLayout.visibility=View.VISIBLE
                progressBar.visibility=View.VISIBLE
                errorText.visibility=View.GONE
                Handler().postDelayed(
                    Runnable {
                        refresh.isRefreshing=false
                        loadContents(subject,unit,semester)

                    },1000
                )
            }
        })






        return view
    }




    fun loadContents(subject:String?,unit:String?,semester:String?) {

        if (ConnectionManager().checkConnectivity(activity as MainActivity)) {

            if(subject!=null && unit!=null && semester!=null)
            {
                val new_reference=database.child("branch").child("is").child(semester.toString())
                    .child("2018").child("subjects").child(subject.toString()).child("units").child(unit.toString())
                new_reference.addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        println("Response: is null")
                        if (snapshot.value != "")  {
                            val response = snapshot.value as HashMap<*, *>?
                            if (response != null) {
                                for (data in response) {
                                    itemArray.add(data.key.toString())
                                }
                            }
                            progressLayout.visibility = View.GONE
                            recyclerView.adapter =
                                NotesAdapter(activity as MainActivity,
                                    itemArray,
                                    2,
                                    semester,
                                    null,unit,subject)
                        }
                        else
                        {

                            empty_box.visibility=View.VISIBLE
                            progressBar.visibility = View.GONE

                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        progressBar.visibility = View.GONE
                        errorText.visibility = View.VISIBLE
                        println("Response: $error")
                    }


                })

            }


        }
        else {
            errorText.visibility=View.VISIBLE
            progressBar.visibility = View.GONE
            ConnectionManager().createDialog((activity as MainActivity).findViewById(R.id.lytCoordinator),activity as MainActivity)
        }
    }


}