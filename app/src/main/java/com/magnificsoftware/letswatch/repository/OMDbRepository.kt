package com.magnificsoftware.letswatch.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.magnificsoftware.letswatch.api.OMDbService
import com.magnificsoftware.letswatch.data_class.plain.stub_data.OMDbSearchResponse
import com.magnificsoftware.letswatch.util.ApiResponse
import com.magnificsoftware.letswatch.util.AppExecutors
import com.magnificsoftware.letswatch.util.Resource
import com.magnificsoftware.letswatch.util.network_livedata.NetworkBoundResource
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Repository that handles OMDb objects.
 */
@Singleton
class OMDbRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    val omdbService: OMDbService
) {
    private var data: OMDbSearchResponse? = null

    fun loadData(): LiveData<Resource<OMDbSearchResponse>> {
        Timber.w("Loading data from OBDbService")
        return object : NetworkBoundResource<OMDbSearchResponse, OMDbSearchResponse>(appExecutors) {
            override fun saveCallResult(item: OMDbSearchResponse) {
                data = item
            }

            override fun shouldFetch(data: OMDbSearchResponse?) = data == null

            override fun loadFromResource(): LiveData<OMDbSearchResponse> = MutableLiveData(data)

            override fun createCall(): LiveData<ApiResponse<OMDbSearchResponse>> {
                Timber.w("Creating call from OBDbService::getData")
                return omdbService.getData()
            }
        }.asLiveData()
    }

    fun loadMovieDetails() {
        TODO("A method for getting movie details")
    }

    fun loadSimilarShows(): LiveData<Resource<OMDbSearchResponse>> {
        return loadData()
    }

    fun getEpisodes() {

    }
}
