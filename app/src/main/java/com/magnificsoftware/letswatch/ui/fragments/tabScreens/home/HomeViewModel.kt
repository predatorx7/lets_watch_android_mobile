package com.magnificsoftware.letswatch.ui.fragments.tabScreens.home

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import com.magnificsoftware.letswatch.data_class.plain.banners.ShowBannersDataResponse
import com.magnificsoftware.letswatch.data_class.plain.category.ShowCategoriesDataResponse
import com.magnificsoftware.letswatch.repository.CategoryVideoShowRepository
import com.magnificsoftware.letswatch.util.Resource
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: CategoryVideoShowRepository) : ViewModel(),
    LifecycleObserver {
    private val showsPerCategoriesLiveData = repository.getShowsPerCategories()

    private val showBanners = repository.getShowBanners()

    val showsPerCategories: LiveData<Resource<ShowCategoriesDataResponse>>
        get() {
            return showsPerCategoriesLiveData
        }

    fun addShowBannersObserver(
        owner: LifecycleOwner,
        observer: Observer<Resource<ShowBannersDataResponse>>
    ) {
        showBanners.observe(owner, observer)
    }
}