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

class SocketService{
    private val TAG = SocketService::class.java.canonicalName
    private val SOCKET_ENDPOINT = "http://192.168.43.133:5000"
    private var socket : Socket = IO.socket(URI.create(SOCKET_ENDPOINT))
    private var listener : SocketEventsListener? = null

    fun registerListener(listener: SocketEventsListener){
        this.listener = listener
    }

    fun unRegisterListener(listener: SocketEventsListener) {
        this.listener = null
    }

    fun initializeSocketAndConnect(){
        socket.on(Socket.EVENT_CONNECT) {
            listener?.connectionStatus(Socket.EVENT_CONNECT)
        }.on(Socket.EVENT_CONNECT_ERROR) {
            listener?.connectionStatus(Socket.EVENT_CONNECT_ERROR)
        }.on(Socket.EVENT_DISCONNECT) {
            listener?.connectionStatus(Socket.EVENT_DISCONNECT)
        }.on(SocketConstants.videoPlayed){
            listener?.playEvent()
        }.on(SocketConstants.videoPaused){
            listener?.pauseEvent()
        }.on(SocketConstants.previousVideo){
            listener?.previousVideoEvent()
        }.on(SocketConstants.nextVideo){
            listener?.nextVideoEvent()
        }.on(SocketConstants.videoSynced){
            listener?.syncVideoEvent()
        }.on(SocketConstants.participantJoined){
            val participant = App.gson.fromJson<ParticipantsItem>(
                it[0] as String,
                ParticipantsItem::class.java
            )
            listener?.newParticipantJoinedEvent(participant)
        }.on(SocketConstants.participantLeft){
            listener?.userLeft()
        }.on(SocketConstants.roomJoined){
            val response = App.gson.fromJson<JoinedRoomResponse>(
                it[0].toString(),
                JoinedRoomResponse::class.java
            )
            listener?.joinRoomResponse(response)
        }.on(SocketConstants.onMessage){
            val message = App.gson.fromJson<Message>(it[0] as String, Message::class.java)
            listener?.onMessage(message)
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

    interface SocketEventsListener{
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