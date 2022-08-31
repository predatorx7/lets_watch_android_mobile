package com.magnificsoftware.letswatch.ui.fragments.tabScreens.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.databinding.FragmentWatchlistEmptyBinding
import com.magnificsoftware.letswatch.manager.navigation.MainTabsNavigation

@AndroidEntryPoint
class EmptyWatchlistFragment : Fragment(R.layout.fragment_watchlist_empty) {

    private var _binding: FragmentWatchlistEmptyBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchlistEmptyBinding.inflate(inflater, container, false)

        binding.browseToWatchLaterButton.setOnClickListener {
            MainTabsNavigation.lastControlledBottomNavbar?.selectedItemId =
                R.id.HomeBottomNavbarOption
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}