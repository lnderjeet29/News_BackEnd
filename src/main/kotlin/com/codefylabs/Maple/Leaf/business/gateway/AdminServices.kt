package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.persistance.User
import com.codefylabs.Maple.Leaf.rest.dto.EnterNewDto
import com.codefylabs.Maple.Leaf.rest.dto.PageResponse
import com.codefylabs.Maple.Leaf.rest.dto.UserDto
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface AdminServices {
    fun searchByUserEmail(email: String?): User
    fun searchByUsername(email: String?): List<User>?
    fun getAllData(pageNumber: Int, pageSize: Int): PageResponse<UserDto>
    fun blockUser(email: String):Optional<User>?

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