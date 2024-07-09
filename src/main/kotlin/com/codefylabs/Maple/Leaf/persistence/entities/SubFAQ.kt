package com.codefylabs.Maple.Leaf.persistence.entities

import jakarta.persistence.Embeddable
import jakarta.persistence.Lob

@Embeddable
data class SubFAQ(
    val question: String,
    @Lob
    val answer: String
){
    constructor():this(
        question="",
        answer=""
    )
}
