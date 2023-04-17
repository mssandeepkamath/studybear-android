package com.sandeep.studybear.activity.activity


import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sandeep.studybear.R
import com.sandeep.studybear.activity.model.AccessControlInterface
import com.sandeep.studybear.activity.util.ConnectionManager


class SplashActivity : Activity(), AccessControlInterface {
    lateinit var screen: ViewGroup
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
//    private val CHANNEL_ID = "i.apps.notifications"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        //status bar color and visibilty code
//        createNotificationChannel()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, com.sandeep.studybear.R.color.white)
        super.onCreate(savedInstanceState)
        setContentView(com.sandeep.studybear.R.layout.activity_splash)
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        val current_user = auth.currentUser
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

//        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(com.sandeep.studybear.R.drawable.company_logo)
//            .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
//                com.sandeep.studybear.R.drawable.flames))
//            .setContentTitle("+1 Check-in")
//            .setContentText("Happy learning!")
//            .setStyle(NotificationCompat.BigTextStyle()
//                .bigText("Learn all the concepts from hand written notes for free from Studybear, Got doubts? check out our server!, also get updated about coding events and technology news. WAIT! don't forget to watch some memes XD"))
//            .setPriority(NotificationCompat.PRIORITY_MAX)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)


//        with(NotificationManagerCompat.from(this)) {
//            // notificationId is a unique int for each notification that you must define
//            notify(1234, builder.build())
//        }



        //handlers are used for intent

        android.os.Handler().postDelayed(
            {
                if (current_user != null) {
                    if (ConnectionManager().checkConnectivity(this) == true)
                        accessCheck(this, null,database,current_user)
                    else
                        ConnectionManager().createDialog(findViewById(com.sandeep.studybear.R.id.bg_SplashActivity), this)
                } else {
                    intentProvider(this, LoginActivity::class.java)
                }

            }, 1000)
    }

//    private fun createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = getString(com.sandeep.studybear.R.string.channel_name)
//            val descriptionText = getString(com.sandeep.studybear.R.string.channel_description)
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
//                description = descriptionText
//            }
//            channel.enableVibration(true)
//            // Register the channel with the system
//            val notificationManager: NotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//
//        }
//
//
//    }


}