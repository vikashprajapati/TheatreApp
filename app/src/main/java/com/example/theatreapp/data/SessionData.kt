package com.example.theatreapp.data

import com.example.theatreapp.data.models.requests.User
import com.example.theatreapp.data.models.response.joinroomresponse.Room

object SessionData {
    var localUser : User? = null
    var currentRoom : Room? = null

    // participant map contains <userId, userName> mapping
    val participantsMap = mutableMapOf<String, String>()
}