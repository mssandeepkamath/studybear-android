package com.example.studybear.activity.activity

import android.app.ProgressDialog
import android.net.wifi.WifiConfiguration.GroupCipher.strings
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.studybear.R
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.scroll.ScrollHandle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

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
     }


    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
//        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        pdfView =findViewById(R.id.vwPdf)
        dialog = ProgressDialog(this@PdfActivity)
        dialog.setMessage("Fetching...")
        dialog.show()
        urls = intent.getStringExtra("url").toString()
        uid = intent.getStringExtra("uid").toString()
        println("Error in $uid")
        database=Firebase.database.reference
        auth= FirebaseAuth.getInstance()
        RetrivePdfStream().execute(urls)
         handler= Handler()
        handler.postDelayed(Runnable {
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

        }, 300000)
    }


      class RetrivePdfStream : AsyncTask<String?, Void?, InputStream?>() {

             override fun onPostExecute(inputStream: InputStream?) {
                 println("The response $inputStream")
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