package com.vikash.syncr_core.utils

import com.vikash.syncr_core.data.models.response.youtube.searchResults.ItemsItem

interface SearchResultAdapterListener {
    fun videoSelected(videoItem: ItemsItem?)
}