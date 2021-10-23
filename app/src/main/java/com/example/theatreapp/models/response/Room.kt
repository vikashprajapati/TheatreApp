package com.example.theatreapp.models.response

import com.example.theatreapp.models.participants.Participant
import com.google.gson.annotations.SerializedName

data class Room(
    @SerializedName("name")
    var name : String,
    @SerializedName("host")
    var host : String,
    @SerializedName("participants")
    var participants : List<Participant>
) {
}