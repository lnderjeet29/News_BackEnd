package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.persistance.User
import com.codefylabs.Maple.Leaf.rest.dto.PaginatedResponse
import com.codefylabs.Maple.Leaf.rest.dto.UserDto
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface AdminServices {
    fun searchByUserEmail(email: String?): User
    fun searchByName(email: String?): List<User>?
    fun getAllData(pageNumber: Int, pageSize: Int): PaginatedResponse<UserDto>
    fun blockUser(email: String): User

   fun uploadNews(
                            newsTitle:String?,
                            shortDescription:String,
                            link:String,
                            viewCount:Int,
                            likeCount:Int?,
                            discussion:String?,
                            isTrending:Boolean,thumbnailImage: MultipartFile, detailImage: MultipartFile,
    ): String
}