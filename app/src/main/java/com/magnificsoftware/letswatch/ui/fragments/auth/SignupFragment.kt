package com.magnificsoftware.letswatch.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.databinding.FragmentSignupBinding
import com.magnificsoftware.letswatch.manager.authentication.AppAuthentication
import com.magnificsoftware.letswatch.manager.navigation.SubNavigation
import com.magnificsoftware.letswatch.ui.fragments.details.DetailsScreen
import javax.inject.Inject


@AndroidEntryPoint
class SignupFragment(override val argument: AuthDetailsArgument = AuthDetailsArgument(null)) :
    Fragment(R.layout.fragment_signup), DetailsScreen {
    private var _binding: FragmentSignupBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        binding.includedComponentOnlyBackTopAppBar.componentOnlyBackTopAppBar.setNavigationOnClickListener {
            authentication.openHomePage()
        }

        val registration = FormRegistration { setupUIForVerification() }
        subNavigation.appNavigator.navigateTo(registration, false, R.id.signup_form_placeholder)

        val openLoginOnClick = View.OnClickListener {
            subNavigation.switchScreen(LoginFragment(AuthDetailsArgument(null)))
        }

        binding.textView6.setOnClickListener(openLoginOnClick)
        binding.textView7.setOnClickListener(openLoginOnClick)

        return binding.root
    }


    @Inject
    lateinit var authentication: AppAuthentication

    @Inject
    lateinit var subNavigation: SubNavigation

    fun setupUIForVerification() {
        val circleDrawable = binding.textView5.background
        val circleOutline = binding.textView8.background

        binding.textView5.background = circleOutline
        binding.textView8.background = circleDrawable

        subNavigation.appNavigator.navigateTo(
            FormVerification(),
            false,
            R.id.signup_form_placeholder
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}