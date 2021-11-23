package com.vikash.syncr_core.data.models.response.joinroomresponse

data class Room(
	val name: String,
	val host: String,
	val participants: MutableList<ParticipantsItem>
)
