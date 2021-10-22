package com.example.theatreapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.example.theatreapp.App
import com.example.theatreapp.listeners.MediaPlayerFragmentListener
import com.example.theatreapp.R
import com.example.theatreapp.adapters.StreamingViewPagerAdapter
import com.example.theatreapp.connections.Socket
import com.example.theatreapp.databinding.FragmentStreamingRoomBinding
import com.example.theatreapp.models.requests.Room
import com.example.theatreapp.models.requests.User
import com.google.android.material.bottomsheet.BottomSheetBehavior

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
public const val ROOM = "Room"
public const val USER = "User"

/**
 * A simple [Fragment] subclass.
 * Use the [RoomFrament.newInstance] factory method to
 * create an instance of this fragment.
 */
class StreamingRoomFragment :
    Fragment(),
    MediaPlayerFragmentListener,
    Socket.SocketEventListener {
    // TODO: Rename and change types of parameters
    private var room: String? = null
    private var user: String? = null
    private lateinit var socket: Socket
    private lateinit var binding : FragmentStreamingRoomBinding
    private var mediaPlayerFragment : MediaPlayerFragment? = null
    private lateinit var viewPagerAdapter : StreamingViewPagerAdapter
    private lateinit var bottomSheetBehavior : BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            room = it.getString(ROOM)
            user = it.getString(USER)
        }
        socket = Socket()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStreamingRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        socket.setSocketListener(this)
        socket.initializeSocketEvents()

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayout.bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        })

        binding.searchChat.setOnClickListener {
            bottomSheetBehavior.state = if(bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN)
                BottomSheetBehavior.STATE_HALF_EXPANDED else
                BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun joinRoom(){
        socket.instance
            .emit(
                "join room",
                App.gson.toJson(Room("Movie Night")),
                App.gson.toJson(User("Vikash"))
            )
    }

    override fun onStart() {
        super.onStart()
//        addMediaPlayerFragment()
        addYoutubePlayerFragment()
    }

    override fun onPause() {
        super.onPause()
        socket.instance.disconnect()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayerFragment?.onStop()
    }

    private fun setupViewPager() {
        viewPagerAdapter = StreamingViewPagerAdapter(
            parentFragmentManager,
            listOf(ChatFragment.newInstance("", ""),
                ParticipantsFragment.newInstance(1)))

        binding.viewpager.adapter = viewPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewpager)
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
        socket.instance.emit("played", ROOM)
    }

    override fun onVideoPaused() {
        socket.instance.emit("paused", ROOM)
    }

    override fun onPrevVideo() {
        socket.instance.emit("previousVideo", ROOM)
    }

    override fun onNextVideo() {
        socket.instance.emit("nextVideo", ROOM)
    }

    override fun connectionStatus(eventConnect: String) {
        activity?.runOnUiThread {
            when(eventConnect) {
                io.socket.client.Socket.EVENT_CONNECT -> {
                    Toast.makeText(context, "Socket connected", Toast.LENGTH_SHORT).show()
                    joinRoom()
                }
                io.socket.client.Socket.EVENT_CONNECT_ERROR,
                io.socket.client.Socket.EVENT_DISCONNECT -> {
                    socket.instance.close()
                    Toast.makeText(context, "Socket connection failed", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    override fun joinRoomResponse(room: String) {
        Log.i("TAG", "joinRoomResponse: ${room}" )
    }

    override fun playEvent() {
        TODO("Not yet implemented")
    }

    override fun pauseEvent() {
        TODO("Not yet implemented")
    }

    override fun previousVideoEvent() {
        TODO("Not yet implemented")
    }

    override fun nextVideoEvent() {
        TODO("Not yet implemented")
    }

    override fun syncVideoEvent() {
        TODO("Not yet implemented")
    }

    override fun roomJoinedEvent() {
        TODO("Not yet implemented")
    }

    override fun sendRoomInfo() {
        socket.instance.emit("roomJoinInfo", ROOM)
    }

    override fun newParticipantJoinedEvent() {
        TODO("Not yet implemented")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RoomFrament.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(room : String, name : String = "Ghost Rider") =
            StreamingRoomFragment().apply {
                arguments = Bundle().apply {
                    putString(ROOM, room)
                    putString(USER, name)
                }
            }
    }
}