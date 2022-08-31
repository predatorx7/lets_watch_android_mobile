package com.magnificsoftware.letswatch.ui.fragments.auth

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.databinding.FormSignupRegistrationBinding
import com.magnificsoftware.letswatch.manager.authentication.AppAuthentication
import com.magnificsoftware.letswatch.util.async_future.AsyncFuture
import com.magnificsoftware.letswatch.util.async_future.AsyncFutureTask
import com.magnificsoftware.letswatch.util.form.AppButtonProgressIndicator
import com.magnificsoftware.letswatch.util.form.AppValidation
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FormRegistration(val onContinue: () -> Unit) : Fragment(R.layout.form_signup_registration) {
    private var _binding: FormSignupRegistrationBinding? = null

    private val binding get() = _binding!!

    private val text =
        "By proceeding, you agree to out Terms of use, Privacy Policy and that you are over 18."
    private val link = "http://www.google.com"

    private lateinit var continueButtonProgressIndicator: AppButtonProgressIndicator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FormSignupRegistrationBinding.inflate(inflater, container, false)

        val ssb = SpannableStringBuilder(text)
        val fcsWhite = ForegroundColorSpan(Color.WHITE)
        val underlineSpan = UnderlineSpan()
        val tp = TextPaint()
        tp.linkColor = Color.WHITE
        underlineSpan.updateDrawState(tp)
        val myClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // TODO: here update the link for T&C
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(browserIntent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.WHITE
            }
        }
        ssb.setSpan(fcsWhite, 32, 60, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        ssb.setSpan(underlineSpan, 32, 60, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        ssb.setSpan(myClickableSpan, 32, 60, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        binding.textView9.text = ssb
        binding.textView9.movementMethod = LinkMovementMethod.getInstance()

        continueButtonProgressIndicator = AppButtonProgressIndicator(
            binding.continueButton,
            getString(R.string.continue_message),
            getString(R.string.signing_up),
        )

        binding.continueButton.setOnClickListener {
            onStep1SignUp()
        }

        return binding.root
    }

    @Inject
    lateinit var authentication: AppAuthentication

    private var asyncFuture: AsyncFuture<String>? = null
    private val isLoading get() = asyncFuture?.isLoading ?: false

    private fun onStep1SignUp() {
        if (isLoading) return
        if (validateFields().isNotValid) return
        hideKeyboard()
        continueButtonProgressIndicator.start(requireContext())

        // Sign up details - step
        val email = binding.signUpFormEmail.text.toString()
        val password = binding.signUpFormPassword.text.toString()
        val firstName = binding.signUpFormFirstName.text.toString()
        val lastName = binding.signUpFormLastName.text.toString()
        val phoneNumber = binding.signUpFormMobileNumber.text.toString()
        val task = object : AsyncFutureTask<String> {
            override fun execute(): String? {
                val response =
                    authentication.register(email, firstName, lastName, password, phoneNumber)
                return response.data?.message
            }

            override fun onCancelled() {
                continueButtonProgressIndicator.stop()
            }

            override fun onCompleted(value: String?, error: Throwable?) {
                Timber.i("onCompleted IS CALLED")
                continueButtonProgressIndicator.stop()
                if (!AppAuthentication.hasUser) {
                    // if authentication has user (temporarily)
                    // then continue, else notify USER about a problem
                    notifyUser(value, error)
                } else {
                    onContinue()
                }
            }
        }
        asyncFuture = AsyncFuture(task)
    }

    private fun validateFields(): AppValidation {
        val validation = AppValidation()
        validation.email(binding.signUpFormEmail)
        validation.name(binding.signUpFormFirstName, "First name")
        validation.name(binding.signUpFormLastName, "Last name")
        validation.mobileNumber(binding.signUpFormMobileNumber)
        validation.text(binding.signUpFormPassword, "Password")
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