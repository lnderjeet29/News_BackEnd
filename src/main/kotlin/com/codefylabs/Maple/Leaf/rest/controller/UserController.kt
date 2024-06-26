package com.codefylabs.Maple.Leaf.rest.controller

import com.codefylabs.Maple.Leaf.business.gateway.JWTServices
import com.codefylabs.Maple.Leaf.business.gateway.UserServices
import com.codefylabs.Maple.Leaf.persistance.User
import com.codefylabs.Maple.Leaf.rest.dto.CommonResponse
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
class UserController(val Jwt: JWTServices, val userServices: UserServices) {

    var logger = LoggerFactory.getLogger(UserController::class.java)

    @GetMapping("/info")
    fun user(@RequestHeader(name = "Authorization") token: String): ResponseEntity<CommonResponse<User>> {
        logger.info(token)
        var username: String? = null
        try {
            username = Jwt?.extractUserName(token.substring(7))
            val response: CommonResponse<User> =
                CommonResponse(data = userServices?.findUser(username), message = "user details", status = true)
            return ResponseEntity(response, HttpStatus.OK)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val response =
            CommonResponse<User>(message = "user detail not found..", status = false, data = null)
        return ResponseEntity<CommonResponse<User>>(response, HttpStatus.NOT_FOUND)
    }
}

