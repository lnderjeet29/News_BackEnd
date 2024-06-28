package com.codefylabs.Maple.Leaf.persistence.repository

import com.codefylabs.Maple.Leaf.persistence.entities.news.LikeId
import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsLikes
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface LikeRepository : JpaRepository<NewsLikes, LikeId> {
    // Find all likes by a specific user
    fun findByUserId(userId: Int): List<NewsLikes>

    // Find all likes for a specific news post
    fun findByNewsPostId(newsPostId: Int): List<NewsLikes>

    // Count the number of likes for a specific news post
    fun countByNewsPostId(newsPostId: Int): Int

    // Check if a user has liked a specific news post
    fun existsByUserIdAndNewsPostId(userId: Int, newsPostId: Int): Boolean

}