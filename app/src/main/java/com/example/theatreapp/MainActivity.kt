package com.example.theatreapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.theatreapp.databinding.ActivityMainBinding
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.transports.Polling.NAME
import io.socket.engineio.client.transports.WebSocket.NAME
import okhttp3.WebSocket
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import java.net.URI

class MainActivity :
    AppCompatActivity(),
    MediaPlayerFragmentListener,
        com.example.theatreapp.Socket.SocketEventListener
{
    private lateinit var binding : ActivityMainBinding
    private val TAG : String = "MainActivity"

    private lateinit var mediaPlayerFragment : MediaPlayerFragment
    private val THEATER_ROOM : String = "Theater 1"
    private lateinit var socket: com.example.theatreapp.Socket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        socket = Socket()
        binding.playButton.setOnClickListener {
            if(binding.messageEditText.text.isEmpty())
                Toast.makeText( this@MainActivity, "Enter Message", Toast.LENGTH_SHORT).show()

            if(socket.getInstance().connected())
                socket.getInstance().emit("onMessage", THEATER_ROOM, binding.messageEditText.text.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        addMediaPlayerFragment()
    }

    private fun addMediaPlayerFragment(){
        mediaPlayerFragment = MediaPlayerFragment.newInstance()
        var fragmentTransaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.media_player_fragment_container, mediaPlayerFragment, MediaPlayerFragment.TAG)
        fragmentTransaction.commit()
        mediaPlayerFragment.addMediaPlayerFragmentListener(this)
    }

    override fun onStop() {
        super.onStop()
    }

    private fun updateConnectionStatus(status : String) {
        runOnUiThread(Runnable {
            binding.connectionStatusTextview.text = status
            when(status){
                "connected" -> binding.connectionStatusTextview.setTextColor(getColor(R.color.teal_200))
                "error", "disconnected" -> binding.connectionStatusTextview.setTextColor(getColor(R.color.purple_700))
            }
        })
    }

    override fun onVideoPlayed() {
        socket.getInstance().emit("played", THEATER_ROOM)
    }

    override fun onVideoPaused() {
        socket.getInstance().emit("paused", THEATER_ROOM)
    }

    override fun onPrevVideo() {
        socket.getInstance().emit("previousVideo", THEATER_ROOM)
    }

    override fun onNextVideo() {
        socket.getInstance().emit("nextVideo", THEATER_ROOM)
    }

    override fun connectionSuccess() {
        TODO("Not yet implemented")
    }

    override fun connectionFailed() {
        TODO("Not yet implemented")
    }

    override fun connectionError() {
        TODO("Not yet implemented")
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
}