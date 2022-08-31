package com.magnificsoftware.letswatch.data_class.plain.category

import com.google.gson.annotations.SerializedName
import com.magnificsoftware.letswatch.data_class.plain.DataResponse

/**
 * A response received from API when querying for categories. Categories from this response is displayed on home page.
 * */
data class ShowCategoriesDataResponse(
    @field:SerializedName("status")
    override val status: Boolean,
    @field:SerializedName("message")
    override val message: String,
    @field:SerializedName("data")
    val categories: List<ShowCategoryData>,
) : DataResponse
