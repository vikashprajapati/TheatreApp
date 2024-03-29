package com.vikash.syncr_core.data.connections

import android.util.Log
import com.vikash.syncr_core.SyncrApplication
import com.vikash.syncr_core.constants.SocketConstants.IncomingEvents
import com.vikash.syncr_core.data.models.response.joinroomresponse.JoinedRoomResponse
import com.vikash.syncr_core.data.models.response.joinroomresponse.ParticipantsItem
import com.vikash.syncr_core.data.models.videoplaybackevents.NewVideoSelected
import com.vikash.syncr_core.data.models.videoplaybackevents.VideoChanged
import com.vikash.syncr_core.data.models.videoplaybackevents.VideoPlayback
import com.vikash.syncr_core.data.models.videoplaybackevents.VideoSynced
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URI

class SocketService {
    private var socket: Socket = IO.socket(URI.create(SOCKET_ENDPOINT))
    private var listener: SocketEventsListener? = null
    private var pingInterval: PingInterval = PingInterval(this)

    fun registerListener(listener: SocketEventsListener) {
        this.listener = listener
    }

    fun unRegisterListener() {
        this.listener = null
    }

    fun initializeSocketAndConnect() {
        socket.on(Socket.EVENT_CONNECT) {
            if (!pingInterval.isRunning())
                Thread(pingInterval).start()
            listener?.connectionStatus(Socket.EVENT_CONNECT)
        }.on(Socket.EVENT_CONNECT_ERROR) {
            listener?.connectionStatus(Socket.EVENT_CONNECT_ERROR)
        }.on(Socket.EVENT_DISCONNECT) {
            listener?.connectionStatus(Socket.EVENT_DISCONNECT)
        }.on(IncomingEvents.onVideoPlayback) {
            val videoPlayback = SyncrApplication.gson.fromJson(
                it[0] as String,
                VideoPlayback::class.java
            )
            listener?.playbackEvent(videoPlayback)
        }.on(IncomingEvents.onVideoChanged) {
            val videoChanged = SyncrApplication.gson.fromJson(
                it[0] as String,
                VideoChanged::class.java
            )
            listener?.videoJumpEvent(videoChanged)
        }.on(IncomingEvents.onVideoSynced) {
            val videoSynced = SyncrApplication.gson.fromJson(
                it[0] as String,
                VideoSynced::class.java
            )
            listener?.syncVideoEvent(videoSynced)
        }.on(IncomingEvents.onParticipantJoined) {
            val participant = SyncrApplication.gson.fromJson(
                it[0] as String,
                ParticipantsItem::class.java
            )
            listener?.newParticipantJoinedEvent(participant)
        }.on(IncomingEvents.onParticipantLeft) {
            val participant = SyncrApplication.gson.fromJson(
                it[0] as String,
                ParticipantsItem::class.java
            )
            listener?.userLeft(participant)
        }.on(IncomingEvents.onRoomJoined) {
            val response = SyncrApplication.gson.fromJson(
                it[0].toString(),
                JoinedRoomResponse::class.java
            )
            listener?.joinRoomResponse(response)
        }.on(IncomingEvents.onMessage) {
            listener?.onMessage(it[0] as String)
        }.on(IncomingEvents.onNewVideoSelected) {
            val videoDetails = SyncrApplication.gson.fromJson(
                it[0] as String,
                NewVideoSelected::class.java
            )
            listener?.newVideoSelectedEvent(videoDetails)
        }.on("pong") {
            val latency = System.currentTimeMillis() - pingInterval.startTime
            Log.i(TAG, "initializeSocketAndConnect: latency $latency")
        }

        socket.connect()
    }

    fun send(eventType: String, params: Any? = null) {
        if (params == null) socket.emit(eventType)
        // serialize args
        if (params !is String) {
            socket.emit(eventType, SyncrApplication.gson.toJson(params))
        } else socket.emit(eventType, params)
    }

    fun disconnect() {
        if (pingInterval.isRunning()) {
            pingInterval.stop()
        }
        socket.disconnect()
    }

    fun isConnected(): Boolean = socket.connected()

    interface SocketEventsListener {
        fun playbackEvent(videoPlayback: VideoPlayback)
        fun videoJumpEvent(videoChanged: VideoChanged)
        fun syncVideoEvent(videoSynced: VideoSynced)
        fun newVideoSelectedEvent(videoDetails: NewVideoSelected)
        fun bufferingEvent(bufferingStatus: Boolean)
        fun newParticipantJoinedEvent(participantsItem: ParticipantsItem)
        fun connectionStatus(eventConnect: String)
        fun joinRoomResponse(joinedRoomResponse: JoinedRoomResponse)
        fun userLeft(participantsItem: ParticipantsItem)
        fun onMessage(message: String)
    }

    companion object {
        private const val SOCKET_ENDPOINT = "http://syncr-server.herokuapp.com/"
        private val TAG = SocketService::class.java.canonicalName
    }
}