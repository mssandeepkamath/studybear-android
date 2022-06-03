package com.example.studybear.activity.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.studybear.R
import com.example.studybear.activity.activity.MainActivity
import com.example.studybear.activity.fragment.NotesFragmentThree
import com.example.studybear.activity.fragment.NotesFragmentTwo
import com.example.studybear.activity.model.CircularDataClass
import com.example.studybear.activity.model.NotesDataClass
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView

class CircularAdapter(
    var context: Context, var itemArray: ArrayList<CircularDataClass?>
) :
    RecyclerView.Adapter<CircularAdapter.CircularViewHolder>() {


    class CircularViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText = view.findViewById<TextView>(R.id.txtTitleCircular)
        val cardPdf=view.findViewById<CardView>(R.id.cardCircular)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircularViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.circulars_item, parent, false)
            return CircularViewHolder(view)
    }


    override fun onBindViewHolder(holder: CircularViewHolder, position: Int) {

           holder.nameText.text="Title: "+itemArray[position]?.name
            holder.cardPdf.setOnClickListener {
                val intent= Intent(Intent.ACTION_VIEW)
                intent.data= Uri.parse(itemArray[position]?.url)
                (context as Activity).startActivity(intent)
            }

    }

    override fun getItemCount(): Int {
            return itemArray.size
    }



}