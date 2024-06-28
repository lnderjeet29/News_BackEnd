package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsComment
import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsCommentReply
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsCommentDto

interface NewsCommentService {
    fun addComment(userId: Int, newsId: Int, content: String): NewsComment
    fun addReply(userId: Int, commentId: Int, content: String): NewsCommentReply
    fun likeComment(commentId: Int): NewsComment
    fun unlikeComment(commentId: Int): NewsComment
    fun likeReply(replyId: Int): NewsCommentReply
    fun unlikeReply(replyId: Int): NewsCommentReply
    fun fetchAllComments(newsId: Int, loggedInUserId: Int): List<NewsCommentDto>
    fun getTotalCommentsCount(newsId: Int): Int
}