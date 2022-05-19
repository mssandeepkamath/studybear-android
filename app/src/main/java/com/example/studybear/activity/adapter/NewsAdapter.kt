package com.example.studybear.activity.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.example.studybear.R
import com.example.studybear.activity.model.NewsDataClass
import java.security.Signature


class NewsAdapter(val context: Context, val itemArrayList: ArrayList<NewsDataClass>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolderNews>() {
    class ViewHolderNews(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.txtNewsTitle)
        val description = view.findViewById<TextView>(R.id.txtNewsDescription)
        val image = view.findViewById<ImageView>(R.id.imgNewsPhoto)
        val date = view.findViewById<TextView>(R.id.txtNewsDate)
        val link = view.findViewById<TextView>(R.id.txtNewsLink)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNews {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return ViewHolderNews(view)
    }

    override fun onBindViewHolder(holder: ViewHolderNews, position: Int) {
        holder.title.text = itemArrayList[position].title
        holder.description.text = itemArrayList[position].description
        Glide.with(context).load(itemArrayList[position].urlImage)
            .placeholder(R.drawable.placeholder).diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.no_placeholder_new).into(holder.image)
        holder.date.text = itemArrayList[position].date.substring(0, 10)
        holder.link.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse(itemArrayList[position].urlPost)
            context.startActivity(browserIntent)
        }
    }

    override fun getItemCount(): Int {
        return itemArrayList.size
    }
}