package com.baothanhbin.shortcompose.core.model

data class AppBootstrapState(
    val networkMessage: String,
    val onboardingCompleted: Boolean,
    val cachedItemCount: Int,
    val refreshedAtMillis: Long,
)
