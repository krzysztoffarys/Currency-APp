package com.crys.codingtask.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

//the class with context, to provide information
class InfoProvider(private val context: Context) {
    fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                return true
            }
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                return true
            }
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                return true
            }
        }
        return false
    }

    fun getString(name: String) : String {
        val string = context.resources.getIdentifier(name, "string", context.packageName)
        return context.getString(string)
    }
}