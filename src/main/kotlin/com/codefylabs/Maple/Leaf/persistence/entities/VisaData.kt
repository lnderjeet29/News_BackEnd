package com.codefylabs.Maple.Leaf.persistence.entities

import jakarta.persistence.*

@Entity
@Table(name = "visa_data")
data class VisaData(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visaId")
    var visaId:Int=0,
    @Column(name = "key")
    var key:String,

    @Column(name = "title")
    var title:String,

    @Lob
    @Column(name = "content")
    var content:String,

    @ElementCollection
    @CollectionTable(name = "sub_visa", joinColumns = [JoinColumn(name = "visa_id")])
    @Column(name = "sub_visa")
    val subVisa: List<SubVisa> = mutableListOf()
){
    constructor():this(
        visaId=0,
        title="",
        content="",
        key="",
        subVisa = mutableListOf()
    )
}
