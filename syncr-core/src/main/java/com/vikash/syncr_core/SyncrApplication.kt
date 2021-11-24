package com.vikash.syncr_core

import android.app.Application
import com.google.gson.Gson

class SyncrApplication : Application() {
    private val TAG = SyncrApplication::class.java.canonicalName

    companion object{
        val gson: Gson
            get() = Gson()
    }
}