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
import com.example.theatreapp.constants.VideoPlaybackConstants.Companion.forwardVideo
import com.example.theatreapp.constants.VideoPlaybackConstants.Companion.rewindVideo
import com.example.theatreapp.constants.VideoPlaybackConstants.Companion.videoPaused
import com.example.theatreapp.constants.VideoPlaybackConstants.Companion.videoPlayed
import com.example.theatreapp.databinding.FragmentMediaPlayerBinding
import com.example.theatreapp.ui.fragment.BaseFragment
import com.example.theatreapp.ui.listeners.PlaybackListener
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

/**
 * A simple [Fragment] subclass.
 * Use the [MediaPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MediaPlayerFragment :
	BaseFragment<FragmentMediaPlayerBinding, StreamingRoomFragmentViewModel>(), PlaybackListener,
	Player.Listener, View.OnClickListener {
	private lateinit var exoplayer : ExoPlayer
	private lateinit var rewindButton : ImageButton
	private lateinit var playButton : ImageButton
	private lateinit var pauseButton : ImageButton
	private lateinit var forwardButton : ImageButton

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
		exoplayer.play()

		setupPlayerControlButtonsListener()
	}

	private fun setupPlayerControlButtonsListener() {
		playButton.setOnClickListener(this)
		pauseButton.setOnClickListener(this)
		rewindButton.setOnClickListener(this)
		forwardButton.setOnClickListener(this)
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
		}
	}

	override fun observeData() {
		super.observeData()
		viewModel.videoPlayback.observe(viewLifecycleOwner){
			val playbackState = it.getContentIfNotHandledOrReturnNull()?:return@observe
			when(playbackState){
				videoPlayed -> {
					exoplayer.play()
				}

				videoPaused -> {
					exoplayer.pause()
				}
			}
		}

		viewModel.videoChanged.observe(viewLifecycleOwner){
			val playbackDirection = it.getContentIfNotHandledOrReturnNull()?:return@observe
			when(playbackDirection){
				forwardVideo -> {

				}

				rewindVideo -> {

				}
			}
		}

		viewModel.videoSynced.observe(viewLifecycleOwner){
			val timestamp = it.getContentIfNotHandledOrReturnNull()?:return@observe
			// timestamp to be converted to milliseconds
		}
	}

	override fun onClick(button: View?) {
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
		}
	}

	override fun onVideoPlayed() {
		viewModel.sendVideoPlaybackEvent(videoPlayed)
	}

	override fun onVideoPaused() {
		viewModel.sendVideoPlaybackEvent(videoPaused)
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