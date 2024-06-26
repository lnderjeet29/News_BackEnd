package com.codefylabs.Maple.Leaf.rest.dto.news

data class NewsDto (
        var newId:String,
    var newsTitle:String?=null,
    var shortDescription:String?=null,
    var thumbnailImage:ByteArray,
    var detailImage:ByteArray,
    var link:String?=null,
    var viewCount:Int?=null,
    var likeCount:Int?=null,
    var discussion:String?=null,
    var isTrending:Boolean=false

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewsDto

        if (!thumbnailImage.contentEquals(other.thumbnailImage)) return false
        return detailImage.contentEquals(other.detailImage)
    }

    override fun hashCode(): Int {
        var result = thumbnailImage.contentHashCode()
        result = 31 * result + detailImage.contentHashCode()
        return result
    }
}