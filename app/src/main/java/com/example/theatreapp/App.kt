package com.example.theatreapp

import android.app.Application
import com.google.gson.Gson

class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    companion object{
        val gson: Gson
            get() = Gson()
    }
}