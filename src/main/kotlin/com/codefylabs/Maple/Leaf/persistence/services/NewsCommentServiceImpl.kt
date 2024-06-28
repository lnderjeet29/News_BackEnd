package com.codefylabs.Maple.Leaf.persistence.services

import com.codefylabs.Maple.Leaf.business.gateway.NewsCommentService
import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsComment
import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsCommentReply
import com.codefylabs.Maple.Leaf.persistence.repository.NewsCommentReplyRepository
import com.codefylabs.Maple.Leaf.persistence.repository.NewsCommentRepository
import com.codefylabs.Maple.Leaf.persistence.repository.NewsRepositoryJPA
import com.codefylabs.Maple.Leaf.persistence.repository.UserRepositoryJpa
import com.codefylabs.Maple.Leaf.rest.ExceptionHandler.BadApiRequest
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsCommentDto
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsCommentReplyDto
import org.springframework.stereotype.Service

@Service
class NewsCommentServiceImpl(
    private val commentRepository: NewsCommentRepository,
    private val replyRepository: NewsCommentReplyRepository,
    private val userRepository: UserRepositoryJpa,
    private val newsRepository: NewsRepositoryJPA
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
    override fun likeComment(commentId: Int): NewsComment {
        val comment = commentRepository.findById(commentId).orElseThrow { BadApiRequest("Comment not found") }
        comment.likes++
        return commentRepository.save(comment)
    }
    override fun unlikeComment(commentId: Int): NewsComment {
        val comment = commentRepository.findById(commentId).orElseThrow { BadApiRequest("Comment not found") }
        if (comment.likes > 0) comment.likes--
        return commentRepository.save(comment)
    }
    override fun likeReply(replyId: Int): NewsCommentReply {
        val reply = replyRepository.findById(replyId).orElseThrow { BadApiRequest("Reply not found") }
        reply.likes++
        return replyRepository.save(reply)
    }
    override fun unlikeReply(replyId: Int): NewsCommentReply {
        val reply = replyRepository.findById(replyId).orElseThrow { BadApiRequest("Reply not found") }
        if (reply.likes > 0) reply.likes--
        return replyRepository.save(reply)
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
                    likes = reply.likes,
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
                likes = comment.likes,
                isMine = comment.user.id == loggedInUserId,
                replies = replies
            )
        }
    }
    override fun getTotalCommentsCount(newsId: Int): Int { return commentRepository.findByNewsId(newsId).size }
}