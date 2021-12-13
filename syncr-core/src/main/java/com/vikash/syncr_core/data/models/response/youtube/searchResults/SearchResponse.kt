package com.vikash.syncr_core.data.models.response.youtube.searchResults

import com.google.gson.annotations.SerializedName

data class SearchResponse(

	@field:SerializedName("regionCode")
	val regionCode: String? = null,

	@field:SerializedName("kind")
	val kind: String? = null,

	@field:SerializedName("nextPageToken")
	val nextPageToken: String? = null,

	@field:SerializedName("pageInfo")
	val pageInfo: PageInfo? = null,

	@field:SerializedName("items")
	val items: List<ItemsItem?>? = null
)

data class JsonMemberDefault(

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("height")
	val height: Int? = null
)

data class Medium(

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("height")
	val height: Int? = null
)

data class High(

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("height")
	val height: Int? = null
)

data class PageInfo(

	@field:SerializedName("totalResults")
	val totalResults: Int? = null,

	@field:SerializedName("resultsPerPage")
	val resultsPerPage: Int? = null
)

data class Id(

	@field:SerializedName("kind")
	val kind: String? = null,

	@field:SerializedName("videoId")
	val videoId: String? = null
)

data class Thumbnails(

	@field:SerializedName("default")
	val jsonMemberDefault: JsonMemberDefault? = null,

	@field:SerializedName("high")
	val high: High? = null,

	@field:SerializedName("medium")
	val medium: Medium? = null
)

data class Snippet(

	@field:SerializedName("publishTime")
	val publishTime: String? = null,

	@field:SerializedName("publishedAt")
	val publishedAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("thumbnails")
	val thumbnails: Thumbnails? = null,

	@field:SerializedName("channelId")
	val channelId: String? = null,

	@field:SerializedName("channelTitle")
	val channelTitle: String? = null,

	@field:SerializedName("liveBroadcastContent")
	val liveBroadcastContent: String? = null
)

data class ItemsItem(

	@field:SerializedName("snippet")
	val snippet: Snippet? = null,

	@field:SerializedName("kind")
	val kind: String? = null,

	@field:SerializedName("id")
	val id: Id? = null
)
