package com.example.theatreapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.theatreapp.listeners.MediaPlayerFragmentListener
import com.example.theatreapp.R
import com.example.theatreapp.connections.Socket
import com.example.theatreapp.databinding.FragmentStreamingRoomBinding

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
    private lateinit var mediaPlayerFragment : MediaPlayerFragment

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
//        binding.playButton.setOnClickListener {
//            if(binding.messageEditText.text.isEmpty())
//                Toast.makeText( context, "Enter Message", Toast.LENGTH_SHORT ).show()
//
//            if(socket.instance.connected())
//                socket.instance.emit("onMessage", ROOM, binding.messageEditText.text.toString())
//        }

        socket.setSocketListener(this)
        socket.initializeSocketEvents()
    }

    override fun onStart() {
        super.onStart()
        addMediaPlayerFragment()
    }

    override fun onPause() {
        super.onPause()
        socket.instance.disconnect()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayerFragment.onStop()
    }

    private fun addMediaPlayerFragment(){
        mediaPlayerFragment = MediaPlayerFragment.newInstance()
        var fragmentTransaction : FragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.media_player_fragment_container, mediaPlayerFragment, MediaPlayerFragment.TAG)
        fragmentTransaction.commit()
        mediaPlayerFragment.addMediaPlayerFragmentListener(this)
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
//        activity?.runOnUiThread {
//            binding.connectionStatusTextview.text = eventConnect
//            when(eventConnect) {
//                io.socket.client.Socket.EVENT_CONNECT -> context?.getColor(R.color.teal_200)?.let {
//                    binding.connectionStatusTextview.setTextColor(
//                        it
//                    )
//                }
//                io.socket.client.Socket.EVENT_CONNECT_ERROR,
//                io.socket.client.Socket.EVENT_DISCONNECT -> context?.getColor(R.color.purple_700)?.let {
//                    binding.connectionStatusTextview.setTextColor(
//                        it
//                    )
//
//                }
//            }
//        }
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