package com.magnificsoftware.letswatch.ui.fragments.tabScreens.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.data_class.plain.show.VideoShow
import com.magnificsoftware.letswatch.manager.navigation.LetsWatchNavigator
import com.magnificsoftware.letswatch.ui.fragments.details.show_details.ShowDetailsArgument
import com.magnificsoftware.letswatch.ui.fragments.details.show_details.ShowType
import com.magnificsoftware.letswatch.ui.fragments.tabScreens.search.models.SearchResult
import com.magnificsoftware.letswatch.util.RoundedCornersTransform
import timber.log.Timber

class SearchReyclerAdapter constructor(private val navigator: LetsWatchNavigator) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemList = listOf<SearchResult>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.search_grid_item, parent, false)
        return SearchResultViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SearchResultViewHolder -> {
                holder.bind(itemList[position], navigator)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun filterList(list: List<SearchResult>?) {
        if (list == null) return
        itemList = list
        notifyDataSetChanged()
    }

    fun submitList(searchResultlist: List<SearchResult>) {
        itemList = searchResultlist
    }

    inner class SearchResultViewHolder constructor(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.searchitem_image)
        fun bind(searchResult: SearchResult, navigator: LetsWatchNavigator) {
            Timber.i("$searchResult")
            if (searchResult is VideoShow) {
                Timber.i("Is VideoSHow")
                Picasso.get().load(searchResult.vs_image_1 ?: searchResult.vs_image_2)
                    .transform(RoundedCornersTransform(10F))
                    .into(image)

                val coverClickListener = View.OnClickListener {
                    val showId: Int?
                    val showType: ShowType
                    if (searchResult.isMovie()) {
                        showId = searchResult.video_show_id
                        showType = ShowType.Movie
                    } else {
                        showId = searchResult.tv_show_id
                        showType = ShowType.Series
                    }
                    navigator.openDetailsPage(ShowDetailsArgument(showId, showType))
                }

                image.setOnClickListener(coverClickListener)
            } else {
                Timber.i("Is not Video show")
                Picasso.get().load(searchResult.poster).transform(RoundedCornersTransform(10F))
                    .into(image)
            }
        }
    }
}