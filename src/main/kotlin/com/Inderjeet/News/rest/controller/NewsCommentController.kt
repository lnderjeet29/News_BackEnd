package com.Inderjeet.News.rest.controller

import com.Inderjeet.News.business.gateway.JWTServices
import com.Inderjeet.News.business.gateway.NewsCommentService
import com.Inderjeet.News.business.gateway.UserServices
import com.Inderjeet.News.rest.ExceptionHandler.BadApiRequest
import com.Inderjeet.News.rest.dto.CommonResponse
import com.Inderjeet.News.rest.dto.news.NewsCommentDto
import com.Inderjeet.News.rest.dto.news.NewsCommentReplyDto
import com.Inderjeet.News.rest.dto.news.NewsCommentRequestDto
import com.Inderjeet.News.rest.dto.news.ReplyRequestDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/comments/news")
class NewsCommentController(
    private val commentService: NewsCommentService,
    private val jwtServices: JWTServices, private val userServices: UserServices
) {

    @PostMapping
    fun addComment(
        @RequestHeader(name = "Authorization") token: String,
        @RequestBody commentRequest: NewsCommentRequestDto
    ): ResponseEntity<CommonResponse<NewsCommentDto>> {
        return try {
            val userId = userServices.findUser(jwtServices.extractEmail(token.substring(7)))?.id ?: throw BadApiRequest(
                "user not found!"
            )
            val comment =
                commentService.addComment(userId, newsId = commentRequest.newsId, content = commentRequest.content)
            ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse(message = "Commented Successfully!", status = true, data = comment))
        } catch (e: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse(message = "Session expired!", status = false))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }

    @PostMapping("/reply")
    fun addReply(
        @RequestHeader(name = "Authorization") token: String,
        @RequestParam commentId: Int,
        @RequestBody body: ReplyRequestDto
    ): ResponseEntity<CommonResponse<NewsCommentReplyDto>> {
        return try {
            val userId = userServices.findUser(jwtServices.extractEmail(token.substring(7)))?.id ?: throw BadApiRequest(
                "user not found!"
            )
            val reply = commentService.addReply(userId = userId, commentId = commentId, body.content)
            ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse(message = "Replied Successfully!", status = true, data = reply))
        } catch (e: BadCredentialsException) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse(message = "Session expired!", status = false))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }

    @PostMapping("/like")
    fun likeComment(
        @RequestHeader(name = "Authorization") token: String,
        @RequestParam commentId: Int
    ): ResponseEntity<CommonResponse<Boolean>> {
        return try {
            val userId = userServices.findUser(jwtServices.extractEmail(token.substring(7)))?.id ?: throw BadApiRequest(
                "user not found!"
            )
            val comment = commentService.likeComment(userId = userId, commentId = commentId)
            ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse(message = "Liked Successfully!", status = true, data = comment))
        } catch (e: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse(message = "Session expired!", status = false))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }

    @PostMapping("/unlike")
    fun unlikeComment(
        @RequestHeader(name = "Authorization") token: String,
        @RequestParam commentId: Int
    ): ResponseEntity<CommonResponse<Boolean>> {
        return try {
            val userId = userServices.findUser(jwtServices.extractEmail(token.substring(7)))?.id ?: throw BadApiRequest(
                "user not found!"
            )
            val comment = commentService.unlikeComment(userId = userId, commentId = commentId)
            ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse(message = "Unlike Successfully!", status = true, data = comment))
        } catch (e: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse(message = "Session expired!", status = false))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }

    @PostMapping("/reply/like")
    fun likeReply(
        @RequestHeader(name = "Authorization") token: String,
        @RequestParam replyId: Int
    ): ResponseEntity<CommonResponse<Boolean>> {
        return try {
            val userId = userServices.findUser(jwtServices.extractEmail(token.substring(7)))?.id ?: throw BadApiRequest(
                "user not found!"
            )
            val reply = commentService.likeReply(userId = userId, replyId = replyId)
            ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse(message = "Liked Successfully!", status = true, data = reply))
        } catch (e: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse(message = "Session expired!", status = false))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }

    @PostMapping("/reply/unlike")
    fun unlikeReply(
        @RequestHeader(name = "Authorization") token: String,
        @RequestParam replyId: Int
    ): ResponseEntity<CommonResponse<Boolean>> {
        return try {
            val userId = userServices.findUser(jwtServices.extractEmail(token.substring(7)))?.id ?: throw BadApiRequest(
                "user not found!"
            )
            val reply = commentService.unlikeReply(userId = userId, replyId = replyId)
            ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse(message = "Unliked Successfully!", status = true, data = reply))
        } catch (e: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse(message = "Session expired!", status = false))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }

    @GetMapping
    fun fetchAllComments(
        @RequestHeader(name = "Authorization") token: String,
        @RequestParam newsId: Int,
        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) pageNumber: Int,
        @RequestParam(value = "pageSize", defaultValue = "10", required = false) pageSize: Int,
    ): ResponseEntity<CommonResponse<com.Inderjeet.News.rest.dto.PaginatedResponse<NewsCommentDto>>> {
        return try {
            val loggedInUserId = userServices.findUser(jwtServices.extractEmail(token.substring(7)))?.id
                ?: throw BadApiRequest("user not found!")
            val comments = commentService.fetchAllComments(newsId, loggedInUserId, pageNumber = pageNumber, pageSize = pageSize)
            ResponseEntity.ok()
                .body(CommonResponse(message = "Fetched comment successfully!", status = true, data = comments))
        } catch (e: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse(message = "Session expired!", status = false))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }

    @DeleteMapping("/comment")
    fun deleteComment(@RequestHeader(name = "Authorization") token:String,@RequestParam commentId: Int):ResponseEntity<CommonResponse<Boolean>>{

        return try {
            val loggedInUserId = userServices.findUser(jwtServices.extractEmail(token.substring(7)))?.id
                ?: throw BadApiRequest("user not found!")
            val response = commentService.deleteComment(commentId,loggedInUserId)
            ResponseEntity.ok()
                .body(CommonResponse(message = "Deleted Successfully!", status = true, data = response))
        } catch (e: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse(message = "Session expired!", status = false))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }

    @DeleteMapping("/reply")
    fun deleteReply(@RequestHeader(name = "Authorization") token:String,@RequestParam replyId:Int):ResponseEntity<CommonResponse<Boolean>>{
        return try {
            val loggedInUserId = userServices.findUser(jwtServices.extractEmail(token.substring(7)))?.id
                ?: throw BadApiRequest("user not found!")
            val response = commentService.deleteReply(replyId = replyId,loggedInUserId)
            ResponseEntity.ok()
                .body(CommonResponse(message = "Deleted Successfully!", status = true, data = response))
        } catch (e: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse(message = "Session expired!", status = false))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }
}