package com.example.shared.domain.models.details

import kotlinx.serialization.Serializable

@Serializable
data class Thumbs(
    val large: String,
    val original: String,
    val small: String
)