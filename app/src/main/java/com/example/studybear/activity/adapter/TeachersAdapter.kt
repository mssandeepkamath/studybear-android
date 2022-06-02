package com.example.studybear.activity.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.studybear.R
import com.example.studybear.activity.model.TeachersDataClass


class TeachersAdapter(val context: Context, val dataItem: ArrayList<TeachersDataClass>) :
    RecyclerView.Adapter<TeachersAdapter.TeacherViewHolder>() {

    class TeacherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val url = view.findViewById<ImageView>(R.id.imageUrl)
        val name = view.findViewById<TextView>(R.id.txtNameTeachers)
        val interest = view.findViewById<TextView>(R.id.txtInterest)
        val gmail = view.findViewById<ImageView>(R.id.imageGmail)
        val whatsapp = view.findViewById<ImageView>(R.id.imageWhatsapp)
        val phone = view.findViewById<ImageView>(R.id.imagePhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.teachers_item, parent, false)
        return TeacherViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {

        Glide.with(context).load(dataItem[position].url).placeholder(R.drawable.placeholder)
            .diskCacheStrategy(
                DiskCacheStrategy.ALL).error(R.drawable.no_placeholder_new).into(holder.url)
        Glide.with(context).load("https://i.ibb.co/hRFd69m/gmail.png")
            .placeholder(R.drawable.placeholder).diskCacheStrategy(
            DiskCacheStrategy.ALL).error(R.drawable.no_placeholder_new).into(holder.gmail)
        Glide.with(context).load("https://i.ibb.co/k97MbXj/whatsapp.png")
            .placeholder(R.drawable.placeholder).diskCacheStrategy(
            DiskCacheStrategy.ALL).error(R.drawable.no_placeholder_new).into(holder.whatsapp)
        Glide.with(context).load("https://i.ibb.co/37xPCfV/phone.png")
            .placeholder(R.drawable.placeholder).diskCacheStrategy(
            DiskCacheStrategy.ALL).error(R.drawable.no_placeholder_new).into(holder.phone)
        holder.name.text = "Name: " + dataItem[position].name.toString()
        holder.interest.text = "Specialization: " + dataItem[position].specialization.toString()

        holder.gmail.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${dataItem[position].gmail}"))
            context.startActivity(Intent.createChooser(emailIntent, "Chooser Title"))
        }
        holder.whatsapp.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=91 ${dataItem[position].phoneNumber}"
            if(dataItem[position].phoneNumber.toString().length==10)
            {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                context.startActivity(intent)
            }
            else
            {
                Toast.makeText(context,"Sorry, Cannot whatsapp\n${dataItem[position].name}",Toast.LENGTH_SHORT).show()
            }

        }

        holder.phone.setOnClickListener {

            if(dataItem[position].phoneNumber.toString().length==10)
            {
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:" + dataItem[position].phoneNumber)
                context.startActivity(dialIntent)
            }
            else
            {
                Toast.makeText(context,"Sorry, Cannot call\n${dataItem[position].name}",Toast.LENGTH_SHORT).show()
            }


        }

    }

    override fun getItemCount(): Int {
        return dataItem.size
    }
}