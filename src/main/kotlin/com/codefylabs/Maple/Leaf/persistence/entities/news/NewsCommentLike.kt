package com.codefylabs.Maple.Leaf.persistence.entities.news

import com.codefylabs.Maple.Leaf.persistence.entities.User
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "NewsCommentLike")
@IdClass(NewsCommentLikeId::class)
data class NewsCommentLike(

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    @Id
    @JoinColumn(name = "comment_id")
    @ManyToOne val comment: NewsComment,

) {
    constructor() : this(

        user = User(),
        comment = NewsComment()
    )
}
@Entity
@Table(name = "NewsCommentReplyLike")
@IdClass(NewsCommentReplyLikeId::class)
data class NewsCommentReplyLike(

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    @Id
    @ManyToOne
    @JoinColumn(name = "reply_id")
    val reply: NewsCommentReply,
) {
    constructor() : this(

        user = User(),
        reply = NewsCommentReply()
    )
}

data class NewsCommentLikeId(
    val user: Int = 0,
    val comment: Int = 0
) : Serializable

data class NewsCommentReplyLikeId(
    val user: Int = 0,
    val reply: Int = 0
) : Serializable