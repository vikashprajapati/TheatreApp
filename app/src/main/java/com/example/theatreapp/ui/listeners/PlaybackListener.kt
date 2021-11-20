package com.example.theatreapp.ui.listeners

interface PlaybackListener {
    fun onVideoPlayed()
    fun onVideoPaused()
    fun onVideoJumpForward()
    fun onVideoJumpBackward()
    fun onVideoSync(timeStamp : String)
}