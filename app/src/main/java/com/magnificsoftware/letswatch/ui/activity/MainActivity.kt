package com.magnificsoftware.letswatch.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import com.magnificsoftware.letswatch.databinding.ActivityMainBinding
import com.magnificsoftware.letswatch.manager.navigation.MainTabsNavigation
import com.magnificsoftware.letswatch.manager.navigation.ScreenTab
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var tabsNavigation: MainTabsNavigation

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Hide support action bar
        if (supportActionBar != null)
            supportActionBar?.hide()

        if (savedInstanceState == null) {
            tabsNavigation.changeTab(ScreenTab.Home)
        }

        tabsNavigation.control(binding.includedComponentMainBottomNavBar.componentMainBottomNavBar)
    }


    override fun onBackPressed() {
        super.onBackPressed()

        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }
}