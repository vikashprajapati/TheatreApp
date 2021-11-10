package com.example.theatreapp.ui.fragment.streaming

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.theatreapp.constants.VideoPlaybackConstants
import com.example.theatreapp.constants.VideoPlaybackConstants.Companion.nextVideo
import com.example.theatreapp.constants.VideoPlaybackConstants.Companion.prevVideo
import com.example.theatreapp.constants.VideoPlaybackConstants.Companion.videoPaused
import com.example.theatreapp.constants.VideoPlaybackConstants.Companion.videoPlayed
import com.example.theatreapp.ui.listeners.MediaPlayerFragmentListener
import com.example.theatreapp.ui.listeners.PlaybackListener
import com.example.theatreapp.utils.PlayerController
import com.example.theatreapp.databinding.FragmentMediaPlayerBinding
import com.example.theatreapp.ui.fragment.BaseFragment
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer

/**
 * A simple [Fragment] subclass.
 * Use the [MediaPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MediaPlayerFragment :
	BaseFragment<FragmentMediaPlayerBinding, StreamingRoomFragmentViewModel>(), PlaybackListener {
	private lateinit var libvlc: LibVLC
	private lateinit var mediaPlayer: MediaPlayer

	override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = binding?.root

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupVlcMediaPlayer()
	}

    override fun initViewModel():
            StreamingRoomFragmentViewModel = ViewModelProvider(requireActivity())
        .get(StreamingRoomFragmentViewModel::class.java)

	override fun getViewBinding(): FragmentMediaPlayerBinding =
		FragmentMediaPlayerBinding.inflate(layoutInflater)

	override fun onStart() {
		super.onStart()
		mediaPlayer.attachViews(binding!!.videoPlayerLayout, null, false, false)
		var media = Media(libvlc, context?.assets?.openFd("videoplayback.mp4"))
		mediaPlayer.media = media
	}

	override fun onPause() {
		super.onPause()
		mediaPlayer.pause()
		mediaPlayer.detachViews()
	}

	override fun onStop() {
		super.onStop()
		mediaPlayer.stop()
	}

	private fun setupVlcMediaPlayer() {
		// setup player
		val args = ArrayList<String>()
		args.add("-vvv")
		libvlc = LibVLC(context, args)
		mediaPlayer = MediaPlayer(libvlc)

		// setup video controller
		val videoController = MediaController(context)
		videoController.setMediaPlayer(PlayerController(mediaPlayer, this))
		videoController.setAnchorView(binding!!.videoPlayerLayout)
		binding!!.videoPlayerLayout.setOnClickListener {
			videoController.show(5000)
		}

		videoController.setPrevNextListeners({
            // next is pressed
            mediaPlayer.position = 0F
			viewModel.sendVideoChangedEvent(nextVideo)
        }, {
            // prev is pressed
            mediaPlayer.position = 0F
			viewModel.sendVideoChangedEvent(prevVideo)
            mediaPlayer.pause()
        })

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