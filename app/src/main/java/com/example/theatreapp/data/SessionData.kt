package com.example.theatreapp.data

import com.example.theatreapp.data.models.requests.User
import com.example.theatreapp.data.models.response.joinroomresponse.JoinedRoomResponse
import com.example.theatreapp.data.models.response.joinroomresponse.ParticipantsItem
import com.example.theatreapp.data.models.response.joinroomresponse.Room

object SessionData {
    var localUser : User? = null
    var currentRoom : Room? = null

    // participant map contains <userId, userName> mapping
    val participantsMap = mutableMapOf<String, String>()

    fun updateSessionData(response: JoinedRoomResponse){
        localUser = response.localUser
        currentRoom = response.room

        for(participant in response.room.participants){
            participantsMap[participant.id] = participant.name
        }
    }

    fun addNewParticipant(participant : ParticipantsItem){
        participantsMap[participant.id] = participant.name
        currentRoom?.participants?.add(participant)
    }

    fun getParticipantName(userId : String) : String = participantsMap[userId] ?: userId
}