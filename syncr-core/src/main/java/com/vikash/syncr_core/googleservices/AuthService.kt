package com.vikash.syncr_core.googleservices

import android.Manifest
import android.content.Context
import com.google.android.gms.common.ConnectionResult

import com.google.android.gms.common.GoogleApiAvailability
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.youtube.YouTubeScopes
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class AuthService(val context : Context, val listener : YoutubeApiAuthListener) {

    // Initialize credentials and service object.
    val mCredential = GoogleAccountCredential.usingOAuth2(
        context, listOf(YouTubeScopes.YOUTUBE_READONLY)
    ).setBackOff(ExponentialBackOff());

    private fun getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices()
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount()
        }else {
            listener.youtubeDataPermissionAvailable(mCredential)
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(Companion.REQUEST_PERMISSION_GET_ACCOUNTS)
    private fun chooseAccount() {
        if (EasyPermissions.hasPermissions(context, Manifest.permission.GET_ACCOUNTS)) {
            val accountName: String = "Vikash"
            if (accountName != null) {
                mCredential.selectedAccountName = accountName
                listener.youtubeDataPermissionAvailable(mCredential)
            } else {
                // Start a dialog from which the user can choose an account
                listener.launchIntentForPermissions(mCredential)
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            listener.getPermissions()
        }
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private fun isGooglePlayServicesAvailable(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context)
        return connectionStatusCode == ConnectionResult.SUCCESS
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private fun acquireGooglePlayServices() {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context)
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            listener.showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode)
        }
    }

    companion object {
        const val REQUEST_ACCOUNT_PICKER = 1000
        const val REQUEST_GOOGLE_PLAY_SERVICES = 1002
        const val REQUEST_AUTHORIZATION = 1001
        const val REQUEST_PERMISSION_GET_ACCOUNTS = 1003
    }
}