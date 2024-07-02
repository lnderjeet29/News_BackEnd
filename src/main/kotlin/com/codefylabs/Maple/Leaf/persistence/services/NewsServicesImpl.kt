package com.codefylabs.Maple.Leaf.persistence.services

import com.codefylabs.Maple.Leaf.business.gateway.CategoryService
import com.codefylabs.Maple.Leaf.business.gateway.ImageUploadService
import com.codefylabs.Maple.Leaf.business.gateway.NewsServices
import com.codefylabs.Maple.Leaf.persistence.entities.news.News
import com.codefylabs.Maple.Leaf.persistence.entities.news.PictureType
import com.codefylabs.Maple.Leaf.persistence.repository.NewsRepositoryJPA
import com.codefylabs.Maple.Leaf.rest.ExceptionHandler.BadApiRequest
import com.codefylabs.Maple.Leaf.rest.dto.PaginatedResponse
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsDto
import com.codefylabs.Maple.Leaf.rest.dto.news.UploadNewsDto
import com.codefylabs.Maple.Leaf.rest.dto.news.toDto
import com.codefylabs.Maple.Leaf.rest.helper.PageHelper
import org.modelmapper.ModelMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class NewsServicesImpl(val newsRepository: NewsRepositoryJPA,
                       val imageUploadService: ImageUploadService,
                       val categoryService: CategoryService
) : NewsServices
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

        override fun getNewsDetail(newsId: Int): NewsDto {
            val news = newsRepository.findById(newsId).orElseThrow{throw BadApiRequest("News not found!")}
            news.totalView+=1
            newsRepository.save(news)
            return ModelMapper().map(news, NewsDto::class.java)
        }
         override fun incrementShareCount(newsId:Int){
             val news = newsRepository.findById(newsId).orElseThrow{throw BadApiRequest("News not found!")}
             news.share+=1
             newsRepository.save(news)
         }

        override fun createNews(uploadNewsDto: UploadNewsDto): NewsDto {
            val news = News(
                title = uploadNewsDto.title,
                shortDescription = uploadNewsDto.shortDescription,
                description = uploadNewsDto.description,
                thumbnailUrl = null,
                detailImageUrl = null,
                source = uploadNewsDto.source,
                articleUrl = uploadNewsDto.articleUrl,
                category = uploadNewsDto.category.toString(),
                isTrending = uploadNewsDto.isTrending
            )
            var result= newsRepository.save(news)
            result.thumbnailUrl= imageUploadService.uploadImage(uploadNewsDto.thumbnailImage,PictureType.NEWS_THUMBNAIL,result.id.toString())
            result.detailImageUrl= imageUploadService.uploadImage(uploadNewsDto.detailImage,PictureType.NEWS_DETAIL,result.id.toString())
            result= newsRepository.save(result)
            if (!categoryService.isCategoryNameExists(result.category.lowercase())){
                categoryService.saveCategory(result.category.lowercase())
            }
            return ModelMapper().map(result,NewsDto::class.java)
        }

        override fun deleteNews(newsId: Int): Boolean {
            TODO("Not yet implemented")
        }

        override fun getTrendingNews(userId:Int?,pageNumber: Int, pageSize: Int): PaginatedResponse<NewsDto> {
            val pageable: Pageable = PageRequest.of(pageNumber, pageSize)
                val newsPage=newsRepository.findAllTrending(pageable)
            val response = newsPage.map { news ->
                val totalLikes = newsRepository.countLikesByNewsId(news.id)
                val totalComments = newsRepository.countCommentsByNewsId(news.id)
                val isLiked = if (userId != null) {
                    newsRepository.isNewsLikedByUser(news.id, userId)
                } else {
                    false
                }
                news.toDto(isLiked=isLiked, commentsCount = totalComments, totalLikes = totalLikes)
            }
            return PaginatedResponse(
                content = response.content,
                pageNumber = newsPage.number,
                totalElements = newsPage.totalElements,
                pageSize = newsPage.size,
                isLastPage = newsPage.isLast,
                totalPages = newsPage.totalPages
            )
        }
    }