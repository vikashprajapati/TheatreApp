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
    MediaPlayerFragmentListener
{
    private lateinit var binding : ActivityMainBinding
    private val TAG : String = "MainActivity"

    private lateinit var socket : Socket
    private lateinit var mediaPlayerFragment : MediaPlayerFragment
    private val SOCKET_ENDPOINT : String = "http://192.168.43.133:5000"
    private val THEATER_ROOM : String = "Theater 1"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupSocket()

        binding.playButton.setOnClickListener {
            if(binding.messageEditText.text.isEmpty())
                Toast.makeText( this@MainActivity, "Enter Message", Toast.LENGTH_SHORT).show()

            if(socket.connected())
                socket.emit("onMessage", THEATER_ROOM, binding.messageEditText.text.toString())
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

    private fun setupSocket(){
// socket connection
        socket = IO.socket(URI.create(SOCKET_ENDPOINT))

        socket.on(Socket.EVENT_CONNECT) {
//            binding.connectionStatusTextview.text = "connected"
            Log.i(TAG, "onCreate: connected")
            updateConnectionStatus("connected")
        }.on(Socket.EVENT_CONNECT_ERROR) { itr ->
//            binding.connectionStatusTextview.text = "error"
            Log.i(TAG, "onCreate: error")
            updateConnectionStatus("error")
        }.on(Socket.EVENT_DISCONNECT) {
//            binding.connectionStatusTextview.text = "disconnected"
            Log.i(TAG, "onCreate: disconnected")
            updateConnectionStatus("disconnected")
        }.on("onMessage"){
            Log.i(TAG, "onCreate: onMessage")
            runOnUiThread {
                Toast.makeText(this, "you got a new message ${it[0].toString()}", Toast.LENGTH_SHORT).show()
            }
        }.on("Theater Joined"){
            runOnUiThread{
                Toast.makeText(this, "Theater Joined", Toast.LENGTH_SHORT).show()
            }
        }.on("played"){
            runOnUiThread{
                mediaPlayerFragment.play()
            }
        }.on("paused"){
            runOnUiThread{
                mediaPlayerFragment.pause()
            }
        }.on("previousVideo"){
            runOnUiThread {
                mediaPlayerFragment.previousVideo()
            }
        }

        socket.connect()
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
        socket.emit("played", THEATER_ROOM)
    }

    override fun onVideoPaused() {
        socket.emit("paused", THEATER_ROOM)
    }

    override fun onPrevVideo() {
        socket.emit("previousVideo", THEATER_ROOM)
    }

    override fun onNextVideo() {
        socket.emit("nextVideo", THEATER_ROOM)
    }
}