package com.vikash.tv_theatre_app.fragment.streaming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.vikash.syncr_core.viewmodels.StreamingRoomFragmentViewModel
import com.vikash.tv_theatre_app.R
import com.vikash.tv_theatre_app.databinding.FragmentStreamingRoomBinding
import com.vikash.tv_theatre_app.fragment.BaseFragment
import io.socket.client.Socket

/**
 * A simple [Fragment] subclass.
 * Use the [StreamingRoomFrament.newInstance] factory method to
 * create an instance of this fragment.
 */
class StreamingRoomFragment :
	BaseFragment<FragmentStreamingRoomBinding, StreamingRoomFragmentViewModel>() {
	private var mediaPlayerFragment: MediaPlayerFragment? = null
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

				if(status == null || status == Socket.EVENT_CONNECT_ERROR || status == Socket.EVENT_DISCONNECT){
					shortToast(R.string.socket_disconnected)
					findNavController().popBackStack()
				}
			}

			participantLeft.observe(viewLifecycleOwner){
				val participant = it.getContentIfNotHandledOrReturnNull()?:return@observe
				shortToast("${participant.name} left")
			}

			participantJoined.observe(viewLifecycleOwner){
				val participant = it.getContentIfNotHandledOrReturnNull()?:return@observe
				shortToast("${participant.name} joined")
			}
		}
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