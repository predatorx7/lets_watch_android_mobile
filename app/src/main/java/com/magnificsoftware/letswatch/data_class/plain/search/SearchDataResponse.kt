package com.magnificsoftware.letswatch.data_class.plain.search

import com.google.gson.annotations.SerializedName
import com.magnificsoftware.letswatch.data_class.plain.show.VideoShow

data class SearchDataResponse(
    @field:SerializedName("data")
    val data: RidiculousSearchDataResponse
)

data class RidiculousSearchDataResponse(
    @field:SerializedName("data")
    val data: List<VideoShow>
)