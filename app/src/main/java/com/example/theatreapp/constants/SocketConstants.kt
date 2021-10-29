package com.example.theatreapp.constants

interface SocketConstants {
    companion object{
        val onMessage: String
            get() = "on message"
        val roomJoined: String
            get() = "joined room response"
        val videoPlayed : String
            get() = "video played"
        val videoPaused : String
            get() = "video paused"
        val previousVideo : String
            get() = "previous video"
        val nextVideo : String
            get() = "next video"
        val participantJoined : String
            get() = "participant joined"
        val participantLeft : String
            get() = "participant left"
        val videoSynced : String
            get() = "video synced"
    }
}