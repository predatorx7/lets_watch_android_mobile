package com.magnificsoftware.letswatch.util.async_future

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.magnificsoftware.letswatch.util.*
import java9.util.concurrent.CompletableFuture
import java.util.concurrent.Future

abstract class FutureResource<ResultType, CachedValueType> @MainThread constructor(private val appExecutors: AppExecutors) {

    private val completableFutureResult = CompletableFuture<Resource<ResultType>>()

    private val result = MediatorLiveData<Resource<ResultType>>()

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        completableFutureResult.completeAsync { newValue }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>?) {
        val apiResponse = createCall()

        when (val response = apiResponse.get()) {
            is ApiSuccessResponse -> {
                appExecutors.diskIO().execute {
                    val processedData = processResponse(response)
                    saveCallResult(processedData)
                    appExecutors.mainThread().execute {
                        // we specially request a new live data,
                        // otherwise we will get immediately last cached value,
                        // which may not be updated with latest results received from network.
                        val resource = loadFromResource()
                        if (resource != null) {
                            result.addSource(resource) { newData ->
                                result.removeSource(resource)
                                setValue(Resource.success(newData))
                            }
                        } else {
                            setValue(Resource.success(processedData))
                        }
                    }
                }
            }
            is ApiEmptyResponse -> {
                appExecutors.mainThread().execute {
                    // reload from disk whatever we had
                    val resource = loadFromResource()
                    if (resource != null) {
                        result.addSource(resource) { newData ->
                            result.removeSource(resource)
                            setValue(Resource.success(newData))
                        }
                    } else {
                        setValue(Resource.success(null))
                    }
                }
            }
            is ApiErrorResponse -> {
                onFetchFailed()
                if (dbSource != null) {
                    result.addSource(dbSource) { newData ->
                        result.removeSource(dbSource)
                        setValue(Resource.error(response.errorMessage, newData))
                    }
                } else {
                    setValue(Resource.error(response.errorMessage, null))
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asFuture(): Future<Resource<ResultType>> {
        appExecutors.mainThread().execute {
            result.value = Resource.loading(null)
        }

        @Suppress("LeakingThis")
        val dbSource = loadFromResource()

        if (dbSource == null) {
            if (shouldFetchIfLoadFromResourceNull()) {
                fetchFromNetwork(null)
            } else {
                setValue(Resource.success(null))
            }
        } else {
            result.addSource(dbSource) { data ->
                result.removeSource(dbSource)
                if (shouldFetch(data)) {
                    fetchFromNetwork(dbSource)
                } else {
                    setValue(Resource.success(data))
                }
            }
        }

        return completableFutureResult
    }

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<ResultType>) = response.body

    @WorkerThread
    protected abstract fun saveCallResult(item: ResultType)

    @MainThread
    protected open fun shouldFetch(data: ResultType?): Boolean = data == null

    @MainThread
    protected open fun shouldFetchIfLoadFromResourceNull(): Boolean = true

    /*
    * Load from memory or Db (Usually using Dao)
    */
    @MainThread
    protected open fun loadFromResource(): LiveData<ResultType>? = null

    @MainThread
    protected abstract fun createCall(): Future<ApiResponse<ResultType>>
}