package com.magnificsoftware.letswatch.ui.fragments.tabScreens.home

import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageClickListener
import com.synnapps.carouselview.ImageListener
import com.magnificsoftware.letswatch.data_class.plain.banners.ShowBanner
import com.magnificsoftware.letswatch.manager.navigation.LetsWatchNavigator
import com.magnificsoftware.letswatch.ui.fragments.details.show_details.ShowDetailsArgument
import com.magnificsoftware.letswatch.ui.fragments.details.show_details.ShowType

class CarouselBannerAdapter(
    val carouselView: CarouselView,
    val navigator: LetsWatchNavigator
) {

    fun submitList(banners: List<ShowBanner>) {
        val imageListener = ImageListener { position, imageView ->
            val current = banners[position]
            Picasso.get().load(current.m_banner_image_url_1).into(imageView)
        }

        carouselView.setImageListener(imageListener)

        val imageClickListener = ImageClickListener { position ->
            val current = banners[position]
            navigator.openDetailsPage(
                ShowDetailsArgument(
                    current.order.toIntOrNull() ?: 0,
                    ShowType.Movie
                )
            )
        }

        carouselView.setImageClickListener(imageClickListener)

        carouselView.pageCount = banners.size
    }
}