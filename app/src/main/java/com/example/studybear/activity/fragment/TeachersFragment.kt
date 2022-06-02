package com.example.studybear.activity.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.studybear.R
import com.example.studybear.activity.activity.MainActivity
import com.example.studybear.activity.adapter.NotesAdapter
import com.example.studybear.activity.adapter.TeachersAdapter
import com.example.studybear.activity.model.DatabaseReferenceClass
import com.example.studybear.activity.model.NotesDataClass
import com.example.studybear.activity.model.TeachersDataClass
import com.example.studybear.activity.model.UserDataClass
import com.example.studybear.activity.util.ConnectionManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TeachersFragment : Fragment() {

    private lateinit var database:DatabaseReference
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    private var dataItem= arrayListOf<TeachersDataClass>()
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var errorText: TextView
    lateinit var refresh: SwipeRefreshLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

         val view= inflater.inflate(R.layout.fragment_teachers, container, false)
        recyclerView = view.findViewById(R.id.vwRecyclerViewTeachers)
        layoutManager = LinearLayoutManager(activity as MainActivity)
        progressBar=view.findViewById(R.id.barProgressTeachers)
        progressLayout=view.findViewById(R.id.lytProgressTeachers)
        refresh=view.findViewById(R.id.lytRefreshTeachers)
        refresh.setColorSchemeColors(ContextCompat.getColor(activity as Context,R.color.blue),
            ContextCompat.getColor(activity as Context,R.color.red));
        errorText=view.findViewById(R.id.txtErrorTeachers)
        progressLayout.visibility=View.VISIBLE
        errorText.visibility=View.GONE
        database= Firebase.database.reference
        loadContents()



        refresh.setOnRefreshListener(object :SwipeRefreshLayout.OnRefreshListener
        {

            override fun onRefresh() {
                dataItem.clear()
                recyclerView.adapter?.notifyDataSetChanged()
                progressLayout.visibility=View.VISIBLE
                progressBar.visibility=View.VISIBLE
                errorText.visibility=View.GONE
                Handler().postDelayed(
                    Runnable {
                        refresh.isRefreshing=false
                        loadContents()
                    },1000
                )
            }
        })



        return view
    }


    private fun loadContents()
    {

        if (ConnectionManager().checkConnectivity(activity as MainActivity)) {
            database.child("branch").child("is").child("teachers").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val response = snapshot.value as HashMap<*, *>?
                    if (response != null) {
                        for (data in response) {
                            val item = data.value as HashMap<*, *>
                            dataItem.add(TeachersDataClass(item["url"].toString(),data.key.toString(),item["specialization"].toString(),item["email"].toString(),item["mobile"].toString()))
                        }
                    }
                    println("The array is: $dataItem")
                    progressLayout.visibility = View.GONE
                    recyclerView.layoutManager=layoutManager
                    recyclerView.adapter =TeachersAdapter(activity as Context,dataItem)

                }


                override fun onCancelled(error: DatabaseError) {
                    progressBar.visibility = View.GONE
                    errorText.visibility = View.VISIBLE
                    Toast.makeText(activity, "Sorry,Something went wrong", Toast.LENGTH_SHORT)
                        .show()
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

