package com.codefylabs.Maple.Leaf.rest.Controller

import com.codefylabs.Maple.Leaf.business.gateway.AdminServices
import com.codefylabs.Maple.Leaf.persistance.User
import com.codefylabs.Maple.Leaf.rest.dto.ApiUserMessage
import com.codefylabs.Maple.Leaf.rest.dto.PageResponse
import com.codefylabs.Maple.Leaf.rest.dto.UserDto
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Optional


@RestController
@RequestMapping("/api/v1/admin")
class AdminController (val adminServices:AdminServices){
    var logger = LoggerFactory.getLogger(AdminController::class.java)
    @GetMapping("/getAllData")
    fun getAllData(
        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) pageNumber: Int,
        @RequestParam(value = "pageSize", defaultValue = "2", required = false) pageSize: Int
    ): ResponseEntity<PageResponse<UserDto>> {
        val pageResponse: PageResponse<UserDto> =
            adminServices.getAllData(pageNumber, pageSize)
        return ResponseEntity<PageResponse<UserDto>>(pageResponse, HttpStatus.OK)
    }

    @GetMapping("/searchByEmail")
    fun searchByEmail(@RequestParam(value= "Email")email: String?): ResponseEntity<ApiUserMessage>{
        try {

            val user :User=adminServices.searchByUserEmail(email)

            val response: ApiUserMessage =
                ApiUserMessage(message = "user detail found..", status = true, response =user )
           return  ResponseEntity(response,HttpStatus.OK)
        } catch (e: Exception) {
            val response: ApiUserMessage =
                ApiUserMessage(message = "user not found..", status = false, response = null)
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

    @PostMapping("/block_user")
    fun blockUser(@RequestParam(value = "email") user_email:String): ResponseEntity<Optional<User>> {
        val user: Optional<User>? = adminServices.blockUser(user_email)
        return ResponseEntity(user,HttpStatus.OK)
    }
}