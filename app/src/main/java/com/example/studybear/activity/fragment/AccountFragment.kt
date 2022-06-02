package com.example.studybear.activity.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.studybear.R
import com.example.studybear.activity.activity.LoginActivity
import com.example.studybear.activity.activity.MainActivity
import com.example.studybear.activity.util.ConnectionManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.coroutines.currentCoroutineContext


class AccountFragment : Fragment(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    lateinit var imageView: ImageView
    lateinit var name: TextView
    lateinit var email: TextView
    lateinit var points: RelativeLayout
    lateinit var aboutUs: RelativeLayout
    lateinit var reportBug: RelativeLayout
    lateinit var logOut: RelativeLayout
    lateinit var spinner: PowerSpinnerView
    lateinit var navigationView: NavigationView
    private lateinit var database:DatabaseReference
    lateinit var shimmer:ShimmerFrameLayout



    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_account, container, false)
        auth = FirebaseAuth.getInstance()
        database= Firebase.database.reference
        imageView = view.findViewById(R.id.imgProfile)
        name = view.findViewById(R.id.txtName)
        email = view.findViewById(R.id.txtEmail)
        points = view.findViewById(R.id.lytPoints)
        aboutUs = view.findViewById(R.id.lytAboutUs)
        reportBug = view.findViewById(R.id.lytReport)
        logOut = view.findViewById(R.id.lytLogOut)
        shimmer=view.findViewById(R.id.lytShimmerAccount)
        spinner = view.findViewById(R.id.spinnerSem)
        spinner.lifecycleOwner= MainActivity()//prevent memory leakage
        navigationView=(activity as MainActivity).findViewById(R.id.vwNavigation)
        val current_user = auth.currentUser
        name.text = current_user?.displayName
        email.text = current_user?.email
        shimmer.startShimmer()
       val ref= database.child("users").child(current_user!!.uid).child("semester")
           ref.addListenerForSingleValueEvent(object:ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                val response=snapshot.value.toString().toInt()
                spinner.selectItemByIndex(response-1)
                shimmer.hideShimmer()
            }

            override fun onCancelled(error: DatabaseError) {
              Toast.makeText(activity ,"Sorry, Something went wrong..",Toast.LENGTH_SHORT).show()
            }

        })



        Glide.with(activity as Context).load(current_user?.photoUrl)
            .placeholder(R.drawable.placeholder).listener(object : RequestListener<Drawable> {
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
                    return false
                }

            }).into(imageView)

    spinner.setOnSpinnerItemSelectedListener(object :OnSpinnerItemSelectedListener<Any?>
    {
        override fun onItemSelected(oldIndex: Int, oldItem: Any?, newIndex: Int, newItem: Any?) {
            ref.setValue((newIndex+1))
        }

    })
        logOut.setOnClickListener(this)
        aboutUs.setOnClickListener(this)
        reportBug.setOnClickListener(this)
        points.setOnClickListener(this) // TODO

        return view
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.lytPoints -> {
             //yet to implement
            }
            R.id.lytAboutUs -> {
                navigationView.checkedItem?.isChecked = true
                navigationView.setCheckedItem(R.id.about_us)
            }
            R.id.lytReport -> {
                navigationView.checkedItem?.isChecked = true
                navigationView.setCheckedItem(R.id.report_bug)
                val to = "teamstudybear@gmail.com"
                val subject = "I FOUND A BUG IN STUDYBEAR ANDROID APP!"
                val body = "Please clearly mention page name, bug description, and other useful details here."
                val mailTo = "mailto:" + to +
                        "?&subject=" + Uri.encode(subject) +
                        "&body=" + Uri.encode(body)
                val emailIntent = Intent(Intent.ACTION_VIEW)
                emailIntent.data = Uri.parse(mailTo)
                startActivity(emailIntent)
            }
            R.id.lytLogOut -> {
                auth.signOut()
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                (activity as MainActivity).finish()
            }
        }
    }


}