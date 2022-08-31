package com.magnificsoftware.letswatch.ui.fragments.tabScreens.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.data_class.vo.ShowVO
import com.magnificsoftware.letswatch.databinding.FragmentWatchlistBinding
import com.magnificsoftware.letswatch.manager.authentication.AppAuthentication
import com.magnificsoftware.letswatch.manager.navigation.LetsWatchNavigator
import com.magnificsoftware.letswatch.ui.component.dialogs.DialogsProvider
import com.magnificsoftware.letswatch.view_model.WatchlistViewModel
import javax.inject.Inject

@AndroidEntryPoint
class WatchlistFragment : Fragment(R.layout.fragment_watchlist) {
    @Inject
    lateinit var letsWatchNavigator: LetsWatchNavigator

    private var _binding: FragmentWatchlistBinding? = null

    private val binding get() = _binding!!

    private val viewModel: WatchlistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)

        binding.includedComponentWatchlistTopAppBar.componentOnlyBackTopAppBar.title =
            getString(R.string.watchlist)

        val observer = Observer<List<ShowVO>> {
            if (!it.isNullOrEmpty()) {
                onWatchlistData(it)
            } else {
                onEmptyWatchlist()
            }
        }

        viewModel.subscribe(this, observer)

        return binding.root
    }

    @Inject
    lateinit var authentication: AppAuthentication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!AppAuthentication.isAuthenticated) {
            DialogsProvider.showDialogSignupWatchlist(parentFragmentManager, authentication)
        }
    }

    private var showsLiveData: MutableLiveData<List<ShowVO>>? = null

    private fun onWatchlistData(shows: List<ShowVO>) {
        if (showsLiveData == null) {
            showsLiveData = MutableLiveData(shows)
            letsWatchNavigator.appNavigator.navigateTo(
                WatchlistDataFragment(showsLiveData!!),
                false,
                R.id.watchlistFragmentPlaceholder
            )
        } else {
            showsLiveData!!.value = shows
        }
    }

    private fun onEmptyWatchlist() {
        if (showsLiveData != null) {
            showsLiveData = null
        }
        // If watchlist empty
        letsWatchNavigator.appNavigator.navigateTo(
            EmptyWatchlistFragment(),
            false,
            R.id.watchlistFragmentPlaceholder
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
