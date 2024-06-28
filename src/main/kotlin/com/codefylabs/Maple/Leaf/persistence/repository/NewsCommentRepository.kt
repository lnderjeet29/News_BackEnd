package com.codefylabs.Maple.Leaf.persistence.repository

import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsComment
import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsCommentReply
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NewsCommentRepository : JpaRepository<NewsComment, Int> {
    fun findByNewsId(newsId: Int): List<NewsComment>
}

@Repository
interface NewsCommentReplyRepository : JpaRepository<NewsCommentReply, Int> {
    fun findByCommentId(commentId: Int): List<NewsCommentReply>
}