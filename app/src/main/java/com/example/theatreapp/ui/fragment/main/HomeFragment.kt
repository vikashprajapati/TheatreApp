package com.example.theatreapp.ui.fragment.main

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.theatreapp.R
import com.example.theatreapp.databinding.FragmentHomeBinding
import com.example.theatreapp.ui.composables.LoginForm
import com.example.theatreapp.ui.fragment.BaseFragment
import com.vikash.syncr_core.data.models.response.joinroomresponse.JoinedRoomResponse
import com.vikash.syncr_core.viewmodels.HomeFragmentViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeFragmentViewModel>(){
    private val TAG = HomeFragment::class.java.canonicalName

    override fun getViewBinding(): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater).apply {
            composeContainer.setContent {
                LoginForm(navigationController = ::navigateToStreamingRoom)
            }
        }

    override fun initViewModel(): HomeFragmentViewModel =
        ViewModelProvider(this)[HomeFragmentViewModel::class.java]

    private fun navigateToStreamingRoom(roomDetails: JoinedRoomResponse){
        val bundle = bundleOf("videoUrl" to roomDetails.room.currentVideoUrl)
        findNavController().navigate(R.id.action_homeFragment_to_roomFrament, bundle)
    }
}

