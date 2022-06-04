package com.example.studybear.activity.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.studybear.R
import com.example.studybear.activity.activity.PdfActivity.Companion.uid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class DiscussFragment : Fragment() {
    lateinit var webDisqus: WebView
    lateinit var handler: Handler
    var runnable: Runnable? = null
    lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_discuss, container, false)
        database= Firebase.database.reference
        auth= FirebaseAuth.getInstance()
        handler= Handler()
        handler.postDelayed(Runnable {
            val ref = database.child("users").child(auth.currentUser?.uid.toString())
                .child("extrapoints")
            ref.addListenerForSingleValueEvent(object: ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var response=snapshot.value.toString().toInt()
                    response+=1
                    ref.setValue(response,object :DatabaseReference.CompletionListener
                    {
                        override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {
                            if(error!=null)
                            {
                                println("Error in writting")
                            }
                        }
                    })
                }
                override fun onCancelled(error: DatabaseError) {
                    println("Error in reading")
                }

            })

        }, 120000)


        webDisqus = view.findViewById(R.id.vwDisqus)
        webDisqus.settings.javaScriptEnabled = true
        webDisqus.webViewClient = WebViewClient()


        return view
    }







}