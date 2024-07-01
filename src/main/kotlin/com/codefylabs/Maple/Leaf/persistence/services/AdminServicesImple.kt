package com.codefylabs.Maple.Leaf.persistence.services

import com.codefylabs.Maple.Leaf.business.gateway.AdminServices
import com.codefylabs.Maple.Leaf.business.gateway.ImageUploadService
import com.codefylabs.Maple.Leaf.persistence.entities.User
import com.codefylabs.Maple.Leaf.persistence.repository.UserRepositoryJpa
import com.codefylabs.Maple.Leaf.rest.ExceptionHandler.BadApiRequest
import com.codefylabs.Maple.Leaf.rest.dto.PaginatedResponse
import com.codefylabs.Maple.Leaf.rest.dto.UserDto
import com.codefylabs.Maple.Leaf.rest.helper.PageHelper.getPageResponse
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

    override fun getAllData(pageNumber: Int, pageSize: Int): PaginatedResponse<UserDto> {
        val pageable: Pageable = PageRequest.of(pageNumber, pageSize)
        val page: Page<User?> = userRepository.findAll(pageable)
        return getPageResponse(page, UserDto::class.java)
    }

    override fun blockUser(email: String, isBlocked:Boolean): User {
        val user: User = userRepository.findByEmail(email).orElseThrow{BadApiRequest("user not founded.!")}

        user.isBlocked=isBlocked
        userRepository.save(user)
        return user
    }

}