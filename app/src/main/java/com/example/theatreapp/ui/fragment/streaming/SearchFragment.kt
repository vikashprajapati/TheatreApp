package com.example.theatreapp.ui.fragment.streaming

import android.app.Activity
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.theatreapp.R
import com.example.theatreapp.databinding.FragmentSearchBinding
import com.example.theatreapp.ui.adapters.YoutubeSearchResultsAdapter
import com.example.theatreapp.ui.fragment.BaseBottomSheetFragment
import com.vikash.syncr_core.data.models.response.youtube.searchResults.VideosItem
import com.vikash.syncr_core.data.models.videoplaybackevents.NewVideoSelected
import com.vikash.syncr_core.utils.SearchResultAdapterListener
import com.vikash.syncr_core.viewmodels.SearchFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SearchFragment :
    BaseBottomSheetFragment<FragmentSearchBinding, SearchFragmentViewModel>(),
    SearchResultAdapterListener {
    private val searchViewModel : SearchFragmentViewModel by viewModels()

    private val searchResultsAdapter = YoutubeSearchResultsAdapter(arrayListOf(), this)
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

        val statusBarHeight = requireActivity().getStatusHeight
        binding?.root?.layoutParams = ViewGroup.LayoutParams(
            requireActivity().displayMetrics.widthPixels,
            requireActivity().displayMetrics.heightPixels - playerHeight - statusBarHeight as Int
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

    private val Activity.getStatusHeight : Int
        get() = resources.run {
            val id = getIdentifier("status_bar_height", "dimen", "android")
            if(id > 0) return getDimension(id).toInt()
            return  0
        }

    override fun observeData() {
        Log.i(TAG, "observeData: ${requireActivity().screenSizeInDp}")
        binding?.viewModel?.apply {
            invalidInput.observe(viewLifecycleOwner) {
                val msg = it.getContentIfNotHandledOrReturnNull() ?: return@observe
                shortToast(R.string.search_invalid_input)
            }

            searchError.observe(viewLifecycleOwner){
                val msg = it.getContentIfNotHandledOrReturnNull()?: return@observe
                shortToast(msg)
            }

            searchResults.observe(viewLifecycleOwner){
                Log.i(TAG, "observeData: $it")
                searchResultsAdapter.updateSearchResultList(it)
            }
        }
    }

    override fun setUpViews() {
        super.setUpViews()
        binding?.apply {
            recyclerView.apply {
                this.adapter = searchResultsAdapter
            }
        }
    }

    override fun initViewModel(): SearchFragmentViewModel = searchViewModel

    override fun getViewBinding(): FragmentSearchBinding =
        FragmentSearchBinding.inflate(layoutInflater)

    override fun videoSelected(videoItem: VideosItem?) {
        val newVideo = videoItem
        Log.i(MediaPlayerFragment.TAG, "onVideoChangedEvent: ")
        val title = videoItem?.title
        videoItem?.url?.let {
            viewModel.sendNewVideoEvent(NewVideoSelected( it, title!!))
        }
    }

    companion object {
        val TAG: String = SearchFragment::class.java.canonicalName

        @JvmStatic
        fun newInstance() =
            SearchFragment()
    }
}