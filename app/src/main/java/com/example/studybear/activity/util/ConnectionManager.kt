package com.example.studybear.activity.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.audiofx.BassBoost
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.studybear.R
import com.example.studybear.activity.activity.MainActivity

class ConnectionManager {


    fun checkConnectivity(context: Context):Boolean
    {
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork:NetworkInfo?=connectivityManager.activeNetworkInfo

        if(activeNetwork?.isConnected != null)
        {
            return activeNetwork.isConnected
        }
        else
        {
            return false
        }


    }

    fun createDialog(context:Context) {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(context)
            .setTitle("Important:")
            .setMessage("You are not connected to the Internet!")
            .setIcon(R.drawable.company_logo)
            .setPositiveButton("Okay", DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
            }
            )
            .create()
            .show()
    }

    fun createDialogRecycler(context:Context) {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(context)
            .setTitle("Important:")
            .setMessage("You are not connected to the Internet!")
            .setIcon(R.drawable.company_logo)
            .setPositiveButton("Open settings", DialogInterface.OnClickListener { dialog, _ ->
              val intent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
              context.startActivity(intent)
            }
            )
            .setNegativeButton("Cancel",DialogInterface.OnClickListener { dialog, _->
             dialog.dismiss()
            })
            .create()
            .show()
    }
}