package com.example.theatreapp

import android.content.Context
import android.media.MediaPlayer
import android.widget.MediaController
import org.videolan.libvlc.LibVLC

class PlayerController(
    var context: Context,
    var mediaPlayer: org.videolan.libvlc.MediaPlayer
) : MediaController.MediaPlayerControl {
    override fun start() {
        if(mediaPlayer.hasMedia() && !mediaPlayer.isPlaying) mediaPlayer.play()
    }

    override fun pause() {
        if(mediaPlayer.hasMedia() && mediaPlayer.isPlaying) mediaPlayer.pause()
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