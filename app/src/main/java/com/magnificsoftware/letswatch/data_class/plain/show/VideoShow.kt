package com.magnificsoftware.letswatch.data_class.plain.show

import com.google.gson.annotations.SerializedName
import com.magnificsoftware.letswatch.ui.fragments.tabScreens.search.models.SearchResult

data class VideoShow(
    @field:SerializedName("vc_id")
    val vc_id: Int?,
    @field:SerializedName("rating_id")
    val rating_id: Int?,
    @field:SerializedName("vs_image_1")
    val vs_image_1: String?,
    @field:SerializedName("vs_image_2")
    val vs_image_2: String?,

    @field:SerializedName("video_show_id")
    override val video_show_id: Int?,
    @field:SerializedName("video_show_title")
    override val video_show_title: String?,
    @field:SerializedName("vs_description")
    override val vs_description: String?,
    @field:SerializedName("vs_runtime")
    override val vs_runtime: String?,
    @field:SerializedName("vs_release_date")
    override val vs_release_date: String?,
    @field:SerializedName("tv_show_id")
    override val tv_show_id: Int?,
    @field:SerializedName("tv_show_name")
    override val tv_show_name: String?,
    @field:SerializedName("tv_show_type")
    override val tv_show_type: Int?,
    @field:SerializedName("content_type")
    override val content_type: Int?,
) : SeriesShow, MovieShow, SearchResult {
    override val name: String = video_show_title ?: tv_show_name ?: ""
    override val poster: String = vs_image_1 ?: vs_image_2 ?: ""
}