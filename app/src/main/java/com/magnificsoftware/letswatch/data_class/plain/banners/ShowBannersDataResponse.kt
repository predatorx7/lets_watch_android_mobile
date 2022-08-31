package com.magnificsoftware.letswatch.data_class.plain.banners

import com.google.gson.annotations.SerializedName
import com.magnificsoftware.letswatch.data_class.plain.DataResponse

data class ShowBannersDataResponse(
    @field:SerializedName("status")
    override val status: Boolean,
    @field:SerializedName("message")
    override val message: String,
    @field:SerializedName("data")
    val banners: List<ShowBanner>,
) : DataResponse