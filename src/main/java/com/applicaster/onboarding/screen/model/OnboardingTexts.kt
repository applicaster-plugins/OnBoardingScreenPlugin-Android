package com.applicaster.onboarding.screen.model

import com.google.gson.annotations.SerializedName

data class OnboardingTexts(
        @SerializedName("finishOnboarding")
        val finishOnboarding: Map<String, String>?,
        @SerializedName("skipOnboarding")
        val skipOnboarding: Map<String, String>?,
        @SerializedName("subtitle")
        val subtitle: Map<String, String>?,
        @SerializedName("title")
        val title: Map<String, String>?
)