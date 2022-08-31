package com.magnificsoftware.letswatch.ui.fragments.tabScreens.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.data_class.vo.ShowVO
import com.magnificsoftware.letswatch.databinding.FragmentWatchlistDataBinding
import com.magnificsoftware.letswatch.manager.navigation.LetsWatchNavigator
import javax.inject.Inject

@AndroidEntryPoint
class WatchlistDataFragment(val shows: LiveData<List<ShowVO>>) :
    Fragment(R.layout.fragment_watchlist_data) {
    @Inject
    lateinit var navigator: LetsWatchNavigator

    private var _binding: FragmentWatchlistDataBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWatchlistDataBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val adapter = SavedWatchlistAdapter(navigator)
        binding.savedShows.layoutManager = layoutManager
        binding.savedShows.adapter = adapter

        val showLiveDataObserver = Observer<List<ShowVO>> {
            if (it != null) {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        }


        shows.observe(viewLifecycleOwner, showLiveDataObserver)
        return _binding?.root
    }
}