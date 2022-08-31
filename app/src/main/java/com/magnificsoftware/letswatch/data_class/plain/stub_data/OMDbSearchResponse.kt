package com.magnificsoftware.letswatch.data_class.plain.stub_data

import com.google.gson.annotations.SerializedName
import com.magnificsoftware.letswatch.data_class.vo.OMDbShow

data class OMDbSearchResponse(
    @field:SerializedName("Search")
    val searchResult: List<OMDbShow>?,
    @field:SerializedName("totalResults")
    val totalResults: String?,
    @field:SerializedName("Response")
    val response: String?
)

val OMDbSearchResponse.isSuccess: Boolean get() = response == "True"