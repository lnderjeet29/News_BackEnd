package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsLikes

interface NewsLikeService {
    // Add a like
    fun addLike(userId: Int, newsPostId: Int): NewsLikes
    // Remove a like
    fun removeLike(userId: Int, newsPostId: Int)
    // Check if a user has liked a post
    fun isLikedByUser(userId: Int, newsPostId: Int): Boolean
    // Get all likes by a user
    fun getLikesByUser(userId: Int): List<NewsLikes>
    // Get all likes for a news post
    fun getLikesForNewsPost(newsPostId: Int): List<NewsLikes>
    // Count likes for a news post
    fun countLikesForNewsPost(newsPostId: Int): Long
}