package com.magnificsoftware.letswatch

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import com.magnificsoftware.letswatch.ui.activity.MainActivity
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AppTest {

    companion object {
        fun performClick(layoutId: Int) {
            Espresso.onView(withId(layoutId)).perform(ViewActions.click())
        }

        fun checkFragmentView(layoutId: Int) {
            Espresso.onView(withId(layoutId))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @After
    fun tearDown() {
        // Release resources (if used any)
    }

    @Test
    fun navigationTestsForBottomNavigationBar() {
        ActivityScenario.launch(MainActivity::class.java)

        checkFragmentView(R.id.homePageMovies)
        performClick(R.id.SearchBottomNavbarOption)
        checkFragmentView(R.id.searchView)
        performClick(R.id.DownloadsBottomNavbarOption)
        checkFragmentView(R.id.downloadsFragmentPlaceholder)
        performClick(R.id.WatchlistBottomNavbarOption)
        checkFragmentView(R.id.watchlistFragmentPlaceholder)
        performClick(R.id.MoreBottomNavbarOption)
        checkFragmentView(R.id.moreLikeThisRecyclerView)
        performClick(R.id.HomeBottomNavbarOption)
        checkFragmentView(R.id.homePageMovies)

        // Check if Text on fragment screen is displayed
        // Espresso.onView(ViewMatchers.withText(Matchers.containsString("Interaction with 'Button 1'")))
        // .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}