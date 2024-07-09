package com.codefylabs.Maple.Leaf.persistence.entities

import com.codefylabs.Maple.Leaf.rest.dto.others.subVisaDataDto
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.Size

@Embeddable
data class SubVisa(
    val question: String,
    @Column(length = 1000)
    @field:Size(max=1000)
    val answer: String
){
    constructor():this(
        question="",
        answer=""
    )

    fun toDto(): subVisaDataDto {
        return subVisaDataDto(
            question = this.question,
            answer = this.answer
        )
    }

}