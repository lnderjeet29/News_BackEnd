package com.Inderjeet.News.persistence.entities.news

import com.Inderjeet.News.persistence.entities.User
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "news_comment")
data class NewsComment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    var news: News,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    val content: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "comment", cascade = [CascadeType.ALL], orphanRemoval = true)
    val likes: MutableList<NewsCommentLike> = mutableListOf(),

    @OneToMany(mappedBy = "comment", cascade = [CascadeType.ALL], orphanRemoval = true)
    val replies: MutableList<NewsCommentReply> = mutableListOf()
) {
    constructor() : this(
        id = 0,
        news = News(),
        user = User(),
        content = "",
        likes = mutableListOf(),
        replies= mutableListOf()
    )
}

@Entity
@Table(name = "NewsCommentReply")
data class NewsCommentReply(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    val id: Int=0,

    @ManyToOne val user: User,
    @ManyToOne val comment: NewsComment,


    val content: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @OneToMany(mappedBy = "reply", cascade = [CascadeType.ALL], orphanRemoval = true)
    val replyLikes: MutableList<NewsCommentReplyLike> = mutableListOf()
){
    constructor():this(
        id=0,
        user=User(),
        comment=NewsComment(),
        content=""
    )
}
