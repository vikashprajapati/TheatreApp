package com.example.theatreapp.data.models.response.joinroomresponse

data class Room(
	val name: String,
	val host: String,
	val participants: MutableList<ParticipantsItem>
)
