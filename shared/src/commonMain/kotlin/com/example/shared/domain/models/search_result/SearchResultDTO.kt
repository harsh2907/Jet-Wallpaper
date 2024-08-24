package com.example.shared.domain.models.search_result

import com.example.shared.domain.models.search_result.Meta
import com.example.shared.domain.models.search_result.ResultDTO
import kotlinx.serialization.Serializable

@Serializable
data class SearchResultDTO(
    val data: List<ResultDTO>,
    val meta: Meta
)