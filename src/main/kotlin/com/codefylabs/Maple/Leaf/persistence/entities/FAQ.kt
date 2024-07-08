package com.codefylabs.Maple.Leaf.persistence.entities

import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
@Table(name = "faq")
data class FAQ(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Int = 0,

    @Column(name = "category")
    val category: String,

    @Column(name = "sub_category", length = 300)
    @Size(max = 300)
    val subCategory: String,

    @Column(name = "title")
    val title: String,

    @Lob
    @Column(name = "content")
    val content: String,

    @ElementCollection
    @CollectionTable(name = "sub_faqs", joinColumns = [JoinColumn(name = "faq_id")])
    @Column(name = "sub_faq")
    val subFaqs: List<SubFAQ> = mutableListOf()
){
    constructor():this(
        id=0,
        category="",
        subCategory="",
        title="",
        content="",
        subFaqs = mutableListOf()
    )
}
