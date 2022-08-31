package com.magnificsoftware.letswatch.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import com.magnificsoftware.letswatch.databinding.ActivityDetailsViewBinding
import com.magnificsoftware.letswatch.manager.authentication.AppAuthentication
import com.magnificsoftware.letswatch.manager.navigation.SubNavigation
import javax.inject.Inject

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var navigation: SubNavigation

    @Inject
    lateinit var authentication: AppAuthentication

    private lateinit var binding: ActivityDetailsViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Hide support action bar
        if (supportActionBar != null)
            supportActionBar?.hide()

        navigation.changeScreenFor(this)
    }


    override fun onBackPressed() {
        super.onBackPressed()

        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }
}