package com.codefylabs.Maple.Leaf.persistence.entities.news

import com.codefylabs.Maple.Leaf.persistence.entities.User
import jakarta.persistence.*
import java.time.LocalDateTime
@Entity
@Table(name = "NewsCommentLike")
data class NewsCommentLike(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne val user: User,
    @ManyToOne val comment: NewsComment,

) {
    constructor() : this(
        id = 0,
        user = User(),
        comment = NewsComment()
    )
}
@Entity
@Table(name = "NewsCommentReplyLike")
data class NewsCommentReplyLike(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne val user: User,
    @ManyToOne val reply: NewsCommentReply,
) {
    constructor() : this(
        id = 0,
        user = User(),
        reply = NewsCommentReply()
    )
}