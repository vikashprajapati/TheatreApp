package com.example.theatreapp.ui.fragment.streaming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.theatreapp.R
import com.example.theatreapp.databinding.FragmentStreamingRoomBinding
import com.example.theatreapp.ui.adapters.StreamingViewPagerAdapter
import com.example.theatreapp.ui.fragment.BaseFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.socket.client.Socket.EVENT_CONNECT_ERROR
import io.socket.client.Socket.EVENT_DISCONNECT

/**
 * A simple [Fragment] subclass.
 * Use the [StreamingRoomFrament.newInstance] factory method to
 * create an instance of this fragment.
 */
class StreamingRoomFragment :
	BaseFragment<FragmentStreamingRoomBinding, StreamingRoomFragmentViewModel>() {
	private var mediaPlayerFragment: MediaPlayerFragment? = null
	private lateinit var viewPagerAdapter: StreamingViewPagerAdapter
	private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
	private val backPressedCallback = object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			isEnabled = false
			// disconnect socket and return to previous fragment
			notifyAndDisconnectSocket()
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
	}

	private fun notifyAndDisconnectSocket() {
		viewModel.leaveRoom()
	}

	override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
		return binding!!.root
	}

	override fun initViewModel(): StreamingRoomFragmentViewModel =
		ViewModelProvider(requireActivity()).get(StreamingRoomFragmentViewModel::class.java)

	override fun getViewBinding(): FragmentStreamingRoomBinding =
		FragmentStreamingRoomBinding.inflate(layoutInflater)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
	}

	override fun setUpViews() {
		super.setUpViews()
		addMediaPlayerFragment()

		setupViewPager()

		bottomSheetBehavior = BottomSheetBehavior.from(binding!!.bottomSheetLayout.bottomSheet)

		binding!!.searchChat.setOnClickListener {
			bottomSheetBehavior.state =
				if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN)
					BottomSheetBehavior.STATE_HALF_EXPANDED else
					BottomSheetBehavior.STATE_HIDDEN
		}
	}

	override fun onStart() {
		super.onStart()
//        addYoutubePlayerFragment
	}

	override fun onStop() {
		super.onStop()
		mediaPlayerFragment?.onStop()
	}

	override fun onDestroyView() {
		super.onDestroyView()

		// backPressedCallback is not lifecycle aware so we need to unregister callback manually
		backPressedCallback.isEnabled = false
		backPressedCallback.remove()
	}

	override fun observeData() {
		super.observeData()
		viewModel.apply {
			connectionState.observe(viewLifecycleOwner){
				val status = it

				if(status == null || status == EVENT_CONNECT_ERROR || status == EVENT_DISCONNECT){
					shortToast(R.string.socket_disconnected)
					findNavController().popBackStack()
				}
			}
		}
	}

	private fun setupViewPager() {
		viewPagerAdapter = StreamingViewPagerAdapter(parentFragmentManager)
		binding!!.viewpager.adapter = viewPagerAdapter
		binding!!.tabLayout.setupWithViewPager(binding!!.viewpager)
	}

	private fun addMediaPlayerFragment() {
		mediaPlayerFragment = MediaPlayerFragment.newInstance()
		val fragmentTransaction: FragmentTransaction = parentFragmentManager.beginTransaction()
		mediaPlayerFragment?.let {
			fragmentTransaction.add(
                R.id.media_player_fragment_container, it,
                MediaPlayerFragment.TAG
            )
			fragmentTransaction.commit()
		}
	}

	private fun addYoutubePlayerFragment() {
		val youtubePlayerFragment = YoutubePlayerFragment.newInstance("", "")
		parentFragmentManager.beginTransaction()
			.add(
                R.id.media_player_fragment_container,
                youtubePlayerFragment,
                youtubePlayerFragment.tag
            )
			.commit()
	}

	companion object {
		/**
		 * Use this factory method to create a new instance of
		 * this fragment using the provided parameters.
		 *
		 * @return A new instance of fragment RoomFrament.
		 */
		@JvmStatic
		fun newInstance() = StreamingRoomFragment()
	}
}