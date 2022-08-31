package com.magnificsoftware.letswatch.ui.fragments.tabScreens.more.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.ui.fragments.tabScreens.more.models.MoreTabItem

class MoreTabsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemlist: List<MoreTabItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.more_tab_recycler_item, parent, false)
        return MoreTabViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MoreTabViewHolder -> {
                holder.initialize(itemlist[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }

    fun submitList(moreTabItemList: List<MoreTabItem>) {
        itemlist = moreTabItemList
    }

    inner class MoreTabViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.textView_more_item_title)

        fun bind(moreItem: MoreTabItem) {
            title.text = moreItem.tileTitle
        }

        fun initialize(moreTabItem: MoreTabItem) {
            title.text = moreTabItem.tileTitle

            itemView.setOnClickListener {
                moreTabItem.onClick?.invoke()
            }
        }
    }
}