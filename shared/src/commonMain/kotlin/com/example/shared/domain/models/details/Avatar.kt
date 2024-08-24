package com.example.shared.domain.models.details

import kotlinx.serialization.Serializable

@Serializable
data class Avatar(
    val `128px`: String,
    val `200px`: String,
    val `20px`: String,
    val `32px`: String
)