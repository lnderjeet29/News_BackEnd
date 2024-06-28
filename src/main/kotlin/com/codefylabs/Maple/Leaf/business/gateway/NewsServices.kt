package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.rest.dto.PaginatedResponse
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsDto


interface NewsServices {

    fun getNews(pageNumber:Int,pageSize:Int,category: String): PaginatedResponse<NewsDto>
}