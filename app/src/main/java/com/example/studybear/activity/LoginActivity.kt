package com.example.studybear.activity

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.studybear.R
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signInButton: CardView
    lateinit var progressBar: ProgressBar


    companion object {
        private const val Req_Code = 120
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        signInButton = findViewById(R.id.cardSignIn)
        progressBar=findViewById(R.id.barProgress)
        progressBarVisibility(R.color.white)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        signInButton.setOnClickListener {
            progressBarVisibility(R.color.red)//set visible
            signInGoogle()
        }


    }

    private fun signInGoogle() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        progressBarVisibility(R.color.white)
        if(requestCode== Req_Code)
        {
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)

                try
                {
                    val account=task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                    progressBarVisibility(R.color.red)
                }
                catch(e:ApiException)
                {
                    Log.d("LoginActivity", e.toString())
                }
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("SignInActivity", "signInWithCredential:success")
                    val intent = Intent(this, AccountVerificationActivity::class.java)
                    progressBarVisibility(R.color.white) //hide progress bar
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this,"Please check your connectivity!",Toast.LENGTH_SHORT).show()
                    progressBarVisibility(R.color.white)
                }
            }
    }


    fun progressBarVisibility(color:Int)
    {
        progressBar.indeterminateDrawable
            .setColorFilter(ContextCompat.getColor(this,color),PorterDuff.Mode.SRC_IN )
    }

}