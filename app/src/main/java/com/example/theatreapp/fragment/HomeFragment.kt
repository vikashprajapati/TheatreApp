package com.example.theatreapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.theatreapp.R
import com.example.theatreapp.adapters.RoomsListAdapter
import com.example.theatreapp.connections.Socket
import com.example.theatreapp.databinding.FragmentHomeBinding
import com.example.theatreapp.listeners.HomeFragmentListener
import com.example.theatreapp.models.room.Room
import com.example.theatreapp.viewmodel.HomeFragmentViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment :
    Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private val TAG = HomeFragment.javaClass.canonicalName
    private val viewModel = HomeFragmentViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun onRoomSelected(room : Room) {
        var roomBundle = bundleOf(ROOM to (room?.name ?: "Test"), USER to "Ghost Rider")
        findNavController().navigate(R.id.action_homeFragment_to_roomFrament, roomBundle)
    }
}