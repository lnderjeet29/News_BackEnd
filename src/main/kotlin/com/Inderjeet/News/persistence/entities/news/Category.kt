package com.Inderjeet.News.persistence.entities.news

import jakarta.persistence.*

@Entity
@Table(name = "categories")
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    val id:Int=0,
    @Column(name = "name")
    val name: String,
){
    constructor():this (
        id = 0,
        name= ""
    )
}
