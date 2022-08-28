package com.example.theatreapp.ui.fragment.streaming

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.theatreapp.R
import com.example.theatreapp.databinding.FragmentMediaPlayerBinding
import com.vikash.syncr_core.ui.BaseFragment
import com.google.android.exoplayer2.ExoPlayer
import com.vikash.syncr_core.constants.VideoPlaybackConstants.Companion.forwardVideo
import com.vikash.syncr_core.constants.VideoPlaybackConstants.Companion.rewindVideo
import com.vikash.syncr_core.constants.VideoPlaybackConstants.Companion.videoPaused
import com.vikash.syncr_core.constants.VideoPlaybackConstants.Companion.videoPlayed
import com.vikash.syncr_core.data.SessionData
import com.vikash.syncr_core.viewmodels.StreamingRoomFragmentViewModel
import org.greenrobot.eventbus.EventBus
import com.danikula.videocache.HttpProxyCacheServer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.vikash.syncr_core.utils.UpdateVideoTitleEvent


/**
 * A simple [Fragment] subclass.
 * Use the [MediaPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MediaPlayerFragment() :
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

	private lateinit var dataSourceFactory : DefaultDataSourceFactory

	private val layoutChangeListener : ViewTreeObserver.OnGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener{
		val height = view?.height
		viewModel.mediaPlayerFragmentViewHeight.postValue(height)
	}

	private val playbackListener = object : Player.Listener{
		override fun onPlaybackStateChanged(playbackState: Int) {
			super.onPlaybackStateChanged(playbackState)
			binding?.progressBar?.visibility = if(playbackState == Player.STATE_BUFFERING){
				viewModel.sendVideoBufferingEvent(true)
				View.VISIBLE
			} else {
				viewModel.sendVideoBufferingEvent(false)
				View.GONE
			}
		}
	}

	private val backPressedCallback = object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			isEnabled = false
			exoplayer.stop()
			exoplayer.release()
			findNavController().popBackStack()
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		val view = binding?.root
		binding?.root?.viewTreeObserver?.addOnGlobalLayoutListener(layoutChangeListener)

		dataSourceFactory = DefaultDataSourceFactory(
			requireContext(),
			Util.getUserAgent(requireContext(), requireActivity().applicationContext.packageName)
		)
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
		exoplayer.play()
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
		exoplayer.addListener(playbackListener)
	}

	override fun onPause() {
		super.onPause()
		exoplayer.pause()
	}

	override fun onStop() {
		exoplayer.removeListener(playbackListener)
		super.onStop()
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

		viewModel.newVideoSelected.observe(viewLifecycleOwner){
			val videoDetails = it.getContentIfNotHandledOrReturnNull()?:return@observe
			EventBus.getDefault().post(UpdateVideoTitleEvent(videoDetails.videoTitle))

			exoplayer.apply {
				prepare(
					ProgressiveMediaSource
						.Factory(dataSourceFactory)
						.createMediaSource(
							MediaItem.fromUri(getProxyUrl(videoDetails.videoUrl)?:"")
						)
				)
				play()
			}
		}

		viewModel.bufferingStatus.observe(viewLifecycleOwner){
			when(it){
				true -> {
					exoplayer.pause()
					binding?.progressBar?.visibility = View.VISIBLE
				}
				false -> {
					exoplayer.play()
					binding?.progressBar?.visibility = View.GONE
				}
			}
		}

		viewModel.videoSyncSlider.observe(viewLifecycleOwner){
			exoplayer.seekTo((exoplayer.currentPosition + it).toLong())
		}

		//
		/*if(videoUrl.isNullOrEmpty()){
			requireActivity().runOnUiThread {
				shortToast("Can't play video")
			}
		}else{
			exoplayer.prepare(
				ProgressiveMediaSource
					.Factory(dataSourceFactory)
					.createMediaSource(Uri.parse(getProxyUrl(videoUrl)))
			)
		}*/
	}

	private fun getProxyUrl(videoUrl: String): String? {
		val proxyServer = HttpProxyCacheServer.Builder(context).maxCacheSize(1024 * 1024 * 1024).build();
		return proxyServer.getProxyUrl(videoUrl)
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