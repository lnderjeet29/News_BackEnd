package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsComment
import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsCommentReply
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsCommentDto

interface NewsCommentService {
    fun addComment(userId: Int, newsId: Int, content: String): NewsComment
    fun addReply(userId: Int, commentId: Int, content: String): NewsCommentReply
    fun likeComment(commentId: Int,userId: Int): Boolean
    fun unlikeComment(commentId: Int, userId: Int): Boolean
    fun likeReply(replyId: Int, userId: Int): Boolean
    fun unlikeReply(replyId: Int, userId: Int): Boolean
    fun fetchAllComments(newsId: Int, loggedInUserId: Int): List<NewsCommentDto>
    fun getTotalCommentsCount(newsId: Int): Int

    fun countLikesForReply(replyId: Int): Long
    fun isReplyLikedByUser(replyId: Int, userId: Int): Boolean
    fun countLikesForComment(commentId: Int): Long
    fun isCommentLikedByUser(commentId: Int, userId: Int): Boolean

    fun deleteComment(commentId: Int,userId: Int): Boolean
    fun deleteReply(replyId: Int,userId: Int): Boolean
}