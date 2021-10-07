package com.example.theatreapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.theatreapp.databinding.ActivityMainBinding
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.transports.Polling.NAME
import io.socket.engineio.client.transports.WebSocket.NAME
import okhttp3.WebSocket
import java.net.URI

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val TAG : String = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // socket connection
        var uri : URI = URI.create("http://192.168.43.133:5000")

        val socket : Socket = IO.socket(uri)

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
        }

        socket.connect()

        binding.sendButton.setOnClickListener {
            if(binding.messageEditText.text.isEmpty())
                Toast.makeText( this@MainActivity, "Enter Message", Toast.LENGTH_SHORT).show()

            if(socket.connected())
                socket.emit("onMessage", "Theater 1", binding.messageEditText.text.toString())
        }
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
}