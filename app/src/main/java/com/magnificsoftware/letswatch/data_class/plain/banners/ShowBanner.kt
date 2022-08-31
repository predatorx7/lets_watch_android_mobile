package com.magnificsoftware.letswatch.data_class.plain.banners

import com.google.gson.annotations.SerializedName

data class ShowBanner(
    @field:SerializedName("banner_is_active")
    val banner_is_active: String,
    @field:SerializedName("content_type")
    val content_type: Int,
    @field:SerializedName("country_id")
    val country_id: Int,
    @field:SerializedName("m_banner_image_url_1")
    val m_banner_image_url_1: String,
    @field:SerializedName("m_banner_type")
    val m_banner_type: String,
    @field:SerializedName("order")
    val order: String,
    @field:SerializedName("redirect_url")
    val redirect_url: String
)