package com.Inderjeet.News.business.gateway

import com.Inderjeet.News.persistence.entities.User
import com.Inderjeet.News.rest.dto.PaginatedResponse
import com.Inderjeet.News.rest.dto.UserDto

interface AdminServices {
    fun searchByUserEmail(email: String?): User
    fun searchByName(email: String?): List<User>?
    fun getAllData(pageNumber: Int, pageSize: Int): PaginatedResponse<UserDto>
    fun blockUser(email: String, isBlocked:Boolean): User
}