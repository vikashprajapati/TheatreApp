package com.example.theatreapp.connections

import com.example.theatreapp.constants.SocketConstants.*
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URI

object Socket {
 private val SOCKET_ENDPOINT = "http://192.168.43.133:5000"
 private var socket : Socket = IO.socket(URI.create(SOCKET_ENDPOINT))
 private lateinit var listener: SocketEventListener
 val instance = socket

 fun initializeSocketEvents(){
  socket.on(Socket.EVENT_CONNECT) {
   listener.connectionStatus(Socket.EVENT_CONNECT)
  }.on(Socket.EVENT_CONNECT_ERROR) {
   listener.connectionStatus(Socket.EVENT_CONNECT_ERROR)
  }.on(Socket.EVENT_DISCONNECT) {
   listener.connectionStatus(Socket.EVENT_DISCONNECT)
  }.on(VIDEO_PLAYED.name){
   listener.playEvent()
  }.on(VIDEO_PAUSED.name){
   listener.pauseEvent()
  }.on(PREVIOUS_VIDEO.name){
   listener.previousVideoEvent()
  }.on(NEXT_VIDEO.name){
   listener.nextVideoEvent()
  }.on(SYNCED_VIDEO.name){
   listener.syncVideoEvent()
  }.on(NEW_USER_JOINED.name){
   listener.newParticipantJoinedEvent()
  }.on(USER_LEFT.name){
   listener.userLeft()
  }.on(ROOM_JOINED.name){
   listener.joinRoomResponse(it[0].toString())
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
  fun sendRoomInfo()
  fun newParticipantJoinedEvent()
  fun connectionStatus(eventConnect: String)
  fun joinRoomResponse(room : String)
  fun userLeft()
 }
}