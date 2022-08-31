package com.magnificsoftware.letswatch.ui.fragments.tabScreens.watchlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.magnificsoftware.letswatch.data_class.vo.ShowVO
import com.magnificsoftware.letswatch.databinding.RecyclerviewItemWatchlistBinding
import com.magnificsoftware.letswatch.manager.navigation.LetsWatchNavigator
import com.magnificsoftware.letswatch.ui.fragments.details.show_details.ShowDetailsArgument
import com.magnificsoftware.letswatch.util.RoundedCornersTransform

class SavedWatchlistAdapter(val navigator: LetsWatchNavigator) :
    ListAdapter<ShowVO, SavedWatchlistAdapter.OMDbShowViewHolder>(
        ShowEpisodeComparator()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OMDbShowViewHolder {
        return OMDbShowViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: OMDbShowViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, navigator)
    }

    class OMDbShowViewHolder(val binding: RecyclerviewItemWatchlistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(show: ShowVO?, navigator: LetsWatchNavigator) {
            if (show != null) {
                Picasso.get().load(show.poster).transform(RoundedCornersTransform(10F))
                    .into(binding.showCover)

                val coverClickListener = View.OnClickListener {
                    navigator.openDetailsPage(ShowDetailsArgument(show.showId, show.showType))
                }

                binding.showCover.setOnClickListener(coverClickListener)

                binding.showTitle.text = show.title

                binding.showTags.visibility = View.INVISIBLE

                binding.showSynopsis.text = show.description
            }
        }

        companion object {
            fun create(parent: ViewGroup): OMDbShowViewHolder {
                val binding =
                    RecyclerviewItemWatchlistBinding.inflate(LayoutInflater.from(parent.context))
                return OMDbShowViewHolder(binding)
            }
        }
    }

    class ShowEpisodeComparator : DiffUtil.ItemCallback<ShowVO>() {
        override fun areItemsTheSame(
            oldItem: ShowVO,
            newItem: ShowVO
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: ShowVO,
            newItem: ShowVO
        ): Boolean {
            return oldItem.id == newItem.id && oldItem.title == newItem.title
        }
    }
}
