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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.studybear.R
import com.example.studybear.activity.activity.MainActivity
import com.example.studybear.activity.fragment.NotesFragment
import com.example.studybear.activity.fragment.NotesFragmentThree
import com.example.studybear.activity.fragment.NotesFragmentTwo
import com.example.studybear.activity.model.DatabaseReferenceClass
import com.example.studybear.activity.model.NotesDataClass
import com.google.firebase.database.DatabaseReference
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView

class NotesAdapter(
    var context: Context, var itemArray: ArrayList<String>?,
    var id: Int, val semester: String?, val itemArrayThree: ArrayList<NotesDataClass>?,val unit:String?,val subject:String?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class ViewHolderOne(view: View) : RecyclerView.ViewHolder(view) {
        val titleText = view.findViewById<TextView>(R.id.txtNotesTitleOne)
        val spinner = view.findViewById<PowerSpinnerView>(R.id.spinnerUnits)
        val button = view.findViewById<Button>(R.id.btnNotesOne)
    }

    class ViewHolderTwo(view: View) : RecyclerView.ViewHolder(view) {
        val titleText = view.findViewById<TextView>(R.id.txtNotesTitleTwo)
        val card=view.findViewById<CardView>(R.id.cardTopics)
    }

    class ViewHolderThree(view: View) : RecyclerView.ViewHolder(view) {
        val nameText = view.findViewById<TextView>(R.id.txtNotesNameThree)
        val repuText = view.findViewById<TextView>(R.id.txtNotesReputationThree)
        val cardPdf=view.findViewById<CardView>(R.id.cardPdf)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        if (id == 1) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.notes_item_one, parent, false)
            return ViewHolderOne(view)

        } else if(id==2) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.notes_item_two, parent, false)
            return ViewHolderTwo(view)
        }
        else
        {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.notes_item_three, parent, false)
            return ViewHolderThree(view)

        }


    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (id==1) {
            val new_holder = holder as ViewHolderOne
            new_holder.titleText.text = itemArray?.get(position).toString()
            new_holder.spinner.setItems(R.array.Units)
            new_holder.spinner.lifecycleOwner = MainActivity()
            var unit: String? = null
            new_holder.spinner.setOnSpinnerItemSelectedListener(object :
                OnSpinnerItemSelectedListener<Any?> {
                override fun onItemSelected(
                    oldIndex: Int,
                    oldItem: Any?,
                    newIndex: Int,
                    newItem: Any?,
                ) {
                    unit = newItem as String?
                }

            })

            new_holder.button.setOnClickListener {
                if (unit == null) {
                    Toast.makeText(context, "Please choose a unit..", Toast.LENGTH_SHORT).show()
                } else {
                    val fragment= NotesFragmentTwo()
                    val args = Bundle()
                    args.putString("subjects", itemArray?.get(position).toString())
                    args.putString("units", unit!!)
                    args.putString("semester",semester)
                    fragment.arguments = args
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .addToBackStack("NotesOne")
                        .replace(R.id.lytFrame, fragment, "NotesTwo").commit()
                }

            }
        } else if(id==2){
            val new_holder = holder as ViewHolderTwo
            new_holder.titleText.text = itemArray?.get(position).toString()
            val fragment=NotesFragmentThree()
            val args=Bundle()
            args.putString("topic", itemArray?.get(position).toString())
            args.putString("subjects", subject)
            args.putString("units", unit)
            args.putString("semester",semester)
            fragment.arguments=args
            new_holder.card.setOnClickListener {
                (context as MainActivity).supportFragmentManager.beginTransaction().addToBackStack("NotesTwo").replace(R.id.lytFrame,fragment,"NotesThree").commit()
            }

        }
        else{

            val new_holder = holder as ViewHolderThree
            new_holder.nameText.text="Author's name: "+itemArrayThree?.get(position)?.name.toString()
            new_holder.repuText.text="Author's reputation: "+itemArrayThree?.get(position)?.reputation.toString()

            new_holder.cardPdf.setOnClickListener {
                val intent= Intent(Intent.ACTION_VIEW)
                intent.data= Uri.parse(itemArrayThree?.get(position)?.url.toString())
                (context as Activity).startActivity(intent)
            }


        }

    }

    override fun getItemCount(): Int {
        if(id==3)
        {
            return itemArrayThree!!.size.toInt()
        }
        else
        {
            return itemArray!!.size
        }

    }



}