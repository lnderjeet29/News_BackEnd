package com.codefylabs.Maple.Leaf.persistance.services


import com.codefylabs.Maple.Leaf.business.gateway.ImageUploadService
import com.codefylabs.Maple.Leaf.rest.ExceptionHandler.BadApiRequest
import com.codefylabs.Maple.Leaf.business.gateway.UserServices
import com.codefylabs.Maple.Leaf.persistence.entities.User
import com.codefylabs.Maple.Leaf.persistence.entities.news.PictureType
import com.codefylabs.Maple.Leaf.persistence.repository.UserRepositoryJpa
import com.codefylabs.Maple.Leaf.rest.dto.CommonResponse
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
@RequiredArgsConstructor
class UserServicesImpl(val userRepository: UserRepositoryJpa,
                       val imageUploadService: ImageUploadService) : UserServices {
    override fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            userRepository.findByEmail(username)
                ?.orElseThrow { UsernameNotFoundException("user not found...") }
        }
    }

    override fun findUser(email: String?): User {
        return userRepository.findByEmail(email)
            .orElseThrow { BadApiRequest("user not found...") }
    }

    override fun uploadProfileImage(email: String?, profileImage: MultipartFile): String {
        val user= userRepository.findByEmail(email).orElseThrow{
            BadCredentialsException("Invalid Token!")
        }
        // Example size validation (adjust size as per your requirements)
            val maxFileSizeBytes = 10 * 1024 * 1024 // 10 MB
            if (profileImage.size > maxFileSizeBytes) {
                throw  BadApiRequest("Images should not exceed 10MB")
            }

            user.profilePicture= imageUploadService.uploadImage(profileImage,PictureType.PROFILE_PICTURE,user.id.toString())
            userRepository.save(user)
            return "Successfully Uploaded."
    }

    override fun updateName(email: String, name: String): Boolean {
        var user= userRepository.findByEmail(email).orElseThrow{BadApiRequest("User not found.")}
        user.name=name
        userRepository.save(user)
        return true
    }

}
