package com.magnificsoftware.letswatch.data_class.plain.show_details

import com.google.gson.annotations.SerializedName
import com.magnificsoftware.letswatch.ui.fragments.details.show_details.ShowType

data class MovieShowDetailsResponse(
    @field:SerializedName("data")
    val data: List<MovieShowDetails?>?,
) : ShowDetailsResponse<MovieShowDetails> {
    override val showDetails: ShowDetails?
        get() = data?.first()

    override val hasData: Boolean get() = !data.isNullOrEmpty()
}

data class SeriesShowDetailsResponse(
    @field:SerializedName("data")
    val data: SeriesShowDetails?,
) : ShowDetailsResponse<SeriesShowDetails> {
    override val showDetails: ShowDetails?
        get() = data
}

interface ShowDetailsResponse<T : ShowDetails> : ShowDetails {
    val hasData: Boolean get() = showDetails != null

    val showDetails: ShowDetails?

    val isMovie: Boolean
        get() {
            return (showDetails is MovieShowDetails?)
        }

    val isSeries: Boolean
        get() {
            return (showDetails is SeriesShowDetails?)
        }

    val movieShowDetail: MovieShowDetails?
        get() {
            return (showDetails as MovieShowDetails?)
        }

    val seriesShowDetail: SeriesShowDetails?
        get() {
            return (showDetails as SeriesShowDetails?)
        }

    val showId: Int?
        get() {
            if (isMovie) {
                return movieShowDetail?.videoShowId
            }
            return seriesShowDetail?.metadata?.showId
        }

    val title: String?
        get() {
            if (isMovie) {
                return movieShowDetail?.videoShowTitle
            }
            return seriesShowDetail?.metadata?.showName
        }

    val description: String?
        get() {
            if (isMovie) {
                return movieShowDetail?.vsDescription
            }
            return seriesShowDetail?.metadata?.summary
        }

    val poster: String?
        get() {
            if (isMovie) {
                return movieShowDetail?.vsImage1 ?: movieShowDetail?.vsImage2
            }
            return seriesShowDetail?.metadata?.showImage1 ?: seriesShowDetail?.metadata?.showImage2
        }

    val showType: ShowType
        get() {
            if (isMovie) {
                return ShowType.Movie
            }
            return ShowType.Series
        }
}