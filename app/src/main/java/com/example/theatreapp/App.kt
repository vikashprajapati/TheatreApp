package com.example.theatreapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.vikash.syncr_core.data.connections.SocketManager
import com.google.gson.Gson

class App : Application(), Application.ActivityLifecycleCallbacks {

    private val TAG = App::class.java.canonicalName
    private var activityCount = 0

    companion object{
        val gson: Gson
            get() = Gson()
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }



    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.i(TAG, "onActivityCreated: $activityCount")
        activityCount++
        Log.i(TAG, "onActivityCreated: $activityCount")
    }

    override fun onActivityStarted(activity: Activity) {
        
    }

    override fun onActivityResumed(activity: Activity) {
        
    }

    override fun onActivityPaused(activity: Activity) {
        
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.i(TAG, "onActivityDestroyed: $activityCount")
        if(--activityCount == 0 && SocketManager.isSocketConnected()){
            SocketManager.stopListeningToServer()
        }
        Log.i(TAG, "onActivityDestroyed: $activityCount")
    }
}