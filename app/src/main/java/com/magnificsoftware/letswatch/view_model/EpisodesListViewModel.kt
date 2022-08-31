package com.magnificsoftware.letswatch.view_model

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.magnificsoftware.letswatch.data_class.plain.show_details.SeriesShowDetailsResponse
import com.magnificsoftware.letswatch.repository.ShowDetailsRepository
import com.magnificsoftware.letswatch.util.Resource
import com.magnificsoftware.letswatch.util.Status
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EpisodesListViewModel @Inject constructor(val repository: ShowDetailsRepository) :
    ViewModel(),
    LifecycleObserver {
    private var seriesDataObserver: Observer<Resource<SeriesShowDetailsResponse>?>? = null

    fun addEpisodeChangesObserver(
        observer: Observer<Resource<SeriesShowDetailsResponse>?>,
    ) {
        seriesDataObserver = observer
    }

    fun changeSeason(showId: Int, seasonNumber: Int = 1, owner: LifecycleOwner) {
        Timber.i("Fetching seasons")
        val data = repository.getShowSeasons(showId, seasonNumber)

        data.observe(owner, {
            if (it?.status == Status.SUCCESS) {
                data.removeObservers(owner)
            }
            seriesDataObserver?.onChanged(it)
        })
    }
}