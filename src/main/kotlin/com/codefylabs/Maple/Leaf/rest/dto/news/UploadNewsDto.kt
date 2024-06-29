package com.codefylabs.Maple.Leaf.rest.dto.news

import org.springframework.web.multipart.MultipartFile

data class UploadNewsDto(
    var title: String? = null,
    var shortDescription: String? = null,
    var description: String? = null,
    var source: String? = null,
    var articleUrl: String? = null,
    var isTrending: Boolean = false,
    var thumbnailImage: String? = null,
    var detailImage: String? = null,
    var category: String?=null
)