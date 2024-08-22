package com.Inderjeet.News.persistence.services

import com.Inderjeet.News.business.gateway.CategoryService
import com.Inderjeet.News.business.gateway.ImageUploadService
import com.Inderjeet.News.persistence.entities.news.News
import com.Inderjeet.News.persistence.entities.news.PictureType
import com.Inderjeet.News.persistence.repository.NewsRepositoryJPA
import com.Inderjeet.News.rest.ExceptionHandler.BadApiRequest
import com.Inderjeet.News.rest.dto.news.NewsDto
import com.Inderjeet.News.rest.dto.news.UploadNewsDto
import com.Inderjeet.News.rest.dto.news.toDto
import com.Inderjeet.News.rest.helper.PageHelper
import org.modelmapper.ModelMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile


@Service
class NewsServicesImpl(val newsRepository: NewsRepositoryJPA,
                       val imageUploadService: ImageUploadService,
                       val categoryService: CategoryService
) : com.Inderjeet.News.business.gateway.NewsServices
    {

    val logger: Logger = LoggerFactory.getLogger(NewsServicesImpl::class.java)

        override fun getNews(pageNumber: Int, pageSize: Int, category: String): com.Inderjeet.News.rest.dto.PaginatedResponse<NewsDto> {
            val pageable: Pageable = PageRequest.of(pageNumber, pageSize)
            val newsPage = if (category.trim().equals("all", ignoreCase = true)) {
                newsRepository.findAll(pageable)
            } else {
                newsRepository.findByCategory(category, pageable)
            }
            return PageHelper.getPageResponse(newsPage, NewsDto::class.java)
        }

        override fun getNewsDetail(newsId: Int, authenticated:Boolean): NewsDto {
            val news = newsRepository.findById(newsId).orElseThrow{throw BadApiRequest("News not found!")}
            if(authenticated){
            news.totalView+=1
            }
            newsRepository.save(news)
            return ModelMapper().map(news, NewsDto::class.java)
        }
         override fun incrementShareCount(newsId:Int){
             val news = newsRepository.findById(newsId).orElseThrow{throw BadApiRequest("News not found!")}
             news.share+=1
             newsRepository.save(news)
         }

        override fun createNews(uploadNewsDto: UploadNewsDto): Int {
            val news = News(
                title = uploadNewsDto.title,
                shortDescription = uploadNewsDto.shortDescription,
                description = uploadNewsDto.description,
                thumbnailUrl = null,
                detailImageUrl = null,
                source = uploadNewsDto.source,
                articleUrl = uploadNewsDto.articleUrl,
                category = uploadNewsDto.category.toString().lowercase(),
                isTrending = uploadNewsDto.isTrending
            )
            val result= newsRepository.save(news)
            if (!categoryService.isCategoryNameExists(result.category.lowercase())){

                categoryService.saveCategory(result.category.lowercase())
            }
            return result.id
        }

        override fun deleteNews(newsId: Int): Boolean {
            TODO("Not yet implemented")
        }

        override fun uploadThumbnailImg(thumbnailImage: MultipartFile,newsId:Int):Boolean{
            return try {
                var result= newsRepository.findById(newsId).orElseThrow{BadApiRequest(message = "News not found!")}
                result.thumbnailUrl=imageUploadService.uploadImage(thumbnailImage,PictureType.NEWS_THUMBNAIL,result.id.toString())
                newsRepository.save(result)
                true
            } catch (e: Exception) {
                logger.info(e.message)
                false
            }
        }
        override fun uploadDetailImg(detailImage: MultipartFile, newsId: Int):Boolean{
            return try {
                var result = newsRepository.findById(newsId).orElseThrow{BadApiRequest(message = "News not found!")}
                result.detailImageUrl=imageUploadService.uploadImage(detailImage,PictureType.NEWS_DETAIL,result.id.toString())
                newsRepository.save(result)
                true
            } catch (e: Exception) {
                logger.info(e.message)
                false
            }
        }
        @Transactional(readOnly = true)
        override fun getTrendingNews(userId:Int?,pageNumber: Int, pageSize: Int): com.Inderjeet.News.rest.dto.PaginatedResponse<NewsDto> {
            val pageable: Pageable = PageRequest.of(pageNumber, pageSize)
            val newsPage=newsRepository.findAllTrending(pageable)
            val response = newsPage.map { news ->
                news.toDto(isLiked=false, commentsCount = 0, totalLikes = 0)
            }
            return com.Inderjeet.News.rest.dto.PaginatedResponse(
                content = response.content,
                pageNumber = newsPage.number,
                totalElements = newsPage.totalElements,
                pageSize = newsPage.size,
                isLastPage = newsPage.isLast,
                totalPages = newsPage.totalPages
            )
        }
    }