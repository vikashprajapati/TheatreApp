package com.vikash.syncr_core.data.models.videoplaybackevents

data class VideoChanged(
    val userId : String,
    val timeStamp : String,
    val playbackDirection : String
)
