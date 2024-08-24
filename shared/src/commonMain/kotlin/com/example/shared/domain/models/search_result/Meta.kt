package com.example.shared.domain.models.search_result

import kotlinx.serialization.Serializable

@Serializable
data class Meta(
    val current_page: Int,
    val last_page: Int,
    val per_page: String,
    val query: String,
    val total: Int
)