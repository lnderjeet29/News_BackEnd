package com.codefylabs.Maple.Leaf.persistence

import com.codefylabs.Maple.Leaf.persistance.User
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "likes")
@IdClass(LikeId::class)
data class NewsLikes(

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User?,
    @Id
    @ManyToOne
    @JoinColumn(name = "news_id")
    val newsPost: News?
){
    constructor(): this(
        user=null,
        newsPost=null
    )
}

data class LikeId(
    val user: Int = 0,
    val newsPost: Int = 0
) : Serializable
