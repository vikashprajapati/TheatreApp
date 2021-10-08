package com.example.theatreapp

interface MediaPlayerFragmentListener {
    fun onVideoPlayed()
    fun onVideoPaused()
    fun onPrevVideo()
    fun onNextVideo()
}