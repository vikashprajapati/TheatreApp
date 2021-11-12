package com.example.theatreapp.constants

interface SocketConstants {
    interface IncomingEvents{
        companion object{
            val onMessage: String
                get() = "on message"
            val onRoomJoined: String
                get() = "joined room response"
            val onVideoPlayback : String
                get() = "video playback"
            val onVideoChanged : String
                get() = "video changed"
            val onVideoSynced : String
                get() = "video synced"
            val onParticipantJoined : String
                get() = "participant joined"
            val onParticipantLeft : String
                get() = "participant left"
        }
    }

    interface OutgoingEvents{
        companion object{
            val sendMessage: String
                get() = "on message"
            val sendJoinRoom: String
                get() = "join room"
            val sendVideoPlayback : String
                get() = "video playback"
            val sendVideoChanged : String
                get() = "video changed"
            val sendVideoSynced : String
                get() = "video synced"
            val sendLeaveRoom : String
                get() = "leave room"
        }
    }
}