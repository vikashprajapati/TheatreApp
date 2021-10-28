package com.example.theatreapp.connections

import com.example.theatreapp.constants.SocketConstants.*
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URI

class SocketService : BaseObservable<SocketService.SocketEventListener>() {
 private val SOCKET_ENDPOINT = "http://192.168.43.133:5000"
 private var socket : Socket = IO.socket(URI.create(SOCKET_ENDPOINT))

 private fun handleConnection(connectionStatus : String){
  for (listener in getListeners()){
   listener.connectionStatus(connectionStatus)
  }
 }

 private fun joinRoom(response :String){
  for (listener in getListeners()){
   listener.joinRoomResponse(response)
  }
 }

 fun initializeSocketAndConnect(){
  socket.on(Socket.EVENT_CONNECT) {
   handleConnection(Socket.EVENT_CONNECT)
  }.on(Socket.EVENT_CONNECT_ERROR) {
   handleConnection(Socket.EVENT_CONNECT_ERROR)
  }.on(Socket.EVENT_DISCONNECT) {
   handleConnection(Socket.EVENT_DISCONNECT)
  }.on(VIDEO_PLAYED.name){
   for (listener in getListeners()){
    listener.playEvent()
   }
  }.on(VIDEO_PAUSED.name){
   for (listener in getListeners()){
    listener.pauseEvent()
   }
  }.on(PREVIOUS_VIDEO.name){
   for (listener in getListeners()){
    listener.previousVideoEvent()
   }
  }.on(NEXT_VIDEO.name){
   for (listener in getListeners()){
    listener.nextVideoEvent()
   }
  }.on(SYNCED_VIDEO.name){
   for (listener in getListeners()){
    listener.syncVideoEvent()
   }
  }.on(NEW_USER_JOINED.name){
   for (listener in getListeners()){
    listener.newParticipantJoinedEvent()
   }
  }.on(USER_LEFT.name){
   for (listener in getListeners()){
    listener.userLeft()
   }
  }.on(ROOM_JOINED.name){
   joinRoom(it[0].toString())
  }

  socket.connect()
 }

 fun send(eventType : String, args : List<Any>){
  // serialize args
  val room = args[0]
  val user = args[1]
  socket.emit(eventType, room, user)
  // check event type and emit event
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