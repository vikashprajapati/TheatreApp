package com.example.theatreapp.constants

enum class SocketConstants(event : String) {
    ON_MESSAGE("on message"),
    ROOM_JOINED("joined room response"),
    VIDEO_PLAYED("video played"),
    VIDEO_PAUSED("video paused"),
    PREVIOUS_VIDEO("previous video"),
    NEXT_VIDEO("next video"),
    NEW_USER_JOINED("participant joined"),
    USER_LEFT("participant left"),
    SYNCED_VIDEO("video synced")
}