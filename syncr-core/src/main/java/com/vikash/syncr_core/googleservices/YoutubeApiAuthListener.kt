package com.vikash.syncr_core.googleservices

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential

interface YoutubeApiAuthListener {
    fun showGooglePlayServicesAvailabilityErrorDialog(statusCode : Int)
    fun networkError()
    fun launchIntentForPermissions(mCredential: GoogleAccountCredential)
    fun getPermissions()
    fun youtubeDataPermissionAvailable(mCredential: GoogleAccountCredential)
}