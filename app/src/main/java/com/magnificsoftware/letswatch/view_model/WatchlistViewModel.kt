package com.magnificsoftware.letswatch.view_model

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import com.magnificsoftware.letswatch.data_class.vo.ShowVO
import com.magnificsoftware.letswatch.manager.authentication.AppAuthentication
import com.magnificsoftware.letswatch.repository.WatchlistRepository
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(val repository: WatchlistRepository) : ViewModel(),
    LifecycleObserver {
    private var resourceData: LiveData<List<ShowVO>>? = null

    fun subscribe(lifecycleOwner: LifecycleOwner, observer: Observer<List<ShowVO>>) {
        resourceData = repository.getAllData(AppAuthentication.userId)
        resourceData!!.observe(lifecycleOwner, observer)
    }

    fun insert(show: ShowVO) {
        repository.insert(show)
    }

    fun remove(show: ShowVO) {
        repository.remove(show)
    }
}