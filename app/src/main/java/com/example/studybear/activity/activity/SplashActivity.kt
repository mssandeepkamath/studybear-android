package com.example.studybear.activity.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.studybear.R
import com.example.studybear.activity.model.AccessControlInterface
import com.example.studybear.activity.util.ConnectionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class SplashActivity : Activity(), AccessControlInterface {
    lateinit var screen: ViewGroup
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        //status bar color and visibilty code
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        val current_user = auth.currentUser


        //handlers are used for intent

        android.os.Handler().postDelayed(
            {
                if (current_user != null) {
                    if (ConnectionManager().checkConnectivity(this) == true)
                        accessCheck(this, null,database,current_user)
                    else
                        ConnectionManager().createDialog(findViewById(R.id.bg_SplashActivity), this)
                } else {
                    intentProvider(this, LoginActivity::class.java)
                }

            }, 1000)
    }


}