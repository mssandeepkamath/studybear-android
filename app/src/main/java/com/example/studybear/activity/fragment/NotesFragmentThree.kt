package com.example.studybear.activity.fragment

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.studybear.R
import com.example.studybear.activity.activity.MainActivity
import com.example.studybear.activity.adapter.NotesAdapter
import com.example.studybear.activity.model.DatabaseReferenceClass
import com.example.studybear.activity.model.NotesDataClass
import com.example.studybear.activity.model.UserDataClass
import com.example.studybear.activity.util.ConnectionManager
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask


class NotesFragmentThree : Fragment() {

    private lateinit var database: DatabaseReference
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var errorText: TextView
    lateinit var refresh: SwipeRefreshLayout
    lateinit var appBar: AppBarLayout
    val itemArray = arrayListOf<NotesDataClass>()
    var myClass: DatabaseReferenceClass? = null
    var myUsersArray = hashMapOf<String, UserDataClass>()
    lateinit var empty_box: ImageView
    lateinit var fab: FloatingActionButton
    val PDF = 0
    lateinit var uri: Uri
    lateinit var storageReference: StorageReference
    var topic: String? = null
    var subject: String? = null
    var unit: String? = null
    var semester: String? = null
    lateinit var auth: FirebaseAuth
    var uid:String?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_notes, container, false)
        appBar = (activity as MainActivity).findViewById(R.id.lytAppBar)
        appBar.setExpanded(true)
        itemArray.clear()
        recyclerView = view.findViewById(R.id.vwRecyclerNotesOne)
        fab = view.findViewById(R.id.fab)
        layoutManager = LinearLayoutManager(activity as MainActivity)
        progressBar = view.findViewById(R.id.barProgressNotesOne)
        progressLayout = view.findViewById(R.id.lytProgressNotesOne)
        empty_box = view.findViewById(R.id.imgempty)
        refresh = view.findViewById(R.id.lytRefreshNotesOne)
        refresh.setColorSchemeColors(ContextCompat.getColor(activity as Context, R.color.blue),
            ContextCompat.getColor(activity as Context, R.color.red));
        errorText = view.findViewById(R.id.txtErrorNotesOne)
        progressLayout.visibility = View.VISIBLE
        errorText.visibility = View.GONE
        empty_box.visibility = View.GONE
        recyclerView.layoutManager = layoutManager
        database = Firebase.database.reference
        storageReference = FirebaseStorage.getInstance().reference
        auth = FirebaseAuth.getInstance()
        uid=auth.currentUser?.uid
        topic = arguments?.getString("topic")
        subject = arguments?.getString("subjects")
        unit = arguments?.getString("units")
        semester = arguments?.getString("semester")
        println("Response is4: ${topic},$subject,$unit,$semester")
        loadContents(topic.toString(), subject, unit, semester)

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
                        loadContents(topic.toString(), subject, unit, semester)

                    }, 1000
                )
            }
        })

        fab.setOnClickListener {
            val galleryIntent = Intent()
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            galleryIntent.type = "application/pdf"
            startActivityForResult(galleryIntent, 1)
        }


        return view
    }


    fun loadContents(topic: String, subject: String?, unit: String?, semester: String?) {
        if (ConnectionManager().checkConnectivity(activity as MainActivity)) {
            database.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val response = snapshot.value as HashMap<*, *>?
                    if (response != null) {
                        for (data in response) {
                            val item = data.value as HashMap<*, *>
                            myUsersArray.put(data.key.toString(),
                                UserDataClass(item["name"].toString(),
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    item["totaluploads"] as Long?,
                                    item["totalviews"] as Long?,
                                    item["extrapoints"] as Long?,
                                    null,
                                    null,
                                    null))
                        }

//                        println("Response is4: ${topic},$subject,$unit,$semester")
                        database.child("branch").child("is").child(semester.toString())
                            .child("2018").child("subjects").child(subject.toString())
                            .child("units").child(unit.toString()).child(topic)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.value != "") {
                                        val res = snapshot.value as HashMap<*, *>?
                                        println("Response is3: $res")
                                        if (res != null) {
                                            for (data in res) {
                                                val myUser: UserDataClass =
                                                    myUsersArray.get(data.key.toString())!!

                                                val points =
                                                    (myUser.extrapoints?.times(5)
                                                        ?: 0) + (myUser.totaluploads?.times(
                                                        10) ?: 0) + (myUser.totalviews?.times(2)
                                                        ?: 0)
                                                val reputation = if (points > 100) {
                                                    "High"
                                                } else if (points > 50) {
                                                    "Average"
                                                } else {
                                                    "Low"
                                                }
                                                itemArray.add(NotesDataClass(myUser.name,
                                                    reputation,
                                                    data.value.toString(),data.key.toString()))
                                            }
                                        }
                                        println("Response is4: ${itemArray}")
                                        empty_box.visibility = View.GONE
                                        progressLayout.visibility = View.GONE
                                        recyclerView.adapter =
                                            NotesAdapter(activity as MainActivity,
                                                null,
                                                3,
                                                null, itemArray, null, null)
                                    } else {
                                        empty_box.visibility = View.VISIBLE
                                        progressBar.visibility = View.GONE
                                    }

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    progressBar.visibility = View.GONE
                                    errorText.visibility = View.VISIBLE
                                    println("Response: $error")
                                }

                            })
                    } else {
                        errorText.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        ConnectionManager().createDialog((activity as MainActivity).findViewById(R.id.lytCoordinator),
                            activity as MainActivity)
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    progressBar.visibility = View.GONE
                    errorText.visibility = View.VISIBLE
                    Toast.makeText(activity, "Sorry,Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            })

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val dialog = ProgressDialog(activity as MainActivity)
            dialog.setMessage("Uploading..please wait")
            dialog.show()
            uri = data?.data!!
            val timestamp = "" + System.currentTimeMillis()
            val filepath = storageReference.child("$timestamp.pdf")
            filepath.putFile(uri)
                .continueWithTask(object : Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                    override fun then(p0: Task<UploadTask.TaskSnapshot>): Task<Uri> {
                        return filepath.downloadUrl
                    }

                }).addOnCompleteListener {
                database.child("branch").child("is").child(semester.toString())
                    .child("2018").child("subjects").child(subject.toString()).child("units")
                    .child(unit.toString()).child(topic.toString())
                    .child(auth.currentUser?.uid.toString())
                    .setValue(it.result.toString(), object : DatabaseReference.CompletionListener {
                        override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {
                            if (error != null) {
                                Toast.makeText(activity as MainActivity,
                                    "Upload failed, Please try again",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    })

                    val ref = database.child("users").child(auth.currentUser?.uid.toString())
                        .child("totaluploads")
                    ref.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var response = snapshot.value.toString().toInt()
                            response += 1
                            ref.setValue(response, object : DatabaseReference.CompletionListener {
                                override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {
                                    if (error != null) {
                                        println("Error in writting")
                                    }
                                    else
                                    {
                                        Toast.makeText(activity as MainActivity,
                                            "Uploaded Successfully",
                                            Toast.LENGTH_SHORT)
                                            .show()
                                        dialog.dismiss()
                                        itemArray.clear()
                                        recyclerView.adapter?.notifyDataSetChanged()
                                        loadContents(topic.toString(), subject, unit, semester)
                                    }
                                }
                            })
                        }

                        override fun onCancelled(error: DatabaseError) {
                            println("Error in reading")
                        }

                    })

            }

        }
    }


}




