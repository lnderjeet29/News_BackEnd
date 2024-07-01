package com.codefylabs.Maple.Leaf.rest.controller

import com.codefylabs.Maple.Leaf.business.gateway.JWTServices
import com.codefylabs.Maple.Leaf.business.gateway.UserServices
import com.codefylabs.Maple.Leaf.persistence.entities.User
import com.codefylabs.Maple.Leaf.rest.dto.CommonResponse
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
class UserController(val jwtServices: JWTServices, val userServices: UserServices) {

    var logger = LoggerFactory.getLogger(UserController::class.java)

    @GetMapping("/info")
    fun user(@RequestHeader(name = "Authorization") token: String): ResponseEntity<CommonResponse<User>> {
        var username: String? = null
        try {
            username = jwtServices.extractEmail(token.substring(7))
            val response: CommonResponse<User> =
                CommonResponse(data = userServices.findUser(username), message = "user details", status = true)
            return ResponseEntity(response, HttpStatus.OK)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val response =
            CommonResponse<User>(message = "user detail not found..", status = false, data = null)
        return ResponseEntity<CommonResponse<User>>(response, HttpStatus.NOT_FOUND)
    }

    @PostMapping("/upload/profile", consumes = ["multipart/form-data", "application/octet-stream"])
    fun uploadProfile(
        @RequestHeader(name = "Authorization") token: String,
        @RequestPart(name = "profileImage") profileImage: MultipartFile
    ): ResponseEntity<CommonResponse<Nothing>> {
        try {
            logger.info(profileImage.contentType.toString() +"this type of image was come.")
            if (profileImage.contentType?.startsWith("image/") == false) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse(status = false, message = "Images must be of type image/jpeg or image/png"))
            }
            val email = jwtServices.extractEmail((token.substring(7)))
            val response = userServices.uploadProfileImage(email, profileImage)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse(status = true, message = response))
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse(status = false, message = e.message ?: "Internal Error!"))
        }

    }
}

