package com.codefylabs.Maple.Leaf.Persistance.Implementation

import com.codefylabs.Maple.Leaf.business.gateway.AdminServices
import com.codefylabs.Maple.Leaf.persistance.User
import com.codefylabs.Maple.Leaf.persistance.UserRepositoryJpa
import com.codefylabs.Maple.Leaf.rest.ExceptionHandler.BadApiRequest

import com.codefylabs.Maple.Leaf.rest.dto.PageResponse
import com.codefylabs.Maple.Leaf.rest.dto.UserDto
import com.codefylabs.Maple.Leaf.rest.helper.PageHelper.getPageResponse
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class AdminServicesImpl(val userRepository: UserRepositoryJpa) : AdminServices {
    val logger = LoggerFactory.getLogger(AdminServicesImpl::class.java)
    override fun searchByUserEmail(email: String?): User {

        return userRepository.findByEmail(email).orElseThrow { BadApiRequest("user not found...") }
    }

    override fun searchByUsername(username: String?): List<User>? {
        return userRepository.findByUserName(username).orElseThrow{BadApiRequest("user not found...")}
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
}