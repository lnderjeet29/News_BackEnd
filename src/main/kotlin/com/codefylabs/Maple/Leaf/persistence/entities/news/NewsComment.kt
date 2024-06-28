package com.codefylabs.Maple.Leaf.persistence.entities.news

import com.codefylabs.Maple.Leaf.persistence.entities.User
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "NewsComment")
data class NewsComment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int=0,
    @ManyToOne val user: User,
    @ManyToOne val news: News,
    val content: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var likes: Int = 0
){
    constructor():this(
        id=0,
        user=User(),
        news= News(),
        content=""
    )
}

@Entity
@Table(name = "NewsCommentReply")
data class NewsCommentReply(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int=0,
    @ManyToOne val user: User,
    @ManyToOne val comment: NewsComment,

    val content: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var likes: Int = 0
){
    constructor():this(
        id=0,
        user=User(),
        comment=NewsComment(),
        content=""
    )
}
