package com.codefylabs.Maple.Leaf.rest.dto.others

import com.codefylabs.Maple.Leaf.persistence.entities.VisaData
import jakarta.persistence.Lob

data class VisaDataDto(
    var title: String,
    @Lob
    var content: String,
    val subVisaData: List<subVisaDataDto>
)
data class subVisaDataDto(
    var question: String,
    var answer: String
)

fun VisaData.toDto(): VisaDataDto {
    return VisaDataDto(
        title = this.title,
        content = this.content,
        subVisaData =this.subVisa.map {
            subVisaDataDto(question = it.question, answer = it.answer)
        }
    )
}