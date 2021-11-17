package com.crys.codingtask.other

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import androidx.lifecycle.LiveData

class InternetChecker(private val context: Context) : LiveData<Boolean>() {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var connectServer: Boolean? = false
    private val receiveConnectivityManager = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            connectServer = false
            updateConnection()
            postValue(connectServer)
        }
    }
    init {
        updateConnection()
    }

    fun updateConnection(){
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        capabilities?.run {
            connectServer = capabilities.hasTransport(TRANSPORT_CELLULAR) || capabilities.hasTransport(TRANSPORT_WIFI)
        }
    }

    override fun onActive() {
        super.onActive()
        context.registerReceiver(
            receiveConnectivityManager,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        )
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(receiveConnectivityManager)
    }
}