package com.magnificsoftware.letswatch.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.magnificsoftware.letswatch.api.LetsWatchService
import com.magnificsoftware.letswatch.data_class.plain.banners.ShowBannersDataResponse
import com.magnificsoftware.letswatch.data_class.plain.category.ShowCategoriesDataResponse
import com.magnificsoftware.letswatch.util.ApiResponse
import com.magnificsoftware.letswatch.util.AppExecutors
import com.magnificsoftware.letswatch.util.Resource
import com.magnificsoftware.letswatch.util.network_livedata.NetworkBoundResource
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryVideoShowRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    val letsWatchService: LetsWatchService
) {
    private var data: ShowCategoriesDataResponse? = null

    fun getShowsPerCategories(): LiveData<Resource<ShowCategoriesDataResponse>> {
        Timber.w("Loading data from OBDbService")
        data = null
        return object :
            NetworkBoundResource<ShowCategoriesDataResponse, ShowCategoriesDataResponse>(
                appExecutors
            ) {

            override fun shouldFetch(data: ShowCategoriesDataResponse?) = data == null

            override fun createCall(): LiveData<ApiResponse<ShowCategoriesDataResponse>> {
                Timber.w("API call for HomeScreen video show categories:")
                return letsWatchService.getShowsPerCategories()
            }

            override fun saveCallResult(item: ShowCategoriesDataResponse) {
                data = item
            }

            override fun loadFromResource(): LiveData<ShowCategoriesDataResponse> =
                MutableLiveData(data)

        }.asLiveData()
    }

    private var bannersDataResponse: ShowBannersDataResponse? = null

    fun getShowBanners(): LiveData<Resource<ShowBannersDataResponse>> {
        Timber.w("Loading data from OBDbService")
        bannersDataResponse = null
        return object :
            NetworkBoundResource<ShowBannersDataResponse, ShowBannersDataResponse>(appExecutors) {

            override fun shouldFetch(data: ShowBannersDataResponse?) = bannersDataResponse == null

            override fun createCall(): LiveData<ApiResponse<ShowBannersDataResponse>> {
                Timber.w("API call for HomeScreen video show categories:")
                return letsWatchService.getShowBanners()
            }

            override fun saveCallResult(item: ShowBannersDataResponse) {
                bannersDataResponse = item
            }

            override fun loadFromResource(): LiveData<ShowBannersDataResponse> =
                MutableLiveData(bannersDataResponse)

        }.asLiveData()
    }
}
