package com.sandeep.studybear.activity.fragment

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.android.material.snackbar.Snackbar
import com.sandeep.studybear.R
import com.sandeep.studybear.activity.activity.MainActivity
import com.sandeep.studybear.activity.util.ConnectionManager
import com.sandeep.studybear.activity.util.MySingleton


class MemeFragment : Fragment(){

    var cururl:String?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view=inflater.inflate(R.layout.fragment_meme, container, false)
        val progress=view.findViewById<ProgressBar>(R.id.progress)
        val iv=view.findViewById<ImageView>(R.id.imageView)
        val next=view.findViewById<Button>(R.id.next)
        val share=view.findViewById<Button>(R.id.share)
        loadMeme(view,progress,iv)

        next.setOnClickListener {
            loadMeme(view,progress,iv)
        }

        share.setOnClickListener {
            val intent= Intent(Intent.ACTION_SEND)
            intent.type="text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,"Have a look at this meme from Studybear X Redit $cururl")
            val chooser=Intent.createChooser(intent,"Share this meme using....")
            startActivity(chooser)
        }

        return view
    }

    private fun loadMeme(view:View,progress:ProgressBar,iv:ImageView)
    {


        progress.visibility=View.VISIBLE

        val url = "https://meme-api.herokuapp.com/gimme/wholesomememes"
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
                cururl=response.getString("url")

                if(ConnectionManager().checkConnectivity(activity as Context)==true)
                {

                        Glide.with(activity as Context)
                            .load(cururl)
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                    val snack = Snackbar.make((activity as MainActivity).findViewById(R.id.lytCoordinator),"Sorry, couldn't load. Please try again",5000)
                                    snack.setAction("Okay", View.OnClickListener {
                                        snack.dismiss()
                                    })
                                    snack.show()
                                    return false
                                }
                                override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                    progress.visibility=View.GONE
                                    return false
                                }
                            })
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.no_placeholder_new)
                            .into(iv)

                }else{
                    ConnectionManager().createDialog((activity as MainActivity).findViewById(R.id.lytCoordinator),activity as Context)
                }


            },
            {

            })


        MySingleton.getInstance((activity as MainActivity).applicationContext)
            .addToRequestQueue(jsonObjectRequest)

    }









}