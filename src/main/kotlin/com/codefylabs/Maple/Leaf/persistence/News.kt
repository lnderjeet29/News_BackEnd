package com.codefylabs.Maple.Leaf.persistence

import jakarta.persistence.*


@Entity
@Table(name = "news_data")
data class News(

        @Id
        @Column(name = "newsId")
        var newsId: String,

    @Column(name = "title")
    var newsTitle:String?=null,

    @Column(name = "short_desc")
    var shortDescription:String?=null,

    @Lob
    @Column(name = "thumbnail_image")
    var thumbnailImage: ByteArray,

    @Lob
    @Column(name = "detail_image")
    var detailImage: ByteArray,

    @Column(name="link")
    var link:String?=null,

    @Column(name="view_count")
    var viewCount:Int?=null,

    @Column(name="like_count")
    var likeCount:Int?=null,

    @Column(name="discoussion")
    var discussion:String?=null,

    @Column(name="isTrending")
    var isTrending:Boolean=false

) {
    constructor() : this(
            newsId = "",
            newsTitle = null,
            shortDescription = null,
            thumbnailImage = ByteArray(0),
            detailImage = ByteArray(0),
            link = null,
            viewCount = null,
            likeCount = null,
            discussion = null,
            isTrending = false
    )
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as News

        if (newsId != other.newsId) return false
        if (newsTitle != other.newsTitle) return false
        if (shortDescription != other.shortDescription) return false
        if (!thumbnailImage.contentEquals(other.thumbnailImage)) return false
        if (!detailImage.contentEquals(other.detailImage)) return false
        if (link != other.link) return false
        if (viewCount != other.viewCount) return false
        if (likeCount != other.likeCount) return false
        if (discussion != other.discussion) return false
        return isTrending == other.isTrending
    }

    override fun hashCode(): Int {
        var result = newsId.hashCode()
        result = 31 * result + (newsTitle?.hashCode() ?: 0)
        result = 31 * result + (shortDescription?.hashCode() ?: 0)
        result = 31 * result + thumbnailImage.contentHashCode()
        result = 31 * result + detailImage.contentHashCode()
        result = 31 * result + (link?.hashCode() ?: 0)
        result = 31 * result + (viewCount ?: 0)
        result = 31 * result + (likeCount ?: 0)
        result = 31 * result + (discussion?.hashCode() ?: 0)
        result = 31 * result + isTrending.hashCode()
        return result
    }

}
