package com.codefylabs.Maple.Leaf.persistence.services

import com.codefylabs.Maple.Leaf.business.gateway.NewsCommentService
import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsComment
import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsCommentLike
import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsCommentReply
import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsCommentReplyLike
import com.codefylabs.Maple.Leaf.persistence.repository.*
import com.codefylabs.Maple.Leaf.rest.ExceptionHandler.BadApiRequest
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsCommentDto
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsCommentReplyDto
import org.springframework.stereotype.Service

@Service
class NewsCommentServiceImpl(
    private val commentRepository: NewsCommentRepository,
    private val replyRepository: NewsCommentReplyRepository,
    private val userRepository: UserRepositoryJpa,
    private val newsRepository: NewsRepositoryJPA,
    private val newsCommentLikeRepository:NewsCommentLikeRepository,
    private val newsCommentReplyLikeRepository: NewsCommentReplyLikeRepository
):NewsCommentService {
   override fun addComment(userId: Int, newsId: Int, content: String): NewsComment {
        val user = userRepository.findById(userId).orElseThrow { BadApiRequest("User not found") }
        val news = newsRepository.findById(newsId).orElseThrow { BadApiRequest("News not found") }
       if (user==null){
           throw BadApiRequest("User not found")
       }
       if (!user.enabled || user.isBlocked){
           throw BadApiRequest("User not allowed")
       }
        val comment = NewsComment(user = user, news = news, content = content)
        return commentRepository.save(comment)
    }
    override fun addReply(userId: Int, commentId: Int, content: String): NewsCommentReply {
        val user = userRepository.findById(userId).orElseThrow { BadApiRequest("User not found") }
        val comment = commentRepository.findById(commentId).orElseThrow { BadApiRequest("Comment not found") }
        if (user==null){
            throw BadApiRequest("User not found")
        }
        if (!user.enabled || user.isBlocked){
            throw BadApiRequest("User not allowed")
        }
        val reply = NewsCommentReply(user = user, comment = comment, content = content)
        return replyRepository.save(reply)
    }
    override fun likeComment(commentId: Int, userId: Int): Boolean {
        if (newsCommentLikeRepository.existsByCommentIdAndUserId(commentId, userId)) {
            return false
        }
        val comment = commentRepository.findById(commentId)
            .orElseThrow { throw RuntimeException("Comment with id $commentId not found") }
        val like = NewsCommentLike(
            user = comment.user,
            comment = comment
        )
        newsCommentLikeRepository.save(like)
        return true
    }
    override fun unlikeComment(commentId: Int, userId: Int): Boolean {
        val like = newsCommentLikeRepository.findByCommentIdAndUserId(commentId, userId) ?: return false
        newsCommentLikeRepository.delete(like)
        return true
    }
    override fun likeReply(replyId: Int, userId: Int): Boolean {
        if (newsCommentReplyLikeRepository.existsByReplyIdAndUserId(replyId, userId)) {
            return false // User already liked this reply
        }
        val reply = replyRepository.findById(replyId)
            .orElseThrow { throw RuntimeException("Reply with id $replyId not found") }
        val like = NewsCommentReplyLike(
            user = reply.user,
            reply = reply
        )
        newsCommentReplyLikeRepository.save(like)
        return true
    }
    override fun unlikeReply(replyId: Int, userId: Int): Boolean {
        val like = newsCommentReplyLikeRepository.findByReplyIdAndUserId(replyId, userId)
            ?: return false
        newsCommentReplyLikeRepository.delete(like)
        return true
    }
   override fun fetchAllComments(newsId: Int, loggedInUserId: Int): List<NewsCommentDto> {
        return commentRepository.findByNewsId(newsId).map { comment ->
            val replies = replyRepository.findByCommentId(comment.id).map { reply ->
                NewsCommentReplyDto(
                    id = reply.id,
                    userId = reply.user.id,
                    userName = comment.user.name.toString(),
                    userProfile = comment.user.profilePicture,
                    commentId = reply.comment.id,
                    content = reply.content,
                    createdAt = reply.createdAt,
                    likes = countLikesForReply(reply.id),
                    isLiked = isReplyLikedByUser(reply.id, userId = loggedInUserId),
                    isMine = reply.user.id == loggedInUserId
                )
            }
            NewsCommentDto(
                id = comment.id,
                userId = comment.user.id,
                userName = comment.user.name.toString(),
                userProfile = comment.user.profilePicture,
                newsId = comment.news.id,
                content = comment.content,
                createdAt = comment.createdAt,
                likes = countLikesForComment(comment.id),
                isLiked = isCommentLikedByUser(commentId = comment.id,loggedInUserId),
                isMine = comment.user.id == loggedInUserId,
                replies = replies
            )
        }
    }
    override fun getTotalCommentsCount(newsId: Int): Int { return commentRepository.findByNewsId(newsId).size }

    override fun countLikesForComment(commentId: Int): Long { return newsCommentLikeRepository.countByCommentId(commentId) }
    override fun isCommentLikedByUser(commentId: Int, userId: Int): Boolean { return newsCommentLikeRepository.existsByCommentIdAndUserId(commentId, userId) }
    override fun deleteComment(commentId: Int,userId: Int): Boolean {
        if (commentRepository.existsById(commentId) && commentRepository.findById(commentId).get().user.id==userId) {
            commentRepository.deleteById(commentId)
            return true
        }
        return false
    }

    override fun deleteReply(replyId: Int,userId: Int): Boolean {
        if (replyRepository.existsById(replyId) && replyRepository.findById(replyId).get().user.id==userId) {
            replyRepository.deleteById(replyId)
            return true
        }
        return false
    }

    override fun countLikesForReply(replyId: Int): Long { return newsCommentReplyLikeRepository.countByReplyId(replyId) }
    override fun isReplyLikedByUser(replyId: Int, userId: Int): Boolean { return newsCommentReplyLikeRepository.existsByReplyIdAndUserId(replyId, userId) }
}