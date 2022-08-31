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
import com.magnificsoftware.letswatch.databinding.FormSignupVerificationBinding
import com.magnificsoftware.letswatch.manager.authentication.AppAuthentication
import com.magnificsoftware.letswatch.util.async_future.AsyncFuture
import com.magnificsoftware.letswatch.util.async_future.AsyncFutureTask
import com.magnificsoftware.letswatch.util.form.AppButtonProgressIndicator
import com.magnificsoftware.letswatch.util.form.AppValidation
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FormVerification : Fragment(R.layout.form_signup_registration) {
    private var _binding: FormSignupVerificationBinding? = null

    private val binding get() = _binding!!

    private lateinit var verifyOTPButtonProgressIndicator: AppButtonProgressIndicator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FormSignupVerificationBinding.inflate(inflater, container, false)

        binding.verifyYourAccountOtpSent.text =
            getString(R.string.enter_otp_sent_to, AppAuthentication.email)

        verifyOTPButtonProgressIndicator = AppButtonProgressIndicator(
            binding.verifyOTPButton,
            getString(R.string.verify_otp),
            getString(R.string.verifying),
        )

        binding.verifyOTPButton.setOnClickListener {
            onStep2SignUp()
        }

        return binding.root
    }

    @Inject
    lateinit var authentication: AppAuthentication

    private var asyncFuture: AsyncFuture<String>? = null
    private val isLoading get() = asyncFuture?.isLoading ?: false


    private fun onStep2SignUp() {
        if (isLoading) return
        if (validateFields().isNotValid) return
        hideKeyboard()
        verifyOTPButtonProgressIndicator.start(requireContext())
        // verify OTP - step
        val otp = binding.signUpFormOtp.text.toString()
        val task = object : AsyncFutureTask<String> {
            override fun execute(): String? {
                val response =
                    authentication.verify(otp)
                return response.data?.message
            }

            override fun onCancelled() {
                verifyOTPButtonProgressIndicator.stop()
            }

            override fun onCompleted(value: String?, error: Throwable?) {
                Timber.i("onCompleted IS CALLED")
                verifyOTPButtonProgressIndicator.stop()
                notifyUser(value, error)
            }
        }
        asyncFuture = AsyncFuture(task)
    }

    private fun validateFields(): AppValidation {
        val validation = AppValidation()
        validation.otp(binding.signUpFormOtp)
        return validation
    }

    private fun notifyUser(message: String?, error: Throwable?) {
        Timber.e(error)
        val value = message ?: "Something went wrong"
        Snackbar.make(binding.root, value, Snackbar.LENGTH_LONG).show()
    }

    private fun hideKeyboard() {
        val imm = this.requireContext()
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

}