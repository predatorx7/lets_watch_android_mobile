package com.magnificsoftware.letswatch.ui.activity

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticatorActivity : AppCompatActivity() {
    companion object {
        const val ARG_ACCOUNT_TYPE = "ARG_ACCOUNT_TYPE"
        const val ARG_AUTH_TYPE = "ARG_AUTH_TYPE"
        const val ARG_IS_ADDING_NEW_ACCOUNT = "ARG_IS_ADDING_NEW_ACCOUNT"
    }
}