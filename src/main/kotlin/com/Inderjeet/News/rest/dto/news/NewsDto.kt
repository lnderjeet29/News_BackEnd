package com.Inderjeet.News.rest.dto.news

import com.Inderjeet.News.persistence.entities.news.News
import jakarta.persistence.Lob
import java.time.LocalDateTime

data class NewsDto (

    var id: Int=0,
    var title:String?=null,
    var shortDescription:String?=null,
    @Lob
    var description:String?=null,
    var thumbnailUrl: String?=null,
    var detailImageUrl: String?=null,
    var source: String?=null,
    var articleUrl:String?=null,
    var publishedAt: LocalDateTime = LocalDateTime.now(),
    var view:Int=0,
    var totalLikes: Long=0,
    var share:Int=0,
    var comments:Long=0,
    var isLiked:Boolean=false,
    var isTrending:Boolean=false
)

fun News.toDto(commentsCount: Long, isLiked: Boolean, totalLikes: Long)=NewsDto(
    id=this.id,
    title= this.title,
    shortDescription=this.shortDescription,
    description= this.description,
    thumbnailUrl= this.thumbnailUrl,
    detailImageUrl= this.detailImageUrl,
    source= this.source,
    articleUrl=this.articleUrl,
    publishedAt=this.publishedAt,
    isTrending = this.isTrending,
    share = this.share,
    view = this.totalView,
    isLiked = isLiked,
    comments = commentsCount,
    totalLikes = totalLikes,
)