package com.vikash.syncr_core.events

data class VideoUrlExtractedEvent(
    val videoUrl : String,
    val videoTitle: String
) {
}