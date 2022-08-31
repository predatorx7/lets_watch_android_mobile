package com.magnificsoftware.letswatch.util.form

import android.widget.EditText

class AppValidation {
    private var _isFormValid: Boolean = true

    /**
     * Is form valid
     */
    val isValid get() = _isFormValid

    /**
     * Is form invalid
     */
    val isNotValid get() = !isValid

    private fun mergeFieldValidityWithFormValidity(isFieldValid: Boolean) {
        _isFormValid = _isFormValid && isFieldValid
    }

    private fun setErrorIfInvalid(
        textField: EditText,
        isFieldValid: Boolean,
        fieldName: String,
        showError: Boolean = true,
        customInvalidError: String? = null,
    ) {
        if (!isFieldValid && showError) {
            val value = textField.text.toString().trim()
            val errorMessage: String = if (value.isBlank()) {
                "$fieldName should not be blank"
            } else {
                customInvalidError ?: "$fieldName is not valid"
            }
            textField.error = errorMessage
        }
    }

    private fun validateField(
        textField: EditText,
        fieldName: String,
        showError: Boolean,
        customInvalidError: String? = null,
        validator: ValidatorCallback,
    ): Boolean {
        val value = textField.text.toString().trim()
        val isFieldValid = validator(value)

        mergeFieldValidityWithFormValidity(isFieldValid)
        setErrorIfInvalid(textField, isFieldValid, fieldName, showError, customInvalidError)

        return isFieldValid
    }

    private fun validateField(
        textField: EditText,
        fieldName: String,
        customInvalidError: String? = null,
        validator: ValidatorCallback,
    ): Boolean {
        return validateField(textField, fieldName, true, customInvalidError, validator)
    }

    fun text(
        textField: EditText,
        fieldName: String,
        showError: Boolean = true,
        customInvalidError: String? = null,
    ): Boolean {
        return validateField(
            textField = textField,
            fieldName = fieldName,
            showError = showError,
            customInvalidError = customInvalidError,
        ) {
            it.isNotBlank()
        }
    }

    fun email(
        textField: EditText,
        showError: Boolean = true,
        customInvalidError: String? = null,
    ): Boolean {
        return validateField(
            textField = textField,
            fieldName = "Email",
            showError = showError,
            customInvalidError = customInvalidError,
        ) {
            it.isNotBlank() && MRegexPatterns.EMAIL_ADDRESS.matcher(it).matches()
        }
    }

    fun name(
        textField: EditText,
        fieldName: String = "Name",
        customInvalidError: String? = null,
    ): Boolean {
        return validateField(
            textField = textField,
            fieldName = fieldName,
            customInvalidError = customInvalidError
                ?: "$fieldName should only contain letters and spaces.",
        ) {
            it.isNotBlank() && MRegexPatterns.NAME.matcher(it).matches()
        }
    }

    fun mobileNumber(
        textField: EditText,
        fieldName: String = "Mobile Number",
        customInvalidError: String? = null,
    ): Boolean {
        return validateField(
            textField = textField,
            fieldName = fieldName,
            customInvalidError = customInvalidError ?: "$fieldName should only contain numbers",
        ) {
            it.isNotBlank() && MRegexPatterns.MOBILE_NUMBER.matcher(it).matches()
        }
    }

    fun otp(
        textField: EditText,
        fieldName: String = "OTP",
        customInvalidError: String? = null,
    ): Boolean {
        return validateField(
            textField = textField,
            fieldName = fieldName,
            customInvalidError = customInvalidError,
        ) {
            it.isNotBlank() && MRegexPatterns.SIGNUP_OTP_PIN.matcher(it).matches()
        }
    }
}