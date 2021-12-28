package com.vikash.syncr_core.data.connections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vikash.syncr_core.constants.SocketConstants.OutgoingEvents
import com.vikash.syncr_core.data.SessionData
import com.vikash.syncr_core.data.models.Message
import com.vikash.syncr_core.data.models.requests.JoinRoomRequest
import com.vikash.syncr_core.data.models.requests.Room
import com.vikash.syncr_core.data.models.requests.User
import com.vikash.syncr_core.data.models.response.joinroomresponse.JoinedRoomResponse
import com.vikash.syncr_core.data.models.response.joinroomresponse.ParticipantsItem
import com.vikash.syncr_core.data.models.videoplaybackevents.NewVideoSelected
import com.vikash.syncr_core.data.models.videoplaybackevents.VideoChanged
import com.vikash.syncr_core.data.models.videoplaybackevents.VideoPlayback
import com.vikash.syncr_core.data.models.videoplaybackevents.VideoSynced
import com.vikash.syncr_core.utils.Event
import com.vikash.syncr_core.utils.Helpers

object SocketManager : SocketService.SocketEventsListener {
    // Too much responsibility, class needs to be refactored
    private val socketService = SocketService()
    private var _playbackVideo = MutableLiveData<Event<VideoPlayback>>()
    private var _changedVideo = MutableLiveData<Event<VideoChanged>>()
    private var _syncedVideo = MutableLiveData<Event<VideoSynced>>()
    private var _connectionStatus = MutableLiveData<String>()
    private var _joinedRoomStatus = MutableLiveData<Event<JoinedRoomResponse>>()
    private var _participantJoined = MutableLiveData<Event<ParticipantsItem>>()
    private var _participantLeft = MutableLiveData<Event<ParticipantsItem>>()
    private var _onMessage = MutableLiveData<Event<Message>>()
    private var _onNewVideo = MutableLiveData<Event<NewVideoSelected>>()
    private var _bufferingStatus = MutableLiveData<Boolean>()

    val connectionStatus : LiveData<String> get() = _connectionStatus
    val joinedRoomStatus : LiveData<Event<JoinedRoomResponse>> get() = _joinedRoomStatus
    val participantJoined : LiveData<Event<ParticipantsItem>> get() = _participantJoined
    val participantLeft : LiveData<Event<ParticipantsItem>> get() = _participantLeft
    val playbackVideo : LiveData<Event<VideoPlayback>> get() = _playbackVideo
    val changedVideo : LiveData<Event<VideoChanged>> get() = _changedVideo
    val syncedVideo : LiveData<Event<VideoSynced>> get() = _syncedVideo
    val onMessage : LiveData<Event<Message>> get() = _onMessage
    val onNewVideo : LiveData<Event<NewVideoSelected>> get() = _onNewVideo
    val bufferingStatus : LiveData<Boolean> get() = _bufferingStatus

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Outgoing Socket Events                                                                                                           //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun startListeningToServer(){
        socketService.registerListener(this)
        socketService.initializeSocketAndConnect()
    }

    fun stopListeningToServer(){
        socketService.unRegisterListener(this)
        sendLeaveRoom()
    }

    fun sendJoinRoom(userName : String, roomName : String){
        val joinRoomRequestParams = JoinRoomRequest().apply {
            room = Room(roomName)
            user = User(userName, "")
        }
        socketService.send(OutgoingEvents.sendJoinRoom, joinRoomRequestParams)
    }

    private fun sendLeaveRoom(){
        socketService.send(OutgoingEvents.sendLeaveRoom, "")
    }

    fun sendChatMessage(msg : String){
        val message = Message(from = "", message = msg, timeStamp = Helpers.getCurrentTime())
        socketService.send(OutgoingEvents.sendMessage, message)
    }

    fun sendVideoStartedEvent(playbackStatus: String) {
        socketService.send(OutgoingEvents.sendVideoPlayback, playbackStatus)
    }

    fun sendVideoJumpEvent(direction: String, timeStamp: Long) {
        val videoJumpEvent = VideoChanged(
            SessionData.localUser!!.id,
            timeStamp.toString(),
            direction
        )
        socketService.send(OutgoingEvents.sendVideoChanged, videoJumpEvent)
    }

    fun sendVideoSyncEvent(currentTimestamp: String) {
        socketService.send(OutgoingEvents.sendVideoSynced, currentTimestamp)
    }

    fun sendNewVideoSelectedEvent(videoDetails: NewVideoSelected) {
        socketService.send(OutgoingEvents.sendNewVideoUrl, videoDetails)
    }

    fun sendVideoBufferingEvent(isBuffering : Boolean) {
        socketService.send(OutgoingEvents.sendBufferingStatus, isBuffering)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Helper methods                                                                                                                   //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    fun isSocketConnected() : Boolean = socketService.isConnected()



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Incoming Socket Events                                                                                                           //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun playbackEvent(videoPlayback: VideoPlayback) {
        _playbackVideo.postValue(Event(videoPlayback))
    }

    override fun videoJumpEvent(videoChanged: VideoChanged) {
        _changedVideo.postValue(Event(videoChanged))
    }

    override fun syncVideoEvent(videoSynced: VideoSynced) {
        _syncedVideo.postValue(Event(videoSynced))
    }

    override fun newParticipantJoinedEvent(participantsItem: ParticipantsItem) {
        SessionData.addNewParticipant(participantsItem)
        _participantJoined.postValue(Event(participantsItem))
    }

    override fun connectionStatus(eventConnect: String) {
        _connectionStatus.postValue(eventConnect)
    }

    override fun joinRoomResponse(joinedRoomResponse: JoinedRoomResponse) {
        SessionData.updateSessionData(joinedRoomResponse)
        _joinedRoomStatus.postValue(Event(joinedRoomResponse))
    }

    override fun userLeft(participantsItem: ParticipantsItem) {
        _participantLeft.postValue(Event(participantsItem))
    }

    override fun onMessage(message: Message) {
        _onMessage.postValue(Event(message))
    }

    override fun newVideoSelectedEvent(videoDetails: NewVideoSelected) {
        _onNewVideo.postValue(Event(videoDetails))
    }

    override fun bufferingEvent(bufferingStatus: Boolean) {
        _bufferingStatus.postValue(bufferingStatus)
    }
}