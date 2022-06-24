package com.sandeep.studybear.activity.fragment

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.ShimmerFrameLayout
import com.sandeep.studybear.R
import com.sandeep.studybear.activity.activity.MainActivity
import com.sandeep.studybear.activity.util.ConnectionManager


class AboutUsFragment : Fragment(),View.OnClickListener {

    val urlHashMap= hashMapOf<Int,String>(
        R.id.imgRakshith to "https://i.ibb.co/k3w74vD/rakshith-min.png",
        R.id.imgSandeep to "https://i.ibb.co/NLMXRBN/sandeep-min.png",
        R.id.instagramrakshith to "https://i.ibb.co/7KhMg04/instagram-min.png",
        R.id.imageWhatsappRakshith to "https://i.ibb.co/k97MbXj/whatsapp.png",
        R.id.imageLinkedInRakshith to "https://i.ibb.co/nR2D5j9/linkedin-min.png",
        R.id.instagramSandeep to "https://i.ibb.co/7KhMg04/instagram-min.png",
        R.id.imageWhatsappSandeep to "https://i.ibb.co/k97MbXj/whatsapp.png",
        R.id.imageLinkedInSandeep to "https://i.ibb.co/nR2D5j9/linkedin-min.png",
    )
    lateinit var shimmer: ShimmerFrameLayout
    var count:Int=0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view= inflater.inflate(R.layout.fragment_about_us, container, false)
        shimmer = view.findViewById(R.id.lytShimmerAboutUs)
        shimmer.startShimmer()
        glideImageLoader(view)
        var handler: Handler = Handler()
        var runnable: Runnable? = null
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, 10)
            if(count==8)
            {
                shimmer.hideShimmer()
                count=0
                handler.removeCallbacks(runnable!!)
            }
        }.also { runnable = it }, 10)

        view.findViewById<ImageView>(R.id.instagramrakshith).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageWhatsappRakshith).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageLinkedInRakshith).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.instagramSandeep).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageWhatsappSandeep).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageLinkedInSandeep).setOnClickListener(this)


        return view
    }

    fun glideImageLoader(view:View)
    {

        if(ConnectionManager().checkConnectivity(activity as Context)==true)
        {
            for(data in urlHashMap)
            {
                Glide.with(activity as Context)
                    .load(data.value)
                    .placeholder(R.drawable.placeholder)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }
                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            count++
                            return false
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.no_placeholder_new)
                    .into(view.findViewById(data.key))
            }
        }else{
            ConnectionManager().createDialog((activity as MainActivity).findViewById(R.id.lytCoordinator),activity as Context)
        }

    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.instagramrakshith->
            {
                val uri: Uri = Uri.parse("http://instagram.com/_u/rakshith_dhegde_")
                val likeIng = Intent(Intent.ACTION_VIEW, uri)
                likeIng.setPackage("com.instagram.android")

                try {
                    startActivity(likeIng)
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/rakshith_dhegde_")))
                }
            }
            R.id.imageWhatsappRakshith->
            {
                val url = "https://api.whatsapp.com/send?phone=91 6362497977 "
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                (activity as MainActivity).startActivity(intent)
            }
            R.id.imageLinkedInRakshith->
            {
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/rakshith-hegde-74ab3a226/"))
                val packageManager = activity?.packageManager
                val list = packageManager?.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY)
                if (list != null) {
                    if (list.isEmpty()) {
                        intent = Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.linkedin.com/in/rakshith-hegde-74ab3a226/"))
                    }
                }
                startActivity(intent)
            }
            R.id.instagramSandeep->
            {
                val uri = Uri.parse("http://instagram.com/_u/_msandeep_kamath_")
                val likeIng = Intent(Intent.ACTION_VIEW, uri)
                likeIng.setPackage("com.instagram.android")

                try {
                    startActivity(likeIng)
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/_msandeep_kamath_")))
                }
            }
            R.id.imageWhatsappSandeep->
            {
                val url = "https://api.whatsapp.com/send?phone=91 8618743756"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                (activity as MainActivity).startActivity(intent)

            }
            R.id.imageLinkedInSandeep->
            {
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/m-s-sandeep-kamath-296913233/"))
                val packageManager = activity?.packageManager
                val list = packageManager?.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY)
                if (list != null) {
                    if (list.isEmpty()) {
                        intent = Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.linkedin.com/in/m-s-sandeep-kamath-296913233/"))
                    }
                }
                startActivity(intent)
            }

        }

    }


}