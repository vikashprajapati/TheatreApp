package com.example.theatreapp.connections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.theatreapp.constants.SocketConstants
import com.example.theatreapp.utils.Event
import com.example.theatreapp.models.requests.JoinRoomRequest
import com.example.theatreapp.models.requests.Room
import com.example.theatreapp.models.requests.User
import com.example.theatreapp.models.response.joinroomresponse.JoinedRoomResponse
import com.example.theatreapp.models.response.joinroomresponse.ParticipantsItem
import com.example.theatreapp.storage.SessionData

object SocketManager : SocketService.SocketEventListener {

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

    val connectionState : LiveData<Event<String>> get() = connectionStatus
    val joinedRoomState : LiveData<Event<JoinedRoomResponse>> get() = joinedRoomStatus
    val participantJoined : LiveData<Event<ParticipantsItem>> get() = _participantJoined


    // Events sent to the socket
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
            user = User(userName)
        }
        socketService.send("join room", joinRoomRequestParams)
    }

    fun isSocketConnected() : Boolean = socketService.isConnected()

    // Events from the socket server
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
        _participantJoined.postValue(Event(participant))
        SessionData.currentRoom?.participants?.add(participant)
    }

    override fun connectionStatus(eventConnect: String) {
        connectionStatus.postValue(Event(eventConnect))
    }

    override fun joinRoomResponse(joinedRoomResponse: JoinedRoomResponse) {
        joinedRoomStatus.postValue(Event(joinedRoomResponse))
    }

    override fun userLeft() {
        TODO("Not yet implemented")
    }
}