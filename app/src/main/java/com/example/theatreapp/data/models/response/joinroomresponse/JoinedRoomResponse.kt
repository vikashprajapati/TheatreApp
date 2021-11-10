package com.example.theatreapp.data.models.response.joinroomresponse

import com.example.theatreapp.data.models.requests.User
import com.google.gson.annotations.SerializedName

data class JoinedRoomResponse(
	val room: Room,
	@SerializedName("user")
	val localUser : User
)
