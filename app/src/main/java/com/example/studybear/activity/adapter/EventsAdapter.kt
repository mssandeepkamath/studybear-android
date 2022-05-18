package com.example.studybear.activity.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.studybear.R
import com.example.studybear.activity.model.EventsDataClass
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList

class EventsAdapter(val context: Context,val itemArray:ArrayList<EventsDataClass>):RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {





    @RequiresApi(Build.VERSION_CODES.O)
    fun convertUTCtoLocalTime(utcDateString: String): String {
        val formatter: DateTimeFormatter = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)
            .withLocale(Locale.US)

        val formattedDateTime: String = Instant.parse(utcDateString)
            .atZone(ZoneId.systemDefault())
            .format(formatter)
        return formattedDateTime
    }

    class EventsViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val platform=view.findViewById<TextView>(R.id.txtEventPlatform)
        val name=view.findViewById<TextView>(R.id.txtEventName)
        val start=view.findViewById<TextView>(R.id.txtEventStart)
        val end=view.findViewById<TextView>(R.id.txtEventEnd)
        val url=view.findViewById<TextView>(R.id.txtLinkEvents)
        val imageView=view.findViewById<ImageView>(R.id.imgCalender)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.events_item,parent,false)
        return EventsViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        Glide.with(context).load("https://i.ibb.co/gDDm54z/add-event.png").placeholder(R.drawable.placeholder).diskCacheStrategy(
            DiskCacheStrategy.ALL).error(R.drawable.no_placeholder).into(holder.imageView)
        holder.platform.text=itemArray[position].platform
        holder.name.text=itemArray[position].name
        holder.start.text="Start: "+convertUTCtoLocalTime(itemArray[position].start)
        holder.end.text="End: "+convertUTCtoLocalTime(itemArray[position].end)
        holder.url.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse(itemArray[position].url)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
       return itemArray.size
    }
}