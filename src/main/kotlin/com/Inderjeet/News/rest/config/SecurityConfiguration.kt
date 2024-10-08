package com.Inderjeet.News.rest.config

import com.Inderjeet.News.business.filter.JWTAuthenticatorFilter
import com.Inderjeet.News.business.gateway.UserServices
import com.Inderjeet.News.persistence.entities.Role
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
class SecurityConfiguration(
    private val jwtAuthenticatorFilter: JWTAuthenticatorFilter,
    private val userServices: UserServices?
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf { it.disable() }
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers(
                        "/api/v1/auth/**", "/", "/api/v1/news/**","/api/v1/visa","/api/v1/faq/**",
                        "/api/v1/forgot-password/**", "/test","/api/v1/news"
                    ).permitAll()
                    .requestMatchers("/api/v1/user").hasAuthority(Role.USER.name)
                    .requestMatchers("/api/v1/admin").hasAuthority(Role.ADMIN.name)
                    .anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticatorFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(userDetailsService())
        provider.setPasswordEncoder(passwordEncoder())
        return provider
    }


    private fun userDetailsService(): UserDetailsService {
        return userServices?.userDetailsService() ?: defaultUserDetailsService()
    }

    private fun defaultUserDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            throw UsernameNotFoundException("UserDetailsService not implemented for username: $username")
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    @Bean
    fun corsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration().apply {
            allowCredentials = true
            allowedOrigins = listOf("http://mapleleaf.codefy-testing.com", "https://mapleleaf.codefy-testing.com")
            allowedHeaders = listOf("*")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        }
        source.registerCorsConfiguration("/**", config)
        return source
    }
}
