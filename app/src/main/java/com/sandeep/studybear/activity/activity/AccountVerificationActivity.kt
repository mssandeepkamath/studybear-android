package com.sandeep.studybear.activity.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
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
import com.sandeep.studybear.R
import com.sandeep.studybear.activity.model.AccessControlInterface
import com.sandeep.studybear.activity.util.ConnectionManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AccountVerificationActivity : AppCompatActivity(), AccessControlInterface {
    lateinit var myTextView: TextView
    lateinit var imageView: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var database: DatabaseReference
    lateinit var shimmer: ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_verification)
        myTextView = findViewById(R.id.txt_type_writter_two)
        myTextView.typeWrite(this, "Verifying account..", 80L)
        imageView = findViewById(R.id.imgVerification)
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        shimmer = findViewById(R.id.lytShimmerTwo)
        shimmer.startShimmer()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        Glide.with(this).load("https://i.ibb.co/0FC4V4h/verification.gif")
            .placeholder(R.drawable.verification_placeholder)
            .listener(object : RequestListener<Drawable> {
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
            .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.no_placeholder_new)
            .into(imageView)

        val current_user = auth.currentUser
        val email_id = current_user?.email
        println("Email is : ${email_id.toString().trim().toLowerCase() == "teststudybear@gmail.com"}")
        Handler().postDelayed(
            {
                if(current_user==null)
                {
                    message(this@AccountVerificationActivity,"Something went wrong..")
                    googleSignInClient.signOut()
                    current_user?.delete()
                    intentProvider(this@AccountVerificationActivity,
                        LoginActivity::class.java)
                }
                else if(email_id.toString().trim().toLowerCase() == "teststudybear@gmail.com")
                {
                    intentProvider(this@AccountVerificationActivity,
                        MainActivity::class.java)
                }
                else if(email_id!!.contains("@rvce.edu.in",true))
                {
                    accessCheck(this,"Welcome to Studybear",database,current_user)
                }
                else
                {
                    message(this@AccountVerificationActivity,"Please use RVCE email id")
                    googleSignInClient.signOut()
                    current_user.delete()
                    Handler().postDelayed(
                        Runnable {

                            intentProvider(this@AccountVerificationActivity,
                              LoginActivity::class.java)

                        },1000
                    )
                }
            }, 4000
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
        message(this@AccountVerificationActivity,"Please wait..")

    }







}