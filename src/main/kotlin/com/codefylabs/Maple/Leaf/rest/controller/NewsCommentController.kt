package com.codefylabs.Maple.Leaf.rest.controller

import com.codefylabs.Maple.Leaf.business.gateway.JWTServices
import com.codefylabs.Maple.Leaf.business.gateway.NewsCommentService
import com.codefylabs.Maple.Leaf.business.gateway.UserServices
import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsComment
import com.codefylabs.Maple.Leaf.persistence.entities.news.NewsCommentReply
import com.codefylabs.Maple.Leaf.rest.ExceptionHandler.BadApiRequest
import com.codefylabs.Maple.Leaf.rest.dto.CommonResponse
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsCommentDto
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsCommentRequestDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/comments/news")
class NewsCommentController(private val commentService: NewsCommentService,
                            private val jwtServices: JWTServices,private val userServices: UserServices
    ) {

    @PostMapping
    fun addComment(@RequestHeader(name = "Authorization") token:String,@RequestBody commentRequest:NewsCommentRequestDto): ResponseEntity<CommonResponse<NewsComment>> {
        return try {
            val userId = userServices.findUser(jwtServices.extractEmail(token.substring(7)))?.id ?: throw BadApiRequest("user not found!")
            val comment = commentService.addComment(userId, newsId = commentRequest.newsId, content = commentRequest.content)
            ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse(message = "Commented Successfully!", status = true, data = comment))
        }catch (e:BadCredentialsException){
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CommonResponse(message = "Session expired!", status = false))
        }
        catch (e:Exception){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }

    @PostMapping("/{commentId}/replies")
    fun addReply(@RequestHeader(name = "Authorization") token:String,@PathVariable commentId: Int, @RequestParam content: String): ResponseEntity<CommonResponse<NewsCommentReply>> {
        return try {
            val userId = userServices.findUser(jwtServices.extractEmail(token.substring(7)))?.id ?: throw BadApiRequest("user not found!")
            val reply = commentService.addReply(userId, commentId, content)
            ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse(message = "Replied Successfully!", status = true, data = reply))
        } catch (e:BadCredentialsException){
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CommonResponse(message = "Session expired!", status = false))
        }catch (e:Exception){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }
    @PostMapping("/{commentId}/like")
    fun likeComment(@PathVariable commentId: Int): ResponseEntity<CommonResponse<NewsComment>> {
       return  try {
            val comment = commentService.likeComment(commentId)
            ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse(message = "Liked Successfully!", status = true, data = comment))
        }catch (e:BadCredentialsException){
           ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CommonResponse(message = "Session expired!", status = false))
       }catch (e:Exception){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }
    @PostMapping("/{commentId}/unlike")
    fun unlikeComment(@PathVariable commentId: Int): ResponseEntity<CommonResponse<NewsComment>> {
       return  try {
            val comment = commentService.unlikeComment(commentId)
            ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse(message = "Unlike Successfully!", status = true, data = comment))
        } catch (e:BadCredentialsException){
           ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CommonResponse(message = "Session expired!", status = false))
       }catch (e:Exception){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }
    @PostMapping("/replies/{replyId}/like")
    fun likeReply(@PathVariable replyId: Int): ResponseEntity<CommonResponse<NewsCommentReply>> {
      return try {
            val reply = commentService.likeReply(replyId)
          ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse(message = "Liked Successfully!", status = true, data = reply))
      } catch (e:BadCredentialsException){
          ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CommonResponse(message = "Session expired!", status = false))
      }catch (e:Exception){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }
    @PostMapping("/replies/{replyId}/unlike")
    fun unlikeReply(@PathVariable replyId: Int): ResponseEntity<CommonResponse<NewsCommentReply>> {
       return try {
            val reply = commentService.unlikeReply(replyId)
           ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse(message = "Unliked Successfully!", status = true, data = reply))
       } catch (e:BadCredentialsException){
           ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CommonResponse(message = "Session expired!", status = false))
       }catch (e:Exception){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }
    @GetMapping
    fun fetchAllComments(@RequestHeader(name = "Authorization") token:String,@RequestParam newsId: Int): ResponseEntity<CommonResponse<List<NewsCommentDto>>> {
       return try {
           val loggedInUserId = userServices.findUser(jwtServices.extractEmail(token.substring(7)))?.id ?: throw BadApiRequest("user not found!")
           val comments = commentService.fetchAllComments(newsId, loggedInUserId)
             ResponseEntity.ok().body(CommonResponse(message = "Fetched comment successfully!", status = true, data = comments))
        } catch (e:BadCredentialsException){
           ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CommonResponse(message = "Session expired!", status = false))
       }catch (e:Exception){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }
}