package com.example.theatreapp

import android.util.Log
import android.widget.Toast
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URI

class Socket {
 private val SOCKET_ENDPOINT = "http://192.168.43.133:5000"
 private var socket : Socket = IO.socket(URI.create(SOCKET_ENDPOINT))
 private lateinit var listener: SocketEventListener
 val instance = socket

 fun setSocketListener(socketEventListener: SocketEventListener){
  listener = socketEventListener
 }

 fun initializeSocketEvents(){
  socket.on(Socket.EVENT_CONNECT) {
   listener.connectionStatus(Socket.EVENT_CONNECT)
  }.on(Socket.EVENT_CONNECT_ERROR) {
   listener.connectionStatus(Socket.EVENT_CONNECT_ERROR)
  }.on(Socket.EVENT_DISCONNECT) {
   listener.connectionStatus(Socket.EVENT_DISCONNECT)
  }.on("Theater Joined"){
   listener.roomJoinedEvent()
  }.on("played"){
   listener.playEvent()
  }.on("paused"){
   listener.pauseEvent()
  }.on("previousVideo"){
   listener.previousVideoEvent()
  }
  socket.connect()
 }

 interface SocketEventListener{
  fun playEvent()
  fun pauseEvent()
  fun previousVideoEvent()
  fun nextVideoEvent()
  fun syncVideoEvent()
  fun roomJoinedEvent()
  fun newParticipantJoinedEvent()
  fun connectionStatus(eventConnect: String)
 }
}