package com.Inderjeet.News.business.gateway

import com.Inderjeet.News.rest.dto.news.NewsCommentDto
import com.Inderjeet.News.rest.dto.news.NewsCommentReplyDto

interface NewsCommentService {
    fun addComment(userId: Int, newsId: Int, content: String): NewsCommentDto
    fun addReply(userId: Int, commentId: Int, content: String): NewsCommentReplyDto
    fun likeComment(commentId: Int,userId: Int): Boolean
    fun unlikeComment(commentId: Int, userId: Int): Boolean
    fun likeReply(replyId: Int, userId: Int): Boolean
    fun unlikeReply(replyId: Int, userId: Int): Boolean
    fun fetchAllComments(newsId: Int, loggedInUserId: Int,pageSize:Int, pageNumber:Int): com.Inderjeet.News.rest.dto.PaginatedResponse<NewsCommentDto>
    fun getTotalCommentsCount(newsId: Int): Long

    fun countLikesForReply(replyId: Int): Long
    fun isReplyLikedByUser(replyId: Int, userId: Int): Boolean
    fun countLikesForComment(commentId: Int): Long
    fun isCommentLikedByUser(commentId: Int, userId: Int): Boolean

    fun deleteComment(commentId: Int,userId: Int): Boolean
    fun deleteReply(replyId: Int,userId: Int): Boolean
}