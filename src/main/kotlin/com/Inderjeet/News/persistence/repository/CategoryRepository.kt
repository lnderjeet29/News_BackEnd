package com.Inderjeet.News.persistence.repository

import com.Inderjeet.News.persistence.entities.news.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository:JpaRepository<Category,Int> {
    fun existsByName(name: String): Boolean
}