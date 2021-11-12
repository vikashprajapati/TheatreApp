package com.example.theatreapp.data.connections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.theatreapp.constants.SocketConstants
import com.example.theatreapp.constants.VideoPlaybackConstants
import com.example.theatreapp.data.SessionData
import com.example.theatreapp.data.models.Message
import com.example.theatreapp.utils.Event
import com.example.theatreapp.data.models.requests.JoinRoomRequest
import com.example.theatreapp.data.models.requests.Room
import com.example.theatreapp.data.models.requests.User
import com.example.theatreapp.data.models.response.joinroomresponse.JoinedRoomResponse
import com.example.theatreapp.data.models.response.joinroomresponse.ParticipantsItem

object SocketManager : SocketService.SocketEventsListener {
    // Too much responsiblity, class needs to be refactored
    private val socketService = SocketService()
    private var _playedVideo = MutableLiveData<Event<String>>()
    private var _pausedVideo = MutableLiveData<Event<String>>()
    private var _previousVideoPlayed = MutableLiveData<Event<String>>()
    private var _nextVideoPlayed = MutableLiveData<Event<String>>()
    private var _connectionStatus = MutableLiveData<Event<String>>()
    private var _joinedRoomStatus = MutableLiveData<Event<JoinedRoomResponse>>()
    private var _syncedVideo = MutableLiveData<Event<String>>()
    private var _participantJoined = MutableLiveData<Event<ParticipantsItem>>()
    private var _participantLeft = MutableLiveData<Event<ParticipantsItem>>()
    private var _onMessage = MutableLiveData<Event<Message>>()

    val connectionStatus : LiveData<Event<String>> get() = _connectionStatus
    val joinedRoomStatus : LiveData<Event<JoinedRoomResponse>> get() = _joinedRoomStatus
    val participantJoined : LiveData<Event<ParticipantsItem>> get() = _participantJoined
    val participantLeft : LiveData<Event<ParticipantsItem>> get() = _participantLeft
    val onMessage : LiveData<Event<Message>> get() = _onMessage

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Events sent to the socket                                                                                                        //
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
        var joinRoomRequestParams = JoinRoomRequest().apply {
            room = Room(roomName)
            user = User(userName, "")
        }
        socketService.send(SocketConstants.OutgoingEvents.sendJoinRoom, joinRoomRequestParams)
    }

    fun sendLeaveRoom(){
        socketService.send(SocketConstants.OutgoingEvents.leaveRoom, "")
    }

    fun sendChatMessage(msg : String){
        val message = Message(from = "", message = msg, timeStamp =  "now")
        socketService.send(SocketConstants.OutgoingEvents.sendMessage, message)
    }

    fun sendVideoStartedEvent(playbackStatus: String) {
        socketService.send(SocketConstants.OutgoingEvents.videoPlayback, playbackStatus);
    }

    fun sendVideoChangedEvent(direction: String) {
        socketService.send(SocketConstants.OutgoingEvents.videoChanged, direction);
    }

    fun sendVideoSyncEvent(currentTimestamp: String) {
        socketService.send(SocketConstants.OutgoingEvents.videoSynced, currentTimestamp);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Helper methods                                                                                                                   //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    fun isSocketConnected() : Boolean = socketService.isConnected()



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Received Events from the socket server                                                                                           //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    override fun playEvent() {
        _playedVideo.postValue(Event(VideoPlaybackConstants.videoPlayed))
    }

    override fun pauseEvent() {
        _pausedVideo.postValue(Event(VideoPlaybackConstants.videoPaused))
    }

    override fun previousVideoEvent() {
        _previousVideoPlayed.postValue(Event(VideoPlaybackConstants.prevVideo))
    }

    override fun nextVideoEvent() {
        _nextVideoPlayed.postValue(Event(VideoPlaybackConstants.nextVideo))
    }

    override fun syncVideoEvent() {
        _syncedVideo.postValue(Event(VideoPlaybackConstants.syncedVideo))
    }

    override fun newParticipantJoinedEvent(participant: ParticipantsItem) {
        SessionData.addNewParticipant(participant)
        _participantJoined.postValue(Event(participant))
    }

    override fun connectionStatus(eventConnect: String) {
        _connectionStatus.postValue(Event(eventConnect))
    }

    override fun joinRoomResponse(joinedRoomResponse: JoinedRoomResponse) {
        SessionData.updateSessionData(joinedRoomResponse)
        _joinedRoomStatus.postValue(Event(joinedRoomResponse))
    }

    override fun userLeft(participant: ParticipantsItem) {
        _participantLeft.postValue(Event(participant))
    }

    override fun onMessage(message: Message) {
        _onMessage.postValue(Event(message))
    }
}