package com.Inderjeet.News.persistence.repository

import com.Inderjeet.News.persistence.entities.news.News
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface NewsRepositoryJPA: JpaRepository<News, Int> {

    fun findByCategory(category: String, pageable: Pageable): Page<News>
    @Query("SELECT n FROM News n WHERE n.isTrending = true")
    fun findAllTrending(pageable: Pageable): Page<News>

    @Query("SELECT COUNT(c) FROM NewsComment c WHERE c.news.id = :newsId")
    fun countCommentsByNewsId(@Param("newsId") newsId: Int): Long
    @Query("SELECT COUNT(l) FROM NewsLikes l WHERE l.newsPost.id = :newsId")
    fun countLikesByNewsId(@Param("newsId") newsId: Int): Long

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM NewsLikes l WHERE l.newsPost.id = :newsId AND l.user.id = :userId")
    fun isNewsLikedByUser(@Param("newsId") newsId: Int, @Param("userId") userId: Int?): Boolean


}