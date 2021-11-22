package com.example.theatreapp.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Helpers {
    companion object{
        /*
        * returns: time value in format hour:min 24 hour format
        * */
        @JvmStatic
        fun getCurrentTime() : String{
            val date = Date()
            return SimpleDateFormat("HH:mm", Locale.ENGLISH).format(date)
        }
    }
}