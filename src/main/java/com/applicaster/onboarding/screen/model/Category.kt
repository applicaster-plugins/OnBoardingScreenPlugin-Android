package com.applicaster.onboarding.screen.model

import com.google.gson.annotations.SerializedName

data class Category(
        @SerializedName("id")
        val id: String,
        @SerializedName("imageUrl")
        val imageUrl: String,
        @SerializedName("segments")
        val segments: List<Segment>,
        @SerializedName("title")
        val title: Title
)