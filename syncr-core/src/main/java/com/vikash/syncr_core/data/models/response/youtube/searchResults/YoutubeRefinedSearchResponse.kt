package com.vikash.syncr_core.data.models.response.youtube.searchResults

data class YoutubeRefinedSearchResponse(
	val activeFilters: List<ActiveFiltersItem?>? = null,
	val correctedQuery: String? = null,
	val originalQuery: String? = null,
	val results: Int? = null,
	val items: List<VideosItem?>? = null,
	val continuation: Any? = null,
	val refinements: List<RefinementsItem?>? = null,
	val status: Boolean? = null
)

data class Author(
	val bestAvatar: BestAvatar? = null,
	val ownerBadges: List<String?>? = null,
	val name: String? = null,
	val verified: Boolean? = null,
	val channelID: String? = null,
	val url: String? = null,
	val avatars: List<AvatarsItem?>? = null
)

data class BestThumbnail(
	val width: Int? = null,
	val url: String? = null,
	val height: Int? = null
)

data class VideosItem(
	val author: Author? = null,
	val description: Any? = null,
	val bestThumbnail: BestThumbnail? = null,
	val type: String? = null,
	val title: String? = null,
	val url: String? = null,
	val badges: List<String?>? = null,
	val duration: String? = null,
	val isLive: Boolean? = null,
	val uploadedAt: String? = null,
	val id: String? = null,
	val thumbnails: List<ThumbnailsItem?>? = null,
	val isUpcoming: Boolean? = null,
	val upcoming: Any? = null,
	val views: Int? = null,
	val bestAvatar: BestAvatar? = null,
	val subscribers: String? = null,
	val name: String? = null,
	val verified: Boolean? = null,
	val videos: Int? = null,
	val descriptionShort: String? = null,
	val channelID: String? = null,
	val avatars: List<AvatarsItem?>? = null
)

data class AvatarsItem(
	val width: Int? = null,
	val url: String? = null,
	val height: Int? = null
)

data class ThumbnailsItem(
	val width: Int? = null,
	val url: String? = null,
	val height: Int? = null
)

data class ActiveFiltersItem(
	val name: String? = null,
	val active: Boolean? = null,
	val description: String? = null,
	val url: Any? = null
)

data class RefinementsItem(
	val Q: String? = null,
	val bestThumbnail: BestThumbnail? = null,
	val thumbnails: List<ThumbnailsItem?>? = null,
	val url: String? = null
)

data class BestAvatar(
	val width: Int? = null,
	val url: String? = null,
	val height: Int? = null
)

