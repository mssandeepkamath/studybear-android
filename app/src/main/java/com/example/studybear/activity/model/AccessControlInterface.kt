package com.example.studybear.activity.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.studybear.activity.activity.MainActivity
import com.example.studybear.activity.activity.RazorPayDataActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

interface AccessControlInterface {
    fun accessCheck(context: Context,message: String?,database:DatabaseReference,current_user:FirebaseUser)
    {
        database.child("users").child(current_user.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.value==null)
                    {
                        println(snapshot)
                        intentProvider(context, RazorPayDataActivity::class.java)
                    }
                    else
                    {
                        val response=snapshot.value as Map<*, *>
                        println(response)
                        if(response["paidbit"]==true)
                        {
                           message(context,message)
                            intentProvider(context, MainActivity::class.java)

                        }
                        else
                        {
                            intentProvider(context, RazorPayDataActivity::class.java)
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError)
                {
                    message(context,"Sorry, Something went wrong..")
                }

            })
    }

    fun message(context: Context, message:String?)
    {
        if(message!=null)
         Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }

    fun intentProvider(context: Context, destination:Class<*>)
    {
        val intent = Intent(context,destination)
        context.startActivity(intent)
        (context as Activity).finish()
    }
}