package com.magnificsoftware.letswatch.ui.fragments.tabScreens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.databinding.FragmentHomeBinding
import com.magnificsoftware.letswatch.manager.navigation.LetsWatchNavigator
import com.magnificsoftware.letswatch.util.Status
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    @Inject
    lateinit var navigator: LetsWatchNavigator

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        populateCarousel()
        populateCategoriesAndShows()

        return binding.root
    }

    private fun populateCarousel() {
        //  Note: A single carousel banner's aspect ratio is approx. 2/1
        val carouselAdapter = CarouselBannerAdapter(binding.carouselView, navigator)

        viewModel.addShowBannersObserver(viewLifecycleOwner) {
            if (it.status == Status.SUCCESS) {
                val banners = it.data?.banners
                if (!banners.isNullOrEmpty()) {
                    carouselAdapter.submitList(banners)
                }
            }
        }
    }

    private fun populateCategoriesAndShows() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.homePageMovies.layoutManager = layoutManager
        val adapter = HomePageCategoriesAndShowsAdapter(navigator)
        binding.homePageMovies.adapter = adapter

        viewModel.showsPerCategories.observe(viewLifecycleOwner) {
            if (it.status == Status.SUCCESS) {
                val categoriesAndShows = it.data?.categories
                if (!categoriesAndShows.isNullOrEmpty()) {
                    adapter.submitList(categoriesAndShows)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}