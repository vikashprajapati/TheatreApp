package com.example.theatreapp.ui.fragment.streaming

import android.Manifest
import android.app.Activity
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.theatreapp.R
import com.example.theatreapp.databinding.FragmentSearchBinding
import com.example.theatreapp.ui.fragment.BaseBottomSheetFragment
import com.vikash.syncr_core.googleservices.AuthService
import com.vikash.syncr_core.googleservices.YoutubeApiAuthListener
import com.vikash.syncr_core.viewmodels.SearchBottomSheetViewModel
import pub.devrel.easypermissions.EasyPermissions
import kotlin.math.roundToInt
import android.content.SharedPreferences

import android.accounts.AccountManager
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Context

import android.content.Intent
import com.vikash.syncr_core.googleservices.AuthService.Companion.REQUEST_ACCOUNT_PICKER
import com.vikash.syncr_core.googleservices.AuthService.Companion.REQUEST_AUTHORIZATION
import com.vikash.syncr_core.googleservices.AuthService.Companion.REQUEST_GOOGLE_PLAY_SERVICES
import com.google.android.gms.common.GoogleApiAvailability





/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment :
    BaseBottomSheetFragment<FragmentSearchBinding, SearchBottomSheetViewModel>(),
    YoutubeApiAuthListener
{
    private var playerHeight: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerHeight = arguments?.getInt("height", 0)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding?.lifecycleOwner = this@SearchFragment
        binding?.viewModel = viewModel

        binding?.root?.layoutParams = ViewGroup.LayoutParams(
            requireActivity().displayMetrics.widthPixels,
            requireActivity().displayMetrics.heightPixels - playerHeight
        )

        return binding?.root
    }

    private val Activity.displayMetrics: DisplayMetrics
        get() {
            val displayMetrics = DisplayMetrics()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                display?.getRealMetrics(displayMetrics)
            } else {
                windowManager.defaultDisplay.getRealMetrics(displayMetrics)
            }

            return displayMetrics
        }

    private val Activity.screenSizeInDp: Point
        get() {
            val point = Point()

            displayMetrics.apply {
                point.let {
                    it.y = (heightPixels / density).roundToInt()
                    it.x = (widthPixels / density).roundToInt()
                }
            }

            return point
        }

    override fun observeData() {
        super.observeData()
        Log.i(TAG, "observeData: ${requireActivity().screenSizeInDp}")
        binding?.viewModel?.apply {
            invalidInput.observe(viewLifecycleOwner) {
                val msg = it.getContentIfNotHandledOrReturnNull() ?: return@observe
                shortToast(R.string.search_invalid_input)
            }
        }
    }

    override fun initViewModel(): SearchBottomSheetViewModel =
        ViewModelProvider(this).get(SearchBottomSheetViewModel::class.java)

    override fun getViewBinding(): FragmentSearchBinding =
        FragmentSearchBinding.inflate(layoutInflater)

    override fun showGooglePlayServicesAvailabilityErrorDialog(statusCode: Int) {
        val apiAvailability: GoogleApiAvailability = GoogleApiAvailability.getInstance()
        val dialog: Dialog = apiAvailability.getErrorDialog(
            this@SearchFragment,
            statusCode,
            REQUEST_GOOGLE_PLAY_SERVICES
        )!!
        dialog.show()
    }

    override fun youtubeDataPermissionAvailable(mCredential: com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential) {
        viewModel.youtubeDataAccessPermission = true
    }

    override fun getPermissions() {
        EasyPermissions.requestPermissions(
            this,
            "This app needs to access your Google account (via Contacts).",
            AuthService.REQUEST_PERMISSION_GET_ACCOUNTS,
            Manifest.permission.GET_ACCOUNTS
        )
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     * activity result.
     * @param data Intent (containing result data) returned by incoming
     * activity result.
     */
    /*override fun onActivityResult(
        requestCode: Int, resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GOOGLE_PLAY_SERVICES -> if (resultCode != RESULT_OK) {
                mOutputText.setText(
                    "This app requires Google Play Services. Please install " +
                            "Google Play Services on your device and relaunch this app."
                )
            } else {
                viewModel.youtubeDataAccessPermission = true
            }
            REQUEST_ACCOUNT_PICKER -> if (resultCode == RESULT_OK && data != null && data.extras != null) {
                val accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                if (accountName != null) {
                    val settings: SharedPreferences = getPreferences(Context.MODE_PRIVATE)
                    val editor = settings.edit()
                    editor.putString(PREF_ACCOUNT_NAME, accountName)
                    editor.apply()
                    mCredential.setSelectedAccountName(accountName)
                    getResultsFromApi()
                }
            }
            REQUEST_AUTHORIZATION -> if (resultCode == RESULT_OK) {
                getResultsFromApi()
            }
        }
    }*/

    override fun networkError() {
        shortToast(R.string.network_error)
    }

    override fun launchIntentForPermissions(mCredential: com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential) {
        startActivityForResult(
            mCredential.newChooseAccountIntent(),
            AuthService.REQUEST_ACCOUNT_PICKER
        )
    }

    companion object {
        val TAG: String = SearchFragment::class.java.canonicalName

        @JvmStatic
        fun newInstance() =
            SearchFragment()
    }
}