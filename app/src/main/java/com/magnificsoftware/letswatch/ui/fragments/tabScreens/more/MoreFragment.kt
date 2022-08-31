package com.magnificsoftware.letswatch.ui.fragments.tabScreens.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.databinding.FragmentMoreBinding
import com.magnificsoftware.letswatch.manager.authentication.AppAuthentication
import com.magnificsoftware.letswatch.manager.navigation.SubNavigation
import com.magnificsoftware.letswatch.ui.component.dialogs.LetsWatchDialog
import com.magnificsoftware.letswatch.ui.component.dialogs.DialogButtonData
import com.magnificsoftware.letswatch.ui.fragments.tabScreens.more.adapter.MoreTabsAdapter
import com.magnificsoftware.letswatch.ui.fragments.tabScreens.more.models.MoreTabItem
import javax.inject.Inject


@AndroidEntryPoint
class MoreFragment : Fragment(R.layout.fragment_more) {

    private var _binding: FragmentMoreBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var authentication: AppAuthentication

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)

        addVersionInformation()
        populateMoreOptionsList()

        return binding.root
    }

    private fun addVersionInformation() {
        val versionName: String = com.magnificsoftware.letswatch.BuildConfig.VERSION_NAME
        val versionCode: Int = com.magnificsoftware.letswatch.BuildConfig.VERSION_CODE
        val versionBuildType: String = com.magnificsoftware.letswatch.BuildConfig.BUILD_TYPE
        binding.versionInformationTextView.text =
            getString(R.string.version_information, versionName, versionCode, versionBuildType)
    }

    private fun showLogOutDialog() {
        LetsWatchDialog(
            "LOG OUT",
            "Are you sure you want to logout?",
            DialogButtonData("YES") {
                authentication.logout()
            },
            DialogButtonData("NO")
        ).show(parentFragmentManager, "LetsWatch Dialog Fragment for Logout")
    }

    private fun populateMoreOptionsList() {
        val data: List<MoreTabItem> = listOf(
            MoreTabItem("Legal"),
            MoreTabItem("Help"),
        )

        val more = if (AppAuthentication.isAuthenticated) {
            listOf(
                MoreTabItem("Account"),
                MoreTabItem("Download Preferences"),
                MoreTabItem("Continue Watching"),
                MoreTabItem("Subscription Management"),
                MoreTabItem("Parental Control"),
                MoreTabItem("Activate TV"),
                MoreTabItem("Log out") {
                    showLogOutDialog()
                }
            )
        } else {
            listOf(
                MoreTabItem("Sign up") {
                    authentication.openAuthPage(signup = true)
                },
                MoreTabItem("Log in") {
                    authentication.openAuthPage()
                }
            )
        }

        val moreTabsAdapter = MoreTabsAdapter()
        moreTabsAdapter.submitList(data + more)

        binding.moreTabsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = moreTabsAdapter
        }
    }

    @Inject
    lateinit var subNavigation: SubNavigation

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}