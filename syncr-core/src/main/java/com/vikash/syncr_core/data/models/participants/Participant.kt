package com.vikash.syncr_core.data.models.participants

import com.google.gson.annotations.SerializedName

data class Participant(
    @SerializedName("id")
    var id : String,
    @SerializedName("name")
    var name : String,
    var onlineStatus : String
)
