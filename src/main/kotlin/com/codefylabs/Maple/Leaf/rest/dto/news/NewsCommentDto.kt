package com.codefylabs.Maple.Leaf.rest.dto.news

import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsComment
import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsCommentReply
import java.time.LocalDateTime

data class NewsCommentDto(
    val id: Int,
    val userId: Int,
    val userName:String,
    val userProfile: String?=null,
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
fun NewsComment.toDto(likes:Long,isCommentLikedByUser:Boolean ,isMine: Boolean, replies: List<NewsCommentReplyDto>)= NewsCommentDto(
    id = this.id,
    userId = this.user.id,
    userName = this.user.userName ?: "",
    userProfile = this.user.profilePicture,
    content = this.content,
    createdAt = this.createdAt,
    likes = likes,
    isLiked = isCommentLikedByUser,
    isMine = isMine,
    replies = replies
)

fun NewsCommentReply.toDto(likes:Long, isReplyLikedByUser:Boolean,isMine: Boolean)= NewsCommentReplyDto(
    id = this.id,
    userId = this.user.id,
    userName = user.name.toString(),
    userProfile = user.profilePicture,
    commentId = this.comment.id,
    content = this.content,
    createdAt = this.createdAt,
    likes = likes,
    isLiked = isReplyLikedByUser,
    isMine = isMine,
)