package com.example.theatreapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.theatreapp.App
import com.example.theatreapp.listeners.MediaPlayerFragmentListener
import com.example.theatreapp.R
import com.example.theatreapp.adapters.StreamingViewPagerAdapter
import com.example.theatreapp.connections.SocketService
import com.example.theatreapp.databinding.FragmentStreamingRoomBinding
import com.example.theatreapp.models.requests.Room
import com.example.theatreapp.models.requests.User
import com.example.theatreapp.models.response.joinroomresponse.JoinedRoomResponse
import com.example.theatreapp.viewmodel.StreamingRoomFragmentViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * A simple [Fragment] subclass.
 * Use the [RoomFrament.newInstance] factory method to
 * create an instance of this fragment.
 */
class StreamingRoomFragment :
    BaseFragment<FragmentStreamingRoomBinding, StreamingRoomFragmentViewModel>(),
    MediaPlayerFragmentListener{
    private var mediaPlayerFragment : MediaPlayerFragment? = null
    private lateinit var viewPagerAdapter : StreamingViewPagerAdapter
    private lateinit var bottomSheetBehavior : BottomSheetBehavior<ConstraintLayout>
    private val backPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            isEnabled = false
            // disconnect socket and return to previous fragment
            notifyAndDisconnectSocket()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, backPressedCallback)
    }

    private fun notifyAndDisconnectSocket(){
        viewModel.leaveRoom()

        // return to previous fragment in backstack
        findNavController().popBackStack()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding!!.root
    }

    override fun initViewModel(): StreamingRoomFragmentViewModel {
        return ViewModelProvider(requireActivity()).get(StreamingRoomFragmentViewModel::class.java)
    }

    override fun getViewBinding(): FragmentStreamingRoomBinding = FragmentStreamingRoomBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()

        bottomSheetBehavior = BottomSheetBehavior.from(binding!!.bottomSheetLayout.bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        })

        binding!!.searchChat.setOnClickListener {
            bottomSheetBehavior.state = if(bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN)
                BottomSheetBehavior.STATE_HALF_EXPANDED else
                BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun onStart() {
        super.onStart()
//        addMediaPlayerFragment()
        addYoutubePlayerFragment()
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

    private fun setupViewPager() {
        viewPagerAdapter = StreamingViewPagerAdapter(parentFragmentManager)
        binding!!.viewpager.adapter = viewPagerAdapter
        binding!!.tabLayout.setupWithViewPager(binding!!.viewpager)
    }

    private fun addMediaPlayerFragment(){
        mediaPlayerFragment = MediaPlayerFragment.newInstance()
        var fragmentTransaction : FragmentTransaction = parentFragmentManager.beginTransaction()
        mediaPlayerFragment?.let {
            fragmentTransaction.add(R.id.media_player_fragment_container, it, MediaPlayerFragment.TAG)
            fragmentTransaction.commit()
            it.addMediaPlayerFragmentListener(this)
        }
    }

    private fun addYoutubePlayerFragment(){
        var youtubePlayerFragment = YoutubePlayerFragment.newInstance("", "")
        parentFragmentManager.beginTransaction()
            .add(
                R.id.media_player_fragment_container,
                youtubePlayerFragment,
                youtubePlayerFragment.tag)
            .commit()
    }

    override fun onVideoPlayed() {
//        socket.instance.emit("played", ROOM)
    }

    override fun onVideoPaused() {
//        socket.instance.emit("paused", ROOM)
    }

    override fun onPrevVideo() {
//        socket.instance.emit("previousVideo", ROOM)
    }

    override fun onNextVideo() {
//        socket.instance.emit("nextVideo", ROOM)
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