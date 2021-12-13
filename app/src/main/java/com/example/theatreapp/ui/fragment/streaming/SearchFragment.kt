package com.example.theatreapp.ui.fragment.streaming

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
import com.example.theatreapp.ui.adapters.ChatMessagesAdapter
import com.example.theatreapp.ui.adapters.YoutubeSearchResultsAdapter
import com.example.theatreapp.ui.fragment.BaseBottomSheetFragment
import com.vikash.syncr_core.viewmodels.SearchBottomSheetViewModel
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment :
    BaseBottomSheetFragment<FragmentSearchBinding, SearchBottomSheetViewModel>()
{
    private val searchResultsAdapter = YoutubeSearchResultsAdapter(arrayListOf())
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
        binding?.recyclerView.apply {
            this?.adapter = searchResultsAdapter
        }
    }

    override fun initViewModel(): SearchBottomSheetViewModel =
        ViewModelProvider(this).get(SearchBottomSheetViewModel::class.java)

    override fun getViewBinding(): FragmentSearchBinding =
        FragmentSearchBinding.inflate(layoutInflater)

    companion object {
        val TAG: String = SearchFragment::class.java.canonicalName

        @JvmStatic
        fun newInstance() =
            SearchFragment()
    }
}