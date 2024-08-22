package com.Inderjeet.News.rest.dto.news

import jakarta.persistence.Lob

data class UploadNewsDto(
    var title: String? = null,
    var shortDescription: String? = null,
    @Lob
    var description: String? = null,
    var source: String? = null,
    var articleUrl: String? = null,
    var isTrending: Boolean = true,
    var category: String?=null
)