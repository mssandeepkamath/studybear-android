package com.example.studybear.activity.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.studybear.R
import com.example.studybear.activity.activity.MainActivity
import com.example.studybear.activity.fragment.NotesFragmentTwo
import com.example.studybear.activity.model.DatabaseReferenceClass
import com.google.firebase.database.DatabaseReference
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView

class NotesAdapter(
    var context: Context, var itemArray: ArrayList<String>,
    var id: Int, val reference: DatabaseReference?,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class ViewHolderOne(view: View) : RecyclerView.ViewHolder(view) {
        val titleText = view.findViewById<TextView>(R.id.txtNotesTitleOne)
        val spinner = view.findViewById<PowerSpinnerView>(R.id.spinnerUnits)
        val button = view.findViewById<Button>(R.id.btnNotesOne)
    }

    class ViewHolderTwo(view: View) : RecyclerView.ViewHolder(view) {
        val titleText = view.findViewById<TextView>(R.id.txtNotesTitleTwo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (id == 1) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.notes_item_one, parent, false)
            return ViewHolderOne(view)

        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.notes_item_two, parent, false)
            return ViewHolderTwo(view)
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (id==1) {
            val new_holder = holder as ViewHolderOne
            new_holder.titleText.text = itemArray[position]
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
                    args.putString("subjects",itemArray[position])
                    args.putString("units", unit!!)
                    args.putSerializable("reference", DatabaseReferenceClass(reference))
                    fragment.arguments = args
                    println("Response: ${itemArray[position]} $unit $ DatabaseReferenceClass(reference) ")
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .addToBackStack("NotesOne")
                        .replace(R.id.lytFrame, fragment, "NotesTwo").commit()
                }

            }
        } else {
            val new_holder = holder as ViewHolderTwo
            new_holder.titleText.text = itemArray[position]
        }

    }

    override fun getItemCount(): Int {
        return itemArray.size
    }


}