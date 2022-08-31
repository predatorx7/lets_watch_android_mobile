package com.magnificsoftware.letswatch.view_model

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.lifecycle.HiltViewModel
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.data_class.plain.show_details.MovieShowDetailsResponse
import com.magnificsoftware.letswatch.data_class.plain.show_details.SeriesShowDetailsResponse
import com.magnificsoftware.letswatch.data_class.plain.show_details.ShowDetails
import com.magnificsoftware.letswatch.data_class.plain.show_details.ShowDetailsResponse
import com.magnificsoftware.letswatch.data_class.vo.ShowVO
import com.magnificsoftware.letswatch.databinding.FragmentShowDetailsScreenBinding
import com.magnificsoftware.letswatch.manager.authentication.AppAuthentication
import com.magnificsoftware.letswatch.manager.authentication.AppAuthenticationStatus
import com.magnificsoftware.letswatch.manager.navigation.LetsWatchNavigator
import com.magnificsoftware.letswatch.repository.OMDbRepository
import com.magnificsoftware.letswatch.repository.ShowDetailsRepository
import com.magnificsoftware.letswatch.repository.WatchlistRepository
import com.magnificsoftware.letswatch.ui.component.adapter.MoreLikeThisListAdapter
import com.magnificsoftware.letswatch.ui.component.dialogs.DialogsProvider
import com.magnificsoftware.letswatch.ui.fragments.details.show_details.ShowDetailsArgument
import com.magnificsoftware.letswatch.ui.fragments.details.show_details.ShowType
import com.magnificsoftware.letswatch.util.Resource
import timber.log.Timber
import javax.inject.Inject

typealias OnMovieDetailsCallback = (Resource<MovieShowDetailsResponse>) -> Unit
typealias OnShowDetailsCallback = (Resource<SeriesShowDetailsResponse>) -> Unit

@HiltViewModel
class ShowDetailsViewModel @Inject constructor(
    val watchlistRepository: WatchlistRepository,
    val repository: OMDbRepository,
    val showDetailsRepository: ShowDetailsRepository,
) : ViewModel(),
    LifecycleObserver {

    private var _moviedetails: LiveData<Resource<MovieShowDetailsResponse>>? = null
    private val isMovie: Boolean get() = _moviedetails != null

    private var _seriesdetails: LiveData<Resource<SeriesShowDetailsResponse>>? = null
    private val isSeries: Boolean get() = _seriesdetails != null

    fun setup(
        argument: ShowDetailsArgument,
        owner: LifecycleOwner,
        onMovieShowDetails: OnMovieDetailsCallback,
        onSeriesShowDetails: OnShowDetailsCallback,
    ) {
        if (argument.showType == ShowType.Movie) {
            _moviedetails = showDetailsRepository.getMovieDetails(argument.showId)
            _moviedetails!!.observe(owner) {
                onMovieShowDetails(it)
            }
        } else {
            Timber.i("Setting up series")
            _seriesdetails = showDetailsRepository.getShowSeasons(argument.showId)
            _seriesdetails!!.observe(owner) {
                Timber.i("Received resource: ${it.status} ${it.message}")
                onSeriesShowDetails(it)
            }
        }
    }


    fun <T> setupWatchlist(
        context: Context,
        parentFragmentManager: FragmentManager,
        authentication: AppAuthentication,
        showDetailsResponse: ShowDetailsResponse<T>,
        binding: FragmentShowDetailsScreenBinding
    ) where T : ShowDetails {
        fun onContainsIcon(context: Context) {
            val icon = AppCompatResources.getDrawable(
                context,
                R.drawable.ic_fluent_checkbox_checked_24_filled
            )
            binding.addToWatchlist.icon = icon
            binding.addToWatchlist.text = context.getString(R.string.remove_watchlist)
        }

        fun onNotContainsIcon(context: Context) {
            val icon =
                AppCompatResources.getDrawable(context, R.drawable.ic_fluent_add_square_24_regular)
            binding.addToWatchlist.icon = icon
            binding.addToWatchlist.text = context.getString(R.string.watchlist)
        }

        val show = ShowVO.from(showDetailsResponse, AppAuthentication.userId)

        if (AppAuthentication.isAuthenticated) {
            // only authenticated users are allowed to use watchlist.
            // Hence, guest users won't have a watchlist
            // Thus we only need to check contains for authenticated users.
            watchlistRepository.contains(show) { isInWatchlist ->
                if (isInWatchlist) {
                    onContainsIcon(context)
                }
            }
        }

        binding.addToWatchlist.setOnClickListener {
            when (AppAuthentication.status) {
                AppAuthenticationStatus.Unauthenticated -> {
                    DialogsProvider.showDialogSignupWatchlist(parentFragmentManager, authentication)
                }
                else -> {
                    watchlistRepository.contains(show) { isInWatchlist ->
                        if (isInWatchlist) {
                            Timber.i("Removing show to watchlist")
                            watchlistRepository.remove(show)
                            onNotContainsIcon(context)
                        } else {
                            Timber.i("Adding show to watchlist")
                            watchlistRepository.insert(show)
                            onContainsIcon(context)
                        }
                    }
                }
            }
        }
    }

    private val similarShows = repository.loadSimilarShows()

    fun addRecommendations(
        lifecycleOwner: LifecycleOwner,
        binding: FragmentShowDetailsScreenBinding,
        navigator: LetsWatchNavigator
    ) {
        val recyclerView = binding.moreLikeThisRecyclerView
        val adapter = MoreLikeThisListAdapter(navigator)
        val layoutManager = LinearLayoutManager(recyclerView.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        similarShows.observe(lifecycleOwner, { response ->
            if (!response.data?.searchResult.isNullOrEmpty()) {
                Timber.i("Adding search result to carousel")
                adapter.submitList(response.data?.searchResult!!)
            } else {
                Timber.w("Response from search result (observer to carousel) is unsuccessful")
            }
        })
    }
}
