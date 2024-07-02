package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.rest.dto.PaginatedResponse
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsDto
import com.codefylabs.Maple.Leaf.rest.dto.news.UploadNewsDto


interface NewsServices {

    fun getNews(pageNumber:Int,pageSize:Int,category: String): PaginatedResponse<NewsDto>

    fun getNewsDetail(newsId:Int):NewsDto

    fun incrementShareCount(newsId:Int)

    fun createNews(uploadNewsDto: UploadNewsDto):NewsDto
    fun deleteNews(newsId:Int):Boolean

    fun getTrendingNews(userId:Int?,pageNumber:Int,pageSize:Int): PaginatedResponse<NewsDto>
}