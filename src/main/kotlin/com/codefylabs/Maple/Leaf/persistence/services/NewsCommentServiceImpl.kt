package com.codefylabs.Maple.Leaf.persistence.services

import com.codefylabs.Maple.Leaf.business.gateway.NewsCommentService
import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsComment
import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsCommentLike
import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsCommentReply
import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsCommentReplyLike
import com.codefylabs.Maple.Leaf.persistence.repository.*
import com.codefylabs.Maple.Leaf.rest.ExceptionHandler.BadApiRequest
import com.codefylabs.Maple.Leaf.rest.dto.PaginatedResponse
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsCommentDto
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsCommentReplyDto
import com.codefylabs.Maple.Leaf.rest.dto.news.toDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
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
   override fun addComment(userId: Int, newsId: Int, content: String): NewsCommentDto {
        val user = userRepository.findById(userId).orElseThrow { BadApiRequest("User not found") }
            ?: throw BadApiRequest("User not found")
       if (!user.enabled || user.isBlocked){
           throw BadApiRequest("User not allowed")
       }
        val comment = commentRepository.save(NewsComment(user = user, newsId = newsId, content = content))
       val replies = replyRepository.findByCommentId(comment.id).map { reply ->
           reply.toDto(
               likes = countLikesForReply(reply.id),
               isReplyLikedByUser = isReplyLikedByUser(reply.id, userId = userId),
               isMine = reply.user.id == userId
           )
       }
      val dto= comment.toDto(likes = countLikesForComment(comment.id),
           isCommentLikedByUser = isCommentLikedByUser(commentId = comment.id,userId),
           isMine = comment.user.id == userId,
           replies = replies)
       return dto
    }
    override fun addReply(userId: Int, commentId: Int, content: String): NewsCommentReplyDto {
        val user = userRepository.findById(userId).orElseThrow { BadApiRequest("User not found") }
        val comment = commentRepository.findById(commentId).orElseThrow { BadApiRequest("Comment not found") }
        if (user==null){
            throw BadApiRequest("User not found")
        }
        if (!user.enabled || user.isBlocked){
            throw BadApiRequest("User not allowed")
        }
        val reply = replyRepository.save(NewsCommentReply(user = user, comment = comment, content = content))
         return    reply.toDto(
                likes = countLikesForReply(reply.id),
                isReplyLikedByUser = isReplyLikedByUser(reply.id, userId = userId),
                isMine = reply.user.id == userId
            )
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
    override fun fetchAllComments(
        newsId: Int,
        loggedInUserId: Int,
        pageSize: Int,
        pageNumber: Int
    ): PaginatedResponse<NewsCommentDto> {
        val pageable: Pageable = PageRequest.of(pageNumber, pageSize)
        val commentsPage: Page<NewsComment> = commentRepository.findByNewsId(newsId, pageable)

        val response = commentsPage.map { comment ->
            val replies = replyRepository.findByCommentId(comment.id).map { reply ->
                reply.toDto(
                    likes = countLikesForReply(reply.id),
                    isReplyLikedByUser = isReplyLikedByUser(reply.id, userId = loggedInUserId),
                    isMine = reply.user.id == loggedInUserId
                )
            }
            comment.toDto(
                likes = countLikesForComment(comment.id),
                isCommentLikedByUser = isCommentLikedByUser(comment.id, loggedInUserId),
                isMine = comment.user.id == loggedInUserId,
                replies = replies
            )
        }
        return PaginatedResponse(
            content = response.content,
            pageNumber = commentsPage.number,
            totalElements = commentsPage.totalElements,
            pageSize = commentsPage.size,
            isLastPage = commentsPage.isLast,
            totalPages = commentsPage.totalPages
        )
    }
    override fun getTotalCommentsCount(newsId: Int): Int { return commentRepository.countByNewsId(newsId) }

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