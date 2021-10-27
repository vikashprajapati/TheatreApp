package com.example.theatreapp.connections

import com.example.theatreapp.connections.SocketService

class SocketManager() : SocketService.SocketEventListener {

    private val socketService = SocketService()


    // Events sent to the socket
    fun startListeningToServer(){
        socketService.initializeSocketAndConnect()
        socketService.registerListener(this)
    }


    // Events from the socket server
    override fun playEvent() {
        TODO("Not yet implemented")
    }

    override fun pauseEvent() {
        TODO("Not yet implemented")
    }

    override fun previousVideoEvent() {
        TODO("Not yet implemented")
    }

    override fun nextVideoEvent() {
        TODO("Not yet implemented")
    }

    override fun syncVideoEvent() {
        TODO("Not yet implemented")
    }

    override fun roomJoinedEvent() {
        TODO("Not yet implemented")
    }

    override fun sendRoomInfo() {
        TODO("Not yet implemented")
    }

    override fun newParticipantJoinedEvent() {
        TODO("Not yet implemented")
    }

    override fun connectionStatus(eventConnect: String) {
        TODO("Not yet implemented")
    }

    override fun joinRoomResponse(room: String) {
        TODO("Not yet implemented")
    }

    override fun userLeft() {
        TODO("Not yet implemented")
    }
}