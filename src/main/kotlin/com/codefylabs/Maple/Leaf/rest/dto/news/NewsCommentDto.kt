package com.codefylabs.Maple.Leaf.rest.dto.news

import java.time.LocalDateTime

data class NewsCommentDto(
    val id: Int,
    val userId: Int,
    val userName:String,
    val userProfile: String?=null,
    val newsId: Int,
    val content: String,
    val createdAt: LocalDateTime,
    val likes: Long,
    val isLiked: Boolean,
    val isMine: Boolean,
    val replies: List<NewsCommentReplyDto> = emptyList()
)
data class NewsCommentReplyDto(
    val id: Int,
    val userId: Int,
    val userName:String,
    val userProfile: String?=null,
    val commentId: Int,
    val content: String,
    val createdAt: LocalDateTime,
    val likes: Long,
    val isLiked: Boolean,
    val isMine: Boolean
)