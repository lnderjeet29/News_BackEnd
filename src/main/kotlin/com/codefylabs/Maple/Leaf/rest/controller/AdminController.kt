package com.codefylabs.Maple.Leaf.rest.controller

import com.codefylabs.Maple.Leaf.business.gateway.*
import com.codefylabs.Maple.Leaf.persistence.entities.Role
import com.codefylabs.Maple.Leaf.persistence.entities.User
import com.codefylabs.Maple.Leaf.persistence.entities.VisaData
import com.codefylabs.Maple.Leaf.persistence.repository.NewsRepositoryJPA
import com.codefylabs.Maple.Leaf.persistence.repository.UserRepositoryJpa
import com.codefylabs.Maple.Leaf.rest.ExceptionHandler.BadApiRequest
import com.codefylabs.Maple.Leaf.rest.dto.*
import com.codefylabs.Maple.Leaf.rest.dto.news.NewsDto
import com.codefylabs.Maple.Leaf.rest.dto.news.UploadNewsDto
import com.codefylabs.Maple.Leaf.rest.dto.others.VisaDataDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import java.net.URISyntaxException


@RestController
@RequestMapping("/api/v1/admin")
class AdminController(
    val adminServices: AdminServices,
    val newsServices: NewsServices,
    val newsRepositoryJPA: NewsRepositoryJPA,
    val jwtServices: JWTServices,
    val userRepositoryJpa: UserRepositoryJpa
) {
    var logger: Logger = LoggerFactory.getLogger(AdminController::class.java)

    @GetMapping("/users")
    fun getAllUserData(
        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) pageNumber: Int,
        @RequestParam(value = "pageSize", defaultValue = "4", required = false) pageSize: Int
    ): ResponseEntity<CommonResponse<PaginatedResponse<UserDto>>> {
        logger.info("get all data start ")
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
                if(user?.isEmpty() == true){
                    throw BadApiRequest("user not found!")
                }
                val response= CommonResponse(message = "Users with the same email", status = true, data = user)
                ResponseEntity(response,HttpStatus.OK)
            } catch (e: Exception) {
                val response =
                    CommonResponse<List<User>>(message = e.message ?: "user not found.", status = false, data = null)
                ResponseEntity(response, HttpStatus.NOT_FOUND)
            }
        }
        return try {

            val user: User = adminServices.searchByUserEmail(email)
            val list = listOf(user)
            val response =
                CommonResponse(message = "user detail found..", status = true, data = list)
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
        return try {
            val user = adminServices.blockUser(userEmail,isBlocked)
            val response = CommonResponse(message = "user blocked successfully.!", status = true, data = user)
            ResponseEntity(response, HttpStatus.OK)
        } catch (e: Exception) {
            val response = CommonResponse<User>(message = e.message ?: "something went wrong.!", status = false, data = null)
            ResponseEntity(response, HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/news/create")
    fun createNews(@RequestBody uploadNewsDto: UploadNewsDto): ResponseEntity<CommonResponse<Int>> {
        // Input validation
        try {
            requireNotNull(uploadNewsDto.title) { "Title must not be empty" }
            requireNotNull(uploadNewsDto.shortDescription) { "Short description must not be empty" }
            requireNotNull(uploadNewsDto.description) { "Description must not be empty" }
            requireNotNull(uploadNewsDto.source) { "Source must not be empty" }
            requireNotNull(uploadNewsDto.category) { "Category must not be empty" }
            requireNotNull(uploadNewsDto.articleUrl) { "Article URL must not be empty" }
            // Validate articleUrl format if provided
            if (uploadNewsDto.articleUrl!!.isNotEmpty() && !isValidUrl(uploadNewsDto.articleUrl!!)) {
                throw IllegalArgumentException("Invalid article URL format")
            }

            // Call service to create news
            val createdNews = newsServices.createNews(uploadNewsDto)
            // Return success response
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse(status = true, message =  "Upload successfully", data = createdNews))
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


    @PostMapping("/upload/thumbnailImg")
    fun uploadNewsThumbnailImg(
        @RequestPart("thumbnailImage") thumbnailImage: MultipartFile,
        @RequestParam(name = "news_id") newsId: Int
    ): ResponseEntity<CommonResponse<Boolean>> {
        // Example content type validation
        if (thumbnailImage.contentType?.startsWith("image/")==false) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse(status = false, message =  "Images must be of type image/jpeg or image/png", data = false))
        }
// Example size validation (adjust size as per your requirements)
        val maxFileSizeBytes = 10 * 1024 * 1024 // 10 MB
        if (thumbnailImage.size > maxFileSizeBytes) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse(status = false, message = "Images should not exceed 10MB", data = false))
        }
        val response = newsServices.uploadThumbnailImg(thumbnailImage = thumbnailImage, newsId = newsId)
        return ResponseEntity.ok().body(CommonResponse("Thumbnail Image Upload.",status = true, data = response))

    }

    @PostMapping("/upload/DetailImg", consumes = ["multipart/form-data", "application/octet-stream"])
    fun uploadNewsDetailImg(
        @RequestPart("DetailImage") detailImage: MultipartFile,
        @RequestParam(name = "news_id") newsId: Int
    ): ResponseEntity<CommonResponse<Boolean>> {

        // Example content type validation
        if (detailImage.contentType?.startsWith("image/")==false) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse(status = false, message =  "Images must be of type image/jpeg or image/png", data = false))
        }
// Example size validation (adjust size as per your requirements)
        val maxFileSizeBytes = 10 * 1024 * 1024 // 10 MB
        if (detailImage.size > maxFileSizeBytes) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse(status = false, message = "Images should not exceed 10MB", data = false))
        }

        val response = newsServices.uploadDetailImg(detailImage= detailImage, newsId = newsId)
        return ResponseEntity.ok().body(CommonResponse("Detail Image Upload.",status = true, data = response))
    }


    private fun isValidUrl(url: String): Boolean {
        return try {
            val uri = URI(url)
            uri.isAbsolute && uri.scheme != null && uri.host != null
        } catch (e: URISyntaxException) {
            false
        }
    }

    @DeleteMapping("/news")
    fun deleteNews(@RequestHeader(name = "Authorization") token:String,@RequestParam newsId: Int):ResponseEntity<CommonResponse<Nothing>>{

        return try {
            val userEmail = jwtServices.extractEmail(token.substring(7))
            val userRole = userRepositoryJpa.findByEmail(userEmail).get().role
            if(userRole == Role.ADMIN){
                newsRepositoryJPA.deleteById(newsId)
            ResponseEntity.ok()
                .body(CommonResponse(message = "Deleted Successfully!", status = true))
            }else {
                throw BadCredentialsException("User is not allowed to delete this resource.")
            }
        } catch (e: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse(message = "Session expired!", status = false))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse(message = e.message ?: "Something went wrong!", status = false ))
        }
    }



}