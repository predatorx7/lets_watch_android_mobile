package com.magnificsoftware.letswatch.util.form

import android.util.Patterns
import java.util.regex.Pattern

class MRegexPatterns private constructor() {
    companion object {
        val EMAIL_ADDRESS: Pattern = Patterns.EMAIL_ADDRESS
        val NAME: Pattern = Pattern.compile("^[\\p{L} .'-]+$")
        val MOBILE_NUMBER: Pattern = Pattern.compile("^\\d{7,15}$")
        val SIGNUP_OTP_PIN: Pattern = Pattern.compile("^\\d{4,10}\$")
    }
}