package com.magnificsoftware.letswatch.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.magnificsoftware.letswatch.api.LetsWatchService
import com.magnificsoftware.letswatch.data_class.plain.show_details.MovieShowDetailsResponse
import com.magnificsoftware.letswatch.data_class.plain.show_details.SeriesShowDetailsResponse
import com.magnificsoftware.letswatch.util.ApiResponse
import com.magnificsoftware.letswatch.util.AppExecutors
import com.magnificsoftware.letswatch.util.Resource
import com.magnificsoftware.letswatch.util.network_livedata.NetworkBoundResource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowDetailsRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val letsWatchService: LetsWatchService
) {
    private var movieDataInMemory: MovieShowDetailsResponse? = null
    private var seriesDataInMemory: SeriesShowDetailsResponse? = null

    fun getMovieDetails(showId: Int): LiveData<Resource<MovieShowDetailsResponse>> {
        movieDataInMemory = null
        return object :
            NetworkBoundResource<MovieShowDetailsResponse, MovieShowDetailsResponse>(appExecutors) {
            override fun saveCallResult(item: MovieShowDetailsResponse) {
                movieDataInMemory = item
            }

            override fun shouldFetch(data: MovieShowDetailsResponse?) = movieDataInMemory == null

            override fun loadFromResource(): LiveData<MovieShowDetailsResponse> {
                return MutableLiveData(movieDataInMemory)
            }

            override fun createCall(): LiveData<ApiResponse<MovieShowDetailsResponse>> {
                return letsWatchService.getMovieShowDetails(showId)
            }
        }.asLiveData()
    }

    fun getShowSeasons(
        showId: Int,
        seasonNumber: Int = 1
    ): LiveData<Resource<SeriesShowDetailsResponse>> {
        seriesDataInMemory = null
        return object :
            NetworkBoundResource<SeriesShowDetailsResponse, SeriesShowDetailsResponse>(appExecutors) {
            override fun saveCallResult(item: SeriesShowDetailsResponse) {
                seriesDataInMemory = item
            }

            override fun shouldFetch(data: SeriesShowDetailsResponse?) = seriesDataInMemory == null

            override fun loadFromResource(): LiveData<SeriesShowDetailsResponse> {
                return MutableLiveData(seriesDataInMemory)
            }

            override fun createCall(): LiveData<ApiResponse<SeriesShowDetailsResponse>> {
                return letsWatchService.getSeriesShowDetails(showId, seasonNumber)
            }
        }.asLiveData()
    }
}