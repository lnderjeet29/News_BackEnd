package com.codefylabs.Maple.Leaf.persistence.implementation

import com.codefylabs.Maple.Leaf.business.gateway.AdminServices
import com.codefylabs.Maple.Leaf.persistance.User
import com.codefylabs.Maple.Leaf.persistance.UserRepositoryJpa
import com.codefylabs.Maple.Leaf.persistence.News
import com.codefylabs.Maple.Leaf.persistence.NewsRepositoryJPA
import com.codefylabs.Maple.Leaf.rest.ExceptionHandler.BadApiRequest
import com.codefylabs.Maple.Leaf.rest.dto.PageResponse
import com.codefylabs.Maple.Leaf.rest.dto.UserDto
import com.codefylabs.Maple.Leaf.rest.helper.PageHelper.getPageResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*
import kotlin.random.Random

@Service
class AdminServicesImpl(val userRepository: UserRepositoryJpa,val newsRepositoryJPA: NewsRepositoryJPA) : AdminServices {
    val logger:Logger = LoggerFactory.getLogger(AdminServicesImpl::class.java)
    override fun searchByUserEmail(email: String?): User {

        return userRepository.findByEmail(email).orElseThrow { BadApiRequest("user not found...") }
    }

    override fun searchByUsername(username: String?): List<User>? {
        return userRepository.findByName(username).orElseThrow{BadApiRequest("user not found...")}
    }

    override fun getAllData(pageNumber: Int, pageSize: Int): PageResponse<UserDto> {
        val pageable: Pageable = PageRequest.of(pageNumber, pageSize)
        val page: Page<User?> = userRepository.findAll(pageable)
        return getPageResponse(page, UserDto::class.java)
    }

    override fun blockUser(email: String): Optional<User>? {
        val user: Optional<User>? = userRepository.findByEmail(email)

        if (user != null) {
            user.get().isBlocked=true
            userRepository.save(user.get())
        }
        return user
    }
    @Transactional
    override fun uploadNews(
                           newsTitle:String?,
                             shortDescription:String,
                           link:String,
                            viewCount:Int,
                            likeCount:Int?,
                            discussion:String?,
                             isTrending:Boolean,thumbnailImage: MultipartFile, detailImage: MultipartFile,
    ): String {
        logger.info("admin service run...")
            val image = News(newsId = Random.nextInt(100_00, 999_99).toString(),
                newsTitle = newsTitle, shortDescription = shortDescription, thumbnailImage = thumbnailImage.bytes,
                detailImage = detailImage.bytes, link = link, likeCount = likeCount, isTrending = isTrending, viewCount = viewCount,
                discussion = discussion
            )
        logger.info("admin servicerss add image to variable ....")
            newsRepositoryJPA.save(image)
        logger.info("save successfully...")
            return "successfully upload the news data..."
    }
}