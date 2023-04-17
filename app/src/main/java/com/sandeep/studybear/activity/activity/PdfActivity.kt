package com.sandeep.studybear.activity.activity

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import com.sandeep.studybear.R

 class PdfActivity : AppCompatActivity() {


     companion object
     {
         var urls: String?=null
         var uid: String?=null
         lateinit var pdfView: PDFView
         lateinit var dialog: ProgressDialog
         lateinit var handler:Handler
         var runnable: Runnable? = null
         lateinit var database:DatabaseReference
         lateinit var auth:FirebaseAuth
         lateinit var mAdView:AdView
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)
        MobileAds.initialize(this) {}
        pdfView =findViewById(R.id.vwPdf)
        mAdView = findViewById(R.id.adView_new1)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        dialog = ProgressDialog(this@PdfActivity)
        dialog.setMessage("Fetching...\nPlease wait if pdf remains blank")
        dialog.setCancelable(false)
        dialog.show()
        urls = intent.getStringExtra("url").toString()
        uid = intent.getStringExtra("uid").toString()
        println("Error in $uid")
        database=Firebase.database.reference
        auth= FirebaseAuth.getInstance()
        RetrivePdfStream().execute(urls)
         handler= Handler()
        runnable= Runnable {
            val ref= database.child("users").child(uid.toString()).child("totalviews")
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
        }
        handler.postDelayed(runnable!!, 120000)
    }

     override fun onDestroy() {
         handler.removeCallbacks(runnable!!)
         super.onDestroy()
     }


      class RetrivePdfStream : AsyncTask<String?, Void?, InputStream?>() {

             override fun onPostExecute(inputStream: InputStream?) {
                 pdfView.fromStream(inputStream).load()
                 Handler().postDelayed(
                     {
                         dialog.dismiss()
                     },10000
                 )
             }

             override fun doInBackground(vararg params: String?): InputStream? {
                 var inputStream: InputStream? = null
                 try {

                     val url = URL(params[0])
                     val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection

                     if (urlConnection.getResponseCode() == 200) {
                         inputStream = BufferedInputStream(urlConnection.getInputStream())
                     }
                 }
                 catch (e: IOException) {
                     return null
                 }
                 return inputStream
             }
         }




}
