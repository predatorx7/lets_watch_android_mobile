package com.magnificsoftware.letswatch.data_class.plain.show_details

import com.google.gson.annotations.SerializedName

data class MovieShowDetails(
    @field:SerializedName("video_show_id")
    val videoShowId: Int?,
    @field:SerializedName("tvs_id")
    val tvsId: Int?,
    @field:SerializedName("video_show_title")
    val videoShowTitle: String?,
    @field:SerializedName("vc_id")
    val vcId: Int,
    @field:SerializedName("genre_id")
    val genreId: Int?,
    @field: SerializedName("ct_id")
    val ctId: Int?,
    @field:SerializedName("tst_id")
    val tsdId: Int?,
    @field:SerializedName("trailer_hls_group")
    val trailerHlsGroup: String?,
    @field:SerializedName("hls_group")
    val hlsGroup: String?,
    @field:SerializedName("video_url")
    val videoUrl: String?,
    @field:SerializedName("vs_description")
    val vsDescription: String?,
    @field:SerializedName("vs_runtime")
    val vsRuntime: String?,
    @field:SerializedName("vs_release_date")
    val vsReleaseDate: String?,
    @field:SerializedName("vs_image_1")
    val vsImage1: String?,
    @field:SerializedName("vs_image_2")
    val vsImage2: String?,
    @field:SerializedName("trailer_url")
    val trailerUrl: String?,
    @field:SerializedName("rating_name")
    val ratingName: String?,
    @field:SerializedName("ct_name")
    val ctName: String?,
    @field:SerializedName("userWishList")
    val userWishList: String?
) : ShowDetails