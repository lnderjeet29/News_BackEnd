package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.persistance.User
import com.codefylabs.Maple.Leaf.rest.dto.PageResponse
import com.codefylabs.Maple.Leaf.rest.dto.UserDto
import java.util.Optional

interface AdminServices {
    fun searchByUserEmail(email: String?): User
    fun searchByUsername(email: String?): List<User>?
    fun getAllData(pageNumber: Int, pageSize: Int): PageResponse<UserDto>
    fun blockUser(email: String):Optional<User>?
}