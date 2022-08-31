package com.magnificsoftware.letswatch.ui.fragments.tabScreens.downloads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.databinding.FragmentDownloadsBinding


import com.magnificsoftware.letswatch.manager.navigation.LetsWatchNavigator
import javax.inject.Inject

@AndroidEntryPoint
class DownloadsFragment : Fragment(R.layout.fragment_downloads) {

    @Inject
    lateinit var letsWatchNavigator: LetsWatchNavigator

    private var _binding: FragmentDownloadsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadsBinding.inflate(inflater, container, false)

        binding.includedComponentDownloadsTopAppBar.componentOnlyBackTopAppBar.title =
            getString(R.string.downloads)

        emptyDownloadsList()

        return binding.root
    }

    private fun emptyDownloadsList() {
        letsWatchNavigator.appNavigator.navigateTo(
            EmptyDownloadsFragment(),
            false,
            R.id.downloadsFragmentPlaceholder
        )
    }
}