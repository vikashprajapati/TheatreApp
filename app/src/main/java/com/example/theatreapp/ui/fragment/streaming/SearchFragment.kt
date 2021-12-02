package com.example.theatreapp.ui.fragment.streaming

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.theatreapp.R
import com.example.theatreapp.databinding.FragmentSearchBinding
import com.example.theatreapp.ui.fragment.BaseBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vikash.syncr_core.viewmodels.SearchBottomSheetViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : BaseBottomSheetFragment<FragmentSearchBinding, SearchBottomSheetViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding?.lifecycleOwner = this@SearchFragment
        binding?.viewModel = viewModel

        return binding?.root
    }

    override fun observeData() {
        super.observeData()
        binding?.viewModel?.apply {
            invalidInput.observe(viewLifecycleOwner){
                val msg = it.getContentIfNotHandledOrReturnNull()?:return@observe
                shortToast(R.string.search_invalid_input)
            }
        }
    }

    override fun initViewModel(): SearchBottomSheetViewModel = ViewModelProvider(this).get(SearchBottomSheetViewModel::class.java)

    override fun getViewBinding(): FragmentSearchBinding = FragmentSearchBinding.inflate(layoutInflater)

    companion object {
        @JvmStatic
        fun newInstance() =
            SearchFragment()
    }
}