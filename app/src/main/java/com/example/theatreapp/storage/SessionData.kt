package com.example.theatreapp.storage

import com.example.theatreapp.models.participants.Participant
import com.example.theatreapp.models.requests.User
import com.example.theatreapp.models.response.joinroomresponse.ParticipantsItem
import com.example.theatreapp.models.response.joinroomresponse.Room

object SessionData {
    lateinit var participants : MutableList<ParticipantsItem>
    var localUser : User? = null
    var currentRoom : Room? = null
}