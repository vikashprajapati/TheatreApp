package com.vikash.syncr_core.data.models.response.joinroomresponse

import com.vikash.syncr_core.data.models.requests.User
import com.google.gson.annotations.SerializedName

data class JoinedRoomResponse(
	val room: Room,
	@SerializedName("user")
	val localUser : User
)
