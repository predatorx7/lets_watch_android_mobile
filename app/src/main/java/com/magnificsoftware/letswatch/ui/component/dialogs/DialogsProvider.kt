package com.magnificsoftware.letswatch.ui.component.dialogs

import androidx.fragment.app.FragmentManager
import com.magnificsoftware.letswatch.manager.authentication.AppAuthentication

object DialogsProvider {
    fun showDialogSubscribe(parentFragmentManager: FragmentManager) {
        LetsWatchDialog(
            "Subscribe",
            "To watch our content, please complete your subscription and valid payment details.",
            DialogButtonData("SUBSCRIBE"),
            DialogButtonData("CANCEL")
        ).show(parentFragmentManager, "Subscribe request dialog")
    }

    fun showDialogSignupWatchlist(
        parentFragmentManager: FragmentManager,
        authentication: AppAuthentication
    ) {
        LetsWatchDialog(
            "Sign up or Login in",
            "To save shows in watchlist, please sign up or login.",
            DialogButtonData("LOGIN") {
                authentication.openAuthPage()
            },
            DialogButtonData("CANCEL")
        ).show(parentFragmentManager, "Sign up request dialog for watchlist")
    }

    fun showDialogSignup(
        parentFragmentManager: FragmentManager,
        authentication: AppAuthentication
    ) {
        LetsWatchDialog(
            "Sign up or Login in",
            "To watch our content, please sign up or login.",
            DialogButtonData("LOGIN") {
                authentication.openAuthPage()
            },
            DialogButtonData("CANCEL")
        ).show(parentFragmentManager, "Sign up request dialog")
    }
}