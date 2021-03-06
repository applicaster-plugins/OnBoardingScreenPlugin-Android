package com.applicaster.onboarding.screen.model

import com.google.gson.annotations.SerializedName

data class Segment(
        @SerializedName("id")
        val id: String,
        @SerializedName("imageUrl")
        val imageUrl: String,
        @SerializedName("title")
        val title: Map<String, String>
)