package com.codefylabs.Maple.Leaf.persistence.implementation

import com.codefylabs.Maple.Leaf.business.gateway.NewsLikeService
import com.codefylabs.Maple.Leaf.persistance.User
import com.codefylabs.Maple.Leaf.persistence.LikeRepository
import com.codefylabs.Maple.Leaf.persistence.News
import com.codefylabs.Maple.Leaf.persistence.NewsLikes
import org.springframework.stereotype.Service

@Service
class NewsLikeServiceImpl (
    private val likeRepository: LikeRepository
) : NewsLikeService {
    override fun addLike(userId: Int, newsPostId: Int): NewsLikes {
        // Assume user and newsPost are retrieved from their respective repositories
        val user = User().apply { id = userId }
        val newsPost = News().apply { id = newsPostId }
        val like = NewsLikes(user, newsPost)
        return likeRepository.save(like)
    }
    override fun removeLike(userId: Int, newsPostId: Int) {
        val user = User().apply { id = userId }
        val newsPost = News().apply { id = newsPostId }
        val like = NewsLikes(user, newsPost)
        likeRepository.delete(like)
    }
    override fun isLikedByUser(userId: Int, newsPostId: Int): Boolean {
        return likeRepository.existsByUserIdAndNewsPostId(userId, newsPostId)
    }
    override fun getLikesByUser(userId: Int): List<NewsLikes> {
        return likeRepository.findByUserId(userId)
    }
    override fun getLikesForNewsPost(newsPostId: Int): List<NewsLikes> {
        return likeRepository.findByNewsPostId(newsPostId)
    }
    override fun countLikesForNewsPost(newsPostId: Int): Int {
        return likeRepository.countByNewsPostId(newsPostId)
    }
}
