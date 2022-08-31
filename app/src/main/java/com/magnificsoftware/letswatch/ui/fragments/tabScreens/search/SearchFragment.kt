package com.magnificsoftware.letswatch.ui.fragments.tabScreens.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.databinding.FragmentSearchBinding
import com.magnificsoftware.letswatch.manager.navigation.LetsWatchNavigator
import com.magnificsoftware.letswatch.manager.navigation.SubNavigation
import com.magnificsoftware.letswatch.repository.SearchRepository
import com.magnificsoftware.letswatch.ui.fragments.tabScreens.search.adapter.SearchReyclerAdapter
import com.magnificsoftware.letswatch.util.Status
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    private lateinit var searchResultAdapter: SearchReyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        initialiseRecyclerView()
        getTopResults()
        binding.searchView.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    filter(s.toString())
                }

            },
        )
        return binding.root
    }

    @Inject
    lateinit var searchRepository: SearchRepository

    private fun filter(text: String) {


        val value = searchRepository.searchShow(text)

        value.observe(viewLifecycleOwner) {
            Timber.i("Received search result response")
            if (it?.status == Status.SUCCESS) {
                val results = it.data?.data?.data
                Timber.i("Received search data with size: ${results?.size}")
                Timber.i("${it.status} ${it.message}")

                searchResultAdapter.filterList(results)
            } else {
                searchResultAdapter.filterList(listOf())
            }
        }
    }

    private fun getTopResults() {
        filter("")
    }

    @Inject
    lateinit var navigator: LetsWatchNavigator

    private fun initialiseRecyclerView() {
        binding.searchViewRecycler.apply {
            layoutManager = GridLayoutManager(context, 2)
            searchResultAdapter = SearchReyclerAdapter(navigator)
            adapter = searchResultAdapter
        }
    }

    @Inject
    lateinit var subNavigation: SubNavigation

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}