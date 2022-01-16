package com.example.theatreapp.ui.fragment.streaming

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.SparseArray
import android.view.*
import androidx.core.util.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.theatreapp.R
import com.example.theatreapp.databinding.FragmentSearchBinding
import com.example.theatreapp.ui.adapters.YoutubeSearchResultsAdapter
import com.example.theatreapp.ui.fragment.BaseBottomSheetFragment
import com.vikash.syncr_core.data.models.response.youtube.searchResults.VideosItem
import com.vikash.syncr_core.data.models.videoplaybackevents.NewVideoSelected
import com.vikash.syncr_core.utils.SearchResultAdapterListener
import com.vikash.syncr_core.viewmodels.SearchFragmentViewModel
import com.vikash.youtube_extractor.VideoMeta
import com.vikash.youtube_extractor.YouTubeExtractor
import com.vikash.youtube_extractor.YtFile
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment :
    BaseBottomSheetFragment<FragmentSearchBinding, SearchFragmentViewModel>(),
    SearchResultAdapterListener {
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
        binding?.apply {
            lifecycleOwner = this@SearchFragment
            this.viewModel = viewModel
        }

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
        super.observeData()
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

            /*searchResults.observe(viewLifecycleOwner){
                Log.i(TAG, "observeData: $it")
                searchResultsAdapter.updateSearchResultList(it)
            }*/

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

    override fun initViewModel(): SearchFragmentViewModel =
        ViewModelProvider(this).get(SearchFragmentViewModel::class.java)

    override fun getViewBinding(): FragmentSearchBinding =
        FragmentSearchBinding.inflate(layoutInflater)

    @SuppressLint("StaticFieldLeak")
    override fun videoSelected(videoItem: VideosItem?) {
        val newVideo = videoItem
        Log.i(MediaPlayerFragment.TAG, "onVideoChangedEvent: ")
        binding?.progressBar?.visibility = View.VISIBLE
        object : YouTubeExtractor(context) {
            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, vMeta: VideoMeta?) {
                if (ytFiles != null) {
                    var videoUrl = ""
                    ytFiles.forEach{key, ytFile ->
                        Log.i(MediaPlayerFragment.TAG, "onExtractionComplete: $key \n $ytFile \n\n")
                        if(
                            ytFile.format.ext.equals("mp4")
                            && ytFile.format.audioBitrate > 0
                            && ytFile.format.height > 0
                        ){
                            videoUrl = ytFile.url
                        }
                    }

                    if(videoUrl.isNullOrEmpty()){
                        requireActivity().runOnUiThread {
                            shortToast("Can't play video")
                        }
                    }else{
                        binding?.progressBar?.visibility = View.GONE
                        viewModel.sendNewVideoEvent(NewVideoSelected(videoUrl, newVideo?.title!!))
                    }
                }
            }
        }.extract(newVideo?.url)
    }

    companion object {
        val TAG: String = SearchFragment::class.java.canonicalName

        @JvmStatic
        fun newInstance() =
            SearchFragment()
    }
}