package com.magnificsoftware.letswatch.ui.fragments.details.show_details.episodes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.magnificsoftware.letswatch.data_class.plain.show_details.SeriesEpisode
import com.magnificsoftware.letswatch.databinding.RecyclerviewItemEpisodeBinding
import com.magnificsoftware.letswatch.manager.authentication.AppAuthentication
import com.magnificsoftware.letswatch.manager.authentication.AppAuthenticationStatus
import com.magnificsoftware.letswatch.manager.navigation.LetsWatchNavigator
import com.magnificsoftware.letswatch.ui.component.dialogs.DialogsProvider
import com.magnificsoftware.letswatch.util.RoundedCornersTransform

class EpisodesListAdapter(
    val navigator: LetsWatchNavigator,
    val fragment: Fragment,
    val authentication: AppAuthentication,
) :
    ListAdapter<SeriesEpisode, EpisodesListAdapter.MovieViewHolder>(
        ShowEpisodeComparator()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, navigator, fragment, authentication)
    }

    class MovieViewHolder(val binding: RecyclerviewItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            episode: SeriesEpisode?,
            navigator: LetsWatchNavigator,
            fragment: Fragment,
            authentication: AppAuthentication,
        ) {
            if (episode != null) {
                Picasso.get().load(episode.episodeImage1 ?: episode.episodeImage2)
                    .transform(RoundedCornersTransform(10F))
                    .into(binding.episodeThumbnail)

                val coverClickListener = View.OnClickListener {
                    when (AppAuthentication.status) {
                        AppAuthenticationStatus.Unauthenticated -> {
                            // if not authenticated then show login/signup dialog
                            DialogsProvider.showDialogSignup(
                                fragment.parentFragmentManager,
                                authentication
                            )
                        }
                        // AppAuthenticationStatus.Authenticated -> {
                        //    // if authenticated but not has subscription then show subscription dialog
                        //    DialogsProvider.showDialogSubscribe(fragment.parentFragmentManager)
                        // }
                        // AppAuthenticationStatus.Subscribed -> {
                        else -> {
                            // show show if authenticated & has subscription
                            // TODO(mushaheedx): Pass episode playlist file when it's provided in API
                            navigator.openVideoPlayer(
                                ""
                            )
                        }
                    }
                }

                binding.episodeThumbnail.setOnClickListener(coverClickListener)

                binding.episodeTitle.text =
                    if (episode.episodeNumber != null) "E${episode.episodeNumber} - ${episode.episodeTitle}" else episode.episodeTitle

                binding.episodeSubtitle.text = episode.episodeSummary
            }
        }

        companion object {
            fun create(parent: ViewGroup): MovieViewHolder {
                val binding =
                    RecyclerviewItemEpisodeBinding.inflate(LayoutInflater.from(parent.context))
                return MovieViewHolder(binding)
            }
        }
    }

    class ShowEpisodeComparator : DiffUtil.ItemCallback<SeriesEpisode>() {
        override fun areItemsTheSame(oldItem: SeriesEpisode, newItem: SeriesEpisode): Boolean {
            return oldItem.episodeTitle == newItem.episodeTitle
        }

        override fun areContentsTheSame(oldItem: SeriesEpisode, newItem: SeriesEpisode): Boolean {
            return oldItem.episodeId == newItem.episodeId && oldItem.episodeTitle == newItem.episodeTitle
        }
    }
}
