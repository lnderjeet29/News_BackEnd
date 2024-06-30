package com.codefylabs.Maple.Leaf.persistence.entities.news

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.time.LocalDateTime


@Entity
@Table(name = "news_data")
data class News(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_Id")
    var id: Int=0,

    @Column(name = "title")
    var title:String?=null,

    @Column(name = "short_description")
    var shortDescription:String?=null,

    @Column(name = "description", length = 3000)
    @Size(max = 3000)
    var description:String?=null,

    @Column(name = "thumbnail_url")
    var thumbnailUrl: String?=null,

    @Column(name = "detail_image_url")
    var detailImageUrl: String?=null,

    @Column(name = "source")
    var source: String?=null,

    @Column(name="article_url")
    var articleUrl:String?=null,

    @Column(name="publish_date", nullable = false)
    var publishedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "category", nullable = false)
    var category: String,

    @Column(name="view_count")
    var totalView:Int=0,


    @Column(name="share_count")
    var share:Int=0,

    @Column(name="comment_count")
    var totalComments:Int=0,


    @Column(name="is_trending")
    var isTrending:Boolean=false,
    @OneToMany(mappedBy = "news", cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: List<NewsComment> = emptyList()

){
    constructor() : this(
        id=0,
        publishedAt = LocalDateTime.now(),
        totalComments = 0,
        share = 0,
        isTrending = false,
        totalView = 0,
        category = "null"
    )
}
