package com.sandeep.studybear.activity.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sandeep.studybear.R
import com.sandeep.studybear.activity.activity.MainActivity
import com.sandeep.studybear.activity.adapter.CircularAdapter
import com.sandeep.studybear.activity.model.CircularDataClass
import com.sandeep.studybear.activity.util.ConnectionManager


class CircularFragment : Fragment() {

    private lateinit var database: DatabaseReference
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var errorText: TextView
    lateinit var refresh: SwipeRefreshLayout
    val itemArray = arrayListOf<CircularDataClass?>()
    lateinit var empty_box: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view= inflater.inflate(R.layout.fragment_circular, container, false)
        val mAdView1 = view.findViewById<AdView>(R.id.adView3)
        val adRequest = AdRequest.Builder().build()
        mAdView1.loadAd(adRequest)
        itemArray.clear()
        recyclerView = view.findViewById(R.id.vwRecyclerCircular)
        layoutManager = LinearLayoutManager(activity as MainActivity)
        progressBar = view.findViewById(R.id.barProgressCircular)
        progressLayout = view.findViewById(R.id.lytProgressCircular)
        empty_box = view.findViewById(R.id.imgCircular)
        refresh = view.findViewById(R.id.lytRefreshCircular)
        refresh.setColorSchemeColors(ContextCompat.getColor(activity as Context, R.color.blue),
            ContextCompat.getColor(activity as Context, R.color.red))
        errorText = view.findViewById(R.id.txtErrorCircular)
        progressLayout.visibility = View.VISIBLE
        errorText.visibility = View.GONE
        empty_box.visibility = View.GONE
        recyclerView.layoutManager = layoutManager
        database = Firebase.database.reference
        loadContents()

        refresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                itemArray.clear()
                recyclerView.adapter?.notifyDataSetChanged()
                progressLayout.visibility = View.VISIBLE
                progressBar.visibility = View.VISIBLE
                errorText.visibility = View.GONE
                Handler().postDelayed(
                    Runnable {
                        refresh.isRefreshing = false
                        loadContents()

                    }, 1000
                )
            }
        })

        return view
    }


    fun loadContents() {
        if (ConnectionManager().checkConnectivity(activity as MainActivity)) {
            database.child("branch").child("is").child("circulars")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val response = snapshot.value as HashMap<*, *>?
                        if (response != null) {
                            for (data in response) {
                                itemArray.add(CircularDataClass(data.key as String?,
                                    data.value as String?))
                            }

                            empty_box.visibility = View.GONE
                            progressLayout.visibility = View.GONE
                            recyclerView.adapter =
                                CircularAdapter(activity as MainActivity, itemArray)
                        }
                        else {
                            empty_box.visibility = View.VISIBLE
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        progressBar.visibility = View.GONE
                        errorText.visibility = View.VISIBLE
                        Toast.makeText(activity,"Sorry, Something went wrong",Toast.LENGTH_SHORT).show()
                    }

                })
        }
        else {
                        errorText.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        ConnectionManager().createDialog((activity as MainActivity).findViewById(R.id.lytCoordinator),
                            activity as MainActivity)
                    }
                }






}