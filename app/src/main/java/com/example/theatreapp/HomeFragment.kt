package com.example.theatreapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.theatreapp.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment :
    Fragment(),
    MediaPlayerFragmentListener,
    Socket.SocketEventListener{
    private lateinit var binding : FragmentHomeBinding
    private lateinit var socket: Socket
    private lateinit var mediaPlayerFragment : MediaPlayerFragment
    private val TAG = HomeFragment.javaClass.canonicalName
    private val ROOM = "Testing Room"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        socket = Socket()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.playButton.setOnClickListener {
            if(binding.messageEditText.text.isEmpty())
                Toast.makeText( context, "Enter Message", Toast.LENGTH_SHORT ).show()

            if(socket.instance.connected())
                socket.instance.emit("onMessage", ROOM, binding.messageEditText.text.toString())
        }

        socket.setSocketListener(this)
        socket.initializeSocketEvents()
    }

    override fun onStart() {
        super.onStart()
        addMediaPlayerFragment()
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
        activity?.runOnUiThread {
            binding.connectionStatusTextview.text = eventConnect
            when(eventConnect) {
                io.socket.client.Socket.EVENT_CONNECT -> context?.getColor(R.color.teal_200)?.let {
                    binding.connectionStatusTextview.setTextColor(
                        it
                    )
                }
                io.socket.client.Socket.EVENT_CONNECT_ERROR,
                io.socket.client.Socket.EVENT_DISCONNECT -> context?.getColor(R.color.purple_700)?.let {
                    binding.connectionStatusTextview.setTextColor(
                        it
                    )

                }
            }
        }
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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}