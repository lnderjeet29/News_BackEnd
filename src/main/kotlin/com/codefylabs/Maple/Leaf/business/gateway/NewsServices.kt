package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.persistence.News
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsDto
import com.codefylabs.Maple.Leaf.rest.dto.PaginatedResponse
import java.util.*


interface NewsServices {

    fun getNews(pageNumber:Int,pageSize:Int): PaginatedResponse<NewsDto>
    fun getNewsById(id:String): Optional<News>
//    fun getNewsByFilter(filter:String):List<News>
}