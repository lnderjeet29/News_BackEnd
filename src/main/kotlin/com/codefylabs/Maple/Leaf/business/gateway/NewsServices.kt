package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.rest.dto.PaginatedResponse
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsDto
import com.codefylabs.Maple.Leaf.rest.dto.news.UploadNewsDto
import org.springframework.web.multipart.MultipartFile


interface NewsServices {

    fun getNews(pageNumber:Int,pageSize:Int,category: String): PaginatedResponse<NewsDto>

    fun getNewsDetail(newsId:Int,authenticated: Boolean):NewsDto

    fun incrementShareCount(newsId:Int)

    fun createNews(uploadNewsDto: UploadNewsDto):Boolean
    fun uploadDetailImg(detailImage: MultipartFile, newsId: Int):Boolean
    fun uploadThumbnailImg(thumbnailImage: MultipartFile,newsId:Int):Boolean
    fun deleteNews(newsId:Int):Boolean

    fun getTrendingNews(userId:Int?,pageNumber:Int,pageSize:Int): PaginatedResponse<NewsDto>
}