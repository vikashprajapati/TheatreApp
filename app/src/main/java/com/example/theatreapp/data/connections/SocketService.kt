package com.example.theatreapp.data.connections

import com.example.theatreapp.App
import com.example.theatreapp.constants.SocketConstants
import com.example.theatreapp.data.models.response.joinroomresponse.JoinedRoomResponse
import com.example.theatreapp.data.models.response.joinroomresponse.ParticipantsItem
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
            val response = App.gson.fromJson<JoinedRoomResponse>(response, JoinedRoomResponse::class.java)
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
        }.on(SocketConstants.videoPlayed){
            for (listener in getListeners()){
                listener.playEvent()
            }
        }.on(SocketConstants.videoPaused){
            for (listener in getListeners()){
                listener.pauseEvent()
            }
        }.on(SocketConstants.previousVideo){
            for (listener in getListeners()){
                listener.previousVideoEvent()
            }
        }.on(SocketConstants.nextVideo){
            for (listener in getListeners()){
                listener.nextVideoEvent()
            }
        }.on(SocketConstants.videoSynced){
            for (listener in getListeners()){
                listener.syncVideoEvent()
            }
        }.on(SocketConstants.participantJoined){
            val participant = App.gson.fromJson<ParticipantsItem>(it[0] as String, ParticipantsItem::class.java)
            for (listener in getListeners()){
                listener.newParticipantJoinedEvent(participant)
            }
        }.on(SocketConstants.participantLeft){
            for (listener in getListeners()){
                listener.userLeft()
            }
        }.on(SocketConstants.roomJoined){
            joinRoom(it[0].toString())
        }

        socket.connect()
    }

    fun send(eventType : String, params: Any){
        // serialize args
        val params = App.gson.toJson(params)
        socket.emit(eventType, params)
    }

    fun disconnectSocket(){
        socket.emit("leave room")
    }

    fun isConnected() : Boolean = socket.connected()

    interface SocketEventListener{
        fun playEvent()
        fun pauseEvent()
        fun previousVideoEvent()
        fun nextVideoEvent()
        fun syncVideoEvent()
        fun roomJoinedEvent()
        fun newParticipantJoinedEvent(participantsItem: ParticipantsItem)
        fun connectionStatus(eventConnect: String)
        fun joinRoomResponse(joinedRoomResponse: JoinedRoomResponse)
        fun userLeft()
    }
}