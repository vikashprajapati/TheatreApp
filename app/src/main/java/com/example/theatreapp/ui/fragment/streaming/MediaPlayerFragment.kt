package com.example.theatreapp.ui.fragment.streaming

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.theatreapp.R
import com.example.theatreapp.databinding.FragmentMediaPlayerBinding
import com.example.theatreapp.ui.fragment.BaseFragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.vikash.syncr_core.constants.VideoPlaybackConstants.Companion.forwardVideo
import com.vikash.syncr_core.constants.VideoPlaybackConstants.Companion.rewindVideo
import com.vikash.syncr_core.constants.VideoPlaybackConstants.Companion.videoPaused
import com.vikash.syncr_core.constants.VideoPlaybackConstants.Companion.videoPlayed
import com.vikash.syncr_core.data.SessionData
import com.vikash.syncr_core.data.connections.SocketManager.participantJoined
import com.vikash.syncr_core.data.models.response.youtube.searchResults.ItemsItem
import com.vikash.syncr_core.viewmodels.StreamingRoomFragmentViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [MediaPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MediaPlayerFragment :
	BaseFragment<FragmentMediaPlayerBinding, StreamingRoomFragmentViewModel>(),
	View.OnClickListener {
	private lateinit var exoplayer : ExoPlayer
	private lateinit var rewindButton : ImageButton
	private lateinit var playButton : ImageButton
	private lateinit var pauseButton : ImageButton
	private lateinit var forwardButton : ImageButton
	private lateinit var syncButton : ImageButton
	private lateinit var fullscreenButton : ImageButton
	private lateinit var squeezeButton : ImageButton

	private val layoutChangeListener : ViewTreeObserver.OnGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener{
		val height = view?.height
		viewModel.mediaPlayerFragmentViewHeight.postValue(height)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		val view = binding?.root
		binding?.root?.viewTreeObserver?.addOnGlobalLayoutListener(layoutChangeListener)
		return binding?.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupMediaPlayer()
	}

	override fun initViewModel():
            StreamingRoomFragmentViewModel = ViewModelProvider(requireActivity()).get(StreamingRoomFragmentViewModel::class.java)

	override fun getViewBinding(): FragmentMediaPlayerBinding =
		FragmentMediaPlayerBinding.inflate(layoutInflater)

	override fun onStart() {
		super.onStart()
		exoplayer.setMediaItem(MediaItem.fromUri(Uri.parse("asset:///videoplayback.mp4")))
		exoplayer.prepare()

		setupPlayerControlButtonsListener()
	}

	private fun setupPlayerControlButtonsListener() {
		playButton.setOnClickListener(this)
		pauseButton.setOnClickListener(this)
		rewindButton.setOnClickListener(this)
		forwardButton.setOnClickListener(this)
		syncButton.setOnClickListener(this)
		fullscreenButton.setOnClickListener(this)
		squeezeButton.setOnClickListener(this)
	}

	override fun onPause() {
		super.onPause()
		exoplayer.pause()
	}

	override fun onStop() {
		super.onStop()
		exoplayer.stop()
	}

	private fun setupMediaPlayer() {
		// setup player
		exoplayer = ExoPlayer.Builder(requireContext()).build().also { player ->
			binding?.playerView?.player = player
		}

		binding?.playerView?.run {
			rewindButton = findViewById(R.id.exo_rew)!!
			playButton = findViewById(R.id.exo_play)!!
			pauseButton = findViewById(R.id.exo_pause)!!
			forwardButton = findViewById(R.id.exo_ffwd)!!
			syncButton = findViewById(R.id.exo_video_sync)!!
			fullscreenButton = findViewById(R.id.exo_fullscreen)!!
			squeezeButton = findViewById(R.id.exo_squeeze)!!
		}
	}

	fun changeVideo(videoItem: ItemsItem?){

	}

	override fun observeData() {
		super.observeData()
		viewModel.videoPlayback.observe(viewLifecycleOwner){
			val videoPlaybackData = it.getContentIfNotHandledOrReturnNull()?:return@observe
			when(videoPlaybackData.playbackStatus){
				videoPlayed -> {
					exoplayer.play()
					if(!videoPlaybackData.userId.equals(SessionData.localUser?.id))
						shortToast("${SessionData.getParticipantName(videoPlaybackData.userId)} played video")
				}

				videoPaused -> {
					exoplayer.pause()
					if(!videoPlaybackData.userId.equals(SessionData.localUser?.id))
						shortToast("${SessionData.getParticipantName(videoPlaybackData.userId)} paused video")
				}
			}
		}

		viewModel.videoChanged.observe(viewLifecycleOwner){
			val videoChangedData = it.getContentIfNotHandledOrReturnNull()?:return@observe
			when(videoChangedData.playbackDirection){
				forwardVideo -> {
					exoplayer.seekTo(videoChangedData.timeStamp.toLong())
					if(!videoChangedData.userId.equals(SessionData.localUser?.id))
					shortToast("${SessionData.getParticipantName(videoChangedData.userId)} seeked forward")
				}

				rewindVideo -> {
					exoplayer.seekTo(videoChangedData.timeStamp.toLong())
					if(!videoChangedData.userId.equals(SessionData.localUser?.id))
					shortToast("${SessionData.getParticipantName(videoChangedData.userId)} seeked backward")
				}
			}
		}

		viewModel.videoSynced.observe(viewLifecycleOwner){
			val videoSyncedData = it.getContentIfNotHandledOrReturnNull()?:return@observe
			exoplayer.seekTo(videoSyncedData.playbackTimestamp.toLong())
			exoplayer.play()
		}

		viewModel.participantArrived.observe(viewLifecycleOwner){
			val participant = it.getContentIfNotHandledOrReturnNull()?:return@observe
			viewModel.sendVideoSyncedEvent(exoplayer.currentPosition.toString())
		}
	}

	override fun onClick(button: View?) {
		val currentTime = exoplayer.currentPosition.toString()
		when(button?.id){
			R.id.exo_play -> {
				viewModel.sendVideoPlaybackEvent(videoPlayed)
			}

			R.id.exo_pause -> {
				viewModel.sendVideoPlaybackEvent(videoPaused)
			}

			R.id.exo_rew -> {
				val timeStamp = exoplayer.currentPosition - 5000
				viewModel.sendVideoJumpEvent(rewindVideo, timeStamp)
			}

			R.id.exo_ffwd -> {
				val timeStamp = exoplayer.currentPosition + 5000
				viewModel.sendVideoJumpEvent(forwardVideo, timeStamp)
			}

			R.id.exo_video_sync -> {
				exoplayer.pause()
				viewModel.sendVideoSyncedEvent(currentTime)
			}

			R.id.exo_fullscreen -> {
				viewModel.fullScreenLayout.value = true
				requireActivity().apply {
					window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
					requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
				}
				fullscreenButton.visibility = View.GONE
				squeezeButton.visibility = View.VISIBLE
			}

			R.id.exo_squeeze -> {
				viewModel.fullScreenLayout.value = false
				requireActivity().apply {
					window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
					requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
				}
				fullscreenButton.visibility = View.VISIBLE
				squeezeButton.visibility = View.GONE
			}
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding?.root?.viewTreeObserver?.removeOnGlobalLayoutListener(layoutChangeListener)
	}

	companion object {
		@JvmStatic
		val TAG: String? = MediaPlayerFragment::class.java.canonicalName
		/**
		 * Use this factory method to create a new instance of
		 * this fragment using the provided parameters.
		 *
		 * @return A new instance of fragment MediaPlayerFragment.
		 */
		@JvmStatic
		fun newInstance() = MediaPlayerFragment()

	}
}