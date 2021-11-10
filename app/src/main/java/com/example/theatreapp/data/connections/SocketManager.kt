package com.example.theatreapp.data.connections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private var played = MutableLiveData<Event<String>>()
    private var paused = MutableLiveData<Event<String>>()
    private var previousVideoPlayed = MutableLiveData<Event<String>>()
    private var nextVideoPlayed = MutableLiveData<Event<String>>()
    private var connectionStatus = MutableLiveData<Event<String>>()
    private var joinedRoomStatus = MutableLiveData<Event<JoinedRoomResponse>>()
    private var joinedRoom = MutableLiveData<Event<String>>()
    private var syncedVideo = MutableLiveData<Event<String>>()
    private var _participantJoined = MutableLiveData<Event<ParticipantsItem>>()
    private var _onMessage = MutableLiveData<Event<Message>>()

    val connectionState : LiveData<Event<String>> get() = connectionStatus
    val joinedRoomState : LiveData<Event<JoinedRoomResponse>> get() = joinedRoomStatus
    val participantJoined : LiveData<Event<ParticipantsItem>> get() = _participantJoined
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
        socketService.disconnectSocket()
    }

    fun joinRoom(userName : String, roomName : String){
        var joinRoomRequestParams = JoinRoomRequest().apply {
            room = Room(roomName)
            user = User(userName, "")
        }
        socketService.send("join room", joinRoomRequestParams)
    }

    fun sendChatMessage(msg : String){
        val message = Message(from = "", message = msg, timeStamp =  "now")
        socketService.send("on message", message)
    }

    fun sendVideoStartedEvent(playbackStatus: String) {
        socketService.send("on video playback", playbackStatus);
    }

    fun sendVideoChangedEvent(direction: String) {
        socketService.send("on video changed", direction);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Helper methods                                                                                                                   //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun isSocketConnected() : Boolean = socketService.isConnected()

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Received Events from the socket server                                                                                           //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    override fun playEvent() {
        played.value = Event("played")
    }

    override fun pauseEvent() {
        paused.value = Event("paused")
    }

    override fun previousVideoEvent() {
        previousVideoPlayed.value = Event("previousVideo")
    }

    override fun nextVideoEvent() {
        nextVideoPlayed.value = Event("nextVideo")
    }

    override fun syncVideoEvent() {
        syncedVideo.value = Event("syncedVideo")
    }

    override fun roomJoinedEvent() {
        joinedRoom.value = Event("joined room")
    }

    override fun newParticipantJoinedEvent(participant: ParticipantsItem) {
        SessionData.addNewParticipant(participant)
        _participantJoined.postValue(Event(participant))
    }

    override fun connectionStatus(eventConnect: String) {
        connectionStatus.postValue(Event(eventConnect))
    }

    override fun joinRoomResponse(joinedRoomResponse: JoinedRoomResponse) {
        SessionData.updateSessionData(joinedRoomResponse)
        joinedRoomStatus.postValue(Event(joinedRoomResponse))
    }

    override fun userLeft() {

    }

    override fun onMessage(message: Message) {
        _onMessage.postValue(Event(message))
    }
}