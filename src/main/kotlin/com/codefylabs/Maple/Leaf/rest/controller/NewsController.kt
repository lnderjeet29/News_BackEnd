package com.codefylabs.Maple.Leaf.rest.controller

import com.codefylabs.Maple.Leaf.business.gateway.NewsServices
import com.codefylabs.Maple.Leaf.rest.dto.CommonResponse
import com.codefylabs.Maple.Leaf.rest.dto.PaginatedResponse
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/news")
class NewsController
    (val newsServices: NewsServices) {

    val logger: Logger = LoggerFactory.getLogger(NewsController::class.java)

    @GetMapping("/all")
    fun getNews(
        @RequestHeader(value = "Authorization", required = false) token: String?,
        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) pageNumber: Int,
        @RequestParam(value = "pageSize", defaultValue = "10", required = false) pageSize: Int,
        @RequestParam(value = "category", defaultValue = "all", required = false) category: String
    )
            : ResponseEntity<CommonResponse<PaginatedResponse<NewsDto>>> {
        return try {
            when {
                token.isNullOrEmpty() -> {
                    val response =
                        newsServices.getNews(pageNumber = pageNumber, pageSize = pageSize, category = category)
                    ResponseEntity.ok().body(
                        CommonResponse(
                            message = "News articles retrieved successfully",
                            status = true,
                            data = response
                        )
                    )
                }
                else -> {
                    val response =
                        newsServices.getNews(pageNumber = pageNumber, pageSize = pageSize, category = category)

                    ResponseEntity.ok().body(
                        CommonResponse(
                            message = "News articles retrieved successfully",
                            status = true,
                            data = response
                        )
                    )

                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(
                CommonResponse<PaginatedResponse<NewsDto>>(
                    message = e.message ?: "something went wrong.!",
                    status = true,
                    data = null
                )
            )
        }
    }

}