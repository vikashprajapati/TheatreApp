package com.example.theatreapp.data.connections

import android.util.Log
import com.example.theatreapp.App
import com.example.theatreapp.constants.SocketConstants
import com.example.theatreapp.data.SessionData
import com.example.theatreapp.data.models.Message
import com.example.theatreapp.data.models.response.joinroomresponse.JoinedRoomResponse
import com.example.theatreapp.data.models.response.joinroomresponse.ParticipantsItem
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URI

class SocketService : BaseObservable<SocketService.SocketEvents>() {
    private val TAG = SocketService::class.java.canonicalName
    private val SOCKET_ENDPOINT = "http://192.168.43.133:5000"
    private var socket : Socket = IO.socket(URI.create(SOCKET_ENDPOINT))

    private fun handleConnection(connectionStatus : String){
        for (listener in getListeners()){
            listener.connectionStatus(connectionStatus)
        }
    }

    private fun joinRoom(response :String){
        val response = App.gson.fromJson<JoinedRoomResponse>(response, JoinedRoomResponse::class.java)
        SessionData.updateSessionData(response)
        for (listener in getListeners()){
            listener.joinRoomResponse(response)
        }
    }

    private fun participantJoined(response : String){
        val participant = App.gson.fromJson<ParticipantsItem>(response as String, ParticipantsItem::class.java)
        SessionData.addNewParticipant(participant)
        for (listener in getListeners()){
            listener.newParticipantJoinedEvent(participant)
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
            participantJoined(it[0].toString())
        }.on(SocketConstants.participantLeft){
            for (listener in getListeners()){
                listener.userLeft()
            }
        }.on(SocketConstants.roomJoined){
            joinRoom(it[0].toString())
        }.on(SocketConstants.onMessage){
            val message = App.gson.fromJson<Message>(it[0] as String, Message::class.java)
            for(listener in getListeners()){
                listener.onMessage(message)
            }
        }

        socket.connect()
    }

    fun send(eventType : String, params: Any?){
        // serialize args
        val params = App.gson.toJson(params)
        socket.emit(eventType, params)
    }

    fun disconnectSocket(){
        socket.emit("leave room")
    }

    fun isConnected() : Boolean = socket.connected()

    interface SocketEvents{
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
        fun onMessage(message: Message)
    }
}