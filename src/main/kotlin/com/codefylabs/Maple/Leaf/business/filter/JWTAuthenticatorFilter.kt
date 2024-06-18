package com.codefylabs.Maple.Leaf.business.filter

import com.codefylabs.Maple.Leaf.business.gateway.JWTServices
import com.codefylabs.Maple.Leaf.business.gateway.UserServices
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.apache.commons.lang3.StringUtils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException


@Component
@RequiredArgsConstructor
class JWTAuthenticatorFilter(val jwtServices: JWTServices, val userServices: UserServices) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        val jwt: String?
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }
        jwt = authHeader.substring(7)
        val userEmail: String? = jwtServices?.extractUserName(jwt)
        if (StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().authentication == null) {
            val userDetails: UserDetails? = userServices?.userDetailsService()?.loadUserByUsername(userEmail)
            if (jwtServices?.isTokenValid(jwt, userDetails) == true) {
                val securityContext = SecurityContextHolder.createEmptyContext()
                val token = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails?.authorities
                )
                token.details = WebAuthenticationDetailsSource().buildDetails(request)
                securityContext.authentication = token
                SecurityContextHolder.setContext(securityContext)
            }
        }
        filterChain.doFilter(request, response)
    }
}

