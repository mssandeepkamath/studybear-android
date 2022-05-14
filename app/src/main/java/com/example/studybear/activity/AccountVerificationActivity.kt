package com.example.studybear.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.studybear.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AccountVerificationActivity : AppCompatActivity() {
    lateinit var myTextView: TextView
    lateinit var imageView: ImageView
    private lateinit var auth:FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var shimmer:ShimmerFrameLayout

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_verification)
        myTextView = findViewById(R.id.txt_type_writter_two)
        myTextView.typeWrite(this, "Verifying account..", 80L)
        imageView = findViewById(R.id.imgVerification)
        auth= FirebaseAuth.getInstance()
        shimmer=findViewById(R.id.lytShimmerTwo)

        shimmer.startShimmer()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        Glide.with(this).load("https://i.ibb.co/0FC4V4h/verification.gif")
            .placeholder(R.drawable.verfication_placeholder)
            .listener(object :RequestListener<Drawable>
            {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean,
                ): Boolean {
                  return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    shimmer.hideShimmer()
                    return false
                }

            })
            .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.no_placeholder).into(imageView)

        val current_user=auth.currentUser
        val email_id=current_user?.email
        Handler().postDelayed(
            {
                if(current_user==null)
                {
                    Toast.makeText(this,"Something went wrong..",Toast.LENGTH_LONG).show()
                    googleSignInClient.signOut()
                    current_user?.delete()
                    val intent=Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else if(email_id!!.contains("@rvce.edu.in",true))
                {
//            if(new_usert)
//            {
                    //TODO
//                //go to web view
//            }
                    Toast.makeText(this,"Welcome to Studybear",Toast.LENGTH_LONG).show()
                    val intent=Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    Toast.makeText(this,"Please use RVCE email id",Toast.LENGTH_LONG).show()
                    googleSignInClient.signOut()
                    current_user.delete()
                    Handler().postDelayed(
                        Runnable {
                            val intent=Intent(this,LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        },1000
                    )
                }
            },4000
        )
    }

    fun TextView.typeWrite(lifecycleOwner: LifecycleOwner, text: String, intervalMs: Long) {
        this@typeWrite.text = ""
        lifecycleOwner.lifecycleScope.launch {
            repeat(text.length) {
                delay(intervalMs)
                this@typeWrite.text = text.take(it + 1)
            }
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this,"Please wait..",Toast.LENGTH_SHORT).show()
    }


}