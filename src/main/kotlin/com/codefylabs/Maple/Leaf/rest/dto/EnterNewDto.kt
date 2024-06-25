package com.codefylabs.Maple.Leaf.rest.dto


data class EnterNewDto (
    var newsId:String,
    var newsTitle:String?,
    var shortDescription:String,
    var link:String,
    var viewCount:Int,
    var likeCount:Int?,
    var discussion:String?,
    var isTrending:Boolean
)