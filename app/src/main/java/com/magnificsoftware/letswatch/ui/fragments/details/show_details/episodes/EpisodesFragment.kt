package com.magnificsoftware.letswatch.ui.fragments.details.show_details.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.data_class.plain.show_details.SeriesEpisode
import com.magnificsoftware.letswatch.data_class.plain.show_details.SeriesShowDetailsResponse
import com.magnificsoftware.letswatch.databinding.FragmentEpisodesBinding
import com.magnificsoftware.letswatch.manager.authentication.AppAuthentication
import com.magnificsoftware.letswatch.manager.navigation.LetsWatchNavigator
import com.magnificsoftware.letswatch.util.Status
import com.magnificsoftware.letswatch.view_model.EpisodesListViewModel
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class EpisodesFragment(val show: SeriesShowDetailsResponse?) :
    Fragment(R.layout.fragment_episodes) {
    @Inject
    lateinit var navigator: LetsWatchNavigator

    private var _binding: FragmentEpisodesBinding? = null

    private val binding get() = _binding!!

    private val viewModel: EpisodesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEpisodesBinding.inflate(inflater, container, false)

        if (show?.isSeries == true) {
            setupSeasonSelectionAdapter()
            changeSeason(1)
            setEpisodesCount(0)

            viewModel.addEpisodeChangesObserver {
                if (it?.status == Status.SUCCESS) {
                    val episodes = it.data?.seriesShowDetail?.episodesData?.episodes
                    Timber.i("Episode has been changed $episodes")
                    adaptData(episodes)
                }
            }
        } else {
            onNoData()
        }

        return binding.root
    }

    private fun adaptData(showEpisodes: List<SeriesEpisode>?) {
        Timber.i("Adapt data: $showEpisodes")
        if (showEpisodes.isNullOrEmpty()) {
            Timber.i("Adapt Null or empty")
            onNoData()
        } else {
            Timber.i("Adapt OK")
            setEpisodesCount(showEpisodes.size)
            populateEpisodes(showEpisodes)
        }
    }

    @Inject
    lateinit var appAuthentication: AppAuthentication

    private fun populateEpisodes(episodes: List<SeriesEpisode>?) {
        Timber.i("Populating list with episodes")
        if (episodes.isNullOrEmpty()) {
            binding.episodesList.visibility = View.GONE
        } else {
            val layoutManager = LinearLayoutManager(context)

            layoutManager.orientation = LinearLayoutManager.VERTICAL

            binding.episodesList.layoutManager = layoutManager

            binding.episodesList.visibility = View.VISIBLE


            val adapter = EpisodesListAdapter(navigator, this, appAuthentication)

            adapter.submitList(episodes)

            binding.episodesList.adapter = adapter
        }
    }

    private var selectedSeason = 1

    private fun onNoData() {
        Timber.i("On no data")
        setCurrentSelectedSeasonCount(selectedSeason)
        setEpisodesCount(0)
        populateEpisodes(listOf())
    }

    private fun setupSeasonSelectionAdapter() {
        if (show == null) return
        val seriesMetadata = show.seriesShowDetail?.metadata
        val totalSeasons = seriesMetadata?.totalSeasons ?: 1

        val data = ArrayList<String>()
        val seasonMap = HashMap<String, Int>()

        for (it in 1..totalSeasons) {
            val text = getSeasonText(it)
            seasonMap[text] = it
            data.add(text)
        }

        val seasonsAdapter = ArrayAdapter(requireContext(), R.layout.item_season_selector, data)

        val seasonSelector = binding.seasonSelectorAutoTextview

        seasonSelector.setOnItemClickListener { _, _, position, _ ->
            val item = seasonSelector.adapter.getItem(position) as String
            val result = seasonMap[item]
            if (result != null)
                changeSeason(result)
        }

        seasonSelector.setAdapter(seasonsAdapter)

        setCurrentSelectedSeasonCount(null)

        seasonsAdapter.filter.filter("")

        seasonsAdapter.setNotifyOnChange(true)
    }


    private fun changeSeason(seasonNumber: Int) {
        Timber.i("User clicked to change season to $seasonNumber")
        setCurrentSelectedSeasonCount(seasonNumber)
        viewModel.changeSeason(show?.showId ?: 0, seasonNumber, this)
    }

    private fun setCurrentSelectedSeasonCount(seasonNumber: Int?) {
        val atv = binding.seasonSelectorAutoTextview
        if (atv.adapter.count <= 0) return
        selectedSeason = seasonNumber ?: 1
        val position = (selectedSeason) - 1
        atv.setText(atv.adapter.getItem(position).toString(), false)
    }

    private fun getSeasonText(seasonNumber: Int?): String {
        val seasonCountAsString = seasonNumber?.toString() ?: "-"
        return getString(R.string.season, seasonCountAsString)
    }

    private fun setEpisodesCount(episodeCount: Int?) {
        val episodeCountAsString = episodeCount?.toString() ?: "-"
        binding.episodesCount.text = getString(R.string.episodes, episodeCountAsString)
    }
}