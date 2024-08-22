package com.Inderjeet.News.rest.controller

import com.Inderjeet.News.business.gateway.CategoryService
import com.Inderjeet.News.business.gateway.JWTServices
import com.Inderjeet.News.business.gateway.NewsCommentService
import com.Inderjeet.News.business.gateway.NewsLikeService
import com.Inderjeet.News.persistence.repository.UserRepositoryJpa
import com.Inderjeet.News.rest.dto.CommonResponse
import com.Inderjeet.News.rest.dto.news.NewsDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/news")
class NewsController
    (
    val newsServices: com.Inderjeet.News.business.gateway.NewsServices,
    val likeService: NewsLikeService,
    val jwtServices: JWTServices,
    val userRepositoryJpa: UserRepositoryJpa,
    val commentService: NewsCommentService,
    val categoryService: CategoryService
) {

    val logger: Logger = LoggerFactory.getLogger(NewsController::class.java)

    @PostMapping("/like-or-unlike")
    fun likeOrUnlikeNews(
        @RequestParam("isLiked") isLiked: Boolean,
        @RequestParam("newsId") newsId: Int,
        @RequestHeader(value = "Authorization") token: String,
    ): ResponseEntity<CommonResponse<String>> {
        try {
            logger.info(token)
            val userEmail = jwtServices.extractEmail(token.substring(7))
            val userId = userRepositoryJpa.findByEmail(userEmail).get().id
            if (isLiked) {
                val likes = likeService.addLike(userId, newsId)
                return ResponseEntity.ok().body(
                    CommonResponse(
                        message = "Successfully marked.",
                        status = true
                    )
                )
            } else {
                val likes = likeService.removeLike(userId, newsId)
                return ResponseEntity.ok().body(
                    CommonResponse(
                        message = "Successfully marked.",
                        status = true
                    )
                )
            }
        } catch (e: BadCredentialsException) {
            e.printStackTrace()
            return ResponseEntity.badRequest().body(
                CommonResponse(
                    message = e.message ?: "Session expired!",
                    status = false,
                    data = null
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.badRequest().body(
                CommonResponse(
                    message = e.message ?: "Something went wrong!",
                    status = false,
                    data = null
                )
            )
        }
    }


    @GetMapping("/all")
    fun getNews(
        @RequestHeader(value = "Authorization", required = false) token: String?,
        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) pageNumber: Int,
        @RequestParam(value = "pageSize", defaultValue = "10", required = false) pageSize: Int,
        @RequestParam(value = "category", defaultValue = "all", required = false) category: String
    )
            : ResponseEntity<CommonResponse<com.Inderjeet.News.rest.dto.PaginatedResponse<NewsDto>>> {
        return try {
            val response = newsServices.getNews(pageNumber = pageNumber, pageSize = pageSize, category = category)

            if (!token.isNullOrEmpty()) {
                val userEmail = jwtServices.extractEmail(token.substring(7))
                val userId = userRepositoryJpa.findByEmail(userEmail).get().id
                response.content.forEach { newsDto ->
                    newsDto.isLiked = likeService.isLikedByUser(userId, newsDto.id)
                }
            }
            response.content.forEach(){
                it.totalLikes = likeService.countLikesForNewsPost(it.id)
                it.comments= commentService.getTotalCommentsCount(it.id)
            }
            ResponseEntity.ok().body(
                CommonResponse(
                    message = "News articles retrieved successfully",
                    status = true,
                    data = response
                )
            )
        }
        catch (e: BadCredentialsException) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                CommonResponse(
                    message = "Session Expired.!",
                    status = false,
                    data = null
                )
            )
        }catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(
                CommonResponse(
                    message = e.message ?: "Something went wrong.!",
                    status = false,
                    data = null
                )
            )
        }
    }

    @GetMapping("/categories")
    fun getListOfCategory():ResponseEntity<CommonResponse<List<String>>>{
        return try {
            val response = categoryService.getAllCategory()
            ResponseEntity.ok().body(CommonResponse(message = "Successfully Retrieved List Of Category!", status = true, data = response))
        }catch (e:Exception){
            ResponseEntity.ok().body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }


    @GetMapping("/trending")
    fun getTrendingNews( @RequestHeader(value = "Authorization", required = false) token: String?,
                         @RequestParam(value = "pageNumber", defaultValue = "0", required = false) pageNumber: Int,
                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) pageSize: Int
    ):ResponseEntity<CommonResponse<com.Inderjeet.News.rest.dto.PaginatedResponse<NewsDto>>> {
       return  try {
            if(!token.isNullOrEmpty()) {
                val loggedInUserId = userRepositoryJpa.findByEmail(jwtServices.extractEmail(token.substring(7))).get().id
                newsServices.getTrendingNews(userId = loggedInUserId, pageNumber = pageNumber, pageSize = pageSize)
            }
            val response = newsServices.getTrendingNews(userId = null,pageNumber=pageNumber,pageSize=pageSize)
            ResponseEntity.ok().body(CommonResponse(message = "Trending news retrieved successfully!", status = true, data = response))
        } catch (e: Exception) {
            ResponseEntity.ok().body(CommonResponse(message = e.message ?: "Something went wrong!", status = true, data = null))
        }
    }

    @GetMapping("/detail")
    fun getNewsDetail(@RequestHeader(name = "Authorization", required = false) token: String?,@RequestParam newsId:Int)
    :ResponseEntity<CommonResponse<NewsDto>>
    {
        var userEmail:String? = ""
        return try {
            val newsDto = if(token.isNullOrEmpty()){
            newsServices.getNewsDetail(newsId,false)
            }else{
                userEmail = jwtServices.extractEmail(token.substring(7))
              newsServices.getNewsDetail(newsId,true)
            }
            if (!token.isNullOrEmpty()) {

                val userId = userRepositoryJpa.findByEmail(userEmail).get().id
                    newsDto.isLiked = likeService.isLikedByUser(userId, newsDto.id)
            }
            newsDto.totalLikes = likeService.countLikesForNewsPost(newsDto.id)
            newsDto.comments= commentService.getTotalCommentsCount(newsId)
            ResponseEntity.ok().body(CommonResponse(message = "News Details Fetched Successfully!", status = true, data = newsDto))
        } catch (e: BadCredentialsException) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                CommonResponse(
                    message = "Session Expired.!",
                    status = false,
                    data = null
                )
            )
        }catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(
                CommonResponse(
                    message = e.message ?: "Something went wrong.!",
                    status = false,
                    data = null
                )
            )
        }

    }


    @PutMapping("/share/increment")
    fun incrementShareCount(@RequestParam newsId: Int):ResponseEntity<CommonResponse<Boolean>>{
        return try {
            newsServices.incrementShareCount(newsId)
            ResponseEntity.ok().body(CommonResponse(message = "Success!", status = true))
        }catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(
                CommonResponse(
                    message = e.message ?: "Something went wrong.!",
                    status = false,
                    data = null
                )
            )
        }
    }



}