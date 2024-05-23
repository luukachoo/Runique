package com.luukachoo.auth.domain

interface PatternValidator {
    fun matches(value: String): Boolean
}