package com.magnificsoftware.letswatch.data_class.plain.show_details

import com.google.gson.annotations.SerializedName

data class SeriesShowMetadata(
    @field:SerializedName("tv_show_id")
    val showId: Int?,
    @field:SerializedName("tv_show_name")
    val showName: String?,
    @field:SerializedName("no_of_episodes")
    val totalEpisodes: Int?,
    @field:SerializedName("tv_show_type")
    val showType: Int?,
    @field:SerializedName("no_of_season")
    val totalSeasons: Int?,
    @field:SerializedName("summary")
    val summary: String?,
    @field:SerializedName("show_image_1")
    val showImage1: String?,
    @field:SerializedName("show_image_2")
    val showImage2: String?,
    @field:SerializedName("rating_name")
    val ratingName: String?
)

data class SeriesEpisode(
    @field:SerializedName("episode_id")
    val episodeId: Int?,
    @field:SerializedName("tv_show_id")
    val tvShowId: Int?,
    @field:SerializedName("season_no")
    val seasonNumber: Int?,
    @field:SerializedName("episode_no")
    val episodeNumber: Int?,
    @field:SerializedName("episode_title")
    val episodeTitle: String?,
    @field:SerializedName("episode_summary")
    val episodeSummary: String?,
    @field:SerializedName("episode_runtime")
    val episodeRuntime: Int?,
    @field:SerializedName("episode_image_1")
    val episodeImage1: String?,
    @field:SerializedName("episode_image_2")
    val episodeImage2: String?,
    @field:SerializedName("release_date")
    val releaseDate: String?,
)

data class SeriesEpisodesData(
    @field:SerializedName("data")
    val episodes: List<SeriesEpisode>?
)

data class SeriesShowDetails(
    @field:SerializedName("tv_show")
    val seriesShow: List<SeriesShowMetadata>?,
    @field:SerializedName("episode")
    val episodesData: SeriesEpisodesData?,
    @field:SerializedName("userWishList")
    val userWishList: String?,
    @field:SerializedName("accountId")
    val accountId: String?,

    ) : ShowDetails {
    val metadata get() = seriesShow?.first()
}
