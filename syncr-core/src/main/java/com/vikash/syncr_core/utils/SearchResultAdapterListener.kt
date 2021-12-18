package com.vikash.syncr_core.utils

import com.vikash.syncr_core.data.models.response.youtube.searchResults.VideosItem

interface SearchResultAdapterListener {
    fun videoSelected(videoItem: VideosItem?)
}