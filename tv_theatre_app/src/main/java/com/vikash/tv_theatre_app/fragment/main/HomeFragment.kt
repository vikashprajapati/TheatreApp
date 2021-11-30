package com.vikash.tv_theatre_app.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.vikash.syncr_core.viewmodels.HomeFragmentViewModel
import com.vikash.tv_theatre_app.R
import com.vikash.tv_theatre_app.databinding.FragmentHomeBinding
import com.vikash.tv_theatre_app.fragment.BaseFragment

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeFragmentViewModel>(){
    private val TAG = HomeFragment::class.java.canonicalName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding?.lifecycleOwner = this@HomeFragment
        binding?.viewModel = viewModel
        return binding?.root
    }

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun initViewModel(): HomeFragmentViewModel = ViewModelProvider(this).get(
        HomeFragmentViewModel::class.java)

    override fun observeData() {
        super.observeData()
        binding?.viewModel!!.apply {
            invalidInput.observe(viewLifecycleOwner, { event ->
                event?.getContentIfNotHandledOrReturnNull()?.let {
                    shortToast(R.string.invalid_details_text)
                }
            })

            connectionState.observe(viewLifecycleOwner){
                val msg = it.getContentIfNotHandledOrReturnNull()?:return@observe
                shortToast(msg.toString())
            }

            joinRoomState.observe(viewLifecycleOwner, {
                // navigate to streaming room fragment
                val roomDetails = it.getContentIfNotHandledOrReturnNull()?:return@observe
                removeObservers()
                findNavController().navigate(R.id.action_homeFragment_to_roomFrament)
            })
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment HomeFragment.
         */
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}