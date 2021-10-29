package com.example.theatreapp.connections

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.theatreapp.Event
import com.example.theatreapp.connections.SocketService
import com.example.theatreapp.models.requests.JoinRoomRequest
import com.example.theatreapp.models.requests.Room
import com.example.theatreapp.models.requests.User
import com.example.theatreapp.models.response.joinroomresponse.JoinedRoomResponse

class SocketManager() : SocketService.SocketEventListener {

    private val socketService = SocketService()
    private var played = MutableLiveData<Event<String>>()
    private var paused = MutableLiveData<Event<String>>()
    private var previousVideoPlayed = MutableLiveData<Event<String>>()
    private var nextVideoPlayed = MutableLiveData<Event<String>>()
    private var connectionStatus = MutableLiveData<Event<String>>()
    private var joinedRoomStatus = MutableLiveData<Event<JoinedRoomResponse>>()
    private var joinedRoom = MutableLiveData<Event<String>>()

    val connectionState : MutableLiveData<Event<String>> get() = connectionStatus
    val joinedRoomState : MutableLiveData<Event<JoinedRoomResponse>> get() = joinedRoomStatus


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

    }

    override fun roomJoinedEvent() {
        joinedRoom.value = Event("joined room")
    }

    override fun sendRoomInfo() {
        TODO("Not yet implemented")
    }

    override fun newParticipantJoinedEvent() {
        TODO("Not yet implemented")
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