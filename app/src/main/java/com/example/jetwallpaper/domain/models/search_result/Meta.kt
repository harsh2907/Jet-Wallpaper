package com.example.jetwallpaper.domain.models.search_result

data class Meta(
    val current_page: Int,
    val last_page: Int,
    val per_page: String,
    val query: String,
    val seed: Any,
    val total: Int
)