package com.codefylabs.Maple.Leaf.rest.controller

import com.codefylabs.Maple.Leaf.business.gateway.AdminServices
import com.codefylabs.Maple.Leaf.business.gateway.ImageUploadService
import com.codefylabs.Maple.Leaf.business.gateway.NewsServices
import com.codefylabs.Maple.Leaf.persistence.entities.User
import com.codefylabs.Maple.Leaf.rest.dto.*
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsDto
import com.codefylabs.Maple.Leaf.rest.dto.news.UploadNewsDto
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/api/v1/admin")
class AdminController(val adminServices: AdminServices,val imageUploadService: ImageUploadService,val newsServices: NewsServices) {
    var logger = LoggerFactory.getLogger(AdminController::class.java)

    @GetMapping("/users")
    fun getAllData(
        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) pageNumber: Int,
        @RequestParam(value = "pageSize", defaultValue = "4", required = false) pageSize: Int
    ): ResponseEntity<CommonResponse<PaginatedResponse<UserDto>>> {

        val paginatedResponse: PaginatedResponse<UserDto> =
            adminServices.getAllData(pageNumber, pageSize)
        val commonResponse =
            CommonResponse(message = "All user retrieved successfully", status = true, data = paginatedResponse)
        return ResponseEntity(commonResponse, HttpStatus.OK)
    }

    @GetMapping("/users/search")
    fun searchByEmail(@RequestParam(value = "email" , required = false) email: String?,
                      @RequestParam(value = "name" , required = false) name: String?
                      ): ResponseEntity<CommonResponse<List<User>>> {
        if(email==null && name==null){
            val response =
                CommonResponse<List<User>>(message = "Please enter user email/name..", status = false, data = null)
            return ResponseEntity(response, HttpStatus.NOT_FOUND)
        }

        if (email==null){
            return try {
                val user: List<User>? = adminServices.searchByName(name)
                val response= CommonResponse<List<User>>(message = "Users with the same email", status = true, data = user)
                ResponseEntity(response,HttpStatus.OK)
            } catch (e: Exception) {
                val response =
                    CommonResponse<List<User>>(message = e.message ?: "user not found..", status = false, data = null)
                ResponseEntity(response, HttpStatus.NOT_FOUND)
            }
        }
        return try {

            val user: User = adminServices.searchByUserEmail(email)
            val list = listOf<User>(user)
            val response =
                CommonResponse<List<User>>(message = "user detail found..", status = true, data = list)
            ResponseEntity(response, HttpStatus.OK)
        } catch (e: Exception) {
            val response =
                CommonResponse<List<User>>(message = e.message ?: "user not found..", status = false, data = null)
            ResponseEntity(response, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/block/user")
    fun blockUser(@RequestParam(value = "email") userEmail: String,
                  @RequestParam isBlocked: Boolean): ResponseEntity <CommonResponse<User>> {
        try {
            val user = adminServices.blockUser(userEmail,isBlocked)
            val response = CommonResponse(message = "user blocked successfully.!", status = true, data = user)
            return ResponseEntity(response, HttpStatus.OK)
        } catch (e: Exception) {
            val response = CommonResponse<User>(message = e.message ?: "something went wrong.!", status = false, data = null)
            return ResponseEntity(response, HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/news/create", consumes = ["multipart/form-data","application/octet-stream"])
    fun createNews(
        @RequestPart("title") title: String?,
        @RequestPart("shortDescription") shortDescription: String?,
        @RequestPart("description") description: String?,
        @RequestPart("source") source: String?,
        @RequestPart("articleUrl") articleUrl: String?,
        @RequestPart("category") category: String?,
        @RequestPart("isTrending") isTrending: Boolean,
        @RequestPart("thumbnailImage") thumbnailImage: MultipartFile?,
        @RequestPart("detailImage") detailImage: MultipartFile?
    ): ResponseEntity<CommonResponse<NewsDto>> {
        // Input validation
        try {
            requireNotNull(title) { "Title must not be empty" }
            requireNotNull(shortDescription) { "Short description must not be empty" }
            requireNotNull(description) { "Description must not be empty" }
            requireNotNull(source) { "Source must not be empty" }
            requireNotNull(category) { "Category must not be empty" }
            requireNotNull(articleUrl) { "Article URL must not be empty" }
            // Validate articleUrl format if provided
            if (articleUrl.isNotEmpty() && !isValidUrl(articleUrl)) {
                throw IllegalArgumentException("Invalid article URL format")
            }
            // Handle file uploads
            if (thumbnailImage == null || detailImage == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse(status = false, message = "Thumbnail and Detail Images are required"))
            }
            // Example content type validation
            if (thumbnailImage.contentType?.startsWith("image/")==false || detailImage.contentType?.startsWith("image/")==false) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse(status = false, message =  "Images must be of type image/jpeg or image/png"))
            }
            // Example size validation (adjust size as per your requirements)
            val maxFileSizeBytes = 10 * 1024 * 1024 // 10 MB
            if (thumbnailImage.size > maxFileSizeBytes || detailImage.size > maxFileSizeBytes) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse(status = false, message = "Images should not exceed 10MB"))
            }
            val thumbnailImageUrl = imageUploadService.uploadFile(thumbnailImage)
            val detailImageUrl = imageUploadService.uploadFile(detailImage)

            // Create DTO for service layer
            val uploadNewsDto = UploadNewsDto(
                title = title,
                shortDescription = shortDescription,
                description = description,
                source = source,
                articleUrl = articleUrl,
                isTrending = isTrending,
                thumbnailImage = thumbnailImageUrl,
                detailImage = detailImageUrl,
                category = category
            )
            // Call service to create news
            val createdNews = newsServices.createNews(uploadNewsDto)
            // Return success response
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse(status = false, message =  "News created successfully", data = createdNews))
        } catch (e: IllegalArgumentException) {
            // Handle validation errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse(status = false, message =  e.message ?: "Bad request" ))
        } catch (e: Exception) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse(status = false, message = "Failed to create news"))
        }
    }
    private fun isValidUrl(url: String): Boolean {
        return try {
            java.net.URL(url)
            true
        } catch (e: Exception) {
            false
        }
    }
}