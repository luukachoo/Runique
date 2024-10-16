package com.luukachoo.auth.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class UserDataValidatorTest {

    private lateinit var userDataValidator: UserDataValidator

    @BeforeEach
    fun setUp() {
        userDataValidator = UserDataValidator(
            patternValidator = object : PatternValidator {
                override fun matches(value: String): Boolean {
                    return true
                }
            }
        )
    }

    @ParameterizedTest
    @CsvSource(
        "Test12345, true",
        "test12345, false",
        "12345, false",
        "Test-12345, true",
        "TEST, false"
    )
    fun `test validate password`(password: String, expectedIsValid: Boolean) {
        val state = userDataValidator.validatePassword(password)
        assertThat(state.isValidPassword).isEqualTo(expectedIsValid)
    }
}