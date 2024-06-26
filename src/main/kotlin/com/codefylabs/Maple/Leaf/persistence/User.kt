package com.codefylabs.Maple.Leaf.persistance

import com.codefylabs.Maple.Leaf.persistence.AuthProvider
import jakarta.persistence.*
import lombok.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails



@Entity
@Table(name = "user_details")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_Id")
    var id: Int?=null,

    @Column(name = "user_name")
    var name: String? = null,

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

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    var authProvider:AuthProvider?=null,

    @Column(name="profile_picture")
    var profilePicture:String?=null

) : UserDetails {

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
