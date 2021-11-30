package com.example.theatreapp.ui.fragment.streaming.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.vikash.syncr_core.data.SessionData
import com.example.theatreapp.databinding.FragmentRoomBinding
import com.example.theatreapp.ui.fragment.BaseFragment
import com.vikash.syncr_core.viewmodels.StreamingRoomFragmentViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [RoomFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RoomFragment : BaseFragment<FragmentRoomBinding, StreamingRoomFragmentViewModel>() {

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		binding?.lifecycleOwner = this@RoomFragment
		return binding?.root
	}
	override fun setUpViews() {
		super.setUpViews()
		binding?.room = SessionData.currentRoom
	}

	override fun observeData() {
		super.observeData()
		viewModel.participants.observe(viewLifecycleOwner){
			binding?.room = SessionData.currentRoom
		}
	}

	override fun initViewModel(): StreamingRoomFragmentViewModel = ViewModelProvider(requireActivity()).get(
		StreamingRoomFragmentViewModel::class.java)

	override fun getViewBinding(): FragmentRoomBinding = FragmentRoomBinding.inflate(layoutInflater)

	companion object {
		/**
		 * Use this factory method to create a new instance of
		 * this fragment using the provided parameters.
		 *
		 * @return A new instance of fragment RoomFragment.
		 */
		@JvmStatic
		fun newInstance() = RoomFragment()
	}
}