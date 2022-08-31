package com.magnificsoftware.letswatch.ui.fragments.tabScreens.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.data_class.plain.show.VideoShow
import com.magnificsoftware.letswatch.manager.navigation.LetsWatchNavigator
import com.magnificsoftware.letswatch.ui.fragments.details.show_details.ShowDetailsArgument
import com.magnificsoftware.letswatch.ui.fragments.details.show_details.ShowType
import com.magnificsoftware.letswatch.util.RoundedCornersTransform


class VideoShowsAdapter(private val navigator: LetsWatchNavigator) :
    ListAdapter<VideoShow, VideoShowsAdapter.ShowViewHolder>(
        VideoShowsComparator()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        return ShowViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, navigator)
    }

    class ShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val showCover: ImageView = itemView.findViewById(R.id.movieCover)
        private val showItemView: TextView = itemView.findViewById(R.id.movieViewTextView)
        fun bind(show: VideoShow?, navigator: LetsWatchNavigator) {
            if (show != null) {
                Picasso.get().load(show.vs_image_1).transform(RoundedCornersTransform(10F))
                    .into(showCover)

                val coverClickListener = View.OnClickListener {
                    val showId: Int?
                    val showType: ShowType
                    if (show.isMovie()) {
                        showId = show.video_show_id
                        showType = ShowType.Movie
                    } else {
                        showId = show.tv_show_id
                        showType = ShowType.Series
                    }
                    navigator.openDetailsPage(ShowDetailsArgument(showId, showType))
                }

                showCover.setOnClickListener(coverClickListener)

                if (show.isMovie()) {
                    showItemView.text = show.video_show_title
                } else if (show.isSeries()) {
                    showItemView.text = show.tv_show_name
                }

            }
        }

        companion object {
            fun create(parent: ViewGroup): ShowViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_shows_view_item, parent, false)
                return ShowViewHolder(view)
            }
        }
    }

    class VideoShowsComparator : DiffUtil.ItemCallback<VideoShow>() {
        override fun areItemsTheSame(oldItem: VideoShow, newItem: VideoShow): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: VideoShow, newItem: VideoShow): Boolean {
            return oldItem.video_show_title == newItem.video_show_title
        }
    }
}