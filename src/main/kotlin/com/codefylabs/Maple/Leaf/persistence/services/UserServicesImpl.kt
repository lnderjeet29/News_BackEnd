package com.codefylabs.Maple.Leaf.persistance.Implementation


import com.codefylabs.Maple.Leaf.rest.ExceptionHandler.BadApiRequest
import com.codefylabs.Maple.Leaf.business.gateway.UserServices
import com.codefylabs.Maple.Leaf.persistence.entities.User
import com.codefylabs.Maple.Leaf.persistence.repository.UserRepositoryJpa
import lombok.RequiredArgsConstructor
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class UserServicesImpl(val userRepository: UserRepositoryJpa) : UserServices {
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

}
