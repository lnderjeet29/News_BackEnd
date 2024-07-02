package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.persistence.entities.User
import com.codefylabs.Maple.Leaf.rest.dto.PaginatedResponse
import com.codefylabs.Maple.Leaf.rest.dto.UserDto
import org.springframework.web.multipart.MultipartFile

interface AdminServices {
    fun searchByUserEmail(email: String?): User
    fun searchByName(email: String?): List<User>?
    fun getAllData(pageNumber: Int, pageSize: Int): PaginatedResponse<UserDto>
    fun blockUser(email: String, isBlocked:Boolean): User

}