package com.example.studybear.activity.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.studybear.R
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : Activity() {
    lateinit var screen: ViewGroup
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        //status bar color and visibilty code
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        auth=FirebaseAuth.getInstance()
        val current_user=auth.currentUser
        //handlers are used for intent

        android.os.Handler().postDelayed(
            {
                        if(current_user!=null)
                        {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else
                        {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

            }, 1000)
    }
}