package com.example.studybear.activity.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.example.studybear.R
import com.example.studybear.activity.activity.MainActivity
import com.example.studybear.activity.adapter.EventsAdapter
import com.example.studybear.activity.model.EventsDataClass
import com.example.studybear.activity.util.ConnectionManager
import com.example.studybear.activity.util.MySingleton
import org.json.JSONException


class EventsFragment : Fragment() {


    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var itemArray: ArrayList<EventsDataClass>
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var errorTextView: TextView
    lateinit var refresh: SwipeRefreshLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_events, container, false)
        recyclerView = view.findViewById(R.id.vwRecyclerEvents)
        progressLayout = view.findViewById(R.id.lytProgressEvents)
        progressBar = view.findViewById(R.id.barProgressEvents)
        errorTextView = view.findViewById(R.id.txtErrorEvents)
        refresh = view.findViewById(R.id.lytRefreshEvents)
        layoutManager = GridLayoutManager(activity,2)
        itemArray = ArrayList()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = EventsAdapter(activity as Context, itemArray)
        errorTextView.visibility = View.GONE
        progressLayout.visibility = View.VISIBLE
        refresh.setColorSchemeColors(ContextCompat.getColor(activity as Context,R.color.blue),
            ContextCompat.getColor(activity as Context,R.color.red));
        volleyJsonObjectRequest()


        refresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {

            override fun onRefresh() {
               itemArray.clear()
                recyclerView.adapter?.notifyDataSetChanged()
                progressLayout.visibility=View.VISIBLE
                Handler().postDelayed(
                    Runnable {
                           refresh.isRefreshing=false
                             volleyJsonObjectRequest()
                        progressLayout.visibility=View.GONE
                    }, 1000)

            }

        })

        return view
    }

    fun volleyJsonObjectRequest() {
        val url = "https://kontests.net/api/v1/all"
        if (ConnectionManager().checkConnectivity(activity as MainActivity)) {
            val jsonArrayRequest =
                JsonArrayRequest(Request.Method.GET, url, null, {
                    try {
                        progressLayout.visibility = View.GONE
                        val JSONArray = it
                        for (i in 0 until JSONArray.length()) {
                            val jsonObject = JSONArray.getJSONObject(i)
                            if (jsonObject != null) {
                                val platform = jsonObject.getString("site")
                                val name = jsonObject.getString("name")
                                val start ="${jsonObject.getString("start_time").subSequence(0,10)}"+"T"+jsonObject.getString("start_time").subSequence(11,19)+"Z"
                                val end = "${jsonObject.getString("end_time").subSequence(0,10)}"+"T"+jsonObject.getString("end_time").subSequence(11,19)+"Z"
                                val url = jsonObject.getString("url")
                                itemArray.add(EventsDataClass(platform, url, name, start, end))
                            }
                            recyclerView.layoutManager = layoutManager
                            recyclerView.adapter = EventsAdapter(activity as Context, itemArray)
                        }
                    } catch (e: JSONException) {
                        progressLayout.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        errorTextView.visibility = View.VISIBLE
                        ToastMessage("Some unexpected error occurred.")
                    }
                }, {
                    progressBar.visibility = View.GONE
                    errorTextView.visibility = View.VISIBLE
                })
            MySingleton.getInstance((activity as MainActivity).applicationContext)
                .addToRequestQueue(jsonArrayRequest)
        }else
        {
            ConnectionManager().createDialogRecycler(activity as MainActivity)
        }
    }


    fun ToastMessage(message: String) {
        Toast.makeText(activity as MainActivity, message, Toast.LENGTH_SHORT).show()
    }


}