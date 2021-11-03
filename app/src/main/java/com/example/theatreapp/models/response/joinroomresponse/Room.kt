package com.example.theatreapp.models.response.joinroomresponse

data class Room(
	val name: String,
	val host: String,
	val participants: MutableList<ParticipantsItem>
)
