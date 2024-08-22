package com.Inderjeet.News.persistence.entities

import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
@Table(name = "OnBoardingData")
data class OnBoardingData(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    var id: Int=0,

    @Column(name = "user_id")
    var userId:Int=0,

    @Column(name = "question")
    var question: String?="",

    @Column(name = "answer", length = 200)
    @Size(max = 200)
    var answer:String?=""
){
    constructor():this(
        id=0
    )
}
