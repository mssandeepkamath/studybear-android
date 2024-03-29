package com.sandeep.studybear.activity.util

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
import android.view.View
import com.google.android.material.snackbar.Snackbar

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

    fun createDialog(view:View,context:Context) {
        val snack = Snackbar.make(view,"No internet connection!",5000)

        snack.setAction("Open settings", View.OnClickListener {
            val intent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
            context.startActivity(intent)
        })
        snack.show()
    }

}