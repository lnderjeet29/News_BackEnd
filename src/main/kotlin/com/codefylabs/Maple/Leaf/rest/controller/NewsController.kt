package com.codefylabs.Maple.Leaf.rest.controller

import com.codefylabs.Maple.Leaf.business.gateway.NewsServices
import com.codefylabs.Maple.Leaf.persistence.News
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

@RestController
@RequestMapping("/api/v1/news")
class NewsController(val newsServices: NewsServices) {

    val logger:Logger=LoggerFactory.getLogger(NewsController::class.java)
    @GetMapping("/getNews")
    fun getNews(@RequestParam(value = "id") id:String): Optional<News> {

        return newsServices.getNewsById(id)
    }

    @GetMapping("/thumbnail")
    fun getThumbnailImageById(@RequestParam(value = "id") id: String): ResponseEntity<ByteArrayResource> {
        logger.info(id)
        val news = newsServices.getNewsById(id)
        if(news==null){
            print("value is nulll.....................................")
        }
        logger.info(news.get().newsId)
        val resource = ByteArrayResource(news.get().thumbnailImage)
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(news.get().thumbnailImage.size.toLong())
                .body(resource)
    }
}