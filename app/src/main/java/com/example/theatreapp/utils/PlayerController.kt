package com.example.theatreapp.utils

import android.content.Context
import android.widget.MediaController
import com.example.theatreapp.ui.listeners.PlaybackListener

class PlayerController(
    var context: Context,
    var mediaPlayer: org.videolan.libvlc.MediaPlayer,
    var playbackListener: PlaybackListener
) : MediaController.MediaPlayerControl {
    override fun start() {
        if(mediaPlayer.hasMedia() && !mediaPlayer.isPlaying){
            mediaPlayer.play()
            playbackListener.onVideoPlayed()
        }
    }

    override fun pause() {
        if(mediaPlayer.hasMedia() && mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            playbackListener.onVideoPaused()
        }
    }

    override fun getDuration(): Int = mediaPlayer.length.toInt()

    override fun getCurrentPosition(): Int = mediaPlayer.position.toInt() * duration

    override fun seekTo(pos: Int) {
        mediaPlayer.position = pos.toFloat() / duration
    }

    override fun isPlaying(): Boolean = mediaPlayer.isPlaying

    override fun getBufferPercentage(): Int = 0

    override fun canPause(): Boolean = true

    override fun canSeekBackward(): Boolean = true

    override fun canSeekForward(): Boolean = true

    override fun getAudioSessionId(): Int = 0
}