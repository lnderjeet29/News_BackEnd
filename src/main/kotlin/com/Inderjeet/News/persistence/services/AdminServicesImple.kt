package com.Inderjeet.News.persistence.services

import com.Inderjeet.News.business.gateway.AdminServices
import com.Inderjeet.News.persistence.entities.User
import com.Inderjeet.News.persistence.repository.UserRepositoryJpa
import com.Inderjeet.News.rest.ExceptionHandler.BadApiRequest
import com.Inderjeet.News.rest.dto.UserDto
import com.Inderjeet.News.rest.helper.PageHelper.getPageResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class AdminServicesImpl(val userRepository: UserRepositoryJpa) : AdminServices {
    val logger:Logger = LoggerFactory.getLogger(AdminServicesImpl::class.java)
    override fun searchByUserEmail(email: String?): User {

        return userRepository.findByEmail(email).orElseThrow { BadApiRequest("user not found...") }
    }

    override fun searchByName(name: String?): List<User>? {
        return userRepository.findByName(name).orElseThrow{BadApiRequest("user not found...")}
    }

    override fun getAllData(pageNumber: Int, pageSize: Int): com.Inderjeet.News.rest.dto.PaginatedResponse<UserDto> {
        logger.info("start from top")
        val pageable: Pageable = PageRequest.of(pageNumber, pageSize)
        val page: Page<User?> = userRepository.findAll(pageable)
        logger.info("start from end")
        return getPageResponse(page, UserDto::class.java)
    }

    override fun blockUser(email: String, isBlocked:Boolean): User {
        val user: User = userRepository.findByEmail(email).orElseThrow{BadApiRequest("user not founded.!")}

        user.isBlocked=isBlocked
        userRepository.save(user)
        return user
    }

}