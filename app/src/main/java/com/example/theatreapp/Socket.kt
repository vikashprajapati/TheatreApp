package com.example.theatreapp

import android.util.Log
import android.widget.Toast
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URI

class Socket {
 private val SOCKET_ENDPOINT = "http://192.168.43.133:5000"
 private lateinit var socket : Socket
 private lateinit var listener: SocketEventListener
 init {
  socket = IO.socket(URI.create(SOCKET_ENDPOINT))
 }

 public fun getInstance() : Socket = socket

 fun setSocketListener(socketEventListener: SocketEventListener){
  listener = socketEventListener
 }

 fun initializeSocketEvents(){
  socket.on(Socket.EVENT_CONNECT) {
   listener.connectionSuccess()
  }.on(Socket.EVENT_CONNECT_ERROR) {
   listener.connectionError()
  }.on(Socket.EVENT_DISCONNECT) {
   listener.connectionFailed()
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
  fun connectionSuccess()
  fun connectionFailed()
  fun connectionError()
  fun playEvent()
  fun pauseEvent()
  fun previousVideoEvent()
  fun nextVideoEvent()
  fun syncVideoEvent()
  fun roomJoinedEvent()
  fun newParticipantJoinedEvent()
 }
}