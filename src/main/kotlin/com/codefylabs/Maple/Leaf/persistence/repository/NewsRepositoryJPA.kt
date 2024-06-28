package com.codefylabs.Maple.Leaf.persistence.repository

import com.codefylabs.Maple.Leaf.persistence.entities.news.News
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NewsRepositoryJPA: JpaRepository<News, Int> {

    fun findByCategory(category: String, pageable: Pageable): Page<News>

}