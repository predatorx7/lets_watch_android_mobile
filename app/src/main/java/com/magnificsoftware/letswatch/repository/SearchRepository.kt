package com.magnificsoftware.letswatch.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.magnificsoftware.letswatch.api.LetsWatchService
import com.magnificsoftware.letswatch.data_class.plain.search.SearchDataResponse
import com.magnificsoftware.letswatch.util.ApiResponse
import com.magnificsoftware.letswatch.util.AppExecutors
import com.magnificsoftware.letswatch.util.Resource
import com.magnificsoftware.letswatch.util.network_livedata.NetworkBoundResource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val letsWatchService: LetsWatchService
) {
    var searchResultInMemory: SearchDataResponse? = null

    fun searchShow(query: String): LiveData<Resource<SearchDataResponse>> {
        searchResultInMemory = null
        return object : NetworkBoundResource<SearchDataResponse, SearchDataResponse>(appExecutors) {
            override fun saveCallResult(item: SearchDataResponse) {
                searchResultInMemory = item
            }

            override fun shouldFetch(data: SearchDataResponse?) = searchResultInMemory == null

            override fun loadFromResource(): LiveData<SearchDataResponse> {
                return MutableLiveData(searchResultInMemory)
            }

            override fun createCall(): LiveData<ApiResponse<SearchDataResponse>> {
                return letsWatchService.searchShow(query)
            }
        }.asLiveData()
    }
}