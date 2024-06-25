package com.codefylabs.Maple.Leaf.persistance

import jakarta.persistence.*
import lombok.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "user_details")
data class User(
    @Id
    @Column(name = "user_Id")
    var id: Int,

    @Column(name = "user_name")
    var userName: String? = null,

    @Column(name = "user_password")
    private var password: String? = null,

    @Column(name = "user_email", unique = true)
    var email: String? = null,

    @Column(name = "enabled")
    var enabled: Boolean = false,

    @Column(name = "verification_token")
    var verificationToken: String? = null,

    @Column(name = "forceRest")
    var forRest: Boolean= false,

    @Column(name = "isBlocked")
    var isBlocked: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    var role: Role? = null,
) : UserDetails {
    constructor() : this(id = 0)
    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return null
    }

    override fun getPassword(): String? {
        return password
    }


    override fun getUsername(): String {
        return email!!
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
