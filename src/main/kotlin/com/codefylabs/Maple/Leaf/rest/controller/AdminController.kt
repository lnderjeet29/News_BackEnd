package com.codefylabs.Maple.Leaf.rest.controller

import com.codefylabs.Maple.Leaf.business.gateway.AdminServices
import com.codefylabs.Maple.Leaf.persistance.User
import com.codefylabs.Maple.Leaf.rest.dto.*
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.Optional


@RestController
@RequestMapping("/api/v1/admin")
class AdminController (val adminServices:AdminServices){
    var logger = LoggerFactory.getLogger(AdminController::class.java)
    @GetMapping("/getAllUser")
    fun getAllData(
        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) pageNumber: Int,
        @RequestParam(value = "pageSize", defaultValue = "3", required = false) pageSize: Int
    ): ResponseEntity<CommonResponse<PaginatedResponse<UserDto>>> {

            val paginatedResponse: PaginatedResponse<UserDto> =
                adminServices.getAllData(pageNumber, pageSize)
            val commonResponse=CommonResponse(message = "All user retrieved successfully" , status = true,data = paginatedResponse)
            return ResponseEntity(commonResponse, HttpStatus.OK)
    }

    @GetMapping("/searchByEmail")
    fun searchByEmail(@RequestParam(value= "Email")email: String?): ResponseEntity<CommonResponse<User>>{
        try {

            val user :User=adminServices.searchByUserEmail(email)

            val response =
                CommonResponse(message = "user detail found..", status = true, data =user )
           return  ResponseEntity(response,HttpStatus.OK)
        } catch (e: Exception) {
            val response =
                CommonResponse<User>(message = "user not found..", status = false, data = null)
            return ResponseEntity(response,HttpStatus.NOT_FOUND)
        }
    }
    @GetMapping("/searchByUsername")
    fun searchByUsername(@RequestParam(value= "username")username: String?): ResponseEntity<List<User>>{
        try {

            val user :List<User>? =adminServices.searchByUsername(username)
           return  ResponseEntity(user,HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity(null,HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/blockUser")
    fun blockUser(@RequestParam(value = "email") user_email:String): ResponseEntity<Optional<User>> {
        val user: Optional<User>? = adminServices.blockUser(user_email)
        return ResponseEntity(user,HttpStatus.OK)
    }

    @PostMapping("/uploadNews")
    fun uploadNewsData(
//            @RequestParam newsTitle:String?,
//                       @RequestParam shortDescription:String,(add description)
//                       @RequestParam link:String,
//                       @RequestParam viewCount:Int,
//                       @RequestParam likeCount:Int?,
//                       @RequestParam discussion:String?,
//                       @RequestParam isTrending:Boolean,
                       @RequestParam("thumbnail_image") thumbnailImage: MultipartFile,@RequestParam("detail_image") detailImage: MultipartFile): ResponseEntity<String>{
        try {
//            val response:String = adminServices.uploadNews( newsTitle,
//             shortDescription,
//             link,
//             viewCount,
//             likeCount,
//             discussion,
//             isTrending,thumbnailImage,detailImage)
            val response:String =adminServices.uploadNews("demo","demo descr", "demo link", 1,0,"demo discussion",false,thumbnailImage,detailImage)
            return ResponseEntity(response,HttpStatus.OK)
        } catch (e: Exception) {
            print("exception start from here...........................................")
            print(e.message)
            return ResponseEntity("upload new data not valid....",HttpStatus.NOT_ACCEPTABLE)
        }

    }
}