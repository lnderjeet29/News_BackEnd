package com.codefylabs.Maple.Leaf.rest.dto

import com.codefylabs.Maple.Leaf.persistence.entities.FAQ
import jakarta.persistence.Lob

data class FAQDto(
    var category: String,
    var subCategory: String,
    var title: String,
    @Lob
    var content: String,
    val faqs: List<SubFAQDto>
)
data class SubFAQDto(
    var question: String,
    @Lob
    var answer: String
)

fun FAQ.toDto(): FAQDto {
    return FAQDto(
        category = this.category,
        subCategory = this.subCategory,
        title = this.title,
        content = this.content,
        faqs = this.subFaqs.map { SubFAQDto(question = it.question, answer =  it.answer) }
    )
}

