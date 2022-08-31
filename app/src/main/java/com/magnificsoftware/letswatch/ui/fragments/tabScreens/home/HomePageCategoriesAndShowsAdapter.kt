package com.magnificsoftware.letswatch.ui.fragments.tabScreens.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.data_class.plain.category.ShowCategoryData
import com.magnificsoftware.letswatch.manager.navigation.LetsWatchNavigator
import timber.log.Timber

class HomePageCategoriesAndShowsAdapter constructor(
    private val navigator: LetsWatchNavigator,
) :
    ListAdapter<ShowCategoryData, HomePageCategoriesAndShowsAdapter.CategoriesAndShowsViewHolder>(
        ShowCategoriesComparator()
    ),
    LifecycleObserver {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesAndShowsViewHolder {
        return CategoriesAndShowsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CategoriesAndShowsViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, navigator)
    }

    class CategoriesAndShowsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val movieTitleTextView: TextView =
            itemView.findViewById(R.id.viewMoviesTitleTextView)
        private val moviesRecyclerView: RecyclerView =
            itemView.findViewById(R.id.viewMoviesRecyclerView)

        fun bind(showCategory: ShowCategoryData, navigator: LetsWatchNavigator) {
            if (showCategory.video_shows.isNullOrEmpty()) return

            movieTitleTextView.text = showCategory.vc_name

            Timber.w("Adding movies to child recycle view")
            val adapter = VideoShowsAdapter(navigator)

            adapter.submitList(showCategory.video_shows)

            val layoutManager = LinearLayoutManager(itemView.context)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            moviesRecyclerView.layoutManager = layoutManager

            moviesRecyclerView.adapter = adapter
        }

        companion object {
            fun create(parent: ViewGroup): CategoriesAndShowsViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_movies, parent, false)
                return CategoriesAndShowsViewHolder(view)
            }
        }
    }

    class ShowCategoriesComparator : DiffUtil.ItemCallback<ShowCategoryData>() {
        override fun areItemsTheSame(
            oldItem: ShowCategoryData,
            newItem: ShowCategoryData
        ): Boolean {
            return oldItem.vc_name == newItem.vc_name
        }

        override fun areContentsTheSame(
            oldItem: ShowCategoryData,
            newItem: ShowCategoryData
        ): Boolean {
            return oldItem.vc_id == newItem.vc_id && oldItem.vc_name == newItem.vc_name
        }
    }
}