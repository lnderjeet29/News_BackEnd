package com.codefylabs.Maple.Leaf.persistence.entities

import jakarta.persistence.*

@Entity
@Table(name = "visa_data")
data class VisaData(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visaId")
    var visaId:Int=0,
    @Column(name = "title")
    var title:String,
    @Lob
    @Column(name = "description")
    var description:String,
    @Column(name = "category")
    var category:String
){
    constructor():this(
        visaId=0,
        title="",
        description="",
        category=""
    )
}
