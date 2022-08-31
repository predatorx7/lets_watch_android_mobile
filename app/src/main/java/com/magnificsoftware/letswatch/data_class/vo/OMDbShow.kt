package com.magnificsoftware.letswatch.data_class.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity
data class OMDbShow(
    @field:SerializedName("Title")
    val title: String?,
    @field:SerializedName("Year")
    val year: String?,
    @PrimaryKey
    @field:SerializedName("imdbID")
    val imdbID: String,
    @field:SerializedName("Type")
    val type: String?,
    @field:SerializedName("Poster")
    val poster: String?,
) {
    val isMovie: Boolean get() = type == "movie"

    val isSeries: Boolean get() = type == "series"
}
