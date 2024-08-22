package com.Inderjeet.News.rest.dto

import com.Inderjeet.News.persistence.entities.FAQ
import jakarta.persistence.Lob

data class FAQDto(
    var category: String,
    var subCategory: String,
    var title: String,
    @Lob
    var content: String,
    val faqs: List<com.Inderjeet.News.rest.dto.SubFAQDto>
)
data class SubFAQDto(
    var question: String,
    @Lob
    var answer: String
)

fun FAQ.toDto(): com.Inderjeet.News.rest.dto.FAQDto {
    return com.Inderjeet.News.rest.dto.FAQDto(
        category = this.category,
        subCategory = this.subCategory,
        title = this.title,
        content = this.content,
        faqs = this.subFaqs.map { com.Inderjeet.News.rest.dto.SubFAQDto(question = it.question, answer = it.answer) }
    )
}

