package com.magnificsoftware.letswatch.ui.component.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.magnificsoftware.letswatch.data_class.vo.OMDbShow
import com.magnificsoftware.letswatch.databinding.RecyclerItemShowDetailsRecommendationBinding
import com.magnificsoftware.letswatch.manager.navigation.LetsWatchNavigator
import com.magnificsoftware.letswatch.util.RoundedCornersTransform

class MoreLikeThisListAdapter(val navigator: LetsWatchNavigator) :
    ListAdapter<OMDbShow, MoreLikeThisListAdapter.MoreLikeThisShowViewHolder>(
        MoviesComparator()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreLikeThisShowViewHolder {
        return MoreLikeThisShowViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MoreLikeThisShowViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, navigator, position)
    }

    class MoreLikeThisShowViewHolder(private val viewBinding: RecyclerItemShowDetailsRecommendationBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(OMDbShow: OMDbShow?, navigator: LetsWatchNavigator, position: Int) {
            if (OMDbShow != null) {
                Picasso.get().load(OMDbShow.poster).transform(RoundedCornersTransform(10F))
                    .into(viewBinding.recommendedShowCover)

                val coverClickListener = View.OnClickListener {
                    navigator.openVideoPlayer(
                        TODO("Add video source URI"),
                    )
                }

                viewBinding.recommendedShowCover.setOnClickListener(coverClickListener)
            }
        }

        private fun str(id: Int): String {
            return viewBinding.root.context.getString(id)
        }

        companion object {
            fun create(parent: ViewGroup): MoreLikeThisShowViewHolder {
                val view: RecyclerItemShowDetailsRecommendationBinding =
                    RecyclerItemShowDetailsRecommendationBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return MoreLikeThisShowViewHolder(view)
            }
        }
    }

    class MoviesComparator : DiffUtil.ItemCallback<OMDbShow>() {
        override fun areItemsTheSame(oldItem: OMDbShow, newItem: OMDbShow): Boolean {
            return oldItem.title === newItem.title
        }

        override fun areContentsTheSame(oldItem: OMDbShow, newItem: OMDbShow): Boolean {
            return oldItem.imdbID == newItem.imdbID
        }
    }
}