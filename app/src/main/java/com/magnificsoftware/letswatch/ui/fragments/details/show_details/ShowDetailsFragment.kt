package com.magnificsoftware.letswatch.ui.fragments.details.show_details

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.data_class.plain.show_details.MovieShowDetailsResponse
import com.magnificsoftware.letswatch.data_class.plain.show_details.SeriesShowDetailsResponse
import com.magnificsoftware.letswatch.data_class.plain.show_details.ShowDetails
import com.magnificsoftware.letswatch.data_class.plain.show_details.ShowDetailsResponse
import com.magnificsoftware.letswatch.databinding.FragmentShowDetailsScreenBinding
import com.magnificsoftware.letswatch.manager.authentication.AppAuthentication
import com.magnificsoftware.letswatch.manager.authentication.AppAuthenticationStatus
import com.magnificsoftware.letswatch.manager.navigation.LetsWatchNavigator
import com.magnificsoftware.letswatch.ui.component.dialogs.DialogsProvider
import com.magnificsoftware.letswatch.ui.fragments.details.DetailsScreen
import com.magnificsoftware.letswatch.ui.fragments.details.show_details.episodes.EpisodesFragment
import com.magnificsoftware.letswatch.util.Resource
import com.magnificsoftware.letswatch.view_model.ShowDetailsViewModel
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ShowDetailsFragment(override val argument: ShowDetailsArgument) :
    Fragment(R.layout.fragment_show_details_screen), DetailsScreen {
    private var _binding: FragmentShowDetailsScreenBinding? = null

    private val binding get() = _binding!!
    private val viewModel: ShowDetailsViewModel by viewModels()

    @Inject
    lateinit var letsWatchNavigator: LetsWatchNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowDetailsScreenBinding.inflate(inflater, container, false)

        binding.includedComponentOnlyBackTopAppBar.componentOnlyBackTopAppBar.setNavigationOnClickListener {
            letsWatchNavigator.appNavigator.close(requireActivity())
        }

        val afterData = { it: ShowDetailsResponse<ShowDetails> ->
            viewModel.setupWatchlist(
                requireContext(),
                parentFragmentManager,
                authentication,
                it,
                binding
            )
            // TODO(mushaheedx): Comment until recommendation APIs become available
            // viewModel.addRecommendations(this, binding, letsWatchNavigator)
        }

        val onMovieShowDetails = { it: Resource<MovieShowDetailsResponse> ->
            if (it.data?.data != null) {
                setupMovie(it.data)
                afterData(it.data as ShowDetailsResponse<ShowDetails>)
            }
        }

        val onSeriesShowDetails = { it: Resource<SeriesShowDetailsResponse> ->
            if (it.data?.data != null) {
                setupSeries(it.data)
                afterData(it.data as ShowDetailsResponse<ShowDetails>)
            }
        }

        Timber.i("Argument: ${argument.showId} ${argument.showType}")
        viewModel.setup(argument, viewLifecycleOwner, onMovieShowDetails, onSeriesShowDetails)

        return binding.root
    }

    @Inject
    lateinit var authentication: AppAuthentication

    @SuppressLint("SetTextI18n")
    fun setupMovie(data: MovieShowDetailsResponse) {
        Timber.i("Loading ${data.poster} via Picasso")

        val movie = data.movieShowDetail

        if (!data.poster.isNullOrBlank()) {
            Timber.i("Loading ${data.poster} via Picasso")
            Picasso.get().load(data.poster).into(binding.detailsScreenShowCover)
        }

        if (!data.title.isNullOrBlank())
            binding.detailsScreenShowTitle.text = data.title

        binding.detailsScreenShowTags.text =
            "Movie | ${data.movieShowDetail?.vsRuntime} mins | ${data.movieShowDetail?.ratingName}"

        // After button, filling information in every other text view to describe the show
        addTitleAndDataIfNotNullOrBlank(
            binding.detailsScreenShowSynopsis,
            "Synopsis",
            movie?.vsDescription
        )

        addTitleAndDataIfNotNullOrBlank(
            binding.detailsScreenShowCast,
            "Cast",
            "Alexander Gould, Mary-Louise Parker, Hunter Parrish"
        )

        addTitleAndDataIfNotNullOrBlank(
            binding.detailsScreenShowDirectors,
            "Director",
            "Jenji Kohan"
        )

        addTitleAndDataIfNotNullOrBlank(
            binding.detailsScreenShowLanguages,
            "Languages",
            "English"
        )

        binding.tappablePlayTrailer.visibility = View.VISIBLE

        val trailerButtonOnTap = View.OnClickListener {
            Timber.i("Trailer play button tapped: ${movie?.trailerHlsGroup}")
            if (!movie?.trailerHlsGroup.isNullOrBlank()) {
                // trailer available
                Timber.i("trailer available")
            }
        }

        binding.tappablePlayTrailer.setOnClickListener(trailerButtonOnTap)
        binding.buttonWatchTrailer.setOnClickListener(trailerButtonOnTap)

        if (movie?.trailerHlsGroup.isNullOrBlank()) {
            binding.tappablePlayTrailer.isEnabled = false
            binding.buttonWatchTrailer.isEnabled = false
        }

        binding.episodesLayout.visibility = View.GONE
        binding.detailsScreenPlayButton.visibility = View.VISIBLE

        binding.detailsScreenPlayButton.setOnClickListener {
            Timber.i("Play button is tapped ${movie?.hlsGroup}")
            if (!movie?.hlsGroup.isNullOrBlank()) {
                when (AppAuthentication.status) {
                    AppAuthenticationStatus.Unauthenticated -> {
                        DialogsProvider.showDialogSignup(parentFragmentManager, authentication)
                    }
                    // TODO(mushaheedx): Comment until subscription feature isn't ready
                    // AppAuthenticationStatus.Authenticated -> {
                    //    DialogsProvider.showDialogSubscribe(parentFragmentManager)
                    // }
                    // AppAuthenticationStatus.Subscribed -> {
                    else -> {
                        // if not authenticated then show login/signup dialog
                        // if authenticated but not has subscription then show subscription dialog
                        // show movie if authenticated & has subscription.
                        letsWatchNavigator.openVideoPlayer(
                            movie?.hlsGroup ?: "",
                        )
                    }
                }
            }
            // video is unavailable on else
        }

        if (movie?.hlsGroup.isNullOrBlank()) {
            binding.detailsScreenPlayButton.text = getString(R.string.video_unavailable)
            binding.detailsScreenPlayButton.isEnabled = false
        }
    }

    @SuppressLint("SetTextI18n")
    fun setupSeries(data: SeriesShowDetailsResponse) {
        Timber.i("Loading series data")
        Timber.i("Loading ${data.poster} via Picasso")

        val seriesMetadata = data.seriesShowDetail?.metadata

        if (!data.poster.isNullOrBlank()) {
            Timber.i("Loading ${data.poster} via Picasso")
            Picasso.get().load(data.poster).into(binding.detailsScreenShowCover)
        }

        if (!data.title.isNullOrBlank())
            binding.detailsScreenShowTitle.text = data.title

        binding.detailsScreenShowTags.text =
            "${seriesMetadata?.ratingName} | Series | ${seriesMetadata?.totalSeasons} Seasons"

        // After button, filling information in every other text view to describe the show
        addTitleAndDataIfNotNullOrBlank(
            binding.detailsScreenShowSynopsis,
            "Synopsis",
            seriesMetadata?.summary
        )

        addTitleAndDataIfNotNullOrBlank(
            binding.detailsScreenShowCast,
            "Cast",
            "Alexander Gould, Mary-Louise Parker, Hunter Parrish"
        )

        addTitleAndDataIfNotNullOrBlank(
            binding.detailsScreenShowDirectors,
            "Director",
            "Jenji Kohan"
        )

        addTitleAndDataIfNotNullOrBlank(
            binding.detailsScreenShowLanguages,
            "Languages",
            "English"
        )

        binding.detailsScreenPlayButton.visibility = View.GONE
        binding.downloadMovieButton.visibility = View.GONE
        binding.episodesLayout.visibility = View.VISIBLE
        setupEpisodes(data, binding, letsWatchNavigator)
    }

    private fun setupEpisodes(
        show: SeriesShowDetailsResponse,
        binding: FragmentShowDetailsScreenBinding,
        letsWatchNavigator: LetsWatchNavigator
    ) {
        binding.episodesLayout.visibility = View.VISIBLE
        letsWatchNavigator.appNavigator.navigateTo(
            EpisodesFragment(show),
            false,
            binding.episodesLayout.id
        )
    }

    companion object {
        fun addTitleAndDataIfNotNullOrBlank(textView: TextView, title: String, data: String?) {
            if (data.isNullOrBlank()) {
                textView.visibility = View.GONE
                return
            }
            // Clearing text before adding any composed text.
            // Required to avoid appending data to [TextView] again if this method is called again
            // (usually happens during debug restarts)
            textView.text = ""

            val emphasizedTitle = SpannableString("${title}: ")
            emphasizedTitle.setSpan(StyleSpan(Typeface.BOLD), 0, emphasizedTitle.length, 0)
            emphasizedTitle.setSpan(ForegroundColorSpan(Color.WHITE), 0, emphasizedTitle.length, 0)
            textView.append(emphasizedTitle)
            textView.append(data)
        }
    }
}
