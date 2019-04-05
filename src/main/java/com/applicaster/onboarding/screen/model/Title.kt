package com.applicaster.onboarding.screen.model

import com.google.gson.annotations.SerializedName

data class Title(
        @SerializedName("en")
        val en: String,
        @SerializedName("es")
        val es: String,
        @SerializedName("pt")
        val pt: String
)