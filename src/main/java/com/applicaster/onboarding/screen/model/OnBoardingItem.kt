package com.applicaster.onboarding.screen.model

import com.google.gson.annotations.SerializedName

data class OnBoardingItem(
        @SerializedName("categories")
        val categories: List<Category>,
        @SerializedName("languages")
        val languages: List<String>,
        @SerializedName("onboardingTexts")
        val onboardingTexts: OnboardingTexts
)