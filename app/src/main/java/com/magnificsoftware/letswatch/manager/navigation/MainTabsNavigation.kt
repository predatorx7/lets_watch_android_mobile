package com.magnificsoftware.letswatch.manager.navigation

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.ui.fragments.tabScreens.downloads.DownloadsFragment
import com.magnificsoftware.letswatch.ui.fragments.tabScreens.home.HomeFragment
import com.magnificsoftware.letswatch.ui.fragments.tabScreens.more.MoreFragment
import com.magnificsoftware.letswatch.ui.fragments.tabScreens.search.SearchFragment
import com.magnificsoftware.letswatch.ui.fragments.tabScreens.watchlist.WatchlistFragment
import timber.log.Timber
import javax.inject.Inject


class MainTabsNavigation @Inject constructor(val appNavigator: AppNavigator) {

    private var previousScreenTab: ScreenTab? = null

    /**
     * Change hostFragment fragment with the fragment represented by screen.
     *
     * @param screen The Screen, representing a fragment
     */
    fun changeTab(screen: ScreenTab) {
        val fragment = when (screen) {
            ScreenTab.Downloads -> DownloadsFragment()
            ScreenTab.Home -> HomeFragment()
            ScreenTab.More -> MoreFragment()
            ScreenTab.Search -> SearchFragment()
            ScreenTab.Watchlist -> WatchlistFragment()
        }

        if (screen == previousScreenTab) {
            Timber.i("User select tab that was same as the previously selected tab")
            return
        }

        previousScreenTab = screen
        appNavigator.navigateTo(fragment, false, R.id.main_container)
    }

    companion object {
        var lastControlledBottomNavbar: BottomNavigationView? = null
    }

    fun control(bottomNavbar: BottomNavigationView) {
        lastControlledBottomNavbar = bottomNavbar
        bottomNavbar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.HomeBottomNavbarOption -> {
                    changeTab(ScreenTab.Home)
                }
                R.id.SearchBottomNavbarOption -> {
                    changeTab(ScreenTab.Search)
                }
                R.id.DownloadsBottomNavbarOption ->
                    changeTab(ScreenTab.Downloads)

                R.id.WatchlistBottomNavbarOption -> {
                    changeTab(ScreenTab.Watchlist)
                }
                R.id.MoreBottomNavbarOption -> {
                    changeTab(ScreenTab.More)
                }
            }
            true
        }
    }
}
