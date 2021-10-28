package com.example.theatreapp.connections

import androidx.lifecycle.MutableLiveData
import com.example.theatreapp.Event
import com.example.theatreapp.connections.SocketService

class SocketManager() : SocketService.SocketEventListener {

    private val socketService = SocketService()
    private var played = MutableLiveData<Event<String>>()
    private var paused = MutableLiveData<Event<String>>()
    private var previousVideoPlayed = MutableLiveData<Event<String>>()
    private var nextVideoPlayed = MutableLiveData<Event<String>>()
    private var connectionStatus = MutableLiveData<Event<String>>()
    private var joinedRoom = MutableLiveData<Event<String>>()

    val connectionState : MutableLiveData<Event<String>> get() = connectionStatus


    // Events sent to the socket
    fun startListeningToServer(){
        socketService.registerListener(this)
        socketService.initializeSocketAndConnect()
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

    override fun joinRoomResponse(room: String) {
        TODO("Not yet implemented")
    }

    override fun userLeft() {
        TODO("Not yet implemented")
    }
}