package com.example.theatreapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.theatreapp.HomeViewModelFactory
import com.example.theatreapp.R
import com.example.theatreapp.connections.SocketManager
import com.example.theatreapp.databinding.FragmentHomeBinding
import com.example.theatreapp.listeners.HomeFragmentListener
import com.example.theatreapp.viewmodel.HomeFragmentViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeFragmentViewModel>(){
    private val TAG = HomeFragment.javaClass.canonicalName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding?.root
    }

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun initViewModel(): HomeFragmentViewModel{
        var viewModelFactory = HomeViewModelFactory(SocketManager())
        return ViewModelProvider(this, viewModelFactory).get(HomeFragmentViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.lifecycleOwner = this@HomeFragment
        binding?.viewModel = viewModel

        observeData()
    }

    override fun observeData() {
        super.observeData()
        binding?.viewModel!!.apply {
            invalidInput.observe(viewLifecycleOwner, { event ->
                event?.getContentIfNotHandledOrReturnNull()?.let {
                    shortToast(R.string.invalid_details_text)
                }
            })

            connectionState.observe(viewLifecycleOwner, { msg ->
                shortToast(msg)
            })

            joinRoomState.observe(viewLifecycleOwner, { response ->

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