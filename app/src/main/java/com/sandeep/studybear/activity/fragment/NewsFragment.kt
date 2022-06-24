package com.sandeep.studybear.activity.fragment

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sandeep.studybear.R
import com.sandeep.studybear.activity.activity.MainActivity
import com.sandeep.studybear.activity.adapter.NewsAdapter
import com.sandeep.studybear.activity.model.NewsDataClass
import com.sandeep.studybear.activity.util.ConnectionManager
import com.sandeep.studybear.activity.util.MySingleton
import org.json.JSONException
import org.json.JSONObject


class NewsFragment : Fragment() {

    lateinit var itemArray: ArrayList<NewsDataClass>
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var errorText:TextView
    lateinit var refresh:SwipeRefreshLayout
    lateinit var handler:Handler
    var runnable: Runnable? = null
   lateinit var database:DatabaseReference
   lateinit var auth:FirebaseAuth



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_news, container, false)
        recyclerView = view.findViewById(R.id.vwRecyclerView)
        layoutManager = LinearLayoutManager(activity as MainActivity)
        progressBar = view.findViewById(R.id.barProgress)
        progressLayout = view.findViewById(R.id.lytProgress)
        refresh = view.findViewById(R.id.lytRefresh)
        refresh.setColorSchemeColors(ContextCompat.getColor(activity as Context, R.color.blue),
            ContextCompat.getColor(activity as Context, R.color.red));
        errorText = view.findViewById(R.id.txtError)
        progressLayout.visibility = View.VISIBLE
        errorText.visibility = View.GONE
        auth= FirebaseAuth.getInstance()
        database= Firebase.database.reference
        itemArray = ArrayList()
        volleyGetRequest()
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
                        volleyGetRequest()


                    }, 1000
                )
            }
        })

       handler = Handler()
        runnable= Runnable {

            val ref = database.child("users").child(auth.currentUser?.uid.toString())
                .child("extrapoints")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var response = snapshot.value.toString().toInt()
                    response += 5
                    ref.setValue(response, object : DatabaseReference.CompletionListener {
                        override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {
                            if (error != null) {
                                println("Error in writting")
                            }
                        }
                    })
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Error in reading")
                }

            })

        }
        handler.postDelayed(runnable!!, 120000)



    return view

    }

    override fun onDestroyView() {
      handler.removeCallbacks(runnable!!)
        super.onDestroyView()

    }



    fun volleyGetRequest()
    {
        val url =  "https://newsapi.org/v2/everything?q=technology%20and%20IT&language=en&pageSize=100&sortBy=publishedAt&apiKey=1d439d1ca8b14f969e345c701e93b306"


        if (ConnectionManager().checkConnectivity(activity as MainActivity)){
            val jsonObjectRequest = object : JsonObjectRequest(
                Method.GET,
                url,
                null,
                { response ->
                    try {
                        val obj: JSONObject = response
                        if (response.getString("status") == "ok") {
                            progressLayout.visibility=View.GONE
                            val Itemarray = obj.getJSONArray("articles")
                            for (i in 0 until Itemarray.length()) {
                                if (Itemarray.getJSONObject(i) != null) {
                                    val jsonObject: JSONObject = Itemarray.getJSONObject(i)
                                    val title: String = jsonObject.getString("title")
                                    val description: String = jsonObject.getString("description")
                                    val urlImage: String = jsonObject.getString("urlToImage")
                                    val urlPost: String = jsonObject.getString("url")
                                    val date: String = jsonObject.getString("publishedAt")
                                    itemArray.add(NewsDataClass(title,
                                        description,
                                        urlImage,
                                        urlPost,
                                        date))
                                }
                            }
                            recyclerView.layoutManager = layoutManager
                            recyclerView.adapter = NewsAdapter(activity as MainActivity, itemArray)
                        } else {
                            progressBar.visibility=View.GONE
                            errorText.visibility=View.VISIBLE
                            ToastMessage("Some unexpected error occurred.")
                        }

                    } catch (e: JSONException) {
                        progressLayout.visibility = View.VISIBLE
                        progressBar.visibility=View.GONE
                        errorText.visibility=View.VISIBLE
                        ToastMessage("Some unexpected error occurred.")

                    }

                },
                { error ->
                    progressBar.visibility=View.GONE
                    errorText.visibility=View.VISIBLE
                    ToastMessage("Something went wrong..")
                    println("Error: $error")
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["User-Agent"] = "Mozilla/5.0"
                    return headers
                }
            }

            // add network request to volley queue
            MySingleton.getInstance((activity as MainActivity).applicationContext)
                .addToRequestQueue(jsonObjectRequest)

        } else {
            errorText.visibility=View.VISIBLE
            progressBar.visibility = View.GONE
            ConnectionManager().createDialog((activity as MainActivity).findViewById(R.id.lytCoordinator),activity as MainActivity)
        }

    }

    fun ToastMessage(message: String) {
        Toast.makeText(activity as MainActivity, message, Toast.LENGTH_SHORT).show()
    }


}

