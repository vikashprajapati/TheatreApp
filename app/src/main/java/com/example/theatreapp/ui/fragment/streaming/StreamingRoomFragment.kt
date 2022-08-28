package com.example.theatreapp.ui.fragment.streaming

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.theatreapp.R
import com.example.theatreapp.databinding.FragmentStreamingRoomBinding
import com.example.theatreapp.ui.adapters.StreamingViewPagerAdapter
import com.vikash.syncr_core.ui.BaseFragment
import com.vikash.syncr_core.utils.UpdateVideoTitleEvent
import com.vikash.syncr_core.viewmodels.StreamingRoomFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket.EVENT_CONNECT_ERROR
import io.socket.client.Socket.EVENT_DISCONNECT
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * A simple [Fragment] subclass.
 * Use the [StreamingRoomFrament.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class StreamingRoomFragment :
	BaseFragment<FragmentStreamingRoomBinding, StreamingRoomFragmentViewModel>() {

	private val streamingRoomViewModel : StreamingRoomFragmentViewModel by viewModels(
		ownerProducer = {requireActivity()}
	)
	private var mediaPlayerFragment: MediaPlayerFragment? = null
	private lateinit var viewPagerAdapter: StreamingViewPagerAdapter

	private val backPressedCallback = object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			isEnabled = false
			// disconnect socket and return to previous fragment
			viewModel.leaveRoom()
			findNavController().popBackStack()
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		val videoUrl = arguments?.getString("videoUrl")
		viewModel.extractYoutubeUrl(videoUrl!!, "")

		binding?.let{
			it.viewModel = viewModel
			it.lifecycleOwner = this@StreamingRoomFragment
		}
		return binding?.root
	}

	override fun initViewModel(): StreamingRoomFragmentViewModel = streamingRoomViewModel

	override fun getViewBinding(): FragmentStreamingRoomBinding =
		FragmentStreamingRoomBinding.inflate(layoutInflater)

	override fun setUpViews() {
		super.setUpViews()
		addMediaPlayerFragment()

		setupViewPager()

		binding?.let{
			it.searchButton.setOnClickListener {
				val bundle = Bundle()
				bundle.putInt("height", viewModel.mediaPlayerFragmentViewHeight.value!!)
				findNavController().navigate(R.id.action_roomFrament_to_searchFragment, bundle)
			}
		}
	}

	override fun onStart() {
		super.onStart()
		EventBus.getDefault().register(this)
		requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun updateVideoDetails(updateVideoTitleEvent: UpdateVideoTitleEvent){
		binding?.videoTitleTextView?.text = updateVideoTitleEvent.videoTitle
	}

	override fun onStop() {
		super.onStop()
		EventBus.getDefault().unregister(this)
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

			participantLeft.observe(viewLifecycleOwner){
				val participant = it.getContentIfNotHandledOrReturnNull()?:return@observe
				shortToast("${participant.name} left")
			}

			participantJoined.observe(viewLifecycleOwner){
				val participant = it.getContentIfNotHandledOrReturnNull()?:return@observe
				shortToast("${participant.name} joined")
			}

			fullScreenLayout.observe(viewLifecycleOwner){ isFullscreen ->
				when(isFullscreen){
					true -> {
						binding?.mediaPlayerFragmentContainer?.layoutParams?.apply {
							width = WindowManager.LayoutParams.MATCH_PARENT
							height = WindowManager.LayoutParams.MATCH_PARENT
						}
					}

					false -> {
						binding?.mediaPlayerFragmentContainer?.layoutParams?.apply {
							width = WindowManager.LayoutParams.MATCH_PARENT
							height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240f, resources.displayMetrics).toInt()
						}
					}
				}
			}
		}
	}

	private fun setupViewPager() {
		viewPagerAdapter = StreamingViewPagerAdapter(parentFragmentManager)
		binding?.apply {
			viewpager.adapter = viewPagerAdapter
			tabLayout.setupWithViewPager(viewpager)
		}
	}

	private fun addMediaPlayerFragment() {
		mediaPlayerFragment = MediaPlayerFragment.newInstance()
		val fragmentTransaction: FragmentTransaction = parentFragmentManager.beginTransaction()
		mediaPlayerFragment?.apply {
			fragmentTransaction.add(
				R.id.media_player_fragment_container,
				this,
				MediaPlayerFragment.TAG
			).commit()
		}
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