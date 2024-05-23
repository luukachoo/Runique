package com.luukachoo.auth.data

import android.util.Patterns
import com.luukachoo.auth.domain.PatternValidator

class EmailPatternValidator : PatternValidator {
    override fun matches(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }
}