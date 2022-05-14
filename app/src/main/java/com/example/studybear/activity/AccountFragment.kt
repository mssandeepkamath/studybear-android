package com.example.studybear.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.studybear.R
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    lateinit var imageView: ImageView
    lateinit var name: TextView
    lateinit var email: TextView


    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_account, container, false)
        auth = FirebaseAuth.getInstance()
        imageView = view.findViewById(R.id.imgProfile)
        name = view.findViewById(R.id.txtName)
        email = view.findViewById(R.id.txtEmail)
        val current_user = auth.currentUser

        name.text = current_user?.displayName
        email.text = current_user?.email

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
        val emailId: String? = current_user?.email

//        button.setOnClickListener {
//            auth.signOut()
//            val intent= Intent(activity,LoginActivity::class.java)
//            startActivity(intent)
//            (activity as MainActivity).finish()
//        }


        return view
    }

}