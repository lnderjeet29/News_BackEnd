package com.codefylabs.Maple.Leaf.rest.dto.news

import java.time.LocalDateTime

data class NewsDto (

    var id: Int?=0,
    var title:String?=null,
    var shortDescription:String?=null,
    var description:String?=null,
    var thumbnailUrl: String?=null,
    var detailImageUrl: String?=null,
    var source: String?=null,
    var articleUrl:String?=null,
    var publishedAt: LocalDateTime = LocalDateTime.now(),
    var view:Int=0,
    var totalLikes:Int=0,
    var share:Int=0,
    var comments:Int=0,
    var isLiked:Boolean=false,
    var isTrending:Boolean=false
)