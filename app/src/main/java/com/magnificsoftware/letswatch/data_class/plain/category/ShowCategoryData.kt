package com.magnificsoftware.letswatch.data_class.plain.category

import com.google.gson.annotations.SerializedName
import com.magnificsoftware.letswatch.data_class.plain.show.VideoShow

/**
 * A Show Category which contains list of shows
 * @param [vc_id] Show category id
 * @param [vc_name] Show category name
 * @param [order] The order in which this category should appear in list of categories. Use
 * this value for sorting [ShowCategoryData]
 * */
data class ShowCategoryData(
    @field:SerializedName("vc_id")
    val vc_id: Int,
    @field:SerializedName("order")
    val order: Int,
    @field:SerializedName("vc_name")
    val vc_name: String,
    @field:SerializedName("video_shows")
    val video_shows: List<VideoShow>
)