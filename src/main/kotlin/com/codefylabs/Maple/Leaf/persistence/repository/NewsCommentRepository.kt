package com.codefylabs.Maple.Leaf.persistence.repository

import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsComment
import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsCommentReply
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface NewsCommentRepository : JpaRepository<NewsComment, Int> {
    @Query("SELECT COUNT(c) FROM NewsComment c WHERE c.newsId = :newsId")
    fun countByNewsId(@Param("newsId") newsId: Int): Int
    fun findByNewsId(newsId: Int, pageable: Pageable): Page<NewsComment>
}

@Repository
interface NewsCommentReplyRepository : JpaRepository<NewsCommentReply, Int> {
    fun findByCommentId(commentId: Int): List<NewsCommentReply>
}