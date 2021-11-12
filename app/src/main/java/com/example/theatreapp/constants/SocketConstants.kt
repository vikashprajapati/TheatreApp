package com.example.theatreapp.constants

interface SocketConstants {
    interface IncomingEvents{
        companion object{
            val onMessage: String
                get() = "on message"
            val onRoomJoined: String
                get() = "joined room response"
            val onVideoPlayed : String
                get() = "video played"
            val onVideoPaused : String
                get() = "video paused"
            val onPreviousVideo : String
                get() = "previous video"
            val onNextVideo : String
                get() = "next video"
            val onParticipantJoined : String
                get() = "participant joined"
            val onParticipantLeft : String
                get() = "participant left"
            val onVideoSynced : String
                get() = "video synced"
        }
    }

    interface OutgoingEvents{
        companion object{
            val sendMessage: String
                get() = "on message"
            val sendJoinRoom: String
                get() = "join room"
            val videoPlayback : String
                get() = "video playback"
            val videoChanged : String
                get() = "video changed"
            val videoSynced : String
                get() = "video synced"
            val leaveRoom : String
                get() = "leave room"
        }
    }
}