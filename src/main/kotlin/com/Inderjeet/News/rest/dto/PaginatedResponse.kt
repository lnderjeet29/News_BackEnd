package com.Inderjeet.News.rest.dto

data class PaginatedResponse<T>(
    val content: List<T>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalElements: Long,
    val totalPages: Int,
    val isLastPage: Boolean
)