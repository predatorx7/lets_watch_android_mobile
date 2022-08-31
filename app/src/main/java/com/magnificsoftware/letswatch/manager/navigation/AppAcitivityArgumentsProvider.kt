package com.magnificsoftware.letswatch.manager.navigation

import android.content.Intent

interface AppAcitivityArgumentsProvider {
    fun addArguments(intent: Intent)
}