package com.magnificsoftware.letswatch.repository

import androidx.lifecycle.LiveData
import com.magnificsoftware.letswatch.data.WatchlistDao
import com.magnificsoftware.letswatch.data_class.vo.ShowVO
import com.magnificsoftware.letswatch.util.AppExecutors
import com.magnificsoftware.letswatch.util.async_future.AsyncFuture
import com.magnificsoftware.letswatch.util.async_future.AsyncFutureTask
import timber.log.Timber
import java.util.concurrent.Future
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatchlistRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val watchlistDao: WatchlistDao,
    // private val letsWatchService: LetsWatchService
) {

    /**
     * Asynchronously checks if watchlist has [show]
     */
    fun contains(show: ShowVO, onCompletion: (Boolean) -> Unit): Future<Boolean?> {
        val task = object : AsyncFutureTask<Boolean> {
            override fun execute(): Boolean {
                return watchlistDao.exists(show.id)
            }

            override fun onCompleted(value: Boolean?, error: Throwable?) {
                if (error != null) {
                    Timber.e(error)
                }
                onCompletion(value ?: false)
            }
        }

        return AsyncFuture(task, appExecutors.diskIO()).asFuture()
    }

    fun remove(show: ShowVO): Future<Unit?> {
        val task = object : AsyncFutureTask<Unit> {
            override fun execute() {
                return watchlistDao.remove(show.id)
            }
        }

        return AsyncFuture(task, appExecutors.diskIO()).asFuture()
    }

    // TODO: Add sync with backend
    fun getAllData(userId: String): LiveData<List<ShowVO>> {
        return watchlistDao.getSavedShows(userId)
    }

    fun insert(show: ShowVO): Future<Unit?> {
        val task = object : AsyncFutureTask<Unit> {
            override fun execute() {
                return watchlistDao.insert(show)
            }
        }

        return AsyncFuture(task, appExecutors.diskIO()).asFuture()
    }

//    private class WatchlistResource(
//        appExecutors: AppExecutors,
//        private val watchlistDao: WatchlistDao
//    ) :
//        NetworkBoundResource<List<ShowVO>, List<ShowVO>>(appExecutors) {
//        override fun saveCallResult(item: List<ShowVO>) {
//            watchlistDao.insertAll(item)
//
//        }
//
//        override fun shouldFetch(data: List<ShowVO>?) = data.isNullOrEmpty()
//
//        override fun loadFromResource() = watchlistDao.getSavedShows()
//
//        override fun createCall(): MutableLiveData<ApiResponse<List<ShowVO>>> {
//            // Empty response until API ready
//            return MutableLiveData<ApiResponse<List<ShowVO>>>(
//                ApiResponse.create(
//                    Response.success(listOf())
//                )
//            )
//        }
//    }
}