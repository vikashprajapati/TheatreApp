package com.example.theatreapp.ui.fragment.streaming

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.theatreapp.R
import com.vikash.syncr_core.constants.VideoPlaybackConstants.Companion.forwardVideo
import com.vikash.syncr_core.constants.VideoPlaybackConstants.Companion.rewindVideo
import com.vikash.syncr_core.constants.VideoPlaybackConstants.Companion.videoPaused
import com.vikash.syncr_core.constants.VideoPlaybackConstants.Companion.videoPlayed
import com.vikash.syncr_core.data.SessionData
import com.example.theatreapp.databinding.FragmentMediaPlayerBinding
import com.example.theatreapp.ui.fragment.BaseFragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
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

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? = binding?.root

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupMediaPlayer()
	}

	override fun initViewModel():
            StreamingRoomFragmentViewModel = ViewModelProvider(requireActivity())
		.get(StreamingRoomFragmentViewModel::class.java)

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
		}
	}

	override fun observeData() {
		super.observeData()
		viewModel.videoPlayback.observe(viewLifecycleOwner){
			val videoPlaybackData = it.getContentIfNotHandledOrReturnNull()?:return@observe
			when(videoPlaybackData.playbackStatus){
				videoPlayed -> {
					exoplayer.play()
					if(!videoPlaybackData.id.equals(SessionData.localUser?.id))
						shortToast("${SessionData.getParticipantName(videoPlaybackData.id)} played video")
				}

				videoPaused -> {
					exoplayer.pause()

					if(!videoPlaybackData.id.equals(SessionData.localUser?.id))
						shortToast("${SessionData.getParticipantName(videoPlaybackData.id)} paused video")
				}
			}
		}

		viewModel.videoChanged.observe(viewLifecycleOwner){
			val videoChangedData = it.getContentIfNotHandledOrReturnNull()?:return@observe
			when(videoChangedData.playbackDirection){
				forwardVideo -> {
					exoplayer.seekForward()
					if(!videoChangedData.id.equals(SessionData.localUser?.id))
					shortToast("${SessionData.getParticipantName(videoChangedData.id)} seeked forward")
				}

				rewindVideo -> {
					exoplayer.seekBack()
					if(!videoChangedData.id.equals(SessionData.localUser?.id))
					shortToast("${SessionData.getParticipantName(videoChangedData.id)}  seeked backward")
				}
			}
		}

		viewModel.videoSynced.observe(viewLifecycleOwner){
			val videoSyncedData = it.getContentIfNotHandledOrReturnNull()?:return@observe
			exoplayer.seekTo(videoSyncedData.playbackTimestamp.toLong())
			exoplayer.play()
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
				viewModel.sendVideoJumpEvent(rewindVideo)
			}

			R.id.exo_ffwd -> {
				viewModel.sendVideoJumpEvent(forwardVideo)
			}

			R.id.exo_video_sync -> {
				exoplayer.pause()
				viewModel.sendVideoSyncedEvent(currentTime)
			}
		}
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