package com.example.studybear.activity.fragment

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
import com.example.studybear.R
import com.example.studybear.activity.activity.MainActivity
import com.example.studybear.activity.adapter.NotesAdapter
import com.example.studybear.activity.util.ConnectionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class NotesFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var database: DatabaseReference
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var errorText: TextView
    lateinit var refresh: SwipeRefreshLayout
    lateinit var empty_box: ImageView
    val itemArray= arrayListOf<String>()
    lateinit var fab: FloatingActionButton

    private lateinit var auth:FirebaseAuth
    var semester:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view= inflater.inflate(R.layout.fragment_notes, container, false)
        recyclerView=view.findViewById(R.id.vwRecyclerNotesOne)
        layoutManager=LinearLayoutManager(activity as MainActivity)
        progressBar=view.findViewById(R.id.barProgressNotesOne)
        progressLayout=view.findViewById(R.id.lytProgressNotesOne)
        refresh=view.findViewById(R.id.lytRefreshNotesOne)
        empty_box=view.findViewById(R.id.imgempty)
        refresh.setColorSchemeColors(ContextCompat.getColor(activity as Context,R.color.blue),
            ContextCompat.getColor(activity as Context,R.color.red));
        errorText=view.findViewById(R.id.txtErrorNotesOne)
        progressLayout.visibility=View.VISIBLE
        errorText.visibility=View.GONE
        itemArray.clear()
        fab=view.findViewById(R.id.fab)
        database= Firebase.database.reference
        auth= FirebaseAuth.getInstance()
        recyclerView.layoutManager=layoutManager
        empty_box.visibility=View.GONE
        loadContents()
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
                         loadContents()


                    },1000
                )
            }
        })




        return view
    }



    fun loadContents() {
        val current_user = auth.currentUser
        val uid = current_user!!.uid
        if (ConnectionManager().checkConnectivity(activity as MainActivity)) {
            database.child("users").child(uid).child("semester")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        semester = snapshot.value.toString()
                        val reference =
                            database.child("branch").child("is").child(semester.toString())
                                .child("2018").child("subjects")
                        reference.addListenerForSingleValueEvent(
                            object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.value != "")  {
                                        val response = snapshot.value as Map<*, *>?
                                        if (response != null) {
                                            for (data in response) {
                                                itemArray.add(data.key.toString())
                                            }
                                        }
                                        progressLayout.visibility = View.GONE
                                        recyclerView.adapter =
                                            NotesAdapter(activity as Context,
                                                itemArray,
                                                1,
                                                semester!!,
                                                null,null,null)

                                    }
                                    else
                                    {

                                            empty_box.visibility=View.VISIBLE

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    progressBar.visibility = View.GONE
                                    errorText.visibility = View.VISIBLE
                                    Toast.makeText(activity,
                                        "Sorry, error occurred",
                                        Toast.LENGTH_SHORT).show()
                                }

                            }
                        )
                    }

                    override fun onCancelled(error: DatabaseError) {
                        progressBar.visibility = View.GONE
                        errorText.visibility = View.VISIBLE
                        Toast.makeText(activity, "Sorry, error occurred", Toast.LENGTH_SHORT).show()
                    }
                })

        } else {
            errorText.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            ConnectionManager().createDialog((activity as MainActivity).findViewById(R.id.lytCoordinator),
                activity as MainActivity)
        }

    }


}