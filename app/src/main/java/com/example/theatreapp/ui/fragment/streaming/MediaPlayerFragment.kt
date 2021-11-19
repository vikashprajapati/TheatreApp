package com.example.theatreapp.ui.fragment.streaming

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.theatreapp.constants.VideoPlaybackConstants.Companion.nextVideo
import com.example.theatreapp.constants.VideoPlaybackConstants.Companion.prevVideo
import com.example.theatreapp.constants.VideoPlaybackConstants.Companion.videoPaused
import com.example.theatreapp.constants.VideoPlaybackConstants.Companion.videoPlayed
import com.example.theatreapp.ui.listeners.PlaybackListener
import com.example.theatreapp.databinding.FragmentMediaPlayerBinding
import com.example.theatreapp.ui.fragment.BaseFragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

/**
 * A simple [Fragment] subclass.
 * Use the [MediaPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MediaPlayerFragment :
	BaseFragment<FragmentMediaPlayerBinding, StreamingRoomFragmentViewModel>(), PlaybackListener {
	private lateinit var exoplayer : ExoPlayer

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
		exoplayer = ExoPlayer.Builder(requireContext()).build()
		exoplayer.setVideoSurfaceView(binding?.surfaceView)
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
				nextVideo -> {

				}

				prevVideo -> {

				}
			}
		}

		viewModel.videoSynced.observe(viewLifecycleOwner){
			val timestamp = it.getContentIfNotHandledOrReturnNull()?:return@observe
			// timestamp to be converted to milliseconds
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
		val TAG = MediaPlayerFragment::class.java.canonicalName
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