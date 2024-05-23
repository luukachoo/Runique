package com.luukachoo.auth.domain

class UserDataValidator(
    private val patternValidator: PatternValidator
) {

    fun isValidEmail(email: String): Boolean = patternValidator.matches(email.trim())

    fun validatePassword(password: String): PasswordValidationState {
        val hasMinLength = password.length >= PASSWORD_MIN_LENGTH
        val hasDigit = password.any { it.isDigit() }
        val hasLowerCaseCharacter = password.any { it.isLowerCase() }
        val hasUpperCaseCharacter = password.any { it.isUpperCase() }

        return PasswordValidationState(
            hasMinLength = hasMinLength,
            hasNumber = hasDigit,
            hasLowerCaseCharacter = hasLowerCaseCharacter,
            hasUpperCaseCharacter = hasUpperCaseCharacter
        )
    }

    companion object {
        const val PASSWORD_MIN_LENGTH = 9
    }
}