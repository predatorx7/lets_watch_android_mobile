package com.magnificsoftware.letswatch.data_class.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.magnificsoftware.letswatch.data_class.plain.show_details.ShowDetails
import com.magnificsoftware.letswatch.data_class.plain.show_details.ShowDetailsResponse
import com.magnificsoftware.letswatch.ui.fragments.details.show_details.ShowType
import timber.log.Timber

@Entity
data class ShowVO(
    /**
     * ShowVO identifier created from concatenation of showId & userId
     *
     * Warning: Do not use this value as show identifier. Use [showId] instead.
     */
    @PrimaryKey
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("showId")
    val showId: Int?,
    @field:SerializedName("title")
    val title: String?,
    @field:SerializedName("description")
    val description: String?,
    @field:SerializedName("type")
    val type: String?,
    @field:SerializedName("poster")
    val poster: String?,
    @field:SerializedName("userId")
    val userId: String,
) {
    companion object {
        fun <T> from(value: ShowDetailsResponse<T>, userId: String): ShowVO where T : ShowDetails {
            val id = "${userId}${value.showId}"
            if (userId.isBlank()) {
                Timber.w("userId must not be BLANK")
            }
            return ShowVO(
                id,
                value.showId,
                value.title,
                value.description,
                if (value.isMovie) "movie" else "series",
                value.poster,
                userId,
            )
        }
    }

    val showType: ShowType get() = if (type == "movie") ShowType.Movie else ShowType.Series
}
