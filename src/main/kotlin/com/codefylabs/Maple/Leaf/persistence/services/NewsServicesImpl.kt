package com.codefylabs.Maple.Leaf.persistence.services

import com.codefylabs.Maple.Leaf.business.gateway.NewsServices
import com.codefylabs.Maple.Leaf.persistence.repository.NewsRepositoryJPA
import com.codefylabs.Maple.Leaf.rest.dto.PaginatedResponse
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsDto
import com.codefylabs.Maple.Leaf.rest.helper.PageHelper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class NewsServicesImpl(val newsRepository: NewsRepositoryJPA) : NewsServices
    {

    val logger: Logger = LoggerFactory.getLogger(NewsServicesImpl::class.java)

        override fun getNews(pageNumber: Int, pageSize: Int, category: String): PaginatedResponse<NewsDto> {
            val pageable: Pageable = PageRequest.of(pageNumber, pageSize)
            val newsPage = if (category.trim().equals("all", ignoreCase = true)) {
                newsRepository.findAll(pageable)
            } else {
                newsRepository.findByCategory(category, pageable)
            }
            return PageHelper.getPageResponse(newsPage, NewsDto::class.java)
        }


}