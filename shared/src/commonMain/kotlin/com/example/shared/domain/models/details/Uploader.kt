package com.example.shared.domain.models.details

import kotlinx.serialization.Serializable

@Serializable
data class Uploader(
    val avatar: Avatar,
    val group: String,
    val username: String
)