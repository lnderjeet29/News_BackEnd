package com.codefylabs.Maple.Leaf.persistance

import com.codefylabs.Maple.Leaf.persistence.AuthProvider
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate


@Entity
@Table(name = "user_details")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    var id: Int?=0,

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

    @Column(name = "forceReset")
    var forceReset: Boolean? = false,

    @Column(name = "isBlocked")
    var isBlocked: Boolean = false,

    @Column(name="date")
    var date: LocalDate?,

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    var role: Role? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    var authProvider:AuthProvider?=null,

    @Column(name="profile_picture")
    var profilePicture:String?=null

) : UserDetails {
    constructor() : this(
        id=0, date = LocalDate.now()
    )

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
