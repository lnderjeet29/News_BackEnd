package com.Inderjeet.News.business.gateway
import com.Inderjeet.News.rest.dto.news.NewsDto
import com.Inderjeet.News.rest.dto.news.UploadNewsDto
import org.springframework.web.multipart.MultipartFile


interface NewsServices {

    fun getNews(pageNumber:Int,pageSize:Int,category: String): com.Inderjeet.News.rest.dto.PaginatedResponse<NewsDto>

    fun getNewsDetail(newsId:Int,authenticated: Boolean):NewsDto

    fun incrementShareCount(newsId:Int)

    fun createNews(uploadNewsDto: UploadNewsDto):Int
    fun uploadDetailImg(detailImage: MultipartFile, newsId: Int):Boolean
    fun uploadThumbnailImg(thumbnailImage: MultipartFile,newsId:Int):Boolean
    fun deleteNews(newsId:Int):Boolean

    fun getTrendingNews(userId:Int?,pageNumber:Int,pageSize:Int): com.Inderjeet.News.rest.dto.PaginatedResponse<NewsDto>
}