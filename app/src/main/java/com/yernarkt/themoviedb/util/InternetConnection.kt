package com.yernarkt.themoviedb.util

import android.content.Context
import android.net.ConnectivityManager

object InternetConnection {
    /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT  */
    fun checkConnection(context: Context): Boolean {
        assert(context.getSystemService(Context.CONNECTIVITY_SERVICE) != null)
        return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo != null
    }
}