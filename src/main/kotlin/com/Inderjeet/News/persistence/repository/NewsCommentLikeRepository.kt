package com.Inderjeet.News.persistence.repository

import com.Inderjeet.News.persistence.entities.news.NewsCommentLike
import com.Inderjeet.News.persistence.entities.news.NewsCommentReplyLike
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NewsCommentLikeRepository : JpaRepository<NewsCommentLike, Int> {
    fun countByCommentId(commentId: Int): Long
    fun existsByCommentIdAndUserId(commentId: Int, userId: Int): Boolean
    fun findByCommentIdAndUserId(commentId: Int, userId: Int): NewsCommentLike?
}

@Repository
interface NewsCommentReplyLikeRepository : JpaRepository<NewsCommentReplyLike, Int> {
    fun countByReplyId(replyId: Int): Long
    fun existsByReplyIdAndUserId(replyId: Int, userId: Int): Boolean
    fun findByReplyIdAndUserId(commentId: Int, userId: Int): NewsCommentReplyLike?
}