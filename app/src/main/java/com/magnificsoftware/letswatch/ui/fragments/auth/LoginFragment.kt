package com.magnificsoftware.letswatch.ui.fragments.auth

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.databinding.FragmentLoginBinding
import com.magnificsoftware.letswatch.manager.authentication.AppAuthentication
import com.magnificsoftware.letswatch.manager.navigation.SubNavigation
import com.magnificsoftware.letswatch.ui.fragments.details.DetailsScreen
import com.magnificsoftware.letswatch.util.async_future.AsyncFuture
import com.magnificsoftware.letswatch.util.async_future.AsyncFutureTask
import com.magnificsoftware.letswatch.util.form.AppButtonProgressIndicator
import com.magnificsoftware.letswatch.util.form.AppValidation
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment(override val argument: AuthDetailsArgument) :
    Fragment(R.layout.fragment_login), DetailsScreen {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var authentication: AppAuthentication

    @Inject
    lateinit var subNavigation: SubNavigation

    var asyncFuture: AsyncFuture<String>? = null

    private val isLoading get() = asyncFuture?.isLoading ?: false

    private lateinit var loginButtonProgressIndicator: AppButtonProgressIndicator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.includedComponentOnlyBackTopAppBar.componentOnlyBackTopAppBar.setNavigationOnClickListener {
            authentication.openHomePage()
        }

        loginButtonProgressIndicator = AppButtonProgressIndicator(
            binding.loginButton,
            getString(R.string.log_in),
            getString(R.string.logging_in),
        )

        binding.loginButton.setOnClickListener {
            onLoginButtonPressed()
        }

        val openSignUpOnClick = View.OnClickListener {
            asyncFuture?.cancel()
            subNavigation.switchScreen(SignupFragment())
        }

        binding.textView6.setOnClickListener(openSignUpOnClick)
        binding.textView7.setOnClickListener(openSignUpOnClick)

        return binding.root
    }

    private fun onLoginButtonPressed() {
        if (isLoading) return
        if (validateFields().isNotValid) return
        hideKeyboard()
        loginButtonProgressIndicator.start(requireContext())

        val task = object : AsyncFutureTask<String> {
            override fun execute(): String? {
                val username = binding.editextemailLogin.text.toString()
                val password = binding.editextpassLogin.text.toString()
                val response = authentication.login(username, password)
                return response.data?.message
            }

            override fun onCancelled() {
                loginButtonProgressIndicator.stop()
            }

            override fun onCompleted(value: String?, error: Throwable?) {
                Timber.i("onCompleted IS CALLED")
                if (error != null) {
                    Timber.e(error)
                }
                loginButtonProgressIndicator.stop()
                notifyUser(value)
            }
        }

        asyncFuture = AsyncFuture(task)
    }

    private fun validateFields(): AppValidation {
        val validation = AppValidation()
        validation.email(binding.editextemailLogin)
        validation.text(binding.editextpassLogin, "Password")
        return validation
    }

    private fun notifyUser(message: String?) {
        val value = message ?: "Something went wrong"
        Snackbar.make(binding.root, value, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        asyncFuture?.cancel()
        asyncFuture = null
        _binding = null
    }

    private fun hideKeyboard() {
        val imm = this.requireContext()
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}