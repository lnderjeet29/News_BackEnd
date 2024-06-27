package com.codefylabs.Maple.Leaf.persistence.implementation
//
//import com.codefylabs.Maple.Leaf.business.gateway.NewsServices
//import com.codefylabs.Maple.Leaf.persistence.News
//import com.codefylabs.Maple.Leaf.persistence.NewsRepositoryJPA
//import com.codefylabs.Maple.Leaf.rest.dto.news.NewsDto
//import com.codefylabs.Maple.Leaf.rest.dto.PaginatedResponse
//import com.codefylabs.Maple.Leaf.rest.helper.PageHelper
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import org.springframework.data.domain.Page
//import org.springframework.data.domain.PageRequest
//import org.springframework.stereotype.Service
//import org.springframework.data.domain.Pageable
//import java.lang.RuntimeException
//import java.util.Optional
//
//
//@Service
//class NewsServicesImpl(val newRepository:NewsRepositoryJPA)
////    : NewsServices
//    {
////
////    val logger: Logger = LoggerFactory.getLogger(NewsServicesImpl::class.java)
////    override fun getNews(pageNumber:Int,pageSize:Int): PaginatedResponse<NewsDto> {
////        val pageable: Pageable = PageRequest.of(pageNumber, pageSize)
////        val page: Page<News?> = newRepository.findAll(pageable)
////        return PageHelper.getPageResponse(page, NewsDto::class.java)
////    }
////
////    override fun getNewsById(id: Int): Optional<News> {
////        var data:Optional<News>
////        try {
////            data=newRepository.findById(id)
////        } catch (e: Exception) {
////           println("exception occur from here..............")
////            print(e.message)
////            throw RuntimeException("error")
////        }
////        return data
////    }
////
//////    override fun getNewsByFilter(filter: String): List<News> {
//////        TODO("Not yet implemented")
//////    }
//}