package com.example.studybear.activity.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.studybear.R
import com.example.studybear.activity.model.EventsDataClass
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class EventsAdapter(val context: Context, val itemArray: ArrayList<EventsDataClass>) :
    RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {


    val urlHashMap =
        hashMapOf("Kick Start" to "https://i.ibb.co/m999QF1/google.png",
            "TopCoder" to "https://i.ibb.co/8YCc3x9/topcoder.png",
            "CodeForces" to "https://i.ibb.co/WWnmV1H/code-forces.png",
            "HackerRank" to "https://i.ibb.co/RCfD2Td/hackerrank.png",
            "LeetCode" to "https://i.ibb.co/RPc5L4V/leetcode-2.png",
            "AtCoder" to "https://i.ibb.co/wyNyYkX/atcoder.gif",
            "CodeChef" to "https://i.ibb.co/ySdj6ZS/codechef.png",
            "HackerEarth" to "https://i.ibb.co/JF9ZBsD/Hacker-Earth-logo.png")


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

    class EventsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val platform = view.findViewById<TextView>(R.id.txtEventPlatform)
        val name = view.findViewById<TextView>(R.id.txtEventName)
        val start = view.findViewById<TextView>(R.id.txtEventStart)
        val end = view.findViewById<TextView>(R.id.txtEventEnd)
        val url = view.findViewById<CardView>(R.id.cardEvents)
        val textViewCalender = view.findViewById<TextView>(R.id.txtCalender)
        val imageViewPlatform = view.findViewById<ImageView>(R.id.imgEventsPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.events_item, parent, false)
        return EventsViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val platform = itemArray[position].platform
        holder.platform.text = platform
        glideUrlToImage(urlHashMap[platform], holder.imageViewPlatform)
        holder.name.text = itemArray[position].name
        holder.start.text =convertUTCtoLocalTime(itemArray[position].start)
        holder.end.text = convertUTCtoLocalTime(itemArray[position].end)
        holder.url.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(itemArray[position].url)
            context.startActivity(intent)
        }
        val start=calenderdate("${itemArray[position].start.subSequence(0,10)}"+" "+itemArray[position].start.subSequence(11,19))
        val end=calenderdate("${itemArray[position].end.subSequence(0,10)}"+" "+itemArray[position].end.subSequence(11,19))
        holder.textViewCalender.setOnClickListener {
            addCalendarEvent( itemArray[position].platform,
                itemArray[position].name,
                start,
                end,
                itemArray[position].url)
        }

    }

    override fun getItemCount(): Int {
        return itemArray.size
    }

    fun glideUrlToImage(url: String?, image: ImageView) {
        Glide.with(context).load(url)
            .placeholder(R.drawable.placeholder).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.no_placeholder_new)
            .into(image)
    }

    fun addCalendarEvent(platform: String,name:String,start: Long, end: Long, url: String) {
        val intent = Intent(Intent.ACTION_INSERT)
        intent.type = "vnd.android.cursor.item/event"
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
        intent.putExtra(CalendarContract.Events.TITLE, platform+name);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Coding Contest by $platform");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, platform);
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");
        context.startActivity(intent)
    }


    @RequiresApi(Build.VERSION_CODES.O)

    fun calenderdate(date:String):Long
    {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        format.setTimeZone(TimeZone.getTimeZone("UTC"))

        val dateVar: Date = format.parse(date) as Date
        val millis = dateVar.time
        return millis
    }

}